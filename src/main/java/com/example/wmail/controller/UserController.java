package com.example.wmail.controller;

import com.example.wmail.repository.CaixaDeEntradaRepository;
import com.example.wmail.repository.EmailRepository;
import com.example.wmail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {
	public static ArrayList<User> users = new ArrayList<>();
	public static User usuarioLogado;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CaixaDeEntradaRepository caixaDeEntradaRepository;
	@Autowired
	private EmailRepository emailRepository;


	@GetMapping("")
	public ResponseEntity<List<User>> getUsers() {
		List<User> usuarios = userRepository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(usuarios);
	}

	@PostMapping("/login")
	public ResponseEntity<User> iniciaSessao(@RequestBody User user) {
		for (User usuarioAtual : userRepository.findAll()) {
			if (usuarioAtual.getName().equals(user.getName()) && usuarioAtual.getPassword().equals(user.getPassword())) {
				usuarioLogado = usuarioAtual;

				return ResponseEntity.ok(usuarioAtual);
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/sent-emails")
	public ResponseEntity<List<Email>> exibeEmailsEnviados() {
		if (Objects.nonNull(usuarioLogado)) {
			CaixaDeEntradaDTO caixaDeEntradaDTO = new CaixaDeEntradaDTO(usuarioLogado.getCaixaDeEntrada());
			userRepository.save(usuarioLogado);
			return ResponseEntity.ok().body(usuarioLogado.getCaixaDeEntrada().getEmailsEnviados());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/received-emails")
	public ResponseEntity<List<Email>> exibeEmailsRecebidos() {

		if (Objects.nonNull(usuarioLogado)) {
			for (User user : userRepository.findAll()) {
				if (user.emailAddress.equals(usuarioLogado.emailAddress)) {
					return ResponseEntity.ok(user.getCaixaDeEntrada().obtemEmailsRecebido());
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/send-emails")
	public ResponseEntity<EmailDTO> escreveEmail(@RequestBody EmailDTO emailDTO) {
		Email email = new Email(emailDTO);
		if (Objects.nonNull(usuarioLogado)) {
			for (User destinatario : userRepository.findAll()) {
				if (destinatario.emailAddress.equals(usuarioLogado.getEmailAddress())) {
					destinatario.getCaixaDeEntrada().escreveEmail(email);
					destinatario.getCaixaDeEntrada().recebe(email);

					userRepository.save(destinatario);

					return ResponseEntity.ok(emailDTO);
				} else if (destinatario.emailAddress.equals(email.getDestinatario())) {
					usuarioLogado.getCaixaDeEntrada().escreveEmail(email);
					destinatario.getCaixaDeEntrada().recebe(email);

					caixaDeEntradaRepository.save(usuarioLogado.getCaixaDeEntrada());
					caixaDeEntradaRepository.save(destinatario.getCaixaDeEntrada());

					return ResponseEntity.ok(emailDTO);
				}
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}


	@PostMapping("/responder/{id}")
	public ResponseEntity<String> responderEmail(@PathVariable long id, @RequestBody EmailDTO respostaDTO) {
		Email resposta = new Email(respostaDTO);
		if (Objects.nonNull(usuarioLogado)){
			usuarioLogado.getCaixaDeEntrada().recebeResposta(id, resposta);
			usuarioLogado.getCaixaDeEntrada().getEmailsEnviados().add(resposta);
			caixaDeEntradaRepository.save(usuarioLogado.getCaixaDeEntrada());
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/obtem-fila-de-respostas/{id}")
	public ResponseEntity<List<Email>> exibeFilaDeRespostas(@PathVariable long id) {
		if (usuarioLogado != null) {
			CaixaDeEntradaDTO caixaDeEntradaDTO = new CaixaDeEntradaDTO(usuarioLogado.getCaixaDeEntrada());
			for (Email email : usuarioLogado.getCaixaDeEntrada().obtemEmailsRecebido()) {
				if (email.getId() == id) {
					email.getListaDeRespostas();
					return ResponseEntity.ok(email.getListaDeRespostas());
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/delete-email-recebido/{id}")
	public ResponseEntity<String> deletaEmailRecebido(@PathVariable long id) {
		if (Objects.nonNull(usuarioLogado)){
			for (Email email : usuarioLogado.getCaixaDeEntrada().obtemEmailsRecebido()){
				if (email.getId() == id){
					usuarioLogado.getCaixaDeEntrada().obtemEmailsRecebido().remove(email);
					caixaDeEntradaRepository.save(usuarioLogado.getCaixaDeEntrada());
					return ResponseEntity.ok("Email deletado com sucesso!");
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/delete-email-enviado/{id}")
	public ResponseEntity<String> deletaEmailEnviado(@PathVariable long id) {
		if (Objects.nonNull(usuarioLogado)){
			for (Email email : usuarioLogado.getCaixaDeEntrada().getEmailsEnviados()){
				if (email.getId() == id){
					usuarioLogado.getCaixaDeEntrada().getEmailsEnviados().remove(email);
					caixaDeEntradaRepository.save(usuarioLogado.getCaixaDeEntrada());
					return ResponseEntity.ok("Email deletado com sucesso!");
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/bloqueia-usuario/{id}")
	public ResponseEntity<Void> bloqueiaUsuario(@PathVariable long id) {
		try {
			usuarioLogado.bloqueiaUsuario(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/get-users")
	public ResponseEntity<List<User>> obtemListaDeUsuarios() {
		return ResponseEntity.ok(usuarioLogado.getUsuariosBloqueados());
	}

	@DeleteMapping("/desbloqueia-user/{id}")
	public ResponseEntity<Void> desbloqueiaUsuario(@PathVariable long id) {
		try {
			usuarioLogado.desbloqueiaUsuario(id);
			return ResponseEntity.ok().build();
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/get-list-users")
	public ResponseEntity<List<User>> listaDeUsuario() {
		return ResponseEntity.ok(users);
	}

	@GetMapping("/get-list-of-user")
	public ResponseEntity<List<Long>> listaDeUmUsuario() {
//		return ResponseEntity.ok(UserController.usuarioLogado.getListaDeUsuarios());
		return ResponseEntity.ok().build();
	}
}
