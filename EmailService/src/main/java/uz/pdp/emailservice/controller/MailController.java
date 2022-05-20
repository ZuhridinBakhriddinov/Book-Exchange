package uz.pdp.emailservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import uz.pdp.emailservice.dto.BookRematchEmailDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email-service")
@RequiredArgsConstructor
public class MailController {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @PostMapping("/book-review-email")
    public void sendEmailAboutBookReview(@RequestBody BookRematchEmailDto bookRematchEmailDto)
            throws MessagingException {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("bookTitle", bookRematchEmailDto.getBookTitle());
        templateModel.put("reviewAuthor", bookRematchEmailDto.getAuthorName());
        templateModel.put("reviewBody", bookRematchEmailDto.getBody());
        templateModel.put("acceptUrl", bookRematchEmailDto.getAcceptUrl());
        templateModel.put("rejectUrl", bookRematchEmailDto.getRejectUrl());

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = "";
        try {
             htmlBody = thymeleafTemplateEngine.process("BookRematchEmailTemplate.html", thymeleafContext);
        } catch (Exception e){
            e.printStackTrace();
        }

        sendHtmlMessage(bookRematchEmailDto.getReceiverEmail(), bookRematchEmailDto.getSubject(), htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }

}
