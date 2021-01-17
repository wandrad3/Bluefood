package br.com.softblue.bluefood.domain.restaurante;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import br.com.softblue.bluefood.domain.usuario.Usuario;
import br.com.softblue.bluefood.infraestructure.web.validator.UploadConstraint;
import br.com.softblue.bluefood.util.FileType;
import br.com.softblue.bluefood.util.StringUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="restaurante")
public class Restaurante extends Usuario {
	
	@NotBlank(message = "CNPJ inválido")
	@Pattern(regexp = "[0-9]{14}", message="CNPJ inv�lido")
	@Column(length = 14,nullable = false)
	String cnpj;
	
	@Size(max=80)
	String logotipo;
	
	@UploadConstraint(acceptedTypes = FileType.PNG, message = "O arquivo não é um arquivo de imagem válido")
	private transient MultipartFile logotipoFile;
	
	
	@NotNull(message="A taxa de entrega não pode ser vazia")
	@Min(0)
	@Max(90)
	BigDecimal taxaDeEntrega;
	
	
	@NotNull(message="O tempo de entrega não pode ser vazio")
	@Min(0)
	@Max(120)
	Integer tempoEntregaBase;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "restaurante_has_categoria",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn(name = "categoria_restaurante_id")
	)
	@Size(min = 1, message = "O restaurante precisa ter pelo menos uma categoria")
	@ToString.Exclude
	private Set<CategoriaRestaurante> categorias = new HashSet<>(0);
	
	@OneToMany(mappedBy = "restaurante")
	private Set<ItemCardapio> itensCardapio = new HashSet<>(0);
	
	
	public void setLogotipoFileName() {
		if (getId() == null) {
			throw new IllegalStateException("é preciso primeiro gravar o registro");
		}
		
		this.logotipo = String.format("%04d-logo.%s", getId(), FileType.of(logotipoFile.getContentType()).getExtension());
	}
	
	public String getCategoriasAsText() {
		Set<String> strings = new LinkedHashSet<>();
		
		for(CategoriaRestaurante categoria : categorias) {
			strings.add(categoria.getNome());
		}
		
		return StringUtil.concatenate(strings);
	}
	
	public Integer calcularTempoEntrega(String cep) {
		int soma =0;
		
		for(char c : cep.toCharArray() ) {
			int v = Character.getNumericValue(c);
			
			if(v>0) {
				soma += v;
			}
		}
		
		soma/=2;
		
		return tempoEntregaBase + soma;
	}
	
	


	

}
