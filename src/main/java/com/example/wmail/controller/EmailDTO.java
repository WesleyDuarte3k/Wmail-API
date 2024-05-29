package com.example.wmail.controller;

public class EmailDTO {
	public String remetente;
	public String destinatario;
	public String titulo;
	public String conteudo;

	public EmailDTO(Email email) {
		this.remetente = email.getRemetente();
		this.destinatario = email.getDestinatario();
		this.titulo = email.getTitulo();
		this.conteudo = email.getConteudo();
	}

	public EmailDTO(){

	}
}
