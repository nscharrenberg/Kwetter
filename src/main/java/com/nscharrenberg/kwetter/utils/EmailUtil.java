package com.nscharrenberg.kwetter.utils;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;

@Stateless

public class EmailUtil implements Serializable {
    @Resource(name = "java/mail/kwetter")
    private Session mailSession;

    public void send() throws MessagingException {
        Message message = new MimeMessage(mailSession);

        message.setSubject("Welcome");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("nscharrenberg@hotmail.com"));
        message.setContent("<p>Hi <b>You!</b> </p> <p>How are you doing?</p>", "text/html; charset=utf-8");

        Transport.send(message);
    }
}
