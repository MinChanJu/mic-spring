package com.example.mic_spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.mic_spring.domain.dto.ReportDTO;
import com.example.mic_spring.security.Token;

@Service
public class MailService {

  private JavaMailSender mailSender;

  @Value("${EMAIL_USERNAME}")
  private final String myEmail = "mcj00220077@gmail.com";

  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendMail(ReportDTO reportDTO, Token token) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(myEmail);
    message.setSubject(reportDTO.getTitle());

    String fullMessage = "보낸 사람: " + reportDTO.getEmail() + "\n\n" + reportDTO.getContent();

    message.setText(fullMessage);
    message.setFrom(myEmail);

    mailSender.send(message);
  }
}