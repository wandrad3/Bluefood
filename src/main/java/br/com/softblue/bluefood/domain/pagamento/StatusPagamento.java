package br.com.softblue.bluefood.domain.pagamento;

public enum StatusPagamento {
	
	NaoAutorizado("N�o autorizado pela instituição financeira"),
	Autorizado ("Autorizado"),
	Cartaoinvalido ("Cartão inválido ou bloqueado");
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	private StatusPagamento(String descricao) {
		this.descricao = descricao;
	}
	

}
