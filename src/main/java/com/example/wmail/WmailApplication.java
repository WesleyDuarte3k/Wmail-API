package com.example.wmail;

import com.example.wmail.controller.Email;
import com.example.wmail.controller.User;
import com.example.wmail.controller.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class WmailApplication {

	public static void main(String[] args) {
		criaUsuarios();
		SpringApplication.run(WmailApplication.class, args);
	}

	public static void criaUsuarios (){

		String nomeDoPrimeiroUsuario = "valdeciro";

		String senhaDoPrimeiroUsuario = "123";

		String emailDoPrimeiroUsuario = "valdeciro@wmail.com";


		String nomeDoSegundoUsuario = "valdeclei";

		String senhaDoSegundoUsuario = "123";

		String emailDoSegundoUsuario = "valdeclei@wmail.com";


		String nomeDoTerceiroUsuario = "valdemar";

		String senhaDoTerceiroUsuario = "123";

		String emailDoTerceiro = "valdemar@wmail.com";

		User userUm = new User(nomeDoPrimeiroUsuario, senhaDoPrimeiroUsuario);
		User userDois = new User(nomeDoSegundoUsuario, senhaDoSegundoUsuario);
		User userTres = new User(nomeDoTerceiroUsuario, senhaDoTerceiroUsuario);

		UserController.users.add(userUm);
		UserController.users.add(userDois);
		UserController.users.add(userTres);
	}

	public static void escreveEmailInicial(User user){
		String destinatario = "valdemar@wmail.com";
		String assunto = "Nova musica";
		String conteudo = "Por que que cê quebrou meu coração?\n" +
			"Deixou sem rumo e sem direção\n" +
			"Quando ela me teve na sua mão\n" +
			"E ainda ficou com a minha pior versão\n" +
			"Me acertou com jeito, a porra do meu peito\n" +
			"Eu tinha mil defeitos não ia me entregar\n" +
			"O que que eu faço agora?\n" +
			"Pois sempre que te olho, com esse sorriso lindo\n" +
			"Que não pode parar\n" +
			"Você me chamando pra dar um rolê\n" +
			"No seu mundo, só não ia porque já sofri e vivi de tudo\n" +
			"Meu mundo perdeu suas cores\n" +
			"Pro amor, nem tudo são flores\n" +
			"Tô contando dos meus amores\n" +
			"Dos meus amores\n" +
			"Tô contando dos meus amores\n" +
			"Meu mundo perdeu suas cores\n" +
			"Pro amor, nem tudo são flores\n" +
			"Tô contando dos meus amores\n" +
			"Dos meus amores\n" +
			"Tô contando dos meus amores"  ;

		Email email = new Email(user.getName(), destinatario, assunto, conteudo);
		user.getCaixaDeEntrada().escreveEmail(email);
	}

}
