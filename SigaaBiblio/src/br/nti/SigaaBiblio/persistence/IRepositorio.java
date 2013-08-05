package br.nti.SigaaBiblio.persistence;

import java.util.List;

import br.nti.SigaaBiblio.model.LivroResumido;


public interface IRepositorio {
	  //Insere ou atualiza o carro
	  public boolean salvarPesquisa(String texto);
	  
	  //Insere ou atualiza o carro
	  public boolean salvarLivros(List<LivroResumido> texto);

	  // Retorna uma lista com todos os carros
	  public List<String> listarPalavras();

	  public List<LivroResumido> listarLivros();
	  
	  public int tamanhoListaLivros();

	}

