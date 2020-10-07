package br.com.softblue.bluefood.infraestructure.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.softblue.bluefood.application.service.ServiceCliente;
import br.com.softblue.bluefood.application.service.ServiceRestaurante;
import br.com.softblue.bluefood.application.service.ValidationException;
import br.com.softblue.bluefood.domain.cliente.*;
import br.com.softblue.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import br.com.softblue.bluefood.domain.restaurante.Restaurante;

@Controller
@RequestMapping(path ="/public")
public class PublicController {
	
	@Autowired
	private ServiceCliente serviceCliente;
	@Autowired
	ServiceRestaurante serviceRestaurante;
	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@GetMapping("/cliente/new")
	public String newCliente(Model model) {
		
		//Cliente c = new Cliente();
		//c.setNome("Wesley");
		model.addAttribute("cliente", new Cliente());
		ControllerHelper.setEditMode(model, false);
		
		
		return "cadastro-cliente";
		
	}
	
	@GetMapping("/restaurante/new")
	public String newRestaurante(Model model) {
		
		//Cliente c = new Cliente();
		//c.setNome("Wesley");
		model.addAttribute("restaurante", new Restaurante());
		ControllerHelper.setEditMode(model, false);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		
		return "restaurante-cadastro";
		
	}
	
	@PostMapping(path="/cliente/save")
	public String saveCliente(@ModelAttribute("cliente") @Valid  Cliente cliente, 
			Errors errors,
			Model model) {
		
		if(!errors.hasErrors()) {
			try {
				serviceCliente.saveCliente(cliente);
				model.addAttribute("msg","Cliente cadastrado com sucesso");
				
			}catch (ValidationException e){
				errors.rejectValue("email", null, e.getMessage());
				
			}
			
		}
		
		
		ControllerHelper.setEditMode(model, false);
		return "cadastro-cliente"; 
		
		
	}
	
	
	@PostMapping(path="/restaurante/save")
	public String saveRestaurante (@ModelAttribute("restaurante") @Valid  Restaurante restaurante, 
			Errors errors,
			Model model) {
		
		if(!errors.hasErrors()) {
			try {
				
				serviceRestaurante.saveRestaurante(restaurante);
				model.addAttribute("msg","Restaurante cadastrado com sucesso");
				
			}catch (ValidationException e){
				errors.rejectValue("email", null, e.getMessage());
				
			}
			
		}
		
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		ControllerHelper.setEditMode(model, false);
		return "restaurante-cadastro"; 
		
		
	}

}
