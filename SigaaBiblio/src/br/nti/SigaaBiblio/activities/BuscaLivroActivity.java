package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.nti.SigaaBiblio.R;

import Connection.ConnectJSON;
import Connection.HttpUtils;
import Connection.Operations;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BuscaLivroActivity extends Activity {


	Button goResultados;
	Spinner bibliotecasDisponiveis;
	List<String> bibliotecasLista;
	List<String> bibliotecasIds;
	String bibliotecaSelecionada;
	EditText titulo;
	EditText autor;
	EditText assunto;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_busca_livro);
		
		//recupera o nome das bibliotecas
		Bundle extras = getIntent().getExtras();
		String bibliotecas = extras.getString("Bibliotecas");
		bibliotecasLista=new ArrayList<String>();
		bibliotecasIds= new ArrayList<String>();
		JSONObject bibliotecasJson;
		try {
			bibliotecasJson = new JSONObject(bibliotecas);
			parseBibliotecas(bibliotecasJson);	
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String erro = "Não foi possivel completar a requisição, por favor tente novamente";
			Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG)
			.show();
			e.printStackTrace();
		}
	
		
		//Log.d("MARCILIO_DEBUG", d.toString());
		
		//Exibe as bibliotecas no spinner
		bibliotecasDisponiveis = (Spinner)findViewById(R.id.spinnerBibliotecaLivro);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bibliotecasLista);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bibliotecasDisponiveis.setAdapter(spinnerArrayAdapter);
		bibliotecasDisponiveis.setSelection(arrayAdapter.getPosition("Buscar em Todas as Unidades"));
		bibliotecasDisponiveis.setOnItemSelectedListener(new BiblioItemSelectedListener());
		bibliotecaSelecionada="0";
		//seta o onclick
		
		
		
		

		
	}
		
		
	/* Realiza Uma pesquisa
	 */
		
		public void pesquisar(View v) {
				
			titulo = (EditText)findViewById(R.id.editTextTituloLivro);
			autor = (EditText)findViewById(R.id.editTextAutorLivro);
			assunto = (EditText)findViewById(R.id.editTextAssuntoLivro);
			
			final ProgressDialog pd = new ProgressDialog(BuscaLivroActivity.this);
			pd.setMessage("Processando...");
			pd.setTitle("Aguarde");
			pd.setIndeterminate(false);
			
			
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
					
					
					Map<String, String> map = new HashMap<String, String>();
					String jsonString;
					map.put("Operacao", String.valueOf(Operations.CONSULTAR_ACERVO_LIVRO));
					map.put("IdBiblioteca",bibliotecaSelecionada);
					map.put("TituloBusca", titulo.getText().toString());
					map.put("AutorBusca", autor.getText().toString());
					map.put("AssuntoBusca",assunto.getText().toString());
					JSONObject inputsJson = new JSONObject(map);
					JSONObject resposta;
					
					try {
						jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
						resposta = new JSONObject(jsonString);
						String livros =resposta.getString("Livros");
						Intent intent = new Intent(BuscaLivroActivity.this, ResultadoBuscaActivity.class );
						intent.putExtra("Livros", livros);
						startActivity(intent);
						
						//Log.d("MARCILIO_DEBUG",livros);//ou Artigos
					} catch (Exception ex){
						String erro = "Não foi possivel completar a requisição, por favor tente novamente";
						Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG)
						.show();
						ex.printStackTrace();
					}
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
			
//			

	    }
	
	/*
	 * Classe interna que trata qual biblioteca foi selecionada
	 */
	
		private class BiblioItemSelectedListener implements OnItemSelectedListener {

		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        String selected = parent.getItemAtPosition(pos).toString();
		        
		        bibliotecaSelecionada=bibliotecasIds.get(bibliotecasLista.indexOf(selected));
		        
		        Log.d("MARCILIO_DEBUG", bibliotecaSelecionada);
		    }

		    public void onNothingSelected(AdapterView parent) {
		        // Do nothing.
		    }
		}

	
	/*
	 * Faz o Parse do objeto JSON para as listas
	 */
	
	public void parseBibliotecas(JSONObject bibliotecas) throws JSONException{
		
		bibliotecasLista.add("Buscar em Todas as Unidades"); //desativa o filtro da biblioteca lá no servidor
		bibliotecasIds.add("0");
		Iterator<String> keys = bibliotecas.keys(); //descobre as chaves que são os ids das bibliotecas
		while(keys.hasNext()){
			String key=keys.next();
			bibliotecasLista.add(bibliotecas.getString(key));
			bibliotecasIds.add(key);
			//Log.d("MARCILIO_DEBUG", "isso é uma key "+key);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busca_livro, menu);
		return true;
	}

}