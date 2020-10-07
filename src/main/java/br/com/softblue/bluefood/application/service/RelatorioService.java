package br.com.softblue.bluefood.application.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.softblue.bluefood.domain.pedido.Pedido;
import br.com.softblue.bluefood.domain.pedido.PedidoRepository;
import br.com.softblue.bluefood.domain.pedido.RelatorioItemFaturamento;
import br.com.softblue.bluefood.domain.pedido.RelatorioItemFilter;
import br.com.softblue.bluefood.domain.pedido.RelatorioPedidoFilter;
import br.com.softblue.bluefood.util.IOUtil;

@Service

public class RelatorioService {
	@Autowired
	private PedidoRepository pedidoRepository;

	public List<Pedido> listPedidos(Integer restauranteId, RelatorioPedidoFilter filter) {

		Integer pedidoId = filter.getPedidoId();

		if (pedidoId != null) {
			Pedido pedido = pedidoRepository.findByIdAndRestauranteId(pedidoId, restauranteId);
			return List.of(pedido);
		}

		LocalDate dataInicial = filter.getDataInicial();
		LocalDate dataFinal = filter.getDataFinal();

		if (dataInicial == null) {
			return List.of();
		}

		if (dataFinal == null) {
			dataFinal = LocalDate.now();
		}

		return pedidoRepository.findByDateInterval(restauranteId, dataInicial.atStartOfDay(),
				dataFinal.atTime(23, 59, 59));
	}

	public List<RelatorioItemFaturamento> calcularFaturamentoItens(Integer restauranteId, RelatorioItemFilter filter) {

		Integer itemId = filter.getItemId();

		List<RelatorioItemFaturamento> itens;
		List<Object[]> itensObj;

		LocalDate dataInicial = filter.getDataInicial();
		LocalDate dataFinal = filter.getDataFinal();

		if (dataInicial == null) {
			return List.of();
		}

		if (dataFinal == null) {
			dataFinal = LocalDate.now();
		}

		LocalDateTime dataHoraInicial = dataInicial.atStartOfDay();
		LocalDateTime dataHorafinal = dataFinal.atTime(23, 59, 59);

		if (itemId != 0) {
			itensObj = pedidoRepository.findItensForFaturamento(restauranteId, itemId, dataHoraInicial, dataHorafinal);
		} else {
			
			itensObj = pedidoRepository.findItensForFaturamento(restauranteId, dataHoraInicial, dataHorafinal);

		}
		
		itens = new ArrayList<>();
		
		for(Object[] item : itensObj) {
			String nome = (String) item[0];
			Long quantidade = (Long) item[1];
			BigDecimal valor = (BigDecimal) item[2];
			itens.add(new RelatorioItemFaturamento(nome, quantidade, valor));
		}

		return itens;
	}

}
