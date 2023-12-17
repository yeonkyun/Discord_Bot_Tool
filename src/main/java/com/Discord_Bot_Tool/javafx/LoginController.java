package com.Discord_Bot_Tool.javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField id_input;

    @FXML
    private TextField pw_input;

    @FXML
    private Label status;

    @FXML
    private void Login() throws Exception {
        if (id_input.getText().equals("user") && pw_input.getText().equals("1234")) {
            Stage loginWindow = (Stage) id_input.getScene().getWindow();

            loginWindow.close();

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.setTitle("디스코드 봇 관리");
            stage.getIcons().add(new Image("file:src/main/resources/com/Discord_Bot_Tool/Image/icon.ico"));
            stage.show();
        } else {
            status.setVisible(true);
        }
    }
}
