package com.example.wmail.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class EncryptedToken {
	private LocalDateTime expiredAt;
	private Long userId;
	private String tokenString;

	public EncryptedToken(User user, LocalDateTime currentDate) {
		this.userId = user.getId();
		this.expiredAt = currentDate.plusMinutes(5);
	}

	public EncryptedToken(String tokenCodificado) throws UnsupportedEncodingException {
		byte[] tokenEmBytes = Base64.getDecoder().decode(tokenCodificado);
		String tokenString = new String(tokenEmBytes, "UTF-8");
		String [] primeiraSeparacaoDoToken = tokenString.split("=", 2);
		String [] segundaSeparacaoDoToken = primeiraSeparacaoDoToken[1].split(",");
		String [] terceiraSeparacaoDoToken = segundaSeparacaoDoToken[0].split("}");
		String [] primeiraSeparacaoDoId = segundaSeparacaoDoToken[1].split("=");
		String [] segundaSeparacaoDoId = primeiraSeparacaoDoId[1].split("}");

		this.expiredAt = LocalDateTime.parse(segundaSeparacaoDoToken[0]);
		this.userId = Long.parseLong(segundaSeparacaoDoId[0]);
	}

	public String encodeToken() {
		return Base64.getEncoder().encodeToString(toString().getBytes());
	}

	public boolean isExpired() {
		return expiredAt.isBefore(LocalDateTime.now());
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "{" +
			"expiredAt=" + expiredAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
			", userId=" + userId +
			'}';
	}
}


