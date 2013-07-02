package com.nti.SigaaBiblio.activities;

import android.os.Bundle;
import com.nti.SigaaBiblio.R;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {
	Button situacao;
	Button renovacao;
	Button buscaAcervo;
	Button buscaArtigo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		buscaAcervo = (Button)findViewById(R.id.button1);
		buscaArtigo= (Button)findViewById(R.id.button2);
		situacao = (Button)findViewById(R.id.button4);
		renovacao = (Button) findViewById(R.id.button3);
		
		buscaAcervo.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MenuActivity.this, BuscaLivroActivity.class );
						startActivity(intent);
						
					}
				});
		
		buscaArtigo.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MenuActivity.this, BuscaArtigoActivity.class );
						startActivity(intent);
						
					}
				});
		
		
		situacao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, SituacaoUsuarioActivity.class );
				startActivity(intent);
				
			}
		});
		
		renovacao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, RenovacaoActivity.class );
				startActivity(intent);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

}
