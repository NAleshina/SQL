package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataHelper {
    private DataHelper() {
    }

    static Connection getMySQLConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://192.168.99.100:3306/app",
                    "app", "pass");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clearTables() {
        val runner = new QueryRunner();
        val deleteFromUsers = "DELETE FROM users;";
        val deleteFromAuthCodes = "DELETE FROM auth_codes;";
        val deleteFromCards = "DELETE FROM cards;";
        val deleteFromCardTransaсtions = "DELETE FROM card_transactions;";
        try (val conn = getMySQLConnection()) {
            runner.execute(conn, deleteFromCardTransaсtions);
            runner.execute(conn, deleteFromAuthCodes);
            runner.execute(conn, deleteFromCards);
            runner.execute(conn, deleteFromUsers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Value
    public static class AuthInfo {
        private String id;
        private String login;
        private String password;
    }

    public static AuthInfo getActiveAuthInfo() {
        val faker = new Faker();
        val runner = new QueryRunner();
        val dataSQL = "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, ?);";
        val countSQL = "SELECT COUNT(*) FROM users;";
        val username = faker.name().username();
        val pass = "$2a$10$CIasK0AbTy76H5f.hokUtO3KJWpSVmiRAuFQJS3DFfqqY4Q2/Y0py";
        val status = "active";
        String id = "";
        try (val conn = getMySQLConnection();
             val countStmt = conn.createStatement();) {
            int count;
            try (val rs = countStmt.executeQuery(countSQL)) {
                count = 0;
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
            id = String.valueOf(count + 1);
            runner.update(conn, dataSQL, id, username, pass, status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new AuthInfo(id, username, "qwerty123");
    }

    public static AuthInfo getInActiveAuthInfo() {
        val faker = new Faker();
        val username = faker.name().username();
        return new AuthInfo("4564675678", username, "3454345fvgtf");
    }

    @Value
    public static class VerificationCode {
        private int code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        val userIdSQL = "SELECT code FROM auth_codes WHERE user_id=? order by created desc;";
        int code = 0;
        try (
                val conn = getMySQLConnection();
                val usersStmt = conn.prepareStatement(userIdSQL);
        ) {
            usersStmt.setString(1, authInfo.id);
            try (val rs = usersStmt.executeQuery()) {
                if (rs.next()) {
                    code = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new VerificationCode(code);
    }
}
