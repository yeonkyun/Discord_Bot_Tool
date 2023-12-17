package com.Discord_Bot_Tool.javafx;

import com.Discord_Bot_Tool.bot.Bot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeController {
    @FXML
    private Button StartBotBtn;
    @FXML
    private Button StopBotBtn;
    @FXML
    private Button RestartBotBtn;

    private Bot bot = new Bot();

    @FXML
    private void handleStartBotBtn() throws IOException {
        try {
            bot.JDABuild();
            StartBotBtn.setDisable(true);
            StopBotBtn.setDisable(false);
            RestartBotBtn.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStopBotBtn() throws IOException {
        try {
            bot.shutdown();
            StartBotBtn.setDisable(false);
            StopBotBtn.setDisable(true);
            RestartBotBtn.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRestartBotBtn() throws IOException {
        try {
            bot.restart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void StopProgram() {
        bot.shutdown();
        Platform.exit();
    }
}