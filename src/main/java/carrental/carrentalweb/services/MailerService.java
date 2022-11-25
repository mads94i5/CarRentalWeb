package carrental.carrentalweb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

// Mads
@Service
public class MailerService {
    @Value("${car-rental.email.username}")
    private String username;
    @Value("${car-rental.email.password}")
    private String password;
    JavaMailSender emailSender = getJavaMailSender();

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
        }};

        Session session = Session.getInstance(props, authenticator);
        mailSender.setSession(session);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        return mailSender;
    }

    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }

    public void sendMessageWithAttachment(String to, String subject, String text, File attached) throws MessagingException {
        System.out.println(emailSender);
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        FileSystemResource file = new FileSystemResource(attached);
        helper.addAttachment(file.getFilename(), file);

        emailSender.send(message);
    }

}
