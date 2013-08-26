package br.nti.SigaaBiblio.model;

public class ExemplarLivro {

	
	private String codigoDeBarras;
	private String tipoDeMaterial;
	private String colecao;
	private String biblioteca;
	private String localizacao;
	private String situacao;
	
	public ExemplarLivro(String codigoDeBarras, String tipoDeMaterial,
			String colecao, String biblioteca, String localizacao,
			String situacao) {
		super();
		this.codigoDeBarras = codigoDeBarras;
		this.tipoDeMaterial = tipoDeMaterial;
		this.colecao = colecao;
		this.biblioteca = biblioteca;
		this.localizacao = localizacao;
		this.situacao = situacao;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public String getTipoDeMaterial() {
		return tipoDeMaterial;
	}

	public void setTipoDeMaterial(String tipoDeMaterial) {
		this.tipoDeMaterial = tipoDeMaterial;
	}

	public String getColecao() {
		return colecao;
	}

	public void setColecao(String colecao) {
		this.colecao = colecao;
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	
	
	
}
