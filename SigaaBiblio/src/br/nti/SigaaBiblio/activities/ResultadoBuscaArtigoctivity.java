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
		List<Artigo> listaArtigos = new ArrayList<Artigo>();
		
		listaArtigos = getIntent().getParcelableArrayListExtra("Artigos");	
		
		if(listaArtigos==null)
			lista.add("Não foram encontrados resultados para sua pesquisa.");
			else
				for(Artigo a : listaArtigos){
				lista.add(a.toString());
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

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resultado_busca_artigoctivity, menu);
		return true;
	}

}
