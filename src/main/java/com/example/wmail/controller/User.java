package com.example.wmail.controller;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class User {
	public static long userId = 0;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	public String verificationCode;
	public String name;
	public String password;
	public String emailAddress;
	public String recoveryEmail;
	public boolean verificationCodeChecked;


	@OneToOne
	@JoinColumn(name = "caixa_de_entrada_id")
	private CaixaDeEntrada caixaDeEntrada;
	@ManyToMany
	private List<User> usuariosBloqueados = new ArrayList<>();

	public User(String name, String password, String recoveryEmail) {
		this.id = ++userId;
		this.name = name;
		this.password = password;
		this.emailAddress = name.concat("Wmail.com");
		this.recoveryEmail = recoveryEmail;
		this.caixaDeEntrada = new CaixaDeEntrada(emailAddress);
	}

	public User() {
	}

	public long getId() {
		return id;
	}

	public String getRecoveryEmail() {
		return recoveryEmail;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public List<User> getUsuariosBloqueados() {
		return usuariosBloqueados;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCodeChecked(boolean verificationCodeChecked) {
		this.verificationCodeChecked = verificationCodeChecked;
	}

	public boolean isVerificationCodeChecked() {
		return verificationCodeChecked;
	}

	public User encontraUserPorId(long id, List<User> lista) {
		for (User userAtual : lista) {
			if (userAtual.getId() == id) {
				return userAtual;
			}
		}
		return null;
	}

	public void generateVerificationCode() {

		this.verificationCode = String.valueOf((int) (Math.random() * 900000) + 100000);
	}

	public void changePassword(String newPassword) {
		this.password = newPassword;
	}

	public void bloqueiaUsuario(long id) {
		User usuarioAhBloquear = encontraUserPorId(id, UserController.users);
		if (!usuarioEhBloqueado(id)) {
			usuariosBloqueados.add(usuarioAhBloquear);
			UserController.users.remove(usuarioAhBloquear);
		}
	}

	public void desbloqueiaUsuario(long id) {
		User user = encontraUserPorId(id, usuariosBloqueados);
		if (usuarioEhBloqueado(user.getId())) {
			usuariosBloqueados.remove(user);
			UserController.users.add(user);
		}
	}

	public boolean usuarioEhBloqueado(long id) {
		for (User userAtual : usuariosBloqueados) {
			if (userAtual.getId() == id) {
				return true;
			}
			if (userAtual.getId() != id) {
				throw new RuntimeException("Usuario não encontrado com o ID: " + id);
			}
		}
		return false;
	}
}
