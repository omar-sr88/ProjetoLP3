package br.nti.SigaaBiblio.model;

public class Emprestimo {
	


	/* campos de dados basicos*/
	private String codigoLivro;
	private String autor;
	private String titulo;
	private String ano;
	private String dataEmprestimo;
	private String dataDevolucao;
	
	/* campos de dados para historico de emprestimo */
	private String tipoEmprestimo;
	private String dataRenovacao;
	private String prazoDevolucao;
	private String biblioteca;
	private String localizacao;
	private boolean emAberto;
	private String codigoBarras;
	
	/* campo para verificao do vinculo */
	private boolean renovavel;
	
	/* construtor usado para renovacao de emprestimos */
	public Emprestimo(String codigoLivro, String autor, String titulo,
			String ano, String dataEmprestimo, String dataDevolucao) {
		super();
		this.codigoLivro = codigoLivro;
		this.autor = autor;
		this.titulo = titulo;
		this.ano = ano;
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucao = dataDevolucao;
	}
	
	/* Construtor Utilizado para historico de emprestimos */
	public Emprestimo(String autor, String titulo, String dataEmprestimo,
			String dataDevolucao, String tipoEmprestimo, String dataRenovacao,
			String prazoDevolucao, String biblioteca, String localizacao,
			boolean emAberto, String codigoBarras) {
		super();
		this.autor = autor;
		this.titulo = titulo;
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucao = dataDevolucao;
		this.tipoEmprestimo = tipoEmprestimo;
		this.dataRenovacao = dataRenovacao;
		this.prazoDevolucao = prazoDevolucao;
		this.biblioteca = biblioteca;
		this.localizacao = localizacao;
		this.emAberto = emAberto;
		this.codigoBarras = codigoBarras;
	}
	
	/* construtor utilizado para visualização de vinculo */
	public Emprestimo(String codigoBarra,String dataEmprestimo, 
			String dataRenovacao, String prazoDevolucao, String biblioteca,
			boolean renovavel) {
			super();
		this.codigoBarras = codigoBarra;
		this.prazoDevolucao = prazoDevolucao;
		this.biblioteca = biblioteca;
		this.renovavel = renovavel;
		this.dataEmprestimo = dataEmprestimo;
		this.dataRenovacao = dataRenovacao;
	}
	
	/* toString utilizado para Renovacao de Emprestimo */
	@Override
	public String toString() {
		return "Código do livro: " + codigoLivro + "\nAutor: " + autor
				+ "\nTítulo: " + titulo + "\nAno: " + ano + "\nData de empréstimo: "
				+ dataEmprestimo + "\ndata de devolucao: " + dataDevolucao;
	}

	public String getCodigoLivro() {
		return codigoLivro;
	}

	public void setCodigoLivro(String codigoLivro) {
		this.codigoLivro = codigoLivro;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(String dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public String getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(String dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public String getTipoEmprestimo() {
		return tipoEmprestimo;
	}

	public void setTipoEmprestimo(String tipoEmprestimo) {
		this.tipoEmprestimo = tipoEmprestimo;
	}

	public String getDataRenovacao() {
		return dataRenovacao;
	}

	public void setDataRenovacao(String dataRenovacao) {
		this.dataRenovacao = dataRenovacao;
	}

	public String getPrazoDevolucao() {
		return prazoDevolucao;
	}

	public void setPrazoDevolucao(String prazoDevolucao) {
		this.prazoDevolucao = prazoDevolucao;
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

	public boolean isEmAberto() {
		return emAberto;
	}

	public void setEmAberto(boolean emAberto) {
		this.emAberto = emAberto;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	
	
}
