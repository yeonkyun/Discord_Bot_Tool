package com.Discord_Bot_Tool.db;

import java.sql.*;

public class DBUtil {
    private static final String Url = "jdbc:mysql://localhost:3306/Discord"; // DB 서버 주소
    private static final String MySQLUser = "Discord"; // DB 사용자 계정
    private static final String MySQLPassword = "project7"; // DB 사용자 비밀번호

    // DB 연결
    public static Connection connection() throws SQLException {
        return DriverManager.getConnection(Url, MySQLUser, MySQLPassword);
    }

    public static ResultSet getAll(Connection connection, String tableName) throws SQLException {
        ResultSet resultSet = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void StopConnection(Connection connection) throws SQLException {
        connection.close();
    }
}