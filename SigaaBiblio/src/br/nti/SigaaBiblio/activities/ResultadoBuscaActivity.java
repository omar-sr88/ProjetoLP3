package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import br.nti.SigaaBiblio.model.Livro;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ResultadoBuscaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_resultado_busca);
		
		List<String> lista = new ArrayList<String>(); 
		//recupera o nome dos livros
		Bundle extras = getIntent().getExtras();
		String livros = extras.getString("Livros");
		JSONObject livrosJSON;
		try {
			livrosJSON = new JSONObject(livros);
			List<Livro> listaDeLivros = parseLivros(livrosJSON);
			
			if(listaDeLivros==null)
				lista.add("Não foram encontrados resultados para sua pesquisa.");
			else
				for(Livro l : listaDeLivros){
					lista.add(l.toString());
				}
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String erro = "Não foi possivel completar a requisição, por favor tente novamente";
			Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG)
			.show();
			e.printStackTrace();
		}
	
				
		ListView listaResultados = (ListView) findViewById(R.id.listViewResultados);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, lista);

	    listaResultados.setAdapter(adapter);

		listaResultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ResultadoBuscaActivity.this, DadosTituloActivity.class );
				startActivity(intent);
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
	
	public List<Livro> parseLivros(JSONObject livros){
		
		List<Livro> listaLivros = new ArrayList<Livro>();
		
		String autor="", titulo="", edicao="", ano="", quantidade="", registroSistema="";
		
		Iterator<String> keys = livros.keys();
		
		if(keys.hasNext())
		while(keys.hasNext()){
			String key=keys.next();
			try {
				JSONObject livroJSON = livros.getJSONObject(key);
				autor = JSONObject.NULL.equals(livroJSON.get("Autor"))?"":livroJSON.getString("Autor");
				titulo = JSONObject.NULL.equals(livroJSON.get("Titulo"))?"":livroJSON.getString("Titulo");
				edicao = JSONObject.NULL.equals(livroJSON.get("Edicao"))?"":livroJSON.getString("Edicao");
				ano = JSONObject.NULL.equals(livroJSON.get("Ano"))?"":livroJSON.getString("Ano");
				quantidade = JSONObject.NULL.equals(livroJSON.get("QuantidadeAtivos"))?"":livroJSON.getString("QuantidadeAtivos");
				registroSistema = key;
				
				Livro livro = new Livro(autor,titulo,edicao,ano,quantidade,registroSistema);
				listaLivros.add(livro);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//		Log.d("MARCILIO_DEBUG", "isso é uma key "+autor);
		}
		else{
			return null; //não foi encontrado nenhum livro :(
		}
		
		return listaLivros;
		
	}




}