package br.nti.SigaaBiblio.activities;

import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nti.SigaaBiblio.R;

public class MenuActivity extends Activity {
	Button situacao;
	Button renovacao;
	Button buscaAcervo;
	Button buscaArtigo;
	Button historico;
	Button sair;
	TextView textViewSituacaoUsuario;
	ImageView imageView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);



		textViewSituacaoUsuario = (TextView)findViewById(R.id.textViewSituacaoUsuario1);
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		buscaAcervo = (Button)findViewById(R.id.consultarHistorico);
		buscaArtigo= (Button)findViewById(R.id.button2);
		situacao = (Button)findViewById(R.id.button4);
		renovacao = (Button) findViewById(R.id.button3);
		historico = (Button) findViewById(R.id.button5);
		sair = (Button) findViewById(R.id.button6);

		carregaDados();


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

		sair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, LoginActivity.class );
				startActivity(intent);

			}
		});
		historico.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, SelecionarHistoricoActivity.class );
				startActivity(intent);

			}
		});

	}

	private void carregaDados() {

		String idUsuarioBiblioteca = "";
		String nome = "";
		String matricula = "";
		boolean isAluno = false;
		String curso = "";
		String urlFoto = "";
		String unidade = "";
		
		String situacaoUsuario = "";

		nome = getIntent().getExtras().getString("Nome");
		idUsuarioBiblioteca = getIntent().getExtras().getString("IdUsuarioBiblioteca");

		if(isAluno = getIntent().getExtras().getBoolean("isAluno")){
			curso = getIntent().getExtras().getString("Curso");
			matricula = getIntent().getExtras().getString("Matricula");
			
			String nom[] = nome.split(" ");
			situacaoUsuario = matricula+"\n"+nom[0]+" "+nom[1]+
							  "\n"+curso.substring(0, curso.length() > 18 ? 18 : curso.length());
			
		}else{
			
			unidade = getIntent().getExtras().getString("Unidade");
			situacaoUsuario = nome+"\n"+unidade;
		}

		try {
			urlFoto = getIntent().getExtras().getString("Foto");
			async.execute(urlFoto);
	
			textViewSituacaoUsuario.setText(situacaoUsuario);
			imageView1.setImageBitmap(async.get());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	AsyncTask<String, Void, Bitmap> async = new AsyncTask<String, Void, Bitmap>(){

		@Override
		protected Bitmap doInBackground(String... urlFoto) {
			try{

				URL url = new URL(urlFoto[0]);
				return BitmapFactory.decodeStream(url.openConnection().getInputStream());
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return null;
		}


	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}



}


