package com.example.wmail.controller;

import com.example.wmail.repository.UserRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CaixaDeEntrada {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String emailAddress;
	@OneToMany
	private List<Email> emailsEnviados = new ArrayList<>();
	@OneToMany
	private List<Email> emailsRecebidos = new ArrayList<>();


	public CaixaDeEntrada() {
	}

	public void escreveEmail(Email email) {
		Email emailAtual = new Email(UserController.usuarioLogado.getEmailAdress(), email.getDestinatario(), email.getTitulo(), email.getConteudo());
		emailsEnviados.add(emailAtual);
	}

	public void recebe(Email email){
		emailsRecebidos.add(email);
	}

	public void recebeResposta(Email respostaEmail){
		emailsRecebidos.add(respostaEmail);
	}

	public List<Email> obtemEmailsRecebido(){
		return emailsRecebidos;
	}

	public CaixaDeEntrada(String emailAdress) {
		this.emailAddress = emailAdress;
	}

	public List<Email> obtemEmailsEnviados(){
		return emailsEnviados;
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

	public List<Email> getEmailsEnviados() {
		return emailsEnviados;
	}

	public List<Email> getEmailsRecebidos() {
		return emailsRecebidos;
	}

	public String getEmailAddress() {
		return emailAddress;
	}
}


