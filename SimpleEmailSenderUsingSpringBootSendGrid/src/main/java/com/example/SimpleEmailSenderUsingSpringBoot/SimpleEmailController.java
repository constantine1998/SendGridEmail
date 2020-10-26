package com.example.SimpleEmailSenderUsingSpringBoot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping(value="/email")
public class SimpleEmailController {

    @Autowired
    public JavaMailSender javaMailSender;

    @GetMapping(value = "/sendEmail")
    public String sendEmail()
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo("prasadmanoj09@gmail.com");
        message.setSubject("SpringBootApplication");
        message.setText("Hi, Hello World");
        message.setFrom("manojrules123@protonmail.com");
        javaMailSender.send(message);
        return "Successfully sent email";
    }
//http://localhost:465/email/sendEmail

    @PostMapping(value = "/sendEmail1")
    public String sendEmail(@RequestBody Email email)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setSubject(email.getMessageSubject());
        message.setText(email.getMessageBody());
        message.setFrom(email.getFrom());
        javaMailSender.send(message);
        return "Successfully sent email";
    }

    @PostMapping("/sendEmailAttachment")
    public String sendEmailAttachment(@RequestBody Email email) throws MessagingException {

        MimeMessage message=javaMailSender.createMimeMessage();

        MimeMessageHelper helper=new MimeMessageHelper(message, true);
        helper.setTo(email.getTo());
        helper.setSubject(email.getMessageSubject());
        helper.setText(email.getMessageBody());
        helper.setFrom(email.getFrom());
        ClassPathResource path=new ClassPathResource("GreenArrow.png");
        helper.addAttachment("GreenArrow.png", path);

        javaMailSender.send(message);


        return "Succesfully sent Email";

    }

}
