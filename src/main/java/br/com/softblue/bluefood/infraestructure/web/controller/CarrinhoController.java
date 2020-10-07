package br.com.softblue.bluefood.infraestructure.web.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.annotation.SessionScope;

import br.com.softblue.bluefood.domain.pedido.Carrinho;
import br.com.softblue.bluefood.domain.pedido.ItemPedido;
import br.com.softblue.bluefood.domain.pedido.Pedido;
import br.com.softblue.bluefood.domain.pedido.PedidoRepository;
import br.com.softblue.bluefood.domain.pedido.RestauranteDiferenteException;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapio;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapioRepository;


@Controller
@RequestMapping(path = "/cliente/carrinho")
@SessionAttributes("carrinho")
public class CarrinhoController {
	
	@Autowired
	ItemCardapioRepository itemCardapioRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@ModelAttribute("carrinho")
	public Carrinho carrinho() {
		return new Carrinho();
	}

	@GetMapping(path = "/adcionar")
	public String adcionarItem(@RequestParam("itemId") Integer itemId, 
			@RequestParam("quantidade") Integer quantidade,
			@RequestParam("observacoes") String observacoes,
			@ModelAttribute("carrinho") Carrinho carrinho,
			Model model) {
		
		ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow(NoSuchElementException::new);
		
		try {
			carrinho.adcionarItem(itemCardapio, quantidade, observacoes);
		} catch (RestauranteDiferenteException e) {
			model.addAttribute("msg", "Não é possível adcionar itens de restaurantes diferentes no mesmo carrinho");
		}

		return "cliente-carrinho";

	}
	@GetMapping(path = "/remover")
	public String removerItem(@RequestParam("itemId") Integer itemId, 			
			SessionStatus sessionStatus,		 
			@ModelAttribute("carrinho") Carrinho carrinho,
			Model model) {
		
		ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow(NoSuchElementException::new);
		
		carrinho.removerItem(itemCardapio);
		
		if(carrinho.vazio()) {
			sessionStatus.setComplete();
		}
		
		
		return "cliente-carrinho";
		
	}
	
	@GetMapping(path="/visualizar")
	public String viewCarrinho(){
		return "cliente-carrinho";
	}
		
	@GetMapping(path = "refazerCarrinho")
	public String refazerCarrinho(@RequestParam("pedidoId") Integer pedidoId,
			@ModelAttribute("carrinho") Carrinho carrinho , Model model) {

		Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(NoSuchElementException::new);
		
		carrinho.limpar();
		
		
		for(ItemPedido itemPedido : pedido.getItens()) {
			carrinho.adcionarItem(itemPedido);
		}
		
		
		return "cliente-carrinho";
		
	}
	
}
