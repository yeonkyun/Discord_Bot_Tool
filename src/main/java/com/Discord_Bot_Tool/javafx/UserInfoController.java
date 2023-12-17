package com.Discord_Bot_Tool.javafx;

import com.Discord_Bot_Tool.db.Users;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class UserInfoController {
    @FXML
    private TextField UsernameField;
    @FXML
    private TextField EmailField;
    @FXML
    private TextField UserIDField;

    private Stage dialogStage;
    private Users user;
    private boolean isUpdateMode = false;

    public void UpdateUserInfo(Users users) {
        this.user = users;
        this.isUpdateMode = true;
        UsernameField.setText(users.getUsername());
        EmailField.setText(users.getEmail());
        UserIDField.setText(String.valueOf(users.getUser_id()));
    }

    public void CreateUserInfo() {
        this.user = new Users();
        this.isUpdateMode = false;
    }

    @FXML
    private void handleConfirmAction() {
        try {
            if (isUpdateMode) { // 수정 모드
                user.setUsername(UsernameField.getText());
                user.setEmail(EmailField.getText());
                user.setUser_id(Long.parseLong(UserIDField.getText()));
            } else {       // 추가 모드
                user = new Users(UsernameField.getText(), EmailField.getText(), Long.parseLong(UserIDField.getText()), LocalDate.now());
            }
            dialogStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelAction() {
        try {
            user = null;
            dialogStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public Users getNewUser() {
        return user;
    }
}
