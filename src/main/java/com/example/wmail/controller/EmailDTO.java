package com.example.wmail.controller;

import java.util.ArrayList;
import java.util.List;

public class EmailDTO {
	public String remetente;
	public String titulo;
	public String conteudo;
	public long id;
	public ArrayList<Email> respostas;
	public List<String> destinatarios;

	public EmailDTO(Email email) {
		this.remetente = email.getRemetente();
		this.destinatarios = destinatarios;
		this.titulo = email.getTitulo();
		this.conteudo = email.getConteudo();
		this.id = email.getId();
	}


	public EmailDTO(){}
}
