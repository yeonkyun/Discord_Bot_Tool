module Discord_Bot_Tool.main {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.mail;
    requires java.desktop;
    requires net.dv8tion.jda;
    requires org.slf4j;
    requires kotlin.stdlib;
    requires com.fasterxml.jackson.core;

    opens com.Discord_Bot_Tool.javafx to javafx.fxml;
    opens com.Discord_Bot_Tool.db to javafx.base;
    exports com.Discord_Bot_Tool.javafx;
    exports com.Discord_Bot_Tool.mail;
    opens com.Discord_Bot_Tool.mail to javafx.fxml;
}