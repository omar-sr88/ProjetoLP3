package br.nti.SigaaBiblio.activities;

import java.util.HashMap;
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

		logPref = "";
		senhaPref = "";

		login = (Button) findViewById(R.id.consultarHistorico);
		etLogin = (EditText) findViewById(R.id.editTextLoginUsuario);
		etSenha = (EditText) findViewById(R.id.editTextSenhaUsuario);
		login.setOnClickListener(this);

		if (Prefs.getLembrar(this)) {

			if (getPreferences(MODE_PRIVATE).contains("login"))
				logPref = getPreferences(MODE_PRIVATE).getString("login", "");

			if (getPreferences(MODE_PRIVATE).contains("senha"))
				senhaPref = getPreferences(MODE_PRIVATE).getString("senha", "");

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
		String login = etLogin.getText().toString().trim();
		String senha = ConnectJSON.getMd5Hash(etSenha.getText().toString().trim());		
		 		
		try {
			if (!Prefs.getLembrar(this)) {
				con.execute(login, senha);
			} else {
				con.execute(logPref, senhaPref);
			}

			jsonResult = con.get(20,TimeUnit.SECONDS);
			
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), "Ocorreu um error com a conexão!", Toast.LENGTH_LONG).show();
			ex.printStackTrace();
			return;
		}
		
		jsonResult = con.getJsonResult();

		/**
		 * Atributos
		 */
		Intent intent = new Intent(this, MenuActivity.class);
		Usuario.prepareUsuario();
		Usuario user = Usuario.INSTANCE;
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
					
					Log.d("IRON DEBUG", nome);
				} catch (Exception ex){
					ex.printStackTrace();
				}
				return null;
			}
			
			}.execute();
			
			new AsyncTask<Void,Void,Void>(){
				//IdBiblioteca":"9763","TituloBusca":"Metodologia","AutorBusca":"","AssuntoBusca":""}
				@Override
				protected Void doInBackground(Void... arg0) {
					Map<String, String> map = new HashMap<String, String>();
					String jsonString;
					map.put("Operacao", String.valueOf(Operations.CONSULTAR_ACERVO_LIVRO));
					map.put("IdBiblioteca","9763");
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
						String titulo = resposta.getString("Titulo");
						
						Log.d("IRON_DEBUG", titulo);//ou Artigos
					} catch (Exception ex){
						ex.printStackTrace();
					}
					return null;
				}
				
			}.execute();
			
			new AsyncTask<Void,Void,Void>(){

				@Override
				protected Void doInBackground(Void... params) {
					Map<String, String> map = new HashMap<String, String>();
					String jsonString;
					map.put("Operacao", String.valueOf(Operations.MINHA_SITUACAO));
					map.put("Login","eduardogama");
					map.put("Senha", "202cb962ac59075b964b07152d234b70");
					
					JSONObject inputsJson = new JSONObject(map);
					JSONObject resposta;
					
					try {
						jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());						
						resposta = new JSONObject(jsonString);	
						resposta = new JSONObject(resposta.getString("Emprestimos"));
						resposta = new JSONObject(resposta.getString("28132"));
						
						Log.d("IRON_DEBUG", resposta.toString());//ou Artigos
					} catch (Exception ex){
						ex.printStackTrace();
					}
					return null;
				}
					
				
			}.execute();
		
		
		//
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
