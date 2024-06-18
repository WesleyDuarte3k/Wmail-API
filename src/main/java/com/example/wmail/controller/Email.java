package com.example.wmail.controller;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Email {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String remetente;
	private String destinatario;
	private String titulo;
	private String conteudo;
	@OneToMany
	private List<Email> respostas = new ArrayList<>();


	public Email(String remetente, String destinatario, String titulo, String conteudo) {
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.titulo = titulo;
		this.conteudo = conteudo;
		this.respostas = new ArrayList<>();
	}
	public Email(EmailDTO emailDTO){
		this.remetente = emailDTO.remetente;
		this.destinatario = emailDTO.destinatario;
		this.titulo = emailDTO.titulo;
		this.conteudo = emailDTO.conteudo;
		this.respostas = emailDTO.respostas;
	}

	public Email() {
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
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

	@Override
	public String toString() {
		return "Email { " +
				"ID = '" + id + '\n' +
			"Remetente = '" + remetente + '\n' +
			"\n Destinatario = '" + destinatario + '\n' +
			"\n Titulo = '" + titulo + '\n' +
			"\n Conteudo = \n '" + conteudo +'\n';
	}
}

