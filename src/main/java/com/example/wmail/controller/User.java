package com.example.wmail.controller;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	public long getId(){ return id; }

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode() {
		Random random = new Random();

		Map<Integer, String> mapaAlfabeto = new HashMap<>();
		mapaAlfabeto.put(0, "a");
		mapaAlfabeto.put(1, "b");
		mapaAlfabeto.put(2, "c");
		mapaAlfabeto.put(3, "d");
		mapaAlfabeto.put(4, "e");
		mapaAlfabeto.put(5, "f");
		mapaAlfabeto.put(6, "g");
		mapaAlfabeto.put(7, "h");
		mapaAlfabeto.put(8, "i");
		mapaAlfabeto.put(9, "1");
		mapaAlfabeto.put(10, "2");
		mapaAlfabeto.put(11, "3");
		mapaAlfabeto.put(12, "4");
		mapaAlfabeto.put(13, "5");
		mapaAlfabeto.put(14, "6");
		mapaAlfabeto.put(15, "7");
		mapaAlfabeto.put(16, "8");
		mapaAlfabeto.put(17, "9");

		long upperbound = 18;

		String verificationCodeRandom = "";
		for (int i = 0; i <= 9; i++){
			long verificationCodeToken = random.nextLong(upperbound);
			verificationCodeRandom.concat(mapaAlfabeto.get(verificationCodeToken));
		}

		this.verificationCode = verificationCodeRandom;
	}

	public String getRecoveryEmail() { return recoveryEmail; }

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

	public void bloqueiaUsuario (long id){
		User usuarioAhBloquear = encontraUserPorId(id, UserController.users);
		if (!usuarioEhBloqueado(id)){
			usuariosBloqueados.add(usuarioAhBloquear);
			UserController.users.remove(usuarioAhBloquear);
		}
	}

	public void desbloqueiaUsuario(long id){
		User user = encontraUserPorId(id, usuariosBloqueados);
		if (usuarioEhBloqueado(user.getId())){
			usuariosBloqueados.remove(user);
			UserController.users.add(user);
		}
	}

	public boolean usuarioEhBloqueado(long id){
		for (User userAtual : usuariosBloqueados){
			if (userAtual.getId() == id){
				return true;
			}
			if (userAtual.getId() != id ) {
				throw new RuntimeException("Usuario não encontrado com o ID: " + id);
			}
		}
		return false;
	}

	public User encontraUserPorId(long id, List<User> lista){
		for (User userAtual : lista){
			if (userAtual.getId() == id){
				return userAtual;
			}
		}
		return null;
	}

//	public Long generateRecoveryToken(Long id, List<User> usuarios){
//		for (User user : usuarios){
//			if (user.getId() == id){
//				Random random = new Random();
//
//				long upperbound = 26;
//				long randomToken = random.nextLong(upperbound);
//
//				return randomToken;
//			}
//		}
//		return null;
//	}

	public Email generateRecoveryEmail(){
		Email email = new Email();
		setVerificationCode();

		String conteudo = getVerificationCode();
		email.setConteudo(conteudo);

		return email;
	}

	public void sendRecoveryEmail(List<User> usuarios){
		for (User user : usuarios){
			if (user.getEmailAddress().equals(recoveryEmail)){
				user.getCaixaDeEntrada().recebe(generateRecoveryEmail());
			}
		}
	}

	public void changePassword(String newPassword){
			this.password = newPassword;
	}



	public List<User> getUsuariosBloqueados() {
		return usuariosBloqueados;
	}
}