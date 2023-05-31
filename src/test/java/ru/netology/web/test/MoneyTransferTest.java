package ru.netology.web.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.*;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    public String transferAmount = "5000";

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    // После каждого теста сбрасываем весь баланс обратно

    @AfterEach
    void resetBalance() {
        open("http://localhost:9999");
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        String firstCardId = firstCardInfo.getId();
        String secondCardId = secondCardInfo.getId();
        String card1 = firstCardInfo.getNumber();
        int firstCardbalance = dashboardPage.getCardBalance(firstCardId);
        int secondCardbalance = dashboardPage.getCardBalance(secondCardId);

        if (firstCardbalance == secondCardbalance) {
            return;
        }

        dashboardPage.clickCardDepositButton(secondCardId);
        TransferPage transferPage = new TransferPage();
        transferPage.setTransferAmount(this.transferAmount);
        transferPage.setTransferFromField(card1);
        transferPage.clickTransferButton();
    }

    // Тест на перевод с карты на карту

    @Test
    void shouldTransferCardToCard() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        String id = firstCardInfo.getId();
        String card2 = secondCardInfo.getNumber();
        dashboardPage.clickCardDepositButton(id);
        TransferPage transferPage = new TransferPage();
        transferPage.setTransferAmount(this.transferAmount);
        transferPage.setTransferFromField(card2);
        transferPage.clickTransferButton();

        int expected = 15000;
        int actual = dashboardPage.getCardBalance(id);
        assertEquals(expected, actual);
    }

    // Тест на отмену перевода

    @Test
    void shouldCancelTransfer() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = DataHelper.getFirstCardInfo();
        String id = firstCardInfo.getId();
        dashboardPage.clickCardDepositButton(id);
        TransferPage transferPage = new TransferPage();
        transferPage.clickCancelButton();

        int expected = 10000;
        int actual = dashboardPage.getCardBalance(id);
        assertEquals(expected, actual);
    }

    // Тест на перевод на ту же карту

    @Test
    void shouldCardToThisCardTransfer() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = DataHelper.getFirstCardInfo();
        String id = firstCardInfo.getId();
        String card1 = firstCardInfo.getNumber();
        dashboardPage.clickCardDepositButton(id);
        TransferPage transferPage = new TransferPage();
        transferPage.setTransferAmount(this.transferAmount);
        transferPage.setTransferFromField(card1);
        transferPage.clickTransferButton();

        int expected = 10000;
        int actual = dashboardPage.getCardBalance(id);
        assertEquals(expected, actual);
    }

    // Тест на кнопку обновить после перевода

    @Test
    void shouldRefreshButton() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = DataHelper.getFirstCardInfo();
        String id = firstCardInfo.getId();
        String card1 = firstCardInfo.getNumber();
        int beforeTransferBalance = dashboardPage.getCardBalance(id);
        dashboardPage.clickCardDepositButton(id);
        TransferPage transferPage = new TransferPage();
        transferPage.setTransferAmount(this.transferAmount);
        transferPage.setTransferFromField(card1);
        transferPage.clickTransferButton();
        dashboardPage.clickRefreshButton();

        int expected = 15000;
        int actual =  beforeTransferBalance + parseInt(this.transferAmount);
        assertEquals(expected, actual);
    }
}

