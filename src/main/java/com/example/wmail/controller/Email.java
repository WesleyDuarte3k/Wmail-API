package com.example.wmail.controller;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Email {
	public static long idCounter = 0;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	private String remetente;
	private String destinatario;
	private String titulo;
	private String conteudo;
	@OneToMany
	private ArrayList<Email> listaDeRespostas = new ArrayList<>();


	public Email(String remetente, String destinatario, String titulo, String conteudo) {
		this.id = ++idCounter;
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.titulo = titulo;
		this.conteudo = conteudo;
		this.listaDeRespostas = new ArrayList<>();
	}
	public Email(EmailDTO emailDTO){
		this.id = ++idCounter;
		this.remetente = emailDTO.remetente;
		this.destinatario = emailDTO.destinatario;
		this.titulo = emailDTO.titulo;
		this.conteudo = emailDTO.conteudo;
		this.listaDeRespostas = emailDTO.listaDeRespostas;
	}

	public Email() {
	}

	public long getId(){
		return id;
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

	public List<Email> getListaDeRespostas() {
		return listaDeRespostas;
	}

	public void addResposta(Email resposta) {
		listaDeRespostas.add(resposta);
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

