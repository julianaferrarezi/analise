package br.unesp.academico.analise.vo;

public class UnidadeUniversitariaVO {
	private Long idUnidadeUniversitaria;
	
	private String nome;
	
	private String nomeAbreviado;

	public Long getIdUnidadeUniversitaria() {
		return idUnidadeUniversitaria;
	}

	public void setIdUnidadeUniversitaria(Long idUnidadeUniversitaria) {
		this.idUnidadeUniversitaria = idUnidadeUniversitaria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeAbreviado() {
		return nomeAbreviado;
	}

	public void setNomeAbreviado(String nomeAbreviado) {
		this.nomeAbreviado = nomeAbreviado;
	}
}
