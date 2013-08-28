package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;

import br.nti.SigaaBiblio.model.Artigo;
import br.nti.SigaaBiblio.model.ExemplarLivro;
import br.nti.SigaaBiblio.model.Livro;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DadosTituloActivity extends Activity {

	Button buttonVerifDisp;
	Artigo artigo;
	Livro livro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dados_titulo);
		
		String[] values= new String[]{""};
							artigo = (Artigo) getIntent().getExtras().getParcelable("ExemplarArtigo");
		
		if(artigo!=null){
			values = new String[] { "Autores Secundários: "+artigo.getAutoresSecundarios(), 
					"Intervalo de Páginas: "+artigo.getPaginas(),
					"Local de Publicação: "+ artigo.getLocalPublicacao(),
					"Editora: "+artigo.getEditora(),
			        "Ano: "+artigo.getAno(),
			        "Resumo: "+artigo.getResumo()};
		}
		else{
			livro=(Livro) getIntent().getExtras().getParcelable("InformacoesLivro");
			if(livro!=null){
				values = new String[] { "Registro no Sistema: "+livro.getRegistroNoSistema(), 
						"Número da Chamada: "+livro.getNumeroChamada(),
						"Título: "+ livro.getTitulo(),
						"SubTítulo: "+livro.getSubTitulo(),
				        "Assunto: "+livro.getAssunto(),
				        "Autor: "+livro.getAutor(),
				        "Local de Publicação: "+livro.getLocalDePublicação(),
				        "Editora: "+ livro.getEditora(),
				        "Ano de Publicação: "+livro.getAno()};
			}
		}
				
		
		
		ListView l = (ListView) findViewById(R.id.listViewDadosTitulo);
	    	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, values){

	    	  /*
	    	   * Desabilita o focus da listView que aqui foi utilizada apenas por
	    	   * questão de estetica.  
	    	   */
	    	  @Override 
	    	  public boolean isEnabled(int position) 
	            { 
	                    return false; 
	            } 
	        };

	    l.setAdapter(adapter);

	    buttonVerifDisp=(Button) findViewById(R.id.buttonVerifDisp);

	    buttonVerifDisp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(artigo!=null){
					Artigo artigo = (Artigo) getIntent().getExtras().getParcelable("ExemplarArtigo");
					Intent intent = new Intent(DadosTituloActivity.this, ExemplarArtigoActivity.class );
					intent.putExtra("ExemplarArtigo", artigo);
					startActivity(intent);
				}
				else{
					livro=(Livro) getIntent().getExtras().getParcelable("InformacoesLivro");
					ArrayList<ExemplarLivro> exemplares = livro.getExemplares();
					Intent intent = new Intent(DadosTituloActivity.this, ExemplaresActivity.class );
					intent.putExtra("ExemplaresLivro", exemplares);
					startActivity(intent);
				}
					

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dados_titulo, menu);
		return true;
	}



}