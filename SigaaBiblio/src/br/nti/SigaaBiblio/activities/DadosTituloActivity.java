package br.nti.SigaaBiblio.activities;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DadosTituloActivity extends Activity {

	Button buttonVerifDisp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dados_titulo);

		ListView l = (ListView) findViewById(R.id.listViewDadosTitulo);
	    String[] values = new String[] { "Titulo: Programação Android", "Autor: Alguem do Google ", "Paginas: Um monte",
	        "Windows", "Ubuntu", "Android", "iPhone", "Windows" };
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, values){

	    	  /*
	    	   * Desabilita o focus da listView que aqui foi utilizada apenas por
	    	   * questão de estetica.  
	    	   */
	    	  @Override 
	    	  public boolean isEnabled(int position) 
	            { 
	                    return false; 
	            } 
	        };

	    l.setAdapter(adapter);

	    buttonVerifDisp=(Button) findViewById(R.id.buttonVerifDisp);

	    buttonVerifDisp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DadosTituloActivity.this, ExemplaresActivity.class );
				startActivity(intent);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dados_titulo, menu);
		return true;
	}



}