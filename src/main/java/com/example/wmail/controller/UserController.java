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
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
	public static ArrayList<User> users = new ArrayList<>();
	public static User usuarioLogado;
	public static User usuarioAlterado;
	public static Boolean verificationCodeChecked = false;

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
		if (Objects.isNull(usuarioLogado)) {
			for (User usuarioAtual : userRepository.findAll()) {
				if (usuarioAtual.getName().equals(user.getName()) && usuarioAtual.getPassword().equals(user.getPassword())) {
					usuarioLogado = usuarioAtual;

					return ResponseEntity.ok(usuarioAtual);
				}
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/desloga")
	public ResponseEntity<String> desconecta() {
		if (Objects.nonNull(usuarioLogado)) {
			usuarioLogado = null;
			return ResponseEntity.ok("Desconectado");
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
					return ResponseEntity.ok(user.getCaixaDeEntrada().getEmailsRecebidos());
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/send-emails")
	public ResponseEntity<String> escreveEmail(@RequestBody EmailDTO emailDTO) {
		List<CaixaDeEntrada> caixasDeEntrada = new ArrayList<>();

		if (Objects.nonNull(usuarioLogado)) {
			for (String destinatario : emailDTO.destinatarios) {
				Optional<CaixaDeEntrada> caixaDeEntradaEncontrada = caixaDeEntradaRepository.findByEmailAddress(destinatario);

				if (caixaDeEntradaEncontrada.isPresent()) {
					caixasDeEntrada.add(caixaDeEntradaEncontrada.get());
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Destinatário não encontrado: " + destinatario);
				}
			}
			Email email = new Email(emailDTO, caixasDeEntrada);

			usuarioLogado.getCaixaDeEntrada().escreveEmail(email, caixasDeEntrada);
			userRepository.save(usuarioLogado);

			for (CaixaDeEntrada caixaDeEntrada : caixasDeEntrada) {
				caixaDeEntrada.recebe(email);
				caixaDeEntradaRepository.save(caixaDeEntrada);
			}
			return ResponseEntity.ok("Mensagem enviada!");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não está logado. Não foi possível enviar!");
	}


//	@PostMapping("/responder/{id}")
//	public ResponseEntity<String> responderEmail(@PathVariable long id, @RequestBody EmailDTO respostaDTO) {
//		Email resposta = new Email(respostaDTO);
//		if (Objects.nonNull(usuarioLogado)){
//			usuarioLogado.getCaixaDeEntrada().recebeResposta(id, resposta);
//			usuarioLogado.getCaixaDeEntrada().getEmailsEnviados().add(resposta);
//			caixaDeEntradaRepository.save(usuarioLogado.getCaixaDeEntrada());
//			return ResponseEntity.status(HttpStatus.CREATED).build();
//		}
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//	}


	@DeleteMapping("/delete-email-recebido/{id}")
	public ResponseEntity<String> deletaEmailRecebido(@PathVariable long id) {
		if (Objects.nonNull(usuarioLogado)) {
			for (Email email : usuarioLogado.getCaixaDeEntrada().getEmailsRecebidos()) {
				if (email.getId() == id) {
					usuarioLogado.getCaixaDeEntrada().getEmailsRecebidos().remove(email);
					caixaDeEntradaRepository.save(usuarioLogado.getCaixaDeEntrada());
					return ResponseEntity.ok("Email deletado com sucesso!");
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/delete-email-enviado/{id}")
	public ResponseEntity<String> deletaEmailEnviado(@PathVariable long id) {
		if (Objects.nonNull(usuarioLogado)) {
			for (Email email : usuarioLogado.getCaixaDeEntrada().getEmailsEnviados()) {
				if (email.getId() == id) {
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

	@PostMapping("/send-recovery-email/{id}")
	public ResponseEntity<String> sendRecoveryEmail(@PathVariable long id) {
		Optional<User> userFound = userRepository.findById(id);


		if (userFound.isPresent()) {
			User user = userFound.get();
			String recoveryEmail = user.getRecoveryEmail();
			Optional<User> destinatarioFound = userRepository.findByEmailAddress(recoveryEmail);

			if (destinatarioFound.isPresent()) {
				user.generateVerificationCode();
				userRepository.save(user);
				usuarioLogado = user;

				Email emailRecovery = new Email();
				emailRecovery.setConteudo("Seu código de verificação: " + user.getVerificationCode());
				emailRecovery.adicionaDestinatario(userRepository.findAll(), user.getRecoveryEmail());
				destinatarioFound.get().getCaixaDeEntrada().getEmailsRecebidos().add(emailRecovery);
				userRepository.save(destinatarioFound.get());
				userRepository.save(user);


				return ResponseEntity.ok("Email de recuperação enviado");
			}
			return ResponseEntity.ok("Email de recuperação não encontrado");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
	}

	@PostMapping("/check-verification-code")
	public ResponseEntity<String> checkVerificationCode(@RequestBody String verificationCode) {
		for (User user : userRepository.findAll()) {
			if (user.getVerificationCode().equals(verificationCode)) {
				user.setVerificationCodeChecked(true);
				userRepository.save(user);
			}
			return ResponseEntity.ok("Código de verificação correto");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código de verificação incorreto");
	}

	@PatchMapping("/change-password")
	public ResponseEntity<String> changePassword(@RequestBody String newPassword) {
		if (usuarioAlterado != null && usuarioAlterado.isVerificationCodeChecked()) {
			usuarioAlterado.changePassword(newPassword);
			usuarioAlterado.setVerificationCodeChecked(false);
			userRepository.save(usuarioAlterado);
			return ResponseEntity.ok("Senha alterada com sucesso");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Não autorizado para alterar a senha");
	}
}
