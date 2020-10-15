package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataHelper {
    private DataHelper() {
    }

    private static String url = "jdbc:mysql://192.168.99.100:3306/app";

    public static void clearTables() throws SQLException {

        val runner = new QueryRunner();
        val dataSQL_drop_cards = "DROP TABLE IF EXISTS cards;";
        try (val conn = DriverManager.getConnection(url, "app", "pass");) {
            runner.execute(conn, dataSQL_drop_cards);
        }
        val dataSQL_drop_auth_codes = "DROP TABLE IF EXISTS auth_codes;";
        try (val conn = DriverManager.getConnection(url, "app", "pass");) {
            runner.execute(conn, dataSQL_drop_auth_codes);
        }
        val dataSQL3 = "DROP TABLE IF EXISTS users;";
        try (val conn = DriverManager.getConnection(url, "app", "pass");) {
            runner.execute(conn, dataSQL3);
        }
        val dataSQL4 = "DROP TABLE IF EXISTS card_transactions;";
        try (val conn = DriverManager.getConnection(url, "app", "pass");) {
            runner.execute(conn, dataSQL4);
        }

        val dataSQL_create_users = "CREATE TABLE users(id       CHAR(36) PRIMARY KEY,login    VARCHAR(255) UNIQUE NOT NULL,password VARCHAR(255)        NOT NULL,status   VARCHAR(255)        NOT NULL DEFAULT 'active');";
        try (val conn = DriverManager.getConnection(url, "app", "pass");) {
            runner.execute(conn, dataSQL_create_users);
        }
        val dataSQL_create_cards = "CREATE TABLE cards(id                 CHAR(36) PRIMARY KEY,user_id            CHAR(36)           NOT NULL,number             VARCHAR(19) UNIQUE NOT NULL,balance_in_kopecks INT                NOT NULL,FOREIGN KEY (user_id) REFERENCES users (id));";
        try (val conn = DriverManager.getConnection(url, "app", "pass");) {
            runner.execute(conn, dataSQL_create_cards);
        }
        val dataSQL_create_auth_codes = "CREATE TABLE auth_codes(id      CHAR(36) PRIMARY KEY,user_id CHAR(36)   NOT NULL,code    VARCHAR(6) NOT NULL,created TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY (user_id) REFERENCES users (id));";
        try (val conn = DriverManager.getConnection(url, "app", "pass");) {
            runner.execute(conn, dataSQL_create_auth_codes);
        }
        val dataSQL_create_card_transactions = "CREATE TABLE card_transactions(id                CHAR(36) PRIMARY KEY,source            VARCHAR(19) NOT NULL,target            VARCHAR(19) NOT NULL,amount_in_kopecks INT         NOT NULL,created           TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP);";
        try (val conn = DriverManager.getConnection(url, "app", "pass");) {
            runner.execute(conn, dataSQL_create_card_transactions);
        }
    }

    @Value
    public static class AuthInfo {
        private String id;
        private String login;
        private String password;
    }

    public static AuthInfo getActiveAuthInfo() throws SQLException {
        DataHelper.clearTables();
        val faker = new Faker();
        val runner = new QueryRunner();
        val dataSQL = "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, ?);";
        val id = "6f5a15c3-3a94-44da-b5cc-c5c97d942786";
        val username = faker.name().username();
        val pass = "$2a$10$CIasK0AbTy76H5f.hokUtO3KJWpSVmiRAuFQJS3DFfqqY4Q2/Y0py";
        val status = "active";
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://192.168.99.100:3306/app", "app", "pass"
                );
        ) {
            runner.update(conn, dataSQL, id, username, pass, status);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new AuthInfo(id, username, "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private int code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) throws SQLException {
        val userIdSQL = "SELECT code FROM auth_codes WHERE user_id=? order by created desc;";
        int code = 0;
        try (
                val conn = DriverManager.getConnection(url, "app", "pass");
                val usersStmt = conn.prepareStatement(userIdSQL);
        ) {
            usersStmt.setString(1, authInfo.id);
            try (val rs = usersStmt.executeQuery()) {
                if (rs.next()) {
                    code = rs.getInt(1);
                }
            }
        }
        return new VerificationCode(code);
    }
}
