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

    // Тест на перевод с карты на карту

    @Test
    void shouldTransferCardToCardFirst() {
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

        //Первая карта
        dashboardPage.clickCardDepositButton(firstCardId);
        TransferPage transferPageFirstCard = new TransferPage();
        transferPageFirstCard.setTransferAmount(this.transferAmount);
        transferPageFirstCard.setTransferFromField(card2);
        transferPageFirstCard.clickTransferButton();
        int expected1 = 15000;
        int actual1 = dashboardPage.getCardBalance(firstCardId);
        assertEquals(expected1, actual1);

        //Вторая карта
        dashboardPage.clickCardDepositButton(secondCardId);
        TransferPage transferPageSecondCard = new TransferPage();
        transferPageSecondCard.setTransferAmount(this.transferAmount);
        transferPageSecondCard.setTransferFromField(card1);
        transferPageSecondCard.clickTransferButton();
        int expected2 = 10000;
        int actual2 = dashboardPage.getCardBalance(secondCardId);
        assertEquals(expected2, actual2);
    }

    // Тест на отмену перевода

    @Test
    void shouldCancelTransferFirst() {
        LoginPageV2 loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();
        String firstCardId = firstCardInfo.getId();
        String secondCardId = secondCardInfo.getId();

        //Первая карта
        dashboardPage.clickCardDepositButton(firstCardId);
        TransferPage transferPageFirstCard = new TransferPage();
        transferPageFirstCard.clickCancelButton();

        int expected1 = 10000;
        int actual1 = dashboardPage.getCardBalance(firstCardId);
        assertEquals(expected1, actual1);

        //Вторая карта
        dashboardPage.clickCardDepositButton(secondCardId);
        TransferPage transferPageSecondCard = new TransferPage();
        transferPageSecondCard.clickCancelButton();

        int expected2 = 10000;
        int actual2 = dashboardPage.getCardBalance(secondCardId);
        assertEquals(expected2, actual2);
    }


    // Тест на перевод на ту же карту

    @Test
    void shouldCardToThisCardTransferFirst() {
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

        //Первая карта
        dashboardPage.clickCardDepositButton(firstCardId);
        TransferPage transferPageFirstCard = new TransferPage();
        transferPageFirstCard.setTransferAmount(this.transferAmount);
        transferPageFirstCard.setTransferFromField(card1);
        transferPageFirstCard.clickTransferButton();

        int expected1 = 10000;
        int actual1 = dashboardPage.getCardBalance(firstCardId);
        assertEquals(expected1, actual1);

        //Вторая карта
        dashboardPage.clickCardDepositButton(secondCardId);
        TransferPage transferPageSecondCard = new TransferPage();
        transferPageSecondCard.setTransferAmount(this.transferAmount);
        transferPageSecondCard.setTransferFromField(card2);
        transferPageSecondCard.clickTransferButton();

        int expected2 = 10000;
        int actual2 = dashboardPage.getCardBalance(secondCardId);
        assertEquals(expected2, actual2);
    }


    // Тест на кнопку обновить после пополнения

    @Test
    void shouldRefreshButtonFirst() {
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

        //Первая карта
        int beforeTransferBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        dashboardPage.clickCardDepositButton(firstCardId);
        TransferPage transferPageFirstCard = new TransferPage();
        transferPageFirstCard.setTransferAmount(this.transferAmount);
        transferPageFirstCard.setTransferFromField(card2);
        transferPageFirstCard.clickTransferButton();
        dashboardPage.clickRefreshButton();

        int expected1 = 15000;
        int actual1 =  beforeTransferBalanceFirstCard + parseInt(this.transferAmount);
        assertEquals(expected1, actual1);

        //Вторая карта
        int beforeTransferBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);
        dashboardPage.clickCardDepositButton(secondCardId);
        TransferPage transferPageSecondCard = new TransferPage();
        transferPageSecondCard.setTransferAmount(this.transferAmount);
        transferPageSecondCard.setTransferFromField(card1);
        transferPageSecondCard.clickTransferButton();
        dashboardPage.clickRefreshButton();

        int expected2 = 10000;
        int actual2 =  beforeTransferBalanceSecondCard + parseInt(this.transferAmount);
        assertEquals(expected2, actual2);
    }

}

