package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.*;

import static com.codeborne.selenide.Selenide.$;
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
            dashboardPage.setTransferAmount(this.transferAmount);
            dashboardPage.setTransferFromField(card2);
        } else {
            dashboardPage.clickCardDepositButton(secondCardId);
            dashboardPage.setTransferAmount(this.transferAmount);
            dashboardPage.setTransferFromField(card1);
        }
        dashboardPage.clickTransferButton();
    }




    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() {
        var loginPage = new LoginPageV1();
        // var loginPage = open("http://localhost:9999", LoginPageV1.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV2() {
        var loginPage = new LoginPageV2();
        // var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV3() {
        var loginPage = open("http://localhost:9999", LoginPageV3.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    // Тест на проверку баланса карт

    @Test
    void shouldGetCardBalance() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardInfo = DataHelper.getFirstCardInfo();
        String id = firstCardInfo.getId();

        int expected = 10000;
        int actual = dashboardPage.getCardBalance(id);
        assertEquals(expected, actual);
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
        String card1 = firstCardInfo.getNumber();
        String card2 = secondCardInfo.getNumber();

        dashboardPage.clickCardDepositButton(id);
        dashboardPage.setTransferAmount(this.transferAmount);
        if (id.equals(firstCardInfo.getId())) {
            dashboardPage.setTransferFromField(card2);
        } else {
            dashboardPage.setTransferFromField(card1);
        }
        dashboardPage.clickTransferButton();
        new DashboardPage();
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
        dashboardPage.clickCancelButton();
        new DashboardPage();
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
        int beforeTransferBalance = dashboardPage.getCardBalance(id);

        dashboardPage.clickCardDepositButton(id);
        dashboardPage.setTransferAmount(this.transferAmount);
        dashboardPage.setTransferFromField(card1);
        dashboardPage.clickTransferButton();
        int afterTransferBalance = dashboardPage.getCardBalance(id);

        assertEquals(beforeTransferBalance, afterTransferBalance);
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
        dashboardPage.setTransferAmount(this.transferAmount);
        dashboardPage.setTransferFromField(card1);
        dashboardPage.clickTransferButton();
        dashboardPage.clickRefreshButton();

        int expected = 15000;
        int actual =  beforeTransferBalance + parseInt(this.transferAmount);
        assertEquals(expected, actual);
    }
}

