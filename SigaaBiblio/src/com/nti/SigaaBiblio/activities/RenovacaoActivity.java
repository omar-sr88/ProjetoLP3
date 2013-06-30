package com.nti.SigaaBiblio.activities;

import java.util.ArrayList;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RenovacaoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_renovacao);
		ListView listaLivros = (ListView) findViewById(R.id.listView1);
		
		/*
		 * A variavel livros_emprestados se tornará uma lista quando
		 * a parte de conexão com o servidor for implementada 
		 */
		
		String[] livros_emprestados = new String[] { "Código do livro; Autor, Título," +
				"Ano,etc\n"+"Data de empréstimo, data de devolução",
				"Código do livro; Autor, Título," +
				"Ano,etc\n"+"Data de empréstimo, data de devolução",
				"Código do livro; Autor, Título," + 
				"Ano,etc\n"+"Data de empréstimo, data de devolução",
				"Código do livro; Autor, Título," + 
				"Ano,etc\n"+"Data de empréstimo, data de devolução",
				"Código do livro; Autor, Título," + 
				"Ano,etc\n"+"Data de empréstimo, data de devolução"
				};
		
		       ArrayList<String> lista_para_adapter = new ArrayList<String>();
		       
		       for (int i = 0; i < livros_emprestados.length; ++i) {
		    	   lista_para_adapter.add(livros_emprestados[i]);
		       }
		       
		       ArrayAdapter<String> listViewAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, lista_para_adapter);	 
		       listaLivros.setAdapter(listViewAdapter);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.renovacao, menu);
		return true;
	}

}
