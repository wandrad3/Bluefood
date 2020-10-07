package br.com.softblue.bluefood.infraestructure.web.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.softblue.bluefood.application.service.RelatorioService;
import br.com.softblue.bluefood.application.service.ServiceRestaurante;
import br.com.softblue.bluefood.application.service.ValidationException;
import br.com.softblue.bluefood.domain.cliente.Cliente;
import br.com.softblue.bluefood.domain.cliente.ClienteRepository;
import br.com.softblue.bluefood.domain.pedido.ItemPedido;
import br.com.softblue.bluefood.domain.pedido.ItemPedidoRepository;
import br.com.softblue.bluefood.domain.pedido.Pedido;
import br.com.softblue.bluefood.domain.pedido.PedidoRepository;
import br.com.softblue.bluefood.domain.pedido.RelatorioItemFaturamento;
import br.com.softblue.bluefood.domain.pedido.RelatorioItemFilter;
import br.com.softblue.bluefood.domain.pedido.RelatorioPedidoFilter;
import br.com.softblue.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapio;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.softblue.bluefood.domain.restaurante.Restaurante;
import br.com.softblue.bluefood.domain.restaurante.RestauranteRepository;
import br.com.softblue.bluefood.util.SecurityUtils;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;

@Controller
@RequestMapping(path = "/restaurante/")
public class RestauranteController {
	@Autowired
	private RelatorioService relatorioService;
	@Autowired
	private ServiceRestaurante serviceRestaurante;
	@Autowired
	private RestauranteRepository restauranteRepository;
	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	@Autowired
	private ItemCardapioRepository itemCardapioRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private ClienteRepository ClienteRepository;
	@Autowired
	private ItemPedidoRepository ItemPedidoRepository;

	@GetMapping(path = { "/home" })
	public String home(Restaurante restaurante, Model model) {
		Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
		List<Pedido> pedidos = pedidoRepository.findByRestaurante_Id(restauranteId);
		model.addAttribute("pedidos", pedidos);

		return "restaurante-home";
	}

	@GetMapping(path = "/edit")
	private String edit(Model model, Restaurante restaurante) {

		Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
		restaurante = restauranteRepository.findById(restauranteId).orElseThrow(NoSuchElementException::new);
		model.addAttribute("restaurante", restaurante);

		ControllerHelper.setEditMode(model, true);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		return "restaurante-cadastro";

	}

	@PostMapping(path = "/save")
	public String save(Model model, @ModelAttribute("restaurante") @Valid Restaurante restaurante, Errors errors) {

		if (!errors.hasErrors()) {
			try {
				serviceRestaurante.saveRestaurante(restaurante);
				model.addAttribute("msg", "Restaurante alterado com sucesso");

			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());

			}

		}

		ControllerHelper.setEditMode(model, true);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		return "restaurante-cadastro";

	}

	@GetMapping(path = "/comidas")
	public String viewComidas(Model model) {
		Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
		Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(NoSuchElementException::new);
		ItemCardapio itemCardapio = new ItemCardapio();

		List<ItemCardapio> itensCardapio = itemCardapioRepository.findByRestaurante_IdOrderByNome(restauranteId);
		model.addAttribute("restaurante", restaurante);
		model.addAttribute("itensCardapio", itensCardapio);
		model.addAttribute("itemCardapio", itemCardapio);
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		return "restaurante-comidas";
	}

	@GetMapping(path = "/comidas/remove")
	public String remove(@RequestParam("idItemCardapio") Integer idItemCardapio, Model model) {

		itemCardapioRepository.deleteById(idItemCardapio);

		return "redirect:/restaurante/comidas";
	}

	@PostMapping(path = "/comidas/cadastrar")
	public String cadastrarComida(@Valid @ModelAttribute("itemCardapio") ItemCardapio itemCardapio, Errors errors,
			Model model) {
		Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
		List<ItemCardapio> itensCardapio = itemCardapioRepository.findByRestaurante_IdOrderByNome(restauranteId);
		if (errors.hasErrors()) {

			Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(NoSuchElementException::new);

			model.addAttribute("restaurante", restaurante);
			model.addAttribute("itensCardapio", itensCardapio);
			return "restaurante-comidas";

		}
		model.addAttribute("itensCardapio", itensCardapio);
		serviceRestaurante.saveItemCardapio(itemCardapio);
		return "redirect:/restaurante/comidas";
	}

	@GetMapping(path = "/pedido")
	public String viewPedido(Model model, @RequestParam("pedidoId") Integer pedidoId) {

		Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(NoSuchElementException::new);
		Cliente cliente = ClienteRepository.findById(pedido.getCliente().getId()).orElseThrow(NoSuchElementException::new);
		model.addAttribute("cliente", cliente);
		model.addAttribute("pedido", pedido);

		return "restaurante-pedido";
	}

	@PostMapping(path = "/pedido/proximoStatus")
	public String proximoStatus(@RequestParam("pedidoId") Integer pedidoId, Model model) {

		Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(NoSuchElementException::new);
		pedido.definirProximoStatus();
		pedidoRepository.save(pedido);
		model.addAttribute("pedido", pedido);
		model.addAttribute("msg", "Status Alterado com Sucesso");

		return "redirect:/restaurante/pedido?pedidoId=" + pedido.getId();
	}

	@GetMapping(path = "/relatorio/itens")
	public String relatorioItem(Model model,
			@ModelAttribute("relatorioItemFilter") RelatorioItemFilter filter) {

		Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
		
		List<ItemCardapio> itensCardapio = itemCardapioRepository.findByRestaurante_IdOrderByNome(restauranteId);

		model.addAttribute("relatorioItemFilter", filter);
		model.addAttribute("itensCardapio", itensCardapio);
		
		List<RelatorioItemFaturamento> itensCalculados = relatorioService.calcularFaturamentoItens(restauranteId, filter);
		
		model.addAttribute("itensCalculados", itensCalculados);
		return "restaurante-relatorio-itens";

	}

	@GetMapping(path = "/relatorio/pedidos")
	public String relatorioPedidos(Model model, @ModelAttribute("relatorioPedidoFilter") RelatorioPedidoFilter filter) {

		Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
		List<Pedido> pedidos = relatorioService.listPedidos(restauranteId, filter);
		
		model.addAttribute("pedidos", pedidos);

		
		model.addAttribute("filter", filter);
		return "restaurante-relatorio-pedidos";

	}

}
