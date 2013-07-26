package br.nti.SigaaBiblio.activities;

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

public class ResultadoBuscaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_busca);
		ListView listaResultados = (ListView) findViewById(R.id.listViewResultados);

		//variavel values apenas para fins de demonstração da interface
		String[] values = new String[] {"Autor, Titulo, Edição, Ano, Quantidade",
	    								"Autor, Titulo, Edição, Ano, Quantidade",
							    		"Autor, Titulo, Edição, Ano, Quantidade",
							    		"Autor, Titulo, Edição, Ano, Quantidade",
							    		"Autor, Titulo, Edição, Ano, Quantidade",
							    		"Autor, Titulo, Edição, Ano, Quantidade",
							    		"Autor, Titulo, Edição, Ano, Quantidade"};
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, values);

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





}