package pl.amilosh.managementservice.service.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import pl.amilosh.managementservice.dto.email.EmailDto;

import static jakarta.mail.Message.RecipientType.TO;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;
    private final AmazonSimpleEmailService amazonSes;

    public void sendEmail(EmailDto emailDto) throws MessagingException {
        sendEmailViaSimpleMailMessage(emailDto);
        sendEmailViaMimeMessagePreparator(emailDto);
        sendEmailViaMimeMessageHelper(emailDto);
        sendEmailViaAmazonSimpleEmailService(emailDto);
    }

    private void sendEmailViaSimpleMailMessage(EmailDto emailDto) {
        var message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailDto.getTo());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getBody());
        mailSender.send(message);
    }

    private void sendEmailViaMimeMessagePreparator(EmailDto emailDto) {
        MimeMessagePreparator message = newMessage -> {
            newMessage.setFrom(from);
            newMessage.setRecipient(TO, new InternetAddress(emailDto.getTo()));
            newMessage.setSubject(emailDto.getSubject());
            newMessage.setText(emailDto.getBody());
        };
        mailSender.send(message);
    }

    private void sendEmailViaMimeMessageHelper(EmailDto emailDto) throws MessagingException {
        var mimeMessage = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom(from);
        messageHelper.setTo(emailDto.getTo());
        messageHelper.setSubject(emailDto.getSubject());
        messageHelper.setText(emailDto.getBody());
        mailSender.send(messageHelper.getMimeMessage());
    }

    private void sendEmailViaAmazonSimpleEmailService(EmailDto emailDto) {
        var destination = new Destination().withToAddresses(emailDto.getTo());
        var body = new Body()
            .withText(new Content().withCharset("UTF-8").withData(emailDto.getBody()));
        var message = new Message()
            .withBody(body)
            .withSubject(new Content().withCharset("UTF-8").withData(emailDto.getSubject()));
        var sendEmailRequest = new SendEmailRequest()
            .withSource(from)
            .withDestination(destination)
            .withMessage(message);
        amazonSes.sendEmail(sendEmailRequest);
    }
}
