package com.Discord_Bot_Tool.mail;

import com.Discord_Bot_Tool.db.Users;
import com.Discord_Bot_Tool.db.UsersDAO;
import javafx.collections.ObservableList;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class MailSend {
    private final Properties properties = System.getProperties(); // 시스템 속성
    private static MailAuthenticator authenticator; // 인증자 객체
    private static Session session; // 세션

    // 시스템 속성 설정
    private void setProperties() { // 메일 전송에 필요한 시스템 속성 설정
        properties.put("mail.smtp.host", "smtp.gmail.com"); // 구글 SMTP
        properties.put("mail.smtp.port", "465"); // 구글 SMTP 포트
        properties.put("mail.smtp.auth", "true"); // SMTP 인증 사용
        properties.put("mail.smtp.starttls.enable", "true"); // TLS 사용
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2"); // SSL 프로토콜 사용
        properties.put("mail.smtp.ssl.enable", "true"); // SSL 사용
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // 구글 SMTP 인증서 사용
        properties.put("mail.debug", "true"); // 디버깅 설정

        if("465".equals(properties.getProperty("mail.smtp.port"))) {
            properties.put("mail.smtp.socketFactory.port", "465"); // SSL 포트
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL 소켓 팩토리
            properties.put("mail.smtp.socketFactory.fallback", "false"); // SSL 소켓 팩토리 사용
        } else if ("587".equals(properties.getProperty("mail.smtp.port"))) {
            properties.put("mail.smtp.starttls.enable", "true"); // TLS 사용
            properties.put("mail.smtp.socketFactory.port", "587"); // TLS 포트
        }

        System.setProperties(properties); // 시스템 속성 설정
        authenticator = new MailAuthenticator(); // 인증 객체 생성
        session = Session.getInstance(properties, authenticator); // 세션 생성
    }

    // 비동기식 메일 전송
    public CompletableFuture<Void> sendMailAsync(String To, MailInfo mailInfo) {
        return CompletableFuture.runAsync(() -> {
            setProperties(); // 시스템 속성 설정
            try {
                MimeMessage message = new MimeMessage(session); // 메시지 생성
                message.setFrom(new InternetAddress(authenticator.getID(), "yEoNkYuN")); // 보내는 사람 설정
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(To)); // 받는 사람 설정
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(authenticator.getID())); // 참조 설정
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(authenticator.getID())); // 숨은 참조 설정
                message.setSubject(mailInfo.getSubject()); // 제목 설정
                message.setText(mailInfo.getContent()); // 내용 설정
                Transport.send(message); // 메일 전송
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    // 이메일 유효성 검사
    public boolean isValidEmail(String email) {
        // 선문대학교 이메일인지 확인
        if (!(email.endsWith("@sunmoon.ac.kr") || email.endsWith("@mail.sunmoon.ac.kr"))) {
            return false;
        }

        // 데이터베이스에서 모든 사용자 목록을 가져옴
        ObservableList<Users> users = new UsersDAO().Read_All();

        // 이메일의 '@' 앞 부분 추출
        String localPart = email.split("@")[0];

        // 데이터베이스의 사용자 이메일과 중복되는지 확인
        for (Users user : users) {
            String userLocalPart = user.getEmail().split("@")[0];
            if (localPart.equals(userLocalPart)) {
                return false; // 중복된 이메일이 있으면 false 반환
            }
        }

        // 모든 검사를 통과하면 true 반환
        return true;
    }
}