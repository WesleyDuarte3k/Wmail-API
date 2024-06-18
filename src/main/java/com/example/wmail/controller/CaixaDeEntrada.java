package com.example.wmail.controller;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CaixaDeEntrada {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String emailAddress;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Email> emailsEnviados = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL)
	private List<Email> emailsRecebidos = new ArrayList<>();


	public CaixaDeEntrada() {
	}

	public void escreveEmail(Email email) {
		Email emailAtual = new Email(UserController.usuarioLogado.getEmailAddress(), email.getDestinatario(), email.getTitulo(), email.getConteudo());
		emailsEnviados.add(emailAtual);
	}

	public void recebe(Email email){
		emailsRecebidos.add(email);
	}

	public void recebeResposta(Long id, Email email){
		for (Email emailAtual : emailsRecebidos){
			if (emailAtual.getId() == id){
				emailAtual.addResposta(email);
			}
		}
	}

	public List<Email> obtemEmailsRecebido(){
		return emailsRecebidos;
	}

	public CaixaDeEntrada(String emailAdress) {
		this.emailAddress = emailAdress;
	}



	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setEmailsEnviados(List<Email> emailsEnviados) {
		this.emailsEnviados = emailsEnviados;
	}

	public void setEmailsRecebidos(List<Email> emailsRecebidos) {
		this.emailsRecebidos = emailsRecebidos;
	}

	public List<Email> getEmailsRecebidos() {
		return emailsRecebidos;
	}
	public List<Email> getEmailsEnviados(){
		return emailsEnviados;
	}
	public String getEmailAddress() {
		return emailAddress;
	}

}


