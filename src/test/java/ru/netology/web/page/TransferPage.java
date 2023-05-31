package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement transferAmount = $("[data-test-id='amount'] .input__control");
    private SelenideElement transferFromField = $("[data-test-id='from'] .input__control");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private SelenideElement refreshButton = $("[data-test-id='action-reload']");

    public TransferPage() {
        clickTransferButton();
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

    public void clickCancelButton() {
        cancelButton.click();
    }

    public void clickRefreshButton() {
        refreshButton.click();
    }
}
