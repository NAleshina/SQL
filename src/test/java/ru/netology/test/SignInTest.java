package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignInTest {
    @Order(1)
    @Test
    void shouldSignIn() throws SQLException {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getActiveAuthInfo();
        val dashboardPage = loginPage.validLogin(authInfo).validVerify(DataHelper.getVerificationCodeFor(authInfo));
    }

    @Order(2)
    @Test
    void shouldBlockedWithErrorPass() throws SQLException {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getActiveAuthInfo();
        loginPage.multipleInvalidLogin(authInfo);
    }
}