package com.example.wmail.controller;

import com.example.wmail.repository.UserRepository;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Email {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String remetente;
	private String titulo;
	private String conteudo;

	@ManyToMany
	private List<CaixaDeEntrada> destinatarios = new ArrayList<>();

	@OneToMany
	private List<Email> respostas = new ArrayList<>();


//	public Email(String remetente, String destinatario, String titulo, String conteudo) {
//		this.remetente = remetente;
//		this.destinatario = destinatario;
//		this.titulo = titulo;
//		this.conteudo = conteudo;
//		this.respostas = new ArrayList<>();
//	}
	public Email(EmailDTO emailDTO, List<CaixaDeEntrada> caixaDeEntradas){
		this.remetente = emailDTO.remetente;
		this.destinatarios = caixaDeEntradas;
		this.titulo = emailDTO.titulo;
		this.conteudo = emailDTO.conteudo;
		this.respostas = emailDTO.respostas;
	}



	public Email(String remetente, List<CaixaDeEntrada> destinatarios, String titulo, String conteudo) {
		this.remetente = remetente;
		this.destinatarios = destinatarios;
		this.titulo = titulo;
		this.conteudo = conteudo;
		this.respostas = new ArrayList<>();
	}

	public Email() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CaixaDeEntrada> getDestinatarios() {
		return destinatarios;
	}
	public long getId(){
		return id;
	}

	public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}



	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public List<Email> getListaDeRespostas() {
		return respostas;
	}

	public void setRespostas(List<Email> respostas) {
		this.respostas = respostas;
	}

	public void addResposta(Email email) {
		respostas.add(email);
	}

	public void adicionaDestinatario(List<User> users, String emailAddress){
		for (User user : users){
			if (user.getEmailAddress().equals(emailAddress)){
				destinatarios.add(user.getCaixaDeEntrada());
			}
		}
	}

	@Override
	public String toString() {
		return "Email { " +
				"ID = '" + id + '\n' +
			"Remetente = '" + remetente + '\n' +
			"\n Destinatarios = '" + destinatarios + '\n' +
			"\n Titulo = '" + titulo + '\n' +
			"\n Conteudo = \n '" + conteudo +'\n';
	}
}

