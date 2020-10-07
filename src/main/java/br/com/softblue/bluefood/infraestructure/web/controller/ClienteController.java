package br.com.softblue.bluefood.infraestructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.softblue.bluefood.application.service.ServiceCliente;
import br.com.softblue.bluefood.application.service.ServiceRestaurante;
import br.com.softblue.bluefood.application.service.ValidationException;
import br.com.softblue.bluefood.domain.cliente.Cliente;
import br.com.softblue.bluefood.domain.cliente.ClienteRepository;
import br.com.softblue.bluefood.domain.pedido.Pedido;
import br.com.softblue.bluefood.domain.pedido.PedidoRepository;
import br.com.softblue.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.softblue.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapio;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.softblue.bluefood.domain.restaurante.Restaurante;
import br.com.softblue.bluefood.domain.restaurante.RestauranteComparator;
import br.com.softblue.bluefood.domain.restaurante.RestauranteRepository;
import br.com.softblue.bluefood.domain.restaurante.SearchFilter;
import br.com.softblue.bluefood.util.SecurityUtils;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;

@Controller
@RequestMapping(path = "/cliente/")
public class ClienteController {

	@Autowired
	private ServiceCliente serviceCliente;
	@Autowired
	private ServiceRestaurante serviceRestaurante;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private ItemCardapioRepository itemCardapioRepository;

	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;

	@GetMapping(path = { "/home" })
	public String home(Model model) {

		List<CategoriaRestaurante> categorias = categoriaRestauranteRepository.findAll(Sort.by("nome"));

		model.addAttribute("searchfilter", new SearchFilter());

		model.addAttribute("categorias", categorias);
		
		List<Pedido> pedidos = pedidoRepository.listarPedidos(SecurityUtils.loggedCliente().getId());
		model.addAttribute("pedidos", pedidos);

		return "cliente-home";
	}

	@GetMapping(path = "/edit")
	private String edit(Model model, Cliente cliente) {

		Integer clienteId = SecurityUtils.loggedCliente().getId();
		cliente = clienteRepository.findById(clienteId).orElseThrow();
		model.addAttribute("cliente", cliente);

		ControllerHelper.setEditMode(model, true);
		return "cadastro-cliente";

	}

	@PostMapping(path = "/save")
	public String save(Model model, @ModelAttribute("cliente") @Valid Cliente cliente, Errors errors) {

		if (!errors.hasErrors()) {
			try {
				serviceCliente.saveCliente(cliente);
				model.addAttribute("msg", "Cliente cadastrado com sucesso");

			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());

			}

		}

		ControllerHelper.setEditMode(model, true);
		return "cadastro-cliente";

	}

	@GetMapping(path = "/search")
	public String search(@ModelAttribute("searchfilter") SearchFilter filter, Model model,
			@RequestParam(value = "cmd", required = false) String command) {

		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		filter.proccessFilter(command);
		List<Restaurante> restaurantes = serviceRestaurante.search(filter);
		model.addAttribute("searchfilter", filter);
		model.addAttribute("restaurantes", restaurantes);
		model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());

		return "cliente-busca";

	}

	@GetMapping(path = "/restaurante")
	public String viewRestaurante(@RequestParam(value = "restauranteId") Integer restauranteid,
			@RequestParam(value = "categoria", required = false) String categoria, Model model) {

		Restaurante restaurante = restauranteRepository.findById(restauranteid).orElseThrow();
		model.addAttribute("restaurante", restaurante);
		model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());

		List<String> categorias = itemCardapioRepository.findCategorias(restauranteid);

		model.addAttribute("categorias", categorias);
		List<ItemCardapio> ItensComDestaque;
		List<ItemCardapio> ItensSemDestaque;

		if (categoria == null) {
			ItensComDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteid, true);
			ItensSemDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteid, false);

		}else {
			ItensComDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteid, true, categoria);
			ItensSemDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteid, false, categoria);			
		}
		

		model.addAttribute("ItensComDestaque", ItensComDestaque);
		model.addAttribute("ItensSemDestaque", ItensSemDestaque);
		model.addAttribute("categoriaSelecionada",categoria);

		return "cliente-restaurante";
	}

}
