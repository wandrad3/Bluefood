package br.com.softblue.bluefood;

import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@SpringBootApplication
public class BluefoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(BluefoodApplication.class, args);
	}

}
