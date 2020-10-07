package br.com.softblue.bluefood.infraestructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import br.com.softblue.bluefood.application.service.PedidoService;
import br.com.softblue.bluefood.domain.pagamento.PagamentoException;
import br.com.softblue.bluefood.domain.pedido.Carrinho;
import br.com.softblue.bluefood.domain.pedido.Pedido;

@Controller
@SessionAttributes("carrinho")
@RequestMapping(path = "/cliente/pagamento")
public class PagamentoController {

	@Autowired
	private PedidoService pedidoService;

	@PostMapping("/pagar")
	public String pagar(@RequestParam("numCartao") String numCartao, Carrinho carrinho, SessionStatus sessionStatus,
			Model model) {

		Pedido pedido;
		try {
			pedido = pedidoService.criarEPagar(carrinho, numCartao);
			sessionStatus.setComplete();

			return "redirect:/cliente/pedido/view?pedidoId=" + pedido.getId();
		} catch (PagamentoException e) {
			model.addAttribute("msg", e.getMessage());
			return "cliente-carrinho";
		}

	}

}
