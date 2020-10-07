package br.com.softblue.bluefood.infraestructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.softblue.bluefood.domain.pedido.Pedido;
import br.com.softblue.bluefood.domain.pedido.PedidoRepository;

@Controller
@RequestMapping(path = "/cliente/pedido")
@SessionAttributes("carrinho")
public class PedidoController {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@GetMapping(path = "/view")
	public String viewCarrinho(@RequestParam("pedidoId") Integer pedidoId,

			Model model) {
		
		Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();
		model.addAttribute("pedido", pedido);
		

		return "cliente-pedido";

	}

}
