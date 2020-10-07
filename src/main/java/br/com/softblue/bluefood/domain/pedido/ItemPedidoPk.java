package br.com.softblue.bluefood.domain.pedido;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoPk implements Serializable {

	@ManyToOne
	private Pedido pedido;
	
	@NotNull
	private Integer ordem;
	
	
}
