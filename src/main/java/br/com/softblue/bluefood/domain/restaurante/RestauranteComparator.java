package br.com.softblue.bluefood.domain.restaurante;

import java.util.Comparator;

import br.com.softblue.bluefood.domain.restaurante.SearchFilter.Order;

public class RestauranteComparator implements Comparator<Restaurante> {

	private SearchFilter searchFilter;
	String cep;
	public RestauranteComparator(SearchFilter searchFilter, String cep) {
		
		this.searchFilter = searchFilter;
		this.cep = cep;
	}
	@Override
	public int compare(Restaurante r1, Restaurante r2) {
		int result;
		
		if(searchFilter.getOrder() == Order.Taxa) {
			result = r1.getTaxaDeEntrega().compareTo(r2.getTaxaDeEntrega());
		} else if (searchFilter.getOrder() == Order.Tempo) {
			
			result = r1.calcularTempoEntrega(cep).compareTo(r2.calcularTempoEntrega(cep));
			
		} else {
			throw new IllegalStateException("O valor de ordenação " + searchFilter.getOrder() + " não é do tipo válido");
		}
		
		return searchFilter.isAsc() ? result : -result;
	}
}
