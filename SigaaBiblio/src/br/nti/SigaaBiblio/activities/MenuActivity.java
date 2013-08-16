package br.nti.SigaaBiblio.activities;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import Connection.ConnectJSON;
import Connection.HttpUtils;
import Connection.Operations;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
	String bibliotecas;

	
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
		
		
//		buscaAcervo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				
//				Intent intent = new Intent(MenuActivity.this, BuscaLivroActivity.class );
//				intent.putExtra("Bibliotecas", bibliotecas);
//				startActivity(intent);
//				
//
//			}
//		});

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
	
	
		public void consultarAcervo(View v){
			
			
			final ProgressDialog pd = new ProgressDialog(MenuActivity.this);
			pd.setMessage("Processando...");
			pd.setTitle("Aguarde");
			pd.setIndeterminate(false);
			
			bibliotecas=null;

			
			/*
			 * OBTEM O NOMES DAS BIBLIOTECAS ATIVAS
			 */
			new AsyncTask<Void,Void,Void>(){

				
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					pd.show();
				}
				
				@Override
				protected Void doInBackground(Void... arg0) {
					Map<String, String> map = new HashMap<String, String>();
					String jsonString;
					map.put("Operacao", String.valueOf(Operations.LISTAR_BIBLIOTECAS));
					JSONObject inputsJson = new JSONObject(map);
					JSONObject resposta;
					try {
						jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
						resposta = new JSONObject(jsonString);
						String bibliotecas=resposta.getString("Bibliotecas");
						Intent intent = new Intent(MenuActivity.this, BuscaLivroActivity.class );
						intent.putExtra("Bibliotecas", bibliotecas);
						startActivity(intent);
						
						//Log.d("MARCILIO_DEBUG", "isso é uma key "+resposta.getString("Bibliotecas"));
						//JSONObject bibliotecas=resposta.getJSONObject("Bibliotecas");
//						Iterator<String> keys = bibliotecas.keys(); //descobre as chaves que são os ids das bibliotecas
//						while(keys.hasNext()){
//							String key=keys.next();
//							Log.d("MARCILIO_DEBUG", "isso é uma key "+key);
//						}
						
					} catch (Exception ex){
						String erro = "Não foi possivel completar a requisição, por favor tente novamente";
						Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG)
						.show();
						ex.printStackTrace();
					}
					return null;
				}
				
//				
				@Override
				protected void onPostExecute(Void v) {
					// TODO Auto-generated method stub
					super.onPostExecute(v);
					if(pd!= null && pd.isShowing())
						pd.dismiss();
				}
				
			}.execute();
			
			

		}
		

}


