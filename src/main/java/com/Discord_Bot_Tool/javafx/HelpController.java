package com.Discord_Bot_Tool.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

import java.net.URI;

public class HelpController {
    @FXML
    Hyperlink github;

    @FXML
    private void initialize() {
        github.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(new URI("https://github.com/yeonkyun"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
