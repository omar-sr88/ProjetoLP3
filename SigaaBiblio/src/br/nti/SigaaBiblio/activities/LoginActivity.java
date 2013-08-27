package br.nti.SigaaBiblio.activities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import Connection.ConnectJSON;
import Connection.HttpUtils;
import Connection.Operations;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.Toast;
import br.nti.SigaaBiblio.model.Usuario;
import br.nti.SigaaBiblio.model.VinculoUsuarioSistema;

import com.nti.SigaaBiblio.R;

public class LoginActivity extends Activity implements OnClickListener {

	Button login;
	EditText etLogin;
	EditText etSenha;
	String logPref, senhaPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_login);

			
	 	//Na criacao da ACtivity eu tento buscar a preferencia "Lembrar usuario" 
		logPref = "";
		senhaPref = "";

		login = (Button) findViewById(R.id.consultarHistorico);
		etLogin = (EditText) findViewById(R.id.editTextLoginUsuario);
		etSenha = (EditText) findViewById(R.id.editTextSenhaUsuario);
		login.setOnClickListener(this);

		//Se a pref lembrar eh true eu tento setar os vlores de logpref e senhapref
		if (Prefs.getLembrar(this)) {
		
			if (getPreferences(MODE_PRIVATE).contains("login"))
				logPref = getPreferences(MODE_PRIVATE).getString("login", "");

			if (getPreferences(MODE_PRIVATE).contains("senha"))
				senhaPref = getPreferences(MODE_PRIVATE).getString("senha", "");

			//se nao forem nulos, ele ja realiza o login chamando o botao de login
			if (!logPref.isEmpty() && logPref != null && !senhaPref.isEmpty()
					&& senhaPref != null)
				login.performClick();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		ConnectJSON con = new ConnectJSON(LoginActivity.this);
		JSONObject jsonResult = null;	
		Usuario user = Usuario.prepareUsuario();
		String login = etLogin.getText().toString().trim();
		String senha = ConnectJSON.getMd5Hash(etSenha.getText().toString().trim());		
		user.setLogin(login);
		user.setSenha(senha);
		try {
			if (!Prefs.getLembrar(this)) {
				con.execute(login, senha);
			} else {
				con.execute(logPref, senhaPref);
			}

			jsonResult = con.get(20,TimeUnit.SECONDS);
			
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), "Ocorreu um error com a conexão!", Toast.LENGTH_LONG).show();
			con.desabilitaProgressDialog();
			ex.printStackTrace();
			return;
		}
		
		jsonResult = con.getJsonResult();

		/**
		 * Atributos
		 */
		Intent intent = new Intent(this, MenuActivity.class);		
		
		String erro = "";
		String mensagem = "";
		
		//
		try {
			erro = jsonResult.getString("Error");
			mensagem = jsonResult.getString("Mensagem");
			
			if(!erro.isEmpty()){
				Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG)
				.show();
				return;
			}
			
			user.setNome(jsonResult.getString("Nome"));
			user.setIdUsuarioBiblioteca(jsonResult.getString("IdUsuarioBiblioteca"));
			user.setAluno( Boolean.valueOf(jsonResult.getString("isAluno")));
			user.setUrlFoto(ConnectJSON.SISTEMA	+ jsonResult.getString("Foto"));
			user.setUserVinculo(new VinculoUsuarioSistema(jsonResult.getInt("EmprestimosAbertos"),jsonResult.getBoolean("PodeRealizarEmprestimo")));
			
			if (user.isAluno()) {
				user.setMatricula(jsonResult.getString("Matricula"));
				user.setCurso(jsonResult.getString("Curso"));
							
			} else {
				user.setUnidade(jsonResult.getString("Unidade"));
			}

		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Ocorreu um erro!", Toast.LENGTH_LONG)
			.show();
			e.printStackTrace();
			return;
		}

		if (!erro.isEmpty()) {
			Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG)
					.show();
			return;
		} else {
			Toast.makeText(getApplicationContext(), user.toString(), Toast.LENGTH_LONG)
					.show();
		}
		
		if (Prefs.getLembrar(this)) {
			//se fez o login corretamente, ele salva o usuario
			
			if (!login.isEmpty() && login != null && !senha.isEmpty()
					&& senha != null) {
				getPreferences(MODE_PRIVATE).edit().putString("login", login)
						.commit();
				getPreferences(MODE_PRIVATE).edit()
						.putString("senha", ConnectJSON.getMd5Hash(senha))
						.commit();
			}

		}
		
		/**
		 * CAPTURA BIBLIOTECAS QUE ESTÃO NO BANCO
		 * COLOCAR NO LUGAR CORRETO!
		 */
		new AsyncTask<Void,Void,Void>(){

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
					String bibliotecas = resposta.getString("Bibliotecas");
					String nome = new JSONObject(bibliotecas).getString("9763");
					
					//Log.d("IRON DEBUG", nome);
				} catch (Exception ex){
					ex.printStackTrace();
				}
				return null;
			}
			
			}.execute();
			
			new AsyncTask<Void,Void,Void>(){
				//IdBiblioteca":"9763","TituloBusca":"Metodologia","AutorBusca":"","AssuntoBusca":""}
				/**
				 * Retorno: Livros
				 * Autor  (String)
				 * Titulo (String)
				 * Edicao (String)
				 * Ano    (Int)
				 * QuantidadeAtivos (Int)
				 */
				@Override
				protected Void doInBackground(Void... arg0) {
					Map<String, String> map = new HashMap<String, String>();
					String jsonString;
					map.put("Operacao", String.valueOf(Operations.CONSULTAR_ACERVO_LIVRO));
					map.put("IdBiblioteca","0");
					map.put("TituloBusca", "Metodologia");
					map.put("AutorBusca", "");
					map.put("AssuntoBusca","");
					JSONObject inputsJson = new JSONObject(map);
					JSONObject resposta;
					
					try {
						jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
						resposta = new JSONObject(jsonString);
						resposta = new JSONObject(resposta.getString("Livros"));
						resposta = resposta.getJSONObject("112204");
						//String titulo = resposta.getString("Titulo");
						
						Log.d("IRON_DEBUG", resposta.toString());//ou Artigos
					} catch (Exception ex){
						ex.printStackTrace();
					}
					return null;
				}				
			}.execute();
			
			new AsyncTask<Void,Void,Void>(){
				/**
				 * Retorno: Emprestimos
				 * TipoEmprestimo (String)
				 * DataEmprestimo (Date)
				 * DataRenovacao  (Date)
				 * PrazoDevolucao (Date)
				 * DataDevolucao  (Date)
				 * Informacao     (String)   -- Informacao do Material
				 * 
				 */
				//{"Login":"eduardogama","Senha":"202cb962ac59075b964b07152d234b70","Operacao":"6","Fim":"","Inicio":""}
				@Override
				protected Void doInBackground(Void... arg0) {
					Map<String, String> map = new HashMap<String, String>();
					String jsonString;
					map.put("Operacao", String.valueOf(Operations.MEUS_EMPRESTIMOS));
					map.put("Login", "eduardogama");
					map.put("Senha", "202cb962ac59075b964b07152d234b70");
					map.put("Inicio", "");
					map.put("Fim", "");
					JSONObject inputsJson = new JSONObject(map);
					JSONObject resposta;
					
					try {
						jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
						resposta = new JSONObject(jsonString);					
						resposta = new JSONObject(resposta.getString("Emprestimos"));
						Iterator it = resposta.keys();
					while (it.hasNext()) {
						JSONObject obj = resposta.getJSONObject((String) it.next());
						//Log.d("IRON_DEBUG", obj.toString());
					}
					} catch (Exception ex){
						ex.printStackTrace();
					}
					return null;
				}				
			}.execute();
			
			new AsyncTask<Void,Void,Void>(){
				/**
				 * Retorno: EmprestimosAbertos
				 * Informacao         (String)
				 * DataEmprestimo     (Date)
				 * Prazo              (Date)
				 * IdMaterial         (int) -> Será usado para renovacao
				 * 
				 */
				@Override
				protected Void doInBackground(Void... arg0) {
					try {
					Map<String, String> map = new HashMap<String, String>();
					String jsonString;
					map.put("Operacao", String.valueOf(Operations.LIVROS_EMPRESTADOS));
					map.put("Login", "ironaraujo");
					map.put("Senha", "202cb962ac59075b964b07152d234b70");
					
					JSONObject inputsJson = new JSONObject(map);
					JSONObject resposta;
					
					
					jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
					resposta = new JSONObject(jsonString);					
					resposta = new JSONObject(resposta.getString("EmprestimosAbertos"));
					Iterator it = resposta.keys();
					String renovacao = "";
					
					while (it.hasNext()) {
						JSONObject obj = resposta.getJSONObject((String) it.next());
						renovacao += obj.getInt("IdMaterial")+";";              ///String de Renovacao: ID's separados por ';'
						//Log.d("IRON_DEBUG", obj.toString());// ou Artigos
					}				
					
					
					///// FIM DA LISTAGEM DOS EMPRESTIMOS
					//// RENOVACAO DE EMPRESTIMOS
					
					/**
					 * Retorno: RenovacaoEmprestimo
					 * InfoRenovacao: String
					 * CodigoAutenticacao : String
					 * 
					 * Verificar Mensagem e ERROR
					 */
					map = new HashMap<String, String>();					
					
					map.put("Operacao", String.valueOf(Operations.RENOVACAO));
					map.put("Login", "ironaraujo");
					map.put("Senha", "202cb962ac59075b964b07152d234b70");		
					map.put("IdLivrosRenovacao", renovacao); // ESTA RENOVANDO TODOS OS LIVROS
					inputsJson = new JSONObject(map);					
					
					jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
					resposta = new JSONObject(jsonString);	
					
					resposta = new JSONObject(resposta.getString(("RenovacaoEmprestimo")));
					//{"Mensagem":"","RenovacaoEmprestimo":"{\"InfoRenovacao\":\"00001\/06 - Verger, Pierre. Fluxo e refluxo do tráfico de escravos entre o Golfo do Benin e a Bahia de Todos os Santos dos séculos XVII a XIX.\/ - Biblioteca Central Prazo para Devolução: 12\/09\/2013 23:59:59\\n\",\"CodigoAutenticacao\":\"834B.7D5D5BB \"}","Error":"null"}
					//Log.d("IRON_DEBUG_CODIGOAUTENTICACAO", resposta.getString("CodigoAutenticacao"));
					} catch (Exception ex){
						ex.printStackTrace();
					}
					return null;
				}				
			}.execute();
			
			
			new AsyncTask<Void,Void,Void>(){
				/**
				 * Retorno:   Registro         : int
				 * 			  NumeroChamada    : String
				 * 			  Titulo           : String  
				 * 			  SubTitulo        : String
				 * 			  Assunto          : String
				 * 			  Autor            : String
				 * 			  AutorSecundario  : String
				 * 			  Publicacao       : String    (Local de Publicacao)
				 * 		      Editora 		   : String
				 * 			  AnoPublicacao    : int
				 * 			  NotasGerais 	   : String				
				 * 
				 */
				
				@Override
				protected Void doInBackground(Void... arg0) {
					Map<String, String> map = new HashMap<String, String>();
					String jsonString;
					map.put("Operacao", String.valueOf(Operations.INFORMACOES_EXEMPLAR_ACERVO));
					map.put("IdDetalhes", "112204");					
					JSONObject inputsJson = new JSONObject(map);
					
					
					try {
						jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
						JSONObject resposta = new JSONObject(jsonString);					
						Log.d("IRON_DEBUG", resposta.toString());
					} catch (Exception ex){
						ex.printStackTrace();
					}
					return null;
				}				
			}.execute();
			
			
			new AsyncTask<Void,Void,Void>(){
				/**
				 * Output  : Biblioteca       : String
				 * 			 CodigoBarras     : String
				 * 			 Localizacao      : String
				 * 			 Situacao         : String
				 * 			 AnoCronologico   : String
				 * 			 Ano			  : String
				 * 			 DiaMes  		  : String
				 * 			 Volume			  : String
				 * 			 Numero			  : String
				 * 			 AutorSecundario  : String
				 * 			 IntervaloPaginas : String
				 * 			 LocalPublicacao  : String
				 * 			 Editora   		  : String
				 * 			 AnoExemplar 	  : String
				 * 		     Resumo			  : String		
				 */
				@Override
				protected Void doInBackground(Void... arg0) {
					Map<String, String> map = new HashMap<String, String>();
					String jsonString;
					map.put("Operacao", String.valueOf(Operations.INFORMACOES_EXEMPLAR_ARTIGO));
					map.put("IdDetalhes", "6304");					
					JSONObject inputsJson = new JSONObject(map);
					
					
					try {
						jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
						JSONObject resposta = new JSONObject(jsonString);					
						Log.d("IRON_DEBUG", resposta.toString());
					} catch (Exception ex){
						ex.printStackTrace();
					}
					return null;
				}				
			}.execute();
			
			
			
		startActivity(intent);
		finish();

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
