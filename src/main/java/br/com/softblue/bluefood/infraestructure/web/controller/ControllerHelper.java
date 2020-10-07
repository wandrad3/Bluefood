package br.com.softblue.bluefood.infraestructure.web.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import br.com.softblue.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.softblue.bluefood.domain.restaurante.CategoriaRestauranteRepository;

public class ControllerHelper {

	public static void setEditMode(Model model, boolean isEdit) {
		model.addAttribute("EditMode", isEdit);
	}

	public static void addCategoriasToRequest(CategoriaRestauranteRepository categoriaRestauranteRepository, Model model) {
			List<CategoriaRestaurante>categorias = categoriaRestauranteRepository.findAll(Sort.by("nome"));
			model.addAttribute("categorias",categorias);
	}
}
