package br.com.softblue.bluefood.domain.restaurante;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Integer> {
	
	
	@Query("Select Distinct ic.categoria from ItemCardapio ic where ic.restaurante.id=?1 order by ic.categoria")
	public List<String> findCategorias(Integer restauranteId);
	
	public List<ItemCardapio> findByRestaurante_IdOrderByNome(Integer restauranteId);
	
	public List<ItemCardapio> findByRestaurante_IdAndDestaqueOrderByNome(Integer restauranteId, boolean destaque);
	public List<ItemCardapio> findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(Integer restauranteId, boolean destaque, String categoria);

	
	
	


}
