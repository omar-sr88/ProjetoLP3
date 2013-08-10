package br.nti.SigaaBiblio.activities;

import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.nti.SigaaBiblio.model.Usuario;

import com.nti.SigaaBiblio.R;


public class MenuActivity extends Activity {
	Button situacao;
	Button renovacao;
	Button buscaAcervo;
	Button buscaArtigo;
	Button historico;
	Button sair;
	TextView textViewSituacaoUsuario;
	TextView textViewPodeFazerEmprestimo;
	TextView textViewTotalEmprestimosAbertos;
	ImageView imageView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu);

		textViewSituacaoUsuario = (TextView)findViewById(R.id.textViewSituacaoUsuario1);
		textViewPodeFazerEmprestimo = (TextView)findViewById(R.id.textViewPodeFazerEmprestimo);
		textViewTotalEmprestimosAbertos = (TextView)findViewById(R.id.textViewTotalEmprestimos);
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
		Usuario user = Usuario.INSTANCE;	
		String situacaoUsuario = "";
		
		if(user.isAluno()){
		
			String nom[] = user.getNome().split(" ");
			situacaoUsuario = user.getMatricula()+"\n"+nom[0]+" "+nom[1]+ "\n"
			+user.getCurso().substring(0, user.getCurso().length() > 18 ? 18 : user.getCurso().length());
		
		}else{
			situacaoUsuario = user.getNome()+"\n"+user.getUnidade();
		}
		
		try {
			textViewSituacaoUsuario.setText(situacaoUsuario);
			imageView1.setImageBitmap(user.geraBitmap());
			textViewPodeFazerEmprestimo.setText("Posso fazer Empréstimos: "+(user.getUserVinculo().isPodeRealzarEmprestimos()?"SIM":"NÃO"));
			textViewTotalEmprestimosAbertos.setText("Total de Empréstimos em Aberto: "+user.getUserVinculo().getTotalEmprestimosAbertos());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_settings:
	      startActivity(new Intent(this, Prefs.class));
	      return true;
	      // More items go here (if any) ...
	    }
	    return false;
	  }


}


