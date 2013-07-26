package br.nti.SigaaBiblio.activities;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HistoricoEmprestimosActivity extends Activity {

	Button retornaPesquisaButton;
	Button retornaMenuButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historico_emprestimos);
		
		retornaMenuButton = (Button) findViewById(R.id.retornarMenu);
		retornaPesquisaButton = (Button) (Button) findViewById(R.id.retornarPesquisa);
		
		retornaMenuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HistoricoEmprestimosActivity.this, MenuActivity.class );
				startActivity(intent);

			}
		});
		retornaPesquisaButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HistoricoEmprestimosActivity.this, SelecionarHistoricoActivity.class );
				startActivity(intent);

			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.historico_emprestimos, menu);
		return true;
	}

}
