package br.com.softblue.bluefood.domain.usuario;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.softblue.bluefood.util.StringUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("serial")
@MappedSuperclass
public class Usuario implements Serializable{
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "Nome n�o pode ser vazio")
	@Size(max = 80)
	@Column(length = 80, nullable = false)
	private String nome;
	
	@NotBlank(message = "Email n�o pode ser vazio")
	@Email(message = "E-mail inv�lido")
	@Size(max = 60)
	@Column(length = 60, nullable = false)
	private String email;
	
	@NotBlank(message = "Senha n�o pode ser vazia")
	@Size(max = 80)
	@Column(length = 80, nullable = false)
	private String senha;
	
	@NotBlank(message = "Telefone n�o pode ser vazio")
	@Size(max = 11)
	@Column(length = 11, nullable = false)
	@Pattern(regexp = "[0-9]{11}",message="Numero de telefone inv�lido")
	private String telefone;
	
	public void encryptPassword() {
		this.senha = StringUtil.encrypt(this.senha);
	}
	
}
