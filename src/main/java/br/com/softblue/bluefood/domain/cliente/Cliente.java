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

	@NotBlank(message = "CPF inv�lido")
	@Pattern(regexp = "[0-9]{11}", message="CPF inv�lido")
	@Column(length = 11, nullable = false)
	private String cpf;
	
	@NotBlank(message = "CEP inv�lido")
	@Pattern(regexp = "[0-9]{8}",message="CEP inv�lido")
	@Column(length = 8, nullable = false)
	private String cep;
	
	
	public String getFormattedCep() {
		return cep.substring(0,5) + "-" + cep.substring(5);
	}
	
}