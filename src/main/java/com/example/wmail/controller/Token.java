package com.example.wmail.controller;

import jakarta.persistence.*;

@Entity
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "token")
	private String token;

	public Token() {
	}

	public Token(User user) {
		this.id = getId()+1;
		this.user = user;
		this.token = generateToken();
	}

	private String generateToken() {
		return String.valueOf((int) (Math.random() * 900000) + 100000);
	}

	public Long getId() {
		return id;
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
