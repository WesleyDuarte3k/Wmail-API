package com.example.wmail.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
	public static ArrayList<User>users = new ArrayList<>();
	public static User usuarioLogado;

	@GetMapping("")
	public ResponseEntity<List<User>> getUsers() {
		User richard = new User("Richard", "123");
		User wesley = new User("Wesley", "123");
		users.add(richard);
		users.add(wesley);
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@PostMapping("/login")
	public ResponseEntity<UserDTO> iniciaSessao(@RequestBody User user){
		for (User usuarioAtual : users){
			if (usuarioAtual.getName().equals(user.getName()) && usuarioAtual.getPassword().equals(user.getPassword())){
				usuarioLogado = usuarioAtual;
				UserDTO userDTO = new UserDTO(usuarioAtual);
				return ResponseEntity.ok(userDTO);
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
	public ResponseEntity<List<EmailDTO>> exibeEmailsRecebidos(){
		if (usuarioLogado != null){
			UserDTO userDTO = new UserDTO(usuarioLogado);

			return ResponseEntity.ok().body(userDTO.caixaDeEntrada.emailsRecebidos);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/send-emails")
	public ResponseEntity<Void> escreveEmail(@RequestBody EmailDTO emailDTO){
		Email email = new Email(emailDTO);
		try {
			usuarioLogado.getCaixaDeEntrada().escreveEmail(email);
		}catch (Exception exception){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}



}