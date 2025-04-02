package br.com.batista.desafio01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Desafio01Application {

	public static void main(String[] args) {
		SpringApplication.run(Desafio01Application.class, args);
	}

}
