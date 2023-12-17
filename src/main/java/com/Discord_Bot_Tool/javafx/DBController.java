package com.Discord_Bot_Tool.javafx;

import com.Discord_Bot_Tool.db.Users;
import com.Discord_Bot_Tool.db.UsersDAO;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DBController implements Initializable {
    @FXML
    private TableView<Users> UsersTable;
    @FXML
    private TableColumn<Users, String> UsernameCol;
    @FXML
    private TableColumn<Users, String> EmailCol;
    @FXML
    private TableColumn<Users, Long> UserIDCol;
    @FXML
    private TableColumn<Users, LocalDate> AuthordateCol;
    @FXML
    private Button AddBtn;
    @FXML
    private Button ModBtn;
    @FXML
    private Button DelBtn;
    @FXML
    private Button RefBtn;
    @FXML
    private Button SaveBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TableColumns 설정
        UsernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        EmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        UserIDCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        AuthordateCol.setCellValueFactory(new PropertyValueFactory<>("authordate"));

        // TableView에 데이터 추가
        // ObservableList를 사용하는 이유는 TableView에 데이터를 추가하거나 삭제할 때 자동으로 갱신하기 위해서이다.
        ObservableList<Users> UsersList = new UsersDAO().Read_All();
        UsersTable.setItems(UsersList);

        // AddBtn 클릭 시 Create() 메소드 실행
        AddBtn.setOnAction(event -> handleCreateAction());

        // ModBtn 클릭 시 Update() 메소드 실행
        ModBtn.setOnAction(event -> handleUpdateAction());

        // DelBtn 클릭 시 Delete() 메소드 실행
        DelBtn.setOnAction(event -> handleDeleteAction());

        // RefBtn 클릭 시 TableView 갱신
        RefBtn.setOnAction(event -> handleRefreshAction());

        SaveBtn.setOnAction(event -> {
            UsersTable.setEditable(false);
            SaveBtn.setDisable(true);
        });
    }

    public void handleCreateAction() {
        Users user = null;
        openUserInfoDialog(true, user);
        handleRefreshAction();
    }

    public void handleUpdateAction() {
        Users user = UsersTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            openUserInfoDialog(false, user);
            handleRefreshAction();
        } else {
            Alert("수정할 데이터를 선택해주세요.");
        }
    }

    public void handleDeleteAction() {
        try {
            Users SelectUser = UsersTable.getSelectionModel().getSelectedItem();

            if (SelectUser != null) {
                UsersTable.getItems().remove(SelectUser);
                new UsersDAO().Delete(SelectUser.getUsername());
            } else if (SelectUser == null) {
                Alert("삭제할 데이터를 선택해주세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleRefreshAction() {
        try {
            Task<ObservableList<Users>> task = new Task<ObservableList<Users>>() {
                @Override
                protected ObservableList<Users> call() throws Exception {
                    return new UsersDAO().Read_All();
                }
            };

            task.setOnSucceeded(e -> UsersTable.setItems(task.getValue())); // TableView에 데이터를 새로 갱신한다.

            Thread thread = new Thread(task);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openUserInfoDialog(boolean isCreateMode, Users user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserInfo.fxml"));
            Parent UserInfo = fxmlLoader.load();

            UserInfoController userInfoController = fxmlLoader.getController();

            if (isCreateMode) {
                userInfoController.CreateUserInfo();
            } else {
                userInfoController.UpdateUserInfo(user);
            }

            Stage UserInfoStage = new Stage();
            userInfoController.setDialogStage(UserInfoStage);
            UserInfoStage.setScene(new Scene(UserInfo));
            UserInfoStage.setTitle("디스코드 봇 관리");
            UserInfoStage.setResizable(false);
            UserInfoStage.getIcons().add(new Image("file:src/main/resources/com/Discord_Bot_Tool/Image/icon.ico"));
            UserInfoStage.showAndWait();

            Users newUser = userInfoController.getNewUser();
            if (newUser != null) {
                if (isCreateMode) {
                    new UsersDAO().Create(newUser);
                } else {
                    new UsersDAO().Update(newUser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Alert(String content) {
        try {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("디스코드 봇 관리");
            alert.setHeaderText(null);

            // Alert 창에 들어갈 내용을 Label로 만든다.
            Label label = new Label(content);
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);

            // Alert 창의 DialogPane을 가져온다.
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setContent(label);
            dialogPane.setStyle("-fx-pref-width: 300px; -fx-pref-height: 100px;");

            // Alert 창의 Stage를 가져온다.
            Stage alertstage = (Stage) dialogPane.getScene().getWindow();
            alertstage.getIcons().add(new Image("file:src/main/resources/com/Discord_Bot_Tool/Image/icon.ico"));
            alertstage.setResizable(false);

            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}