package com.example.wmail.controller;

public class UserDTO {
	public long id;
	public String username;
	public String emailAdress;
	public CaixaDeEntradaDTO caixaDeEntrada;

	public UserDTO(User user) {
		this.username = user.getName();
		this.emailAdress = user.getEmailAddress();
		this.id = user.getId();
		this.caixaDeEntrada = new CaixaDeEntradaDTO(user.getCaixaDeEntrada());
	}

	public UserDTO(String emailAdress){
		this.emailAdress = emailAdress;

	}
}
