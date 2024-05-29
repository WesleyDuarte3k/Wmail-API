package com.example.wmail.controller;

public class Email {
	private String remetente;
	private String destinatario;
	private String titulo;
	private String conteudo;

	public Email(String remetente, String destinatario, String titulo, String conteudo) {
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.titulo = titulo;
		this.conteudo = conteudo;
	}
	public Email(EmailDTO emailDTO){
		this.remetente = emailDTO.remetente;
		this.destinatario = emailDTO.destinatario;
		this.titulo = emailDTO.titulo;
		this.conteudo = emailDTO.conteudo;
	}

	public String getRemetente() {
		return remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	@Override
	public String toString() {
		return "Email { " +
			"Remetente = '" + remetente + '\n' +
			"\n Destinatario = '" + destinatario + '\n' +
			"\n Titulo = '" + titulo + '\n' +
			"\n Conteudo = \n '" + conteudo + '\n' ;
	}
}

