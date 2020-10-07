package br.com.softblue.bluefood.domain.pagamento;

public enum StatusPagamento {
	
	NaoAutorizado("N�o autorizado pela institui��o financeira"),
	Autorizado ("Autorizado"),
	Cartaoinvalido ("Cart�o inv�lido ou bloqueado");
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	private StatusPagamento(String descricao) {
		this.descricao = descricao;
	}
	

}
