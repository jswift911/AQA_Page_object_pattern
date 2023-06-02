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
        String card2 = secondCardInfo.getNumber();
        int firstCardbalance = dashboardPage.getCardBalance(firstCardId);
        int secondCardbalance = dashboardPage.getCardBalance(secondCardId);

        if (firstCardbalance == secondCardbalance) {
            return;
        }

        if (firstCardbalance < secondCardbalance) {
            dashboardPage.clickCardDepositButton(firstCardId);
            TransferPage transferPage = new TransferPage();
            transferPage.setTransferAmount(this.transferAmount);
            transferPage.setTransferFromField(card2);
            transferPage.clickTransferButton();
        } else {
            dashboardPage.clickCardDepositButton(secondCardId);
            TransferPage transferPage = new TransferPage();
            transferPage.setTransferAmount(this.transferAmount);
            transferPage.setTransferFromField(card1);
            transferPage.clickTransferButton();
        }
    }

    // Тест на перевод с карты на карту (1-ая карта)

    @Test
    void shouldTransferCardToCardFirst() {
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

    // Тест на перевод с карты на карту (2-ая карта)

    @Test
    void shouldTransferCardToCardSecond() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        String id = secondCardInfo.getId();
        String card1 = firstCardInfo.getNumber();
        dashboardPage.clickCardDepositButton(id);
        TransferPage transferPage = new TransferPage();
        transferPage.setTransferAmount(this.transferAmount);
        transferPage.setTransferFromField(card1);
        transferPage.clickTransferButton();

        int expected = 15000;
        int actual = dashboardPage.getCardBalance(id);
        assertEquals(expected, actual);
    }

    // Тест на отмену перевода (1-ая карта)

    @Test
    void shouldCancelTransferFirst() {
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

    // Тест на отмену перевода (2-ая карта)

    @Test
    void shouldCancelTransferSecond() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var secondCardInfo = DataHelper.getSecondCardInfo();
        String id = secondCardInfo.getId();
        dashboardPage.clickCardDepositButton(id);
        TransferPage transferPage = new TransferPage();
        transferPage.clickCancelButton();

        int expected = 10000;
        int actual = dashboardPage.getCardBalance(id);
        assertEquals(expected, actual);
    }

    // Тест на перевод на ту же карту (1-ая карта)

    @Test
    void shouldCardToThisCardTransferFirst() {
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

    // Тест на перевод на ту же карту (2-ая карта)

    @Test
    void shouldCardToThisCardTransferSecond() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var secondCardinfo = DataHelper.getFirstCardInfo();
        String id = secondCardinfo.getId();
        String card2 = secondCardinfo.getNumber();
        dashboardPage.clickCardDepositButton(id);
        TransferPage transferPage = new TransferPage();
        transferPage.setTransferAmount(this.transferAmount);
        transferPage.setTransferFromField(card2);
        transferPage.clickTransferButton();

        int expected = 10000;
        int actual = dashboardPage.getCardBalance(id);
        assertEquals(expected, actual);
    }

    // Тест на кнопку обновить после пополнения (1-ая карта)

    @Test
    void shouldRefreshButtonFirst() {
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

    // Тест на кнопку обновить после пополнения (2-ая карта)

    @Test
    void shouldRefreshButtonSecond() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var secondCardInfo = DataHelper.getFirstCardInfo();
        String id = secondCardInfo.getId();
        String card2 = secondCardInfo.getNumber();
        int beforeTransferBalance = dashboardPage.getCardBalance(id);
        dashboardPage.clickCardDepositButton(id);
        TransferPage transferPage = new TransferPage();
        transferPage.setTransferAmount(this.transferAmount);
        transferPage.setTransferFromField(card2);
        transferPage.clickTransferButton();
        dashboardPage.clickRefreshButton();

        int expected = 15000;
        int actual =  beforeTransferBalance + parseInt(this.transferAmount);
        assertEquals(expected, actual);
    }
}

