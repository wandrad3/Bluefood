package br.com.softblue.bluefood.domain.cliente;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import br.com.softblue.bluefood.domain.usuario.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
public class Cliente extends Usuario{

	@NotBlank(message = "CPF inválido")
	@Pattern(regexp = "[0-9]{11}", message="CPF inválido")
	@Column(length = 11, nullable = false)
	private String cpf;
	
	@NotBlank(message = "CEP inválido")
	@Pattern(regexp = "[0-9]{8}",message="CEP inválido")
	@Column(length = 8, nullable = false)
	private String cep;
	
	
	public String getFormattedCep() {
		return cep.substring(0,5) + "-" + cep.substring(5);
	}
	
}