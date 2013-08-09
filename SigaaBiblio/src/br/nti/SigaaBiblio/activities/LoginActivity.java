package br.nti.SigaaBiblio.activities;

import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import Connection.ConnectJSON;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.nti.SigaaBiblio.model.Usuario;

import com.nti.SigaaBiblio.R;

public class LoginActivity extends Activity implements OnClickListener {

	Button login;
	EditText etLogin;
	EditText etSenha;
	String logPref, senhaPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			user.setPodeRealizarEmprestimo(jsonResult.getBoolean("PodeRealizarEmprestimo"));
			user.setEmprestimosAbertos(jsonResult.getInt("EmprestimosAbertos"));
			
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
