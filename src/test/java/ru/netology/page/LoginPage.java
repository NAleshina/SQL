package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement notification = $("[data-test-id=error-notification] .notification__content");

    public void login(DataHelper.AuthInfo info) {
        loginField.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        loginField.setValue(info.getLogin());
        passwordField.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        passwordField.setValue(info.getPassword());
        loginButton.click();
    }

    public VerificationPage returnVerificationPage() {
        return new VerificationPage();
    }

    public void blocked() {
        notification.shouldHave(Condition.text("заблокирован"));
    }
}
