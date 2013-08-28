package br.nti.SigaaBiblio.activities;

import br.nti.SigaaBiblio.model.Artigo;

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
import android.widget.TextView;

public class ExemplarArtigoActivity extends Activity {

	TextView codigoBarras;
	TextView numero;
	TextView volume;
	TextView ano;
	TextView situacao;
	TextView biblioteca;
	TextView localizacao;
	Button retornarPesquisa;
	Button retornarMenu;
	
	//InfoCodigoBarra
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exemplar_artigo);
		
		retornarPesquisa = (Button) findViewById(R.id.retornarPesquisa);
		retornarMenu = (Button) findViewById(R.id.retornarMenu);

		Artigo artigo = (Artigo) getIntent().getExtras().getParcelable("ExemplarArtigo");
		
		codigoBarras= (TextView) findViewById(R.id.InfoCodigoBarra);
		numero= (TextView) findViewById(R.id.InfoNumero);
		volume= (TextView) findViewById(R.id.InfoVolume);
		ano= (TextView) findViewById(R.id.InfoAno);
		situacao= (TextView) findViewById(R.id.InfoStatus);
		biblioteca= (TextView) findViewById(R.id.textView1);
		localizacao= (TextView) findViewById(R.id.textView2);
		
		codigoBarras.setText(artigo.getCodigoDeBarras());
		numero.setText(artigo.getNumero());
		volume.setText(artigo.getVolume());
		ano.setText(artigo.getAno());
		situacao.setText(artigo.getSituacao());
		biblioteca.setText("Biblioteca: "+artigo.getBiblioteca());
		localizacao.setText("Localização: "+artigo.getLocalizacao());
		
		retornarPesquisa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ExemplarArtigoActivity.this, BuscaLivroActivity.class );
				startActivity(intent);

			}
		});

		retornarMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ExemplarArtigoActivity.this, MenuActivity.class );
				startActivity(intent);
			}
		});

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exemplar_artigo, menu);
		return true;
	}

}
