package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import Connection.ConnectJSON;
import Connection.HttpUtils;
import Connection.Operations;
import Connection.OperationsFactory;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.nti.SigaaBiblio.model.Biblioteca;
import br.nti.SigaaBiblio.model.Emprestimo;
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
		buscaAcervo = (Button)findViewById(R.id.consultarAcervo);
		buscaArtigo= (Button)findViewById(R.id.consultarArtigo);
		situacao = (Button)findViewById(R.id.button4);
		renovacao = (Button) findViewById(R.id.button3);
		historico = (Button) findViewById(R.id.button5);
		sair = (Button) findViewById(R.id.button6);

		carregaDados();
		



		situacao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(MenuActivity.this, SituacaoUsuarioActivity.class);
				final ProgressDialog pd = new ProgressDialog(MenuActivity.this);
				pd.setMessage("Processando...");
				pd.setTitle("Aguarde");
				pd.setIndeterminate(false);

				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						pd.show();
					}

					@Override
					protected Void doInBackground(Void... params) {
												
						Operations operacao = new OperationsFactory().getOperation(OperationsFactory.REMOTA);
						String mensagem = new String("");
						ArrayList<Emprestimo> emprestimos = operacao.consultarSituacao(Usuario.INSTANCE.getLogin(),Usuario.INSTANCE.getSenha(),mensagem);
						intent.putExtra("Mensagem", mensagem);
						intent.putExtra("Emprestimos", emprestimos);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if (pd != null && pd.isShowing())
							pd.dismiss();
					}

				};

				task.execute();
				try {
					task.get();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				startActivity(intent);

			}
		});

//		renovacao.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(MenuActivity.this, RenovacaoActivity.class );
//				startActivity(intent);
//
//			}
//		});

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
	
	
		//Consulta um acervo no sistema
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
						Operations json = new OperationsFactory().getOperation(OperationsFactory.REMOTA);
						ArrayList<Biblioteca> bibliotecas = json.listarBibliotecas();
						Intent intent = new Intent(MenuActivity.this, BuscaLivroActivity.class );
						intent.putExtra("Bibliotecas", bibliotecas);
						startActivity(intent);
//						
						//Log.d("MARCILIO_DEBUG", ""+bibliotecas);
					
					return null;
				}
				
				@Override
				protected void onPostExecute(Void v) {
					// TODO Auto-generated method stub
					super.onPostExecute(v);
					if(pd!= null && pd.isShowing())
						pd.dismiss();
				}
				
				}.execute();			
			
		}
	
		
		/*
		 * CONSULTAR ARTIGO
		 */
		
		public void consultarArtigo(View v){
			
			
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
					Operations json = new OperationsFactory().getOperation(OperationsFactory.REMOTA);
					ArrayList<Biblioteca> bibliotecas = json.listarBibliotecas();
					Intent intent = new Intent(MenuActivity.this, BuscaArtigoActivity.class );
					intent.putExtra("Bibliotecas", bibliotecas);
					startActivity(intent);								

					return null;
				}
				
				@Override
				protected void onPostExecute(Void v) {
					// TODO Auto-generated method stub
					super.onPostExecute(v);
					if(pd!= null && pd.isShowing())
						pd.dismiss();
				}
				
				}.execute();			
			
		}
		

		/*
		 * CONSULTAR EMPRESTIMOS RENOVAVEIS
		 */
		
		public void emprestimosRenovaveis(View v){
			
			
			final ProgressDialog pd = new ProgressDialog(MenuActivity.this);
			pd.setMessage("Processando...");
			pd.setTitle("Aguarde");
			pd.setIndeterminate(false);
			
			
			
			new AsyncTask<Void,Void,Void>(){

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					pd.show();
				}
				
				
				@Override
				protected Void doInBackground(Void... arg0) {
					Operations operacao = new OperationsFactory().getOperation(OperationsFactory.REMOTA);
					String usuario=Usuario.INSTANCE.getLogin();
					String senha = Usuario.INSTANCE.getSenha();
					ArrayList<Emprestimo> emprestimos = operacao.consultarEmprestimosRenovaveis(usuario,senha);
					Intent intent = new Intent(MenuActivity.this, RenovacaoActivity.class);
					intent.putExtra("EmprestimosRenovaveis", emprestimos);
					startActivity(intent);								

					return null;
				}
				
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


