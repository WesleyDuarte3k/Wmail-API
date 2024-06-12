package com.example.wmail.controller;

import com.example.wmail.repository.CaixaDeEntradaRepository;
import com.example.wmail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
	public static ArrayList<User> users = new ArrayList<>();
	public static User usuarioLogado;

	@Autowired
	private UserRepository userRepository;




	@GetMapping("")	public ResponseEntity<List<User>> getUsers() {
		List<User> usuarios = userRepository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(usuarios);
	}

	@PostMapping("/login")
	public ResponseEntity<User> iniciaSessao(@RequestBody User user){
		for (User usuarioAtual : userRepository.findAll()){
			if (usuarioAtual.getName().equals(user.getName()) && usuarioAtual.getPassword().equals(user.getPassword())){
				usuarioLogado = usuarioAtual;
				return ResponseEntity.ok(usuarioAtual);
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/sent-emails")
	public ResponseEntity<List<EmailDTO>> exibeEmailsEnviados(){

		if (usuarioLogado != null){
			CaixaDeEntradaDTO caixaDeEntradaDTO = new CaixaDeEntradaDTO(usuarioLogado.getCaixaDeEntrada());
			return ResponseEntity.ok().body(caixaDeEntradaDTO.emailsEnviados);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/received-emails")
	public ResponseEntity<List<Email>> exibeEmailsRecebidos(){

		if (usuarioLogado != null){
			CaixaDeEntradaDTO caixaDeEntradaDTO = new CaixaDeEntradaDTO(usuarioLogado.getCaixaDeEntrada());
			List<Email> emails = usuarioLogado.getCaixaDeEntrada().getEmailsRecebidos();
			return ResponseEntity.ok().body(emails);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/send-emails")
	public ResponseEntity<String> escreveEmail(@RequestBody EmailDTO emailDTO){
		Email email = new Email(emailDTO);
		try {
			if (usuarioLogado != null){
				for (User userAtual : userRepository.findAll()){
					if (email.getDestinatario().equals(userAtual.getEmailAdress())){
						usuarioLogado.getCaixaDeEntrada().escreveEmail(email);
						userAtual.getCaixaDeEntrada().recebe(email);
					}
				}
			}
		}catch (Exception exception){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.ok("Email enviado com sucesso! ID: " + email.getId());
	}

	@PostMapping("/responder/{id}")
	public ResponseEntity<String> responderEmail(@PathVariable long id, @RequestBody EmailDTO respostaDTO) {
		Email resposta = new Email(respostaDTO);
		for (Email email : usuarioLogado.getCaixaDeEntrada().obtemEmailsRecebido()) {
			if (email.getId() == id) {
				email.addResposta(resposta);
				usuarioLogado.getCaixaDeEntrada().obtemEmailsEnviados().add(resposta);
				usuarioLogado.getCaixaDeEntrada().recebeResposta(resposta);
				return ResponseEntity.ok("Resposta enviada com sucesso! ID da resposta: " + resposta.getId());
			}
		}
		return ResponseEntity.status(404).body("Email não encontrado");
	}

	@GetMapping("/obtem-fila-de-respostas/{id}")
	public ResponseEntity<List<Email>> exibeFilaDeRespostas(@PathVariable long id){
		if (usuarioLogado != null){
			CaixaDeEntradaDTO caixaDeEntradaDTO = new CaixaDeEntradaDTO(usuarioLogado.getCaixaDeEntrada());
			for (Email email : usuarioLogado.getCaixaDeEntrada().obtemEmailsRecebido()){
				if (email.getId() == id){
					email.getListaDeRespostas();
					return ResponseEntity.ok(email.getListaDeRespostas());
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/delete-email-recebido/{id}")
	public ResponseEntity<String> deletaEmailRecebido(@PathVariable long id){
		for (Email email : usuarioLogado.getCaixaDeEntrada().obtemEmailsRecebido()){
			if (id == email.getId()){
				usuarioLogado.getCaixaDeEntrada().obtemEmailsRecebido().remove(email);
			}
		}
		//usuarioLogado.getCaixaDeEntrada().obtemEmailsRecebido()
				//.removeIf((email) -> email.getId() == id);

		return ResponseEntity.ok("Email deletado com sucesso!");
	}

	@DeleteMapping("/delete-email-enviado/{id}")
	public ResponseEntity<String> deletaEmailEnviado(@PathVariable long id){
		for (Email email : usuarioLogado.getCaixaDeEntrada().obtemEmailsEnviados()){
			if (id == email.getId()){
				usuarioLogado.getCaixaDeEntrada().obtemEmailsEnviados().remove(email);
			}
		}
		return ResponseEntity.ok("Email deletado com sucesso!");
	}

	@DeleteMapping("/bloqueia-usuario/{id}")
	public ResponseEntity<Void> bloqueiaUsuario(@PathVariable long id){
		try {
			usuarioLogado.bloqueiaUsuario(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/get-users")
	public ResponseEntity<List<User>> obtemListaDeUsuarios(){
		return ResponseEntity.ok(usuarioLogado.getUsuariosBloqueados());
	}

	@DeleteMapping("/desbloqueia-user/{id}")
	public ResponseEntity<Void> desbloqueiaUsuario (@PathVariable long id){
		try {
			usuarioLogado.desbloqueiaUsuario(id);
			return ResponseEntity.ok().build();
		}catch (Exception exception){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/get-list-users")
	public ResponseEntity<List<User>>listaDeUsuario(){
		return ResponseEntity.ok(users);
	}

	@GetMapping("/get-list-of-user")
	public ResponseEntity<List<Long>>listaDeUmUsuario(){
//		return ResponseEntity.ok(UserController.usuarioLogado.getListaDeUsuarios());
		return ResponseEntity.ok().build();
	}
}
