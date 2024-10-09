package ru.netology.testmode.test;

import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    private static final String BASE_URL = "http://localhost:9999";
    private static final String LOGIN_SELECTOR = ".form [data-test-id='login'] .input__box .input__control";
    private static final String PASSWORD_SELECTOR = ".form [data-test-id='password'] .input__box .input__control";
    private static final String SUBMIT_BUTTON_TEXT = "Продолжить";
    private static final String ERROR_NOTIFICATION_TEXT = "Ошибка";
    private static final String WRONG_CREDENTIALS_TEXT = "Неверно указан логин или пароль";
    private static final String BLOCKED_USER_TEXT = "Пользователь заблокирован";
    private static final String PERSONAL_ACCOUNT_TEXT = "Личный кабинет";

    @BeforeEach
    void setup() {
        clearBrowserCookies();
        open(BASE_URL);
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $(LOGIN_SELECTOR).setValue(registeredUser.getLogin());
        $(PASSWORD_SELECTOR).setValue(registeredUser.getPassword());
        $$("button").find(exactText(SUBMIT_BUTTON_TEXT)).click();
        $(byText(PERSONAL_ACCOUNT_TEXT)).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $(LOGIN_SELECTOR).setValue(notRegisteredUser.getLogin());
        $(PASSWORD_SELECTOR).setValue(notRegisteredUser.getPassword());
        $$("button").find(exactText(SUBMIT_BUTTON_TEXT)).click();
        $(byText(ERROR_NOTIFICATION_TEXT)).shouldBe(visible, Duration.ofSeconds(10));
        $(withText(WRONG_CREDENTIALS_TEXT)).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $(LOGIN_SELECTOR).setValue(blockedUser.getLogin());
        $(PASSWORD_SELECTOR).setValue(blockedUser.getPassword());
        $$("button").find(exactText(SUBMIT_BUTTON_TEXT)).click();
        $(byText(ERROR_NOTIFICATION_TEXT)).shouldBe(visible, Duration.ofSeconds(10));
        $(withText(BLOCKED_USER_TEXT)).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $(LOGIN_SELECTOR).setValue(wrongLogin);
        $(PASSWORD_SELECTOR).setValue(registeredUser.getPassword());
        $$("button").find(exactText(SUBMIT_BUTTON_TEXT)).click();
        $(byText(ERROR_NOTIFICATION_TEXT)).shouldBe(visible, Duration.ofSeconds(10));
        $(withText(WRONG_CREDENTIALS_TEXT)).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $(LOGIN_SELECTOR).setValue(registeredUser.getLogin());
        $(PASSWORD_SELECTOR).setValue(wrongPassword);
        $$("button").find(exactText(SUBMIT_BUTTON_TEXT)).click();
        $(byText(ERROR_NOTIFICATION_TEXT)).shouldBe(visible, Duration.ofSeconds(10));
        $(withText(WRONG_CREDENTIALS_TEXT)).shouldBe(visible, Duration.ofSeconds(10));
    }
}