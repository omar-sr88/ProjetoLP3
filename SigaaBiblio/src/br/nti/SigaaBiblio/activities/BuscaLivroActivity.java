package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nti.SigaaBiblio.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class BuscaLivroActivity extends Activity {

	Button goResultados;
	Spinner bibliotecasDisponiveis;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_busca_livro);
		
		//recupera o nome das bibliotecas
		Bundle extras = getIntent().getExtras();
		String bibliotecasRaw = extras.getString("Bibliotecas");
		Log.d("MARCILIO_DEBUG", bibliotecasRaw);
		Map<Integer,String> bibliotecaMapa =parseBibliotecas(bibliotecasRaw);
		List<String> bibliotecas = new ArrayList<String>(bibliotecaMapa.values());
		
		//Log.d("MARCILIO_DEBUG", d.toString());
		
		//Exibe as bibliotecas no spinner
		bibliotecasDisponiveis = (Spinner)findViewById(R.id.spinnerBibliotecaLivro);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bibliotecas);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		bibliotecasDisponiveis.setAdapter(spinnerArrayAdapter);

		
		//seta o onclick
		
		
		goResultados = (Button)findViewById(R.id.buttonBuscarLivro);
		goResultados.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BuscaLivroActivity.this, ResultadoBuscaActivity.class );
				startActivity(intent);

			}
		});

	}
	
	/*
	 * Faz o Parse do objeto JSON para um mapa 
	 */
	
	private static final Pattern VALID_PATTERN = Pattern.compile("[0-9]+");
	
	public Map<Integer, String> parseBibliotecas(String bibliotecasRaw){
		
		Map<Integer, String> bibliotecas = new HashMap<Integer,String>();
	 	String myString=bibliotecasRaw;
	    myString = myString.replaceAll("[0-9]", "");
	  	myString = myString.replaceAll("\\{", "");
	  	myString = myString.replaceAll("\\}", "");
	  	myString = myString.replaceAll("=", "");
	  	String[] id=myString.split(",");
	  	int i = 0; 
	 	Matcher matcher = VALID_PATTERN.matcher(bibliotecasRaw);
	    while (matcher.find()&&i<id.length) {
	        bibliotecas.put(Integer.parseInt(matcher.group()),id[i++]);
	    }
	    
	    //adiciona a opção de pesquisar em todas as bibliotecas
	    bibliotecas.put(999999, "Todas as bibliotecas");
	    
	    return bibliotecas;		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busca_livro, menu);
		return true;
	}

}