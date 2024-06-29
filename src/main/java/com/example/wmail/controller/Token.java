package com.example.wmail.controller;

import jakarta.persistence.*;

@Entity
public class Token {

	public static long tokenId = 0;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "token")
	private String token;

	public Token() {
	}

	public Token(User user) {
		this.id = user.getId();
		this.user = user;
		this.token = generateToken();
	}

	private String generateToken() {
		return String.valueOf((int) (Math.random() * 900000) + 100000);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
