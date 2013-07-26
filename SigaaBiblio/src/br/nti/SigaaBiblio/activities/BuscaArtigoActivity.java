package br.nti.SigaaBiblio.activities;

import com.nti.SigaaBiblio.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BuscaArtigoActivity extends Activity {

	Button goResultados;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busca_artigo);

		goResultados = (Button)findViewById(R.id.buttonBuscarArtigo);

		goResultados.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BuscaArtigoActivity.this, ResultadoBuscaActivity.class );
				startActivity(intent);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busca_artigo, menu);
		return true;
	}

}