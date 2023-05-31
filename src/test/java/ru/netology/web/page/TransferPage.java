package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement heading = $(By.xpath("//h1[contains(text(),'Пополнение карты')]"));
    private SelenideElement transferAmount = $("[data-test-id='amount'] .input__control");
    private SelenideElement transferFromField = $("[data-test-id='from'] .input__control");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");

    public TransferPage() {
        heading.shouldBe(visible);
    }

    public void setTransferAmount(String amount) {
        transferAmount.setValue(amount);
    }

    public void setTransferFromField(String cardNumber) {
        transferFromField.setValue(cardNumber);
    }

    public DashboardPage clickTransferButton() {
        transferButton.click();
        return new DashboardPage();
    }

    public DashboardPage clickCancelButton() {
        cancelButton.click();
        return new DashboardPage();
    }
}
