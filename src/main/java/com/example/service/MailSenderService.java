package com.example.service;

import com.example.entity.ProfileEntity;
import com.example.util.JWTUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Value("${server.url}")
    private String serverUrl;

    public void sendEmailVerification(ProfileEntity entity) {
    String jwt= JWTUtil.encode(entity.getId(),entity.getEmail());
    String url=serverUrl+"/api/v1/auth/verification/email/"+jwt;
    String subject="You tube verification link";
    StringBuilder builder= new StringBuilder();
        builder.append( String.format("<h1>Hello %s</h1>",entity.getName()));
        builder.append(" <p>");
        builder.append(String.format("<a href=\"%s\">Click the link to verify your account! </a>",url));
        builder.append(" </p>");
    sendMimeEmail(entity.getEmail(),subject,builder.toString());
    emailHistoryService.sendEmail(builder.toString(),subject,entity);
    }


    private void sendMimeEmail(String toAccount, String subject, String text){
        ExecutorService emailExecutor= Executors.newSingleThreadExecutor();
        emailExecutor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                try {
                    MimeMessage msg=javaMailSender.createMimeMessage();
                    MimeMessageHelper helper=new MimeMessageHelper(msg,true);
                    helper.setTo(toAccount);
                    helper.setSubject(subject);
                    helper.setText("nimadur",text);
                    javaMailSender.send(msg);
                }catch (MessagingException e){
                    throw new RuntimeException(e);
                }
                    }
                }
        );
        emailExecutor.shutdown();
    }
        //update email
    public void sendUpdateEmailVerification(ProfileEntity entity) {
        String jwt= JWTUtil.encode(entity.getId(),entity.getEmail());
        String url=serverUrl+"/api/v1/profile/public/verification/updateEmail/"+jwt;
        String subject="You tube update email verification link";
        StringBuilder builder= new StringBuilder();
        builder.append( String.format("<h1>Hello %s</h1>",entity.getName()));
        builder.append(" <p>");
        builder.append(String.format("<a href=\"%s\">Click the link to verify your account! </a>",url));
        builder.append(" </p>");
        sendMimeEmail(entity.getEmail(),subject,builder.toString());
        emailHistoryService.sendEmail(builder.toString(),subject,entity);
    }
}
