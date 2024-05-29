package com.example.wmail.controller;

import lombok.Getter;

import java.util.ArrayList;

public class User {
		private long id;
		public String name;
		public String password;
		public String emailAdress;
	private CaixaDeEntrada caixaDeEntrada;



	public User(String name, String password) {
			this.name = name;
			this.password = password;
			this.emailAdress = name.concat("Wmail.com");
			this.caixaDeEntrada = new CaixaDeEntrada(emailAdress);
		}

	public String getName() {
		return name;
	}

	public CaixaDeEntrada getCaixaDeEntrada() {
		return caixaDeEntrada;
	}


	public String getPassword() {
		return password;
	}

	public String getEmailAdress() {
		return emailAdress;
	}

}
