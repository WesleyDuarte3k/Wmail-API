package com.example.wmail.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;

class EncryptedTokenTest {

	@Test
	void deveCodificarToken() throws UnsupportedEncodingException {
		User user = new User();
		user.setId(1L);
		LocalDateTime dataAtual = LocalDateTime.parse("2024-07-20T20:00:00");
		EncryptedToken token = new EncryptedToken(user, dataAtual);
		String tokenCodificado = token.encodeToken();

		Assertions.assertThat(tokenCodificado).isEqualTo("e2V4cGlyZWRBdD0yMDI0LTA3LTIwVDIwOjA1OjAwLCB1c2VySWQ9MX0=");
	}

	@Test
	void deveDecodificarToken() throws UnsupportedEncodingException {
		EncryptedToken token = new EncryptedToken("e2V4cGlyZWRBdD0yMDI0LTA3LTIwVDIwOjA1OjAwLCB1c2VySWQ9MX0=");
		Assertions.assertThat(token.toString()).isEqualTo("{expiredAt=2024-07-20T20:05:00, userId=1}");
	}

	@Test
	void deveArmazenarInformacoesDoToken() {
		User user = new User();
		user.setId(1L);
		LocalDateTime dataAtual = LocalDateTime.parse("2024-07-20T20:00:00");
		EncryptedToken token = new EncryptedToken(user, dataAtual);

		Assertions.assertThat(token.getUserId()).isEqualTo(user.getId());
		Assertions.assertThat(token.toString()).contains("expiredAt=2024-07-20T20:05:00");
	}
}
