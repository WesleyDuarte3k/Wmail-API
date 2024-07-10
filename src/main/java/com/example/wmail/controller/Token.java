package com.example.wmail.controller;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	public Token() {
	}

	public Token(User user) {
		this.id = getId()+1;
		this.user = user;
		this.token = generateToken();
		this.createdAt = LocalDateTime.now();
	}

	private String generateToken() {
		return String.valueOf((int) (Math.random() * 900000) + 100000);
	}

	public boolean tokenExpired (){
		return LocalDateTime.now().isAfter(this.createdAt.plusMinutes(2));
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt(){return createdAt;}

	public User getUser() {
		return user;
	}

	public String getToken() {
		return token;
	}





	public void setToken(String token) {
		this.token = token;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt;}
}
