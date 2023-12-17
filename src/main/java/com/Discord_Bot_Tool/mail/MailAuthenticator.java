package com.Discord_Bot_Tool.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
    private final String ID = "";  // Google 계정 ID
    private final String PW = "";   // Google 계정 앱 비밀번호

    public String getID() {
        return ID;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(ID, PW);
    }

}
