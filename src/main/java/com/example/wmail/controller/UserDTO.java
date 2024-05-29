package com.example.wmail.controller;

public class UserDTO {
	public long id;
	public String username;
	public String emailAdress;
	public CaixaDeEntradaDTO caixaDeEntrada;

	public UserDTO(User user) {
		this.username = user.getName();
		this.emailAdress = user.getEmailAdress();

		this.caixaDeEntrada = new CaixaDeEntradaDTO(user.getCaixaDeEntrada());
	}

}
