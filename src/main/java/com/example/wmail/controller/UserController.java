package com.example.wmail.controller;

import com.example.wmail.repository.CaixaDeEntradaRepository;
import com.example.wmail.repository.EmailRepository;
import com.example.wmail.repository.TokenRepository;
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
	public static User currentUser;
	public static User usuarioAlterado;
	public static Token currentToken;
	public static Boolean verificationCodeChecked = false;

	public static Boolean userIsLogged(String token, TokenRepository tokenRepository) {
		Optional<Token> optionalToken = tokenRepository.findByToken(token);
		if (optionalToken.isPresent()) {
			Token tokenFound = optionalToken.get();

			if (tokenFound.tokenExpired() || !tokenFound.getToken().equals(token)) {
				return false;
			}
			return true;

		}

		return false;
	}


	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CaixaDeEntradaRepository caixaDeEntradaRepository;
	@Autowired
	private EmailRepository emailRepository;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private EmailService emailService;


	@GetMapping("")
	public ResponseEntity<List<User>> getUsers() {
		List<User> usuarios = userRepository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(usuarios);
	}


	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		Optional<User> userOptional = userRepository.findByName(user.getName());

		if (userOptional.isPresent()) {
			User userFound = userOptional.get();
			if (user.getPassword().equals(userFound.getPassword())) {
				Optional<Token> tokenOptional = tokenRepository.findByToken(userFound.getToken());
				if (tokenOptional.isPresent()) {
					Token tokenFound = tokenOptional.get();

					if (tokenFound.tokenExpired()) {
						tokenFound.setCreatedAt();
						tokenRepository.save(tokenFound);

						currentUser = userFound;
						currentToken = tokenFound;

						userRepository.save(userFound);
						tokenRepository.save(tokenFound);

						return ResponseEntity.ok("Conectado. Token: " + tokenFound.getToken());
					} else {
						currentToken = tokenFound;
						currentUser = userFound;

						return ResponseEntity.ok("Conectado. Token: " + tokenFound.getToken());
					}
				} else {
					Token newToken = new Token(userFound);
					userFound.setToken(newToken.getToken());

					currentUser = userFound;
					currentToken = newToken;


					userRepository.save(userFound);
					tokenRepository.save(newToken);

					return ResponseEntity.ok("Conectado. Token: " + newToken.getToken());
				}
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
		}
	}


	@GetMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader String token) {
		if (userIsLogged(token, tokenRepository)) {
			currentToken.tokenExpired();
			userRepository.save(currentUser);

			return ResponseEntity.ok("Desconectado");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Precisa estar conectado");
	}

	@GetMapping("/displays-sent-emails")
	public ResponseEntity<List<Email>> exibeEmailsEnviados(@RequestHeader String token) throws UnsupportedEncodingException {
		EncryptedToken encryptedToken = new EncryptedToken(token);

		if (!encryptedToken.isExpired()) {
			User userFound = userRepository.findById(encryptedToken.userId).get();
			for (User user : userRepository.findAll()) {
				if (user.emailAddress.equals(userFound.getEmailAddress())) {
					return ResponseEntity.ok(user.getCaixaDeEntrada().getEmailsEnviados());
				}
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}

	@GetMapping("/received-emails")
	public ResponseEntity<List<Email>> exibeEmailsRecebidos(@RequestHeader String token) {
		if (userIsLogged(token, tokenRepository)) {
			if (Objects.nonNull(currentUser) && Objects.nonNull(currentToken)) {
				for (User user : userRepository.findAll()) {
					if (user.emailAddress.equals(currentUser.emailAddress)) {
						return ResponseEntity.ok(user.getCaixaDeEntrada().getEmailsRecebidos());
					}
				}
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}



	@PostMapping("/send-recovery-email")
	public ResponseEntity<String> sendRecoveryEmail(@RequestBody EmailAddressDTO emailAddressDTO) {
		Optional<User> optionalUser = userRepository.findByEmailAddress(emailAddressDTO.emailAddress);

		if (optionalUser.isPresent()){
			User userFound = optionalUser.get();

			userFound.generateVerificationCode();

			String toEmail = emailAddressDTO.emailRecovery = userFound.recoveryEmail;
			String subject = "Recuperação de Senha";
			String body = "Aqui está o seu código de recuperação: " + userFound.getVerificationCode();

			emailService.sendRecoveryEmail(toEmail, subject, body);

			userRepository.save(userFound);

			return ResponseEntity.ok("Email de recuperação enviado");
		}


		return ResponseEntity.ok("Email não encontrado");
	}

//	@PostMapping("/send-emails")
//	public ResponseEntity<String> escreveEmail(@RequestBody EmailDTO emailDTO) {
//		if (userIsLogged(token, tokenRepository)) {
//			List<CaixaDeEntrada> caixasDeEntrada = new ArrayList<>();
//
//			if (Objects.nonNull(currentUser) && Objects.nonNull(currentToken)) {
//				for (String destinatario : emailDTO.destinatarios) {
//					Optional<CaixaDeEntrada> caixaDeEntradaEncontrada = caixaDeEntradaRepository.findByEmailAddress(destinatario);
//
//					if (caixaDeEntradaEncontrada.isPresent()) {
//						caixasDeEntrada.add(caixaDeEntradaEncontrada.get());
//					} else {
//						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Destinatário não encontrado: " + destinatario);
//					}
//				}
//				Email email = new Email(emailDTO, caixasDeEntrada);
//
//				currentUser.getCaixaDeEntrada().escreveEmail(email, caixasDeEntrada);
//				userRepository.save(currentUser);
//
//				for (CaixaDeEntrada caixaDeEntrada : caixasDeEntrada) {
//					caixaDeEntrada.recebe(email);
//					caixaDeEntradaRepository.save(caixaDeEntrada);
//				}
//				return ResponseEntity.ok("Mensagem enviada!");
//			}
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não está logado. Não foi possível enviar!");
//		}
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não está logado. Não foi possível enviar!");
//	}


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
		if (Objects.nonNull(currentUser) && Objects.nonNull(currentToken)) {
			for (Email email : currentUser.getCaixaDeEntrada().getEmailsRecebidos()) {
				if (email.getId() == id) {
					currentUser.getCaixaDeEntrada().getEmailsRecebidos().remove(email);
					caixaDeEntradaRepository.save(currentUser.getCaixaDeEntrada());
					return ResponseEntity.ok("Email deletado com sucesso!");
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/delete-email-enviado/{id}")
	public ResponseEntity<String> deletaEmailEnviado(@PathVariable long id) {
		if (Objects.nonNull(currentUser) && Objects.nonNull(currentToken)) {
			for (Email email : currentUser.getCaixaDeEntrada().getEmailsEnviados()) {
				if (email.getId() == id) {
					currentUser.getCaixaDeEntrada().getEmailsEnviados().remove(email);
					caixaDeEntradaRepository.save(currentUser.getCaixaDeEntrada());
					return ResponseEntity.ok("Email deletado com sucesso!");
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@DeleteMapping("/bloqueia-usuario/{id}")
	public ResponseEntity<Void> bloqueiaUsuario(@PathVariable long id) {
		try {
			currentUser.bloqueiaUsuario(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/get-users")
	public ResponseEntity<List<User>> obtemListaDeUsuarios() {
		return ResponseEntity.ok(currentUser.getUsuariosBloqueados());
	}

	@DeleteMapping("/desbloqueia-user/{id}")
	public ResponseEntity<Void> desbloqueiaUsuario(@PathVariable long id) {
		try {
			currentUser.desbloqueiaUsuario(id);
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


//	@PostMapping("/send-recovery-email")
//	public ResponseEntity<String> sendRecoveryEmailInWmail(@RequestBody EmailAddressDTO emailAddressDTO) {
//		Optional<User> userFound = userRepository.findByEmailAddress(emailAddressDTO.emailAddress);
//
//		if (userFound.isPresent()) {
//			User user = userFound.get();
//			String recoveryEmail = user.getRecoveryEmail();
//			Optional<User> destinatarioFound = userRepository.findByEmailAddress(recoveryEmail);
//
//			if (destinatarioFound.isPresent()) {
//				user.generateVerificationCode();
//				userRepository.save(user);
//				currentUser = user;
//
//				Email emailRecovery = new Email();
//				emailRecovery.setConteudo("Seu código de verificação: " + user.getVerificationCode());
//				emailRecovery.adicionaDestinatario(userRepository.findAll(), user.getRecoveryEmail());
//				destinatarioFound.get().getCaixaDeEntrada().getEmailsRecebidos().add(emailRecovery);
//				userRepository.save(destinatarioFound.get());
//				userRepository.save(user);
//
//
//				return ResponseEntity.ok("Email de recuperação enviado");
//			}
//			return ResponseEntity.ok("Email de recuperação não encontrado");
//		}
//
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
//	}

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
		if (currentUser != null && currentUser.isVerificationCodeChecked()) {
			currentUser.changePassword(newPassword);
			currentUser.setVerificationCodeChecked(false);
			userRepository.save(currentUser);
			return ResponseEntity.ok("Senha alterada com sucesso");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Não autorizado para alterar a senha");
	}
}
