package com.example.wmail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@SuppressWarnings("ALL")
@Service
public class EmailService {

	@Autowired
	public JavaMailSender mailSender;

	public void sendRecoveryEmail(String toEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		message.setFrom("seu.email@gmail.com");

		mailSender.send(message);
	}
}
