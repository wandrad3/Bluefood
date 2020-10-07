package br.com.softblue.bluefood.domain.pedido;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.softblue.bluefood.domain.restaurante.ItemCardapio;
import br.com.softblue.bluefood.domain.restaurante.Restaurante;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class Carrinho implements Serializable {

	private List<ItemPedido> itens = new ArrayList<>();
	
	@Autowired 
	private Restaurante restaurante;

	public void adcionarItem(ItemCardapio itemCardapio, Integer quantidade, String observacoes)
			throws RestauranteDiferenteException {

		if (itens.size() == 0) {
			restaurante = itemCardapio.getRestaurante();
		} else if (!itemCardapio.getRestaurante().getId().equals(restaurante.getId())) {

			throw new RestauranteDiferenteException();

		}

		if (!exists(itemCardapio)) {

			ItemPedido itemPedido = new ItemPedido();
			itemPedido.setItemCardapio(itemCardapio);
			itemPedido.setQuantidade(quantidade);
			itemPedido.setObservacoes(observacoes);
			itemPedido.setPreco(itemCardapio.getPreco());
			itens.add(itemPedido);

		}

	}
	
	public boolean vazio() {
		
		return itens.size() == 0;
		
	}
	
	public void limpar() {
		itens.clear();
		restaurante = null;
	}

	public void adcionarItem(ItemPedido itemPedido) {
		try {
			adcionarItem(itemPedido.getItemCardapio(), itemPedido.getQuantidade(), itemPedido.getObservacoes());
		} catch (RestauranteDiferenteException e) {
			
		}
	}
	
	public void removerItem(ItemCardapio itemCardapio) {
		
		for (Iterator<ItemPedido> iterator = itens.iterator(); iterator.hasNext();) {
			ItemPedido itemPedido = iterator.next();
			
			if(itemPedido.getItemCardapio().getId().equals(itemCardapio.getId())) {
				iterator.remove();
				break;
			}
			
		}
		
		if(itens.size()==0) {
			restaurante =null;
		}
		
		
	}
	
	public BigDecimal getPrecoTotal(boolean addTaxaEntrega) {
		BigDecimal soma = BigDecimal.ZERO;
		for(ItemPedido item: itens) {
			soma = soma.add(item.getPrecoCalculado());
		}
		if(addTaxaEntrega) {
			soma = soma.add(restaurante.getTaxaDeEntrega());
		}
		return soma;
	}

	private boolean exists(ItemCardapio itemCardapio) {

		for (ItemPedido itemPedido : itens) {
			if (itemPedido.getItemCardapio().getId().equals(itemCardapio.getId())) {
				return true;
			}
		}
		return false;

	}

}
