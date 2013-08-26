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
import android.widget.Button;

public class ExemplaresActivity extends Activity {

	Button retornarPesquisa;
	Button retornarMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exemplares);
		retornarPesquisa = (Button) findViewById(R.id.retornarPesquisa);
		retornarMenu = (Button) findViewById(R.id.retornarMenu);

		retornarPesquisa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ExemplaresActivity.this, BuscaLivroActivity.class );
				startActivity(intent);

			}
		});

		retornarMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ExemplaresActivity.this, MenuActivity.class );
				startActivity(intent);
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exemplares, menu);
		return true;
	}

}