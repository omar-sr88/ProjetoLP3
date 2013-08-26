package br.nti.SigaaBiblio.model;

import java.util.ArrayList;
import java.util.List;

public class Livro {

	/* Campos dados basico */
	private String autor;
	private String titulo;
	private String edicao;
	private String ano;
	private String quantidade;
	/* Campos dados do Titulo */
	private String registroNoSistema;
	private String numeroChamada;
	private String subTitulo;
	private String assunto;
	private String localDePublicação;
	private String editora;
	
	/*Campo Exemplar*/
	List<ExemplarLivro> exemplares;
	
	
	/* 
	 * Construtor padrão, as outras informações são adquiridas através de outras
	 * solicitações ao servidor
	 */
	
	
	
	
	
	public Livro(String autor, String titulo, String edicao, String ano,
			String quantidade, String registroNoSistema) {
		super();
		this.autor = autor;
		this.titulo = titulo;
		this.edicao = edicao;
		this.ano = ano;
		this.quantidade = quantidade;
		this.registroNoSistema=registroNoSistema;
		//this.exemplares=new ArrayList<ExemplarLivro>();
	}


	public List<ExemplarLivro> getExemplares() {
		return exemplares;
	}


	public void setExemplares(List<ExemplarLivro> exemplares) {
		this.exemplares = exemplares;
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


	public String getEdicao() {
		return edicao;
	}


	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}


	public String getAno() {
		return ano;
	}


	public void setAno(String ano) {
		this.ano = ano;
	}


	public String getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}


	public String getRegistroNoSistema() {
		return registroNoSistema;
	}


	public void setRegistroNoSistema(String registroNoSistema) {
		this.registroNoSistema = registroNoSistema;
	}


	public String getNumeroChamada() {
		return numeroChamada;
	}


	public void setNumeroChamada(String numeroChamada) {
		this.numeroChamada = numeroChamada;
	}


	public String getSubTitulo() {
		return subTitulo;
	}


	public void setSubTitulo(String subTitulo) {
		this.subTitulo = subTitulo;
	}


	public String getAssunto() {
		return assunto;
	}


	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}


	public String getLocalDePublicação() {
		return localDePublicação;
	}


	public void setLocalDePublicação(String localDePublicação) {
		this.localDePublicação = localDePublicação;
	}


	public String getEditora() {
		return editora;
	}


	public void setEditora(String editora) {
		this.editora = editora;
	}
	
	public String toString(){
		return "Autor: "+this.autor+"\nTítulo: "+this.titulo+"\nEdição: "+this.edicao+
				"\nAno: "+this.ano+"\nQuantidade: "+this.quantidade;
	}
	
}
