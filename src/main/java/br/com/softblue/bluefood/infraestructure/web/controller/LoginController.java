package br.com.softblue.bluefood.infraestructure.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;

@Controller
public class LoginController {
	
	@GetMapping (path = {"/login","/"})
	
	public String login() {
		return "login";
	}
	
	@GetMapping(path="/login-error")
	public String loginError(Model model) {
		model.addAttribute("msg", "Credenciais inválidas!");
		return "login";
		
	}

}
