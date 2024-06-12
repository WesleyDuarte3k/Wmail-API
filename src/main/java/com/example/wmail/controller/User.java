package com.example.wmail.controller;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
	public static long userId = 0;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	public String name;
	public String password;
	public String emailAdress;
	@OneToOne
	@JoinColumn(name = "caixa_de_entrada_id")
	private CaixaDeEntrada caixaDeEntrada;
	@ManyToMany
	private List<User> usuariosBloqueados = new ArrayList<>();

	public User(String name, String password) {
		this.id = ++userId;
		this.name = name;
		this.password = password;
		this.emailAdress = name.concat("Wmail.com");
		this.caixaDeEntrada = new CaixaDeEntrada(emailAdress);
	}

	public User() {
	}

	public long getId(){ return id; }

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

	public List<User> getUsuariosBloqueados() {
		return usuariosBloqueados;
	}
}