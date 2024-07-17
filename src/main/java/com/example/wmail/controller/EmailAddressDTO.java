package com.example.wmail.controller;

import java.util.Optional;

public class EmailAddressDTO {
	public String emailAddress;
	public String emailRecovery;

	public EmailAddressDTO(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public EmailAddressDTO (){}

}
