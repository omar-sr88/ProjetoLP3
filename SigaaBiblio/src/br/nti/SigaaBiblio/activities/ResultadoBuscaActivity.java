package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSInput;

import br.nti.SigaaBiblio.model.Biblioteca;
import br.nti.SigaaBiblio.model.Livro;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import Connection.Operations;
import Connection.OperationsFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ResultadoBuscaActivity extends Activity {

	private List<Livro> listaLivros;
	private ListView listaResultados;
	private ArrayList<String> lista;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_resultado_busca);
		
		lista = new ArrayList<String>(); 
		listaLivros = new ArrayList<Livro>();
		
		listaLivros = getIntent().getParcelableArrayListExtra("Livros");	
		
		if(listaLivros==null)
			lista.add("Não foram encontrados resultados para sua pesquisa.");
			else
				for(Livro l : listaLivros){
				lista.add(l.toString());
				}
			
		
	
				
		listaResultados = (ListView) findViewById(R.id.listViewResultados);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, lista);

	    listaResultados.setAdapter(adapter);

	    final ProgressDialog pd = new ProgressDialog(ResultadoBuscaActivity.this);
		pd.setMessage("Processando...");
		pd.setTitle("Aguarde");
		pd.setIndeterminate(false);
		
		final Operations operacao = new OperationsFactory().getOperation(OperationsFactory.REMOTA,this);

		listaResultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				// TODO Auto-generated method stub
				@Override
				public void onItemClick(AdapterView<?> parent, final View view, int position,
						long id) {
					// TODO Auto-generated method stub
					String selected = parent.getItemAtPosition(position).toString();
					final String livroSelecionado=listaLivros.get(lista.indexOf(selected)).getRegistroNoSistema();
					
					new AsyncTask<Void,Void,Void>(){
						
						/**
						 * Retorno:   Registro         : int
						 * 			  NumeroChamada    : String
						 * 			  Titulo           : String  
						 * 			  SubTitulo        : String
						 * 			  Assunto          : String
						 * 			  Autor            : String
						 * 			  AutorSecundario  : String
						 * 			  Publicacao       : String    (Local de Publicacao)
						 * 		      Editora 		   : String
						 * 			  AnoPublicacao    : int
						 * 			  NotasGerais 	   : String				
						 * 
						 */
						
						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							
							super.onPreExecute();
							pd.show();
						}
						
						
						@Override
						protected Void doInBackground(Void... arg0) {
							
							
							Livro livro = operacao.informacoesLivro(livroSelecionado);
							Intent intent = new Intent(ResultadoBuscaActivity.this, DadosTituloActivity.class );
							intent.putExtra("InformacoesLivro", livro);
							startActivity(intent);
							return null;
						}
						
						@Override
						protected void onPostExecute(Void v) {
							// TODO Auto-generated method stub
							super.onPostExecute(v);
							if(pd!= null && pd.isShowing())
								pd.dismiss();
						}
						
						
					}.execute();

					
			}

		});

	}	

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resultado_busca, menu);
		return true;
	}

	/*
	 * Faz o parser de objeto JSON para Lista
	 */
	
	



}