package com.Discord_Bot_Tool.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UsersDAO { // DAO = Database Access Object
    // C R U D = Create Read Update Delete
    // Create (INSERT) - 데이터 추가
    public Users Create(Users users) {
        try (Connection connection = DBUtil.connection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Users (username, email, user_id, authordate) VALUES (?, ?, ?, ?)");
            statement.setString(1, users.getUsername());
            statement.setString(2, users.getEmail());
            statement.setLong(3, users.getUser_id());
            statement.setDate(4, Date.valueOf(users.getAuthordate()));
            statement.executeUpdate();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Read (SELECT) - 데이터 불러오기
    public Users Read(Long user_id) {
        Users users = null;
        try (Connection connection = DBUtil.connection(); ResultSet resultSet = connection.createStatement().executeQuery("SEELECT * FROM Users WHERE user_id = " + user_id)) {
            while (resultSet.next()) {
                users = new Users();
                users.setUsername(resultSet.getString("username"));
                users.setEmail(resultSet.getString("email"));
                users.setUser_id(resultSet.getLong("user_id"));
                users.setAuthordate(resultSet.getDate("authordate").toLocalDate());
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return users;
        }
    }
    // Read_All (SELECT) - 모든 데이터 불러오기 [리스트]
    public ObservableList<Users> Read_All() {
        ObservableList<Users> UsersList = FXCollections.observableArrayList();
        try (Connection connection = DBUtil.connection(); ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Users")) {
            while (resultSet.next()) {
                Users users = new Users();
                users.setUsername(resultSet.getString("username"));
                users.setEmail(resultSet.getString("email"));
                users.setUser_id(resultSet.getLong("user_id"));
                users.setAuthordate(resultSet.getDate("authordate").toLocalDate());
                UsersList.add(users);
            }
            return UsersList;
        } catch (SQLException e) {
            e.printStackTrace();
            return UsersList;
        }
    }
    // Update (UPDATE) - 데이터 수정
    public Users Update(Users users) {
        try (Connection connection = DBUtil.connection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE Users SET username = ?, email = ?, user_id = ?, authordate = ? WHERE user_id = ?");
            statement.setString(1, users.getUsername());
            statement.setString(2, users.getEmail());
            statement.setLong(3, users.getUser_id());
            statement.setDate(4, Date.valueOf(users.getAuthordate()));
            statement.setLong(5, users.getUser_id());
            statement.executeUpdate();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Delete (DELETE) - 데이터 삭제
    public Users Delete(String username) {
        Users users = new Users();
        try (Connection connection = DBUtil.connection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Users WHERE username = ?");
            statement.setString(1, username);
            statement.executeUpdate();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
