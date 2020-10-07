package br.com.softblue.bluefood.domain.restaurante;

import org.thymeleaf.util.StringUtils;
import lombok.Data;

@Data
public class SearchFilter {
	public enum SearchType {
		Texto, Categoria

	}
	
	public enum Order{
		Taxa, Tempo
	}
	
	public enum Command{
		EntregaGratis, MaiorTaxa,MenorTaxa,MaiorTempo,MenorTempo
	}

	private String texto;
	private SearchType searchType;
	private Integer categoriaId;
	private Order order = Order.Taxa;
	private boolean EntregaGratis;
	private boolean asc;
	
	
	public void proccessFilter(String cmdString) {
		if(!StringUtils.isEmpty(cmdString)) {
			
			Command cmd = Command.valueOf(cmdString);
			
			if(cmd == Command.EntregaGratis) {
				EntregaGratis = !EntregaGratis;
			} else if (cmd == Command.MaiorTaxa) {
				order = Order.Taxa;
				asc = false;
			} else if (cmd == Command.MenorTaxa) {
				order = Order.Taxa;
				asc = true;
			} else if (cmd == Command.MaiorTempo) {
				order = Order.Tempo;
				asc = false;
			} else if (cmd == Command.MenorTempo) {
				order = Order.Tempo;
				asc = true;
			}
			
			
		}
		if(searchType.name() == "Texto") {
			categoriaId = null;
			
		} else if(searchType.name() == "Categoria") {
			texto = null;
		}
	}

}

