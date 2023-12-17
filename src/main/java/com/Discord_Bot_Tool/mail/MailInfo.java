package com.Discord_Bot_Tool.mail;

public class MailInfo {
    private String Subject = "기초프로젝트 7조 이메일 인증";    // 제목
    private String Content; // 내용

    public String getSubject() {
        return Subject;
    }
    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }
}
