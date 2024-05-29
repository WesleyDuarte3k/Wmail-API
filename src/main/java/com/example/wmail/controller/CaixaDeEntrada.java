package com.example.wmail.controller;

import java.util.ArrayList;

public class CaixaDeEntrada {

	private String emailAdress;
	private final ArrayList<Email> emailsEnviados = new ArrayList<>();
	private final ArrayList<Email> emailsRecebidos = new ArrayList<>();

	public void escreveEmail(Email email) {
		Email emailAtual = new Email(UserController.usuarioLogado.getEmailAdress(), email.getDestinatario(), email.getTitulo(), email.getConteudo());



		for (User userAtual : UserController.users){
			if (userAtual.getEmailAdress().equals(email.getDestinatario())){
				userAtual.getCaixaDeEntrada().recebe(emailAtual);
				emailsEnviados.add(emailAtual);
				return;
			}
		}
		throw new RuntimeException("Destinatario não encontrado!");
	}

	public void recebe(Email email){
		emailsRecebidos.add(email);
	}

	public ArrayList<Email> obtemEmailsRecebido(){
		return emailsRecebidos;
	}

	public CaixaDeEntrada(String emailAdress) {
		this.emailAdress = emailAdress;
	}

	public ArrayList<Email> obtemEmailsEnviados(){
		return emailsEnviados;
	}

	public String getEmailAdress() {
		return emailAdress;
	}
}


