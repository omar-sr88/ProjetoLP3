package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import br.nti.SigaaBiblio.model.Artigo;
import br.nti.SigaaBiblio.model.Livro;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ResultadoBuscaArtigoctivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_busca_artigoctivity);
		
		List<String> lista = new ArrayList<String>(); 
		//recupera o nome dos livros
		Bundle extras = getIntent().getExtras();
		String artigos = extras.getString("Artigos");
		JSONObject artigosJSON;
		try {
			artigosJSON = new JSONObject(artigos);
			List<Artigo> listaDeArtigos = parseArtigos(artigosJSON);
			
			if(listaDeArtigos==null)
				lista.add("Não foram encontrados resultados para sua pesquisa.");
			else
				for(Artigo a : listaDeArtigos){
					lista.add(a.toString());
				}
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String erro = "Não foi possivel completar a requisição, por favor tente novamente";
			Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG)
			.show();
			e.printStackTrace();
		}
		
		ListView listaResultados = (ListView) findViewById(R.id.listViewResultadosArtigo);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, lista);

	    listaResultados.setAdapter(adapter);

		listaResultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ResultadoBuscaArtigoctivity.this, DadosTituloActivity.class );
				startActivity(intent);
			}

		});
	
	}

	
public List<Artigo> parseArtigos(JSONObject artigos){
		
		List<Artigo> listaArtigos = new ArrayList<Artigo>();
		
		String autor="", titulo="", palavrasChave="";
		
		Iterator<String> keys = artigos.keys();
		
		if(keys.hasNext())
		while(keys.hasNext()){
			String key=keys.next();
			try {
				JSONObject artigoJSON = artigos.getJSONObject(key);
				autor = JSONObject.NULL.equals(artigoJSON.get("Autor"))?"-":artigoJSON.getString("Autor");
				titulo = JSONObject.NULL.equals(artigoJSON.get("Titulo"))?"-":artigoJSON.getString("Titulo");
				palavrasChave = JSONObject.NULL.equals(artigoJSON.get("Assunto"))?"-":artigoJSON.getString("Assunto");
				String[] pChaves = palavrasChave.split("\\#\\$\\&");
				String palavraChaveFinal="";
				for(String c : pChaves){
					palavraChaveFinal+=c+"; ";
				}
				Artigo artigo = new Artigo(autor,titulo,palavraChaveFinal);
				listaArtigos.add(artigo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//		Log.d("MARCILIO_DEBUG", "isso é uma key "+autor);
		}
		else{
			return null; //não foi encontrado nenhum livro :(
		}
		
		return listaArtigos;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resultado_busca_artigoctivity, menu);
		return true;
	}

}
