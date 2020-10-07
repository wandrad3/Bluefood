package br.com.softblue.bluefood.domain.restaurante;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import br.com.softblue.bluefood.infraestructure.web.validator.UploadConstraint;
import br.com.softblue.bluefood.util.FileType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Table(name = "item_cardapio")
public class ItemCardapio implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "O nome não pode ser vazio")
	@Size(max = 50)
	private String nome;

	@NotBlank(message = "A categoria não pode ser vazia")
	@Size(max = 25)
	private String categoria;

	@NotBlank(message = "A descrição não pode ser vazia")
	@Size(max = 80)
	private String descricao;

	@NotNull(message = "O preço não pode ser nulo")
	@Min(0)
	private BigDecimal preco;

	@Size(max = 50)
	private String imagem;
	
	@NotNull
	private boolean destaque;

	@UploadConstraint(acceptedTypes = FileType.PNG, message = "Tipo de arquivo inválido")
	private transient MultipartFile imagemFile;

	@ManyToOne
	@JoinColumn(name = "id_restaurante")
	private Restaurante restaurante;

	public void setImageFileName() {
		if (getId() == null) {
			throw new IllegalStateException("O objeto precisa ser criado");
		}

		this.imagem = String.format("%04d-comida.%s", getId(), FileType.of(imagemFile.getContentType()).getExtension());
									
	}

}
