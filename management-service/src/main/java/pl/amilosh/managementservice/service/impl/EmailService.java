package pl.amilosh.managementservice.service.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import freemarker.template.TemplateException;
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
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import pl.amilosh.managementservice.dto.email.EmailDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jakarta.mail.Message.RecipientType.TO;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;
    private final AmazonSimpleEmailService amazonSes;
    private final freemarker.template.Configuration freeMarkerConfigurationBean;

    public void sendEmail(EmailDto emailDto) throws MessagingException, TemplateException, IOException {
        generateHtml(emailDto);

        sendEmailViaSimpleMailMessage(emailDto);
        sendEmailViaMimeMessagePreparator(emailDto);
        sendEmailViaMimeMessageHelper(emailDto);
        sendEmailViaAmazonSimpleEmailService(emailDto);
    }

    private void generateHtml(EmailDto emailDto) throws TemplateException, IOException {
        var template = freeMarkerConfigurationBean.getTemplate(emailDto.getTemplateName());
        var htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, buildModel(emailDto));
        emailDto.setHtmlBody(htmlBody);
    }

    private Map<String, Object> buildModel(EmailDto emailDto) {
        Map<String, Object> model = new HashMap<>();
        model.put("fullName", emailDto.getFullName());
        return model;
    }

    private void sendEmailViaSimpleMailMessage(EmailDto emailDto) {
        var message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailDto.getTo());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getHtmlBody());
        mailSender.send(message);
    }

    private void sendEmailViaMimeMessagePreparator(EmailDto emailDto) {
        MimeMessagePreparator message = newMessage -> {
            newMessage.setFrom(from);
            newMessage.setRecipient(TO, new InternetAddress(emailDto.getTo()));
            newMessage.setSubject(emailDto.getSubject());
            newMessage.setText(emailDto.getHtmlBody(), "UTF-8", "html");
        };
        mailSender.send(message);
    }

    private void sendEmailViaMimeMessageHelper(EmailDto emailDto) throws MessagingException {
        var mimeMessage = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom(from);
        messageHelper.setTo(emailDto.getTo());
        messageHelper.setSubject(emailDto.getSubject());
        messageHelper.setText(emailDto.getHtmlBody(), true);
        mailSender.send(messageHelper.getMimeMessage());
    }

    private void sendEmailViaAmazonSimpleEmailService(EmailDto emailDto) {
        var destination = new Destination().withToAddresses(emailDto.getTo());
        var body = new Body()
            .withHtml(new Content().withCharset("UTF-8").withData(emailDto.getHtmlBody()));
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
