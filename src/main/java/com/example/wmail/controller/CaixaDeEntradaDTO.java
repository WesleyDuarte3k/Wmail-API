package com.example.wmail.controller;

import java.util.ArrayList;
import java.util.List;

public class CaixaDeEntradaDTO {
	public String emailAdress;
	public List<EmailDTO> emailsEnviados = new ArrayList<>();
	public List<EmailDTO> emailsRecebidos = new ArrayList<>();

	public List<EmailDTO> respostas = new ArrayList<>();

	public CaixaDeEntradaDTO(CaixaDeEntrada caixaDeEntrada) {
		this.emailAdress = caixaDeEntrada.getEmailAddress();
		this.emailsEnviados = converteListaDeEmails(caixaDeEntrada.getEmailsEnviados());
		this.emailsRecebidos = converteListaDeEmails(caixaDeEntrada.getEmailsRecebidos());
	}

	private List<EmailDTO> converteListaDeEmails(List<Email> emails){
		List<EmailDTO> listaDTO = new ArrayList<>();
		for (Email emailAtual : emails){
			EmailDTO emailDTO = new EmailDTO(emailAtual);
			listaDTO.add(emailDTO);
		}
		return listaDTO;
	}
}
