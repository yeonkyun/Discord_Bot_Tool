package com.Discord_Bot_Tool.javafx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LogController implements Initializable {
    @FXML
    private TextArea LogTextArea;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PrintStream ps = new PrintStream(new LogController.ConsoleOutputStream(LogTextArea), true);
        System.setOut(ps);
        System.setErr(ps);
    }

    private class ConsoleOutputStream extends OutputStream {
        private TextArea console;
        public ConsoleOutputStream(TextArea console) {
            this.console = console;
        }
        @Override
        public void write(int b) throws IOException {
            Platform.runLater(() -> console.appendText(String.valueOf((char) b)));
        }
    }
}
