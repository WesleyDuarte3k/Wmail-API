package com.example.wmail;

import com.example.wmail.controller.Email;
import com.example.wmail.controller.User;
import com.example.wmail.controller.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.TimeZone;

@SpringBootApplication
public class WmailApplication {

	public static void main(String[] args) {
//		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")));
		SpringApplication.run(WmailApplication.class, args);
	}
}
