package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement transferAmount = $("[data-test-id='amount'] .input__control");
    private SelenideElement transferFromField = $("[data-test-id='from'] .input__control");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private SelenideElement refreshButton = $("[data-test-id='action-reload']");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    // к сожалению, разработчики не дали нам удобного селектора, поэтому так
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public void Dashboard() {
    }

    public int getCardBalance(String id) {
        val card = cards.findBy(attribute("data-test-id", id));
        return extractBalance(card.text());
    }

    public String getLastCardNumbers(String id) {
        val card = cards.findBy(attribute("data-test-id", id));
        return $(card).getAttribute("innerText").split(" ")[3].replaceAll("[^0-9]", "");
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public void setTransferAmount(String amount) {
        transferAmount.setValue(amount);
    }

    public void setTransferFromField(String cardNumber) {
        transferFromField.setValue(cardNumber);
    }

    public void clickTransferButton() {
        transferButton.click();
    }

    public void clickCardDepositButton(String id) {
        $(String.format("[data-test-id='%s'] [data-test-id='action-deposit']", id)).click();
    }

    public void clickCancelButton() {
        cancelButton.click();
    }

    public void clickRefreshButton() {
        refreshButton.click();
    }

}
