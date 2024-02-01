package com.account.SimplestCRUDExample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public String sendEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setText(body);
		message.setSubject(subject);
		
		mailSender.send(message);
		
		return "Mail sent successfully";
	}

}
