package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



import br.nti.SigaaBiblio.model.Artigo;
import br.nti.SigaaBiblio.model.Biblioteca;
import br.nti.SigaaBiblio.model.Livro;

import com.nti.SigaaBiblio.R;

import Connection.ConnectJSON;
import Connection.HttpUtils;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class BuscaArtigoActivity extends Activity {

	Button goResultados;
	Spinner bibliotecasDisponiveis;
	List<String> bibliotecasLista;
	List<Biblioteca> bibliotecas;
	String bibliotecaSelecionada;
	EditText titulo;
	EditText autor;
	EditText palavraChave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_busca_artigo);

		bibliotecasLista=new ArrayList<String>();
		bibliotecas = getIntent().getParcelableArrayListExtra("Bibliotecas");	
		for(Biblioteca b : bibliotecas){
			bibliotecasLista.add(b.getNome());
		}
		
		bibliotecasDisponiveis = (Spinner)findViewById(R.id.spinnerBibliotecaArtigo);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bibliotecasLista);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bibliotecasDisponiveis.setAdapter(spinnerArrayAdapter);
		bibliotecasDisponiveis.setSelection(arrayAdapter.getPosition("Buscar em Todas as Unidades"));
		bibliotecasDisponiveis.setOnItemSelectedListener(new BiblioItemSelectedListener());
		bibliotecaSelecionada="0";
	
	
	
	
	}

	
	public void pesquisar(View v) {
		
		titulo = (EditText)findViewById(R.id.editTextTituloArtigo);
		autor = (EditText)findViewById(R.id.editTextAutorArtigo);
		palavraChave = (EditText)findViewById(R.id.editTextPalavraChaveArtigo);
		
		final ProgressDialog pd = new ProgressDialog(BuscaArtigoActivity.this);
		pd.setMessage("Processando...");
		pd.setTitle("Aguarde");
		pd.setIndeterminate(false);
		final Operations operacao = new OperationsFactory().getOperation(OperationsFactory.REMOTA,this);
		
		new AsyncTask<Void,Void,Void>(){
			//IdBiblioteca":"9763","TituloBusca":"Metodologia","AutorBusca":"","AssuntoBusca":""}
			/**
			 * Retorno: Livros
			 * Autor  (String)
			 * Titulo (String)
			 * Edicao (String)
			 * Ano    (Int)
			 * QuantidadeAtivos (Int)
			 */
			
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd.show();
			}
			
			
			@Override
			protected Void doInBackground(Void... arg0) {
				
				
				
				ArrayList<Artigo> artigos = operacao.consultarAcervoArtigo(bibliotecaSelecionada,titulo.getText().toString(),autor.getText().toString(),palavraChave.getText().toString());
				Intent intent = new Intent(BuscaArtigoActivity.this, ResultadoBuscaArtigoctivity.class );
				intent.putExtra("Artigos", artigos);
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
	
	
	/*
	 * Classe interna que trata qual biblioteca foi selecionada
	 */
	
		private class BiblioItemSelectedListener implements OnItemSelectedListener {

		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        String selected = parent.getItemAtPosition(pos).toString();
		        
		        bibliotecaSelecionada=bibliotecas.get(bibliotecasLista.indexOf(selected)).getId();
			       
		        
		        Log.d("MARCILIO_DEBUG", bibliotecaSelecionada);
		    }

		    public void onNothingSelected(AdapterView parent) {
		        // Do nothing.
		    }
		}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busca_artigo, menu);
		return true;
	}

}