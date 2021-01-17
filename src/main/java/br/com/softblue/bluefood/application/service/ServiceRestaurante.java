package br.com.softblue.bluefood.application.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.softblue.bluefood.domain.cliente.Cliente;
import br.com.softblue.bluefood.domain.cliente.ClienteRepository;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapio;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.softblue.bluefood.domain.restaurante.Restaurante;
import br.com.softblue.bluefood.domain.restaurante.RestauranteComparator;
import br.com.softblue.bluefood.domain.restaurante.RestauranteRepository;
import br.com.softblue.bluefood.domain.restaurante.SearchFilter;
import br.com.softblue.bluefood.domain.restaurante.SearchFilter.SearchType;
import br.com.softblue.bluefood.util.SecurityUtils;

@Service
public class ServiceRestaurante {
	@Autowired
	private RestauranteRepository restauranteRepository;
	@Autowired
	ImageService imageService;

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	private ImageService imagemService;

	@Autowired
	private ItemCardapioRepository itemCardapioRepository;

	@Transactional
	public void saveRestaurante(Restaurante restaurante) throws ValidationException {

		if (!validateEmail(restaurante.getEmail(), restaurante.getId())) {
			throw new ValidationException("O e-mail está duplicado");
		}
		if (restaurante.getId() != null) {
			Restaurante restauranteDB = restauranteRepository.findById(restaurante.getId()).orElseThrow(NoSuchElementException::new);
			restaurante.setLogotipo(restauranteDB.getLogotipo());
			restaurante.setSenha(restauranteDB.getSenha());

		} else {
			restaurante.encryptPassword();
			restaurante = restauranteRepository.save(restaurante);
			restaurante.setLogotipoFileName();
			imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
		}
	}

	private boolean validateEmail(String email, Integer id) {
		Restaurante restaurante = restauranteRepository.findByEmail(email);
		Cliente cliente = clienteRepository.findByEmail(email);

		if (cliente != null) {
			return false;
		}

		if (restaurante != null) {
			if (id == null) {
				return false;
			}
			if (!restaurante.getId().equals(id)) {
				return false;
			}

		}

		return true;
	}

	public List<Restaurante> search(SearchFilter filter) {

		List<Restaurante> restaurantes;

		if (filter.getSearchType() == SearchType.Texto) {
			restaurantes = restauranteRepository.findByNomeIgnoreCaseContaining(filter.getTexto());
		} else if (filter.getSearchType() == SearchType.Categoria) {
			restaurantes = restauranteRepository.findByCategorias_Id(filter.getCategoriaId());
		} else {
			throw new IllegalStateException("O tipo " + filter.getSearchType() + " não é suportado");
		}

		Iterator<Restaurante> it = restaurantes.iterator();

		while (it.hasNext()) {
			Restaurante restaurante = it.next();
			double taxa = restaurante.getTaxaDeEntrega().doubleValue();

			if (filter.isEntregaGratis() && taxa > 0 || !filter.isEntregaGratis() && taxa == 0) {

				it.remove();

			}
		}

		RestauranteComparator restauranteComparator = new RestauranteComparator(filter,
				SecurityUtils.loggedCliente().getCep());
		restaurantes.sort(restauranteComparator);
		return restaurantes;

	}

	@Transactional
	public void saveItemCardapio(ItemCardapio itemCardapio) {

		itemCardapio = itemCardapioRepository.save(itemCardapio);
		itemCardapio.setImageFileName();
		imagemService.uploadComidas(itemCardapio.getImagemFile(), itemCardapio.getImagem());

	}

}
