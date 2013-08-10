package br.nti.SigaaBiblio.activities;

import com.nti.SigaaBiblio.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BuscaLivroActivity extends Activity {

	Button goResultados;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_busca_livro);

		goResultados = (Button)findViewById(R.id.buttonBuscarLivro);

		goResultados.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BuscaLivroActivity.this, ResultadoBuscaActivity.class );
				startActivity(intent);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busca_livro, menu);
		return true;
	}

}