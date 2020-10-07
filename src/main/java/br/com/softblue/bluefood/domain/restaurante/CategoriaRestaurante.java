package br.com.softblue.bluefood.domain.restaurante;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "categoria_restaurante")
public class CategoriaRestaurante implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	Integer id;

	@NotNull
	@Size(max = 20)
	String nome;

	@NotNull
	@Size(max = 50)
	private String imagem;
	
	@ManyToMany(mappedBy = "categorias")
	
	Set<Restaurante> restaurantes = new HashSet<>(0);

}
