package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;



import com.nti.SigaaBiblio.R;

import android.os.Bundle;
import android.app.Activity;
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
	List<String> bibliotecasIds;
	String bibliotecaSelecionada;
	EditText titulo;
	EditText autor;
	EditText palavraChave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_busca_artigo);

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
		
		bibliotecasDisponiveis = (Spinner)findViewById(R.id.spinnerBibliotecaArtigo);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bibliotecasLista);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bibliotecasDisponiveis.setAdapter(spinnerArrayAdapter);
		bibliotecasDisponiveis.setSelection(arrayAdapter.getPosition("Buscar em Todas as Unidades"));
		bibliotecasDisponiveis.setOnItemSelectedListener(new BiblioItemSelectedListener());
		bibliotecaSelecionada="0";
	
	
	
	
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
		getMenuInflater().inflate(R.menu.busca_artigo, menu);
		return true;
	}

}