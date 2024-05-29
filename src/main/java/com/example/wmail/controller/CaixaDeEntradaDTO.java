package com.example.wmail.controller;

import java.util.ArrayList;

public class CaixaDeEntradaDTO {
	public String emailAdress;
	public ArrayList<EmailDTO> emailsEnviados = new ArrayList<>();
	public ArrayList<EmailDTO> emailsRecebidos = new ArrayList<>();

	public CaixaDeEntradaDTO(CaixaDeEntrada caixaDeEntrada) {
		this.emailAdress = caixaDeEntrada.getEmailAdress();
		this.emailsEnviados = converteListaDeEmails(caixaDeEntrada.obtemEmailsEnviados());
		this.emailsRecebidos = converteListaDeEmails(caixaDeEntrada.obtemEmailsRecebido());
	}

	private ArrayList<EmailDTO> converteListaDeEmails(ArrayList<Email> emails){
		ArrayList<EmailDTO> listaDTO = new ArrayList<>();
		for (Email emailAtual : emails){
			EmailDTO emailDTO = new EmailDTO(emailAtual);
			listaDTO.add(emailDTO);
		}
		return listaDTO;
	}
}
