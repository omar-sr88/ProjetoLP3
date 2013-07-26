package br.nti.SigaaBiblio.model;

/*
 *  Esta classe armazena as informações que serão exibidas na lisview
 *  e diz se elas foram checadas 
 */

public class Emprestimo {

	 private String dados;
	 private boolean selecionado;

	  public Emprestimo(String dados) {
	    this.dados = dados;
	    selecionado = false;
	  }
	  
	  public String getDados() {
			return dados;
		}

	  public void setDados(String dados) {
			this.dados = dados;
	  }
	  

	  public boolean isSelected() {
	    return selecionado;
	  }

	  public void setSelected(boolean selected) {
	    this.selecionado = selected;
	  }

}
