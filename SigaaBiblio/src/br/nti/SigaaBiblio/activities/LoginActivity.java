package br.nti.SigaaBiblio.activities;

import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import Connection.ConnectJSON;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nti.SigaaBiblio.R;

public class LoginActivity extends Activity implements OnClickListener {

	Button login;
	EditText etLogin;
	EditText etSenha;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		login = (Button)findViewById(R.id.consultarHistorico);
		etLogin = (EditText)findViewById(R.id.editTextLoginUsuario);
		etSenha = (EditText)findViewById(R.id.editTextSenhaUsuario);

		login.setOnClickListener(this);
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
		try {
			con.execute(etLogin.getText().toString().trim(), etSenha.getText().toString().trim());
			jsonResult = con.get();

		} catch(Exception ex){
			ex.printStackTrace();
			return;
		}
		jsonResult = con.getJsonResult();


		/**
		 * Atributos
		 */
		String erro ="";
		String mensagem = "";
		String nome = "";
		String matricula = "";
		String vinculo = "";
		String curso = "";


		//
		try {

			erro = jsonResult.getString("Error");
			mensagem = jsonResult.getString("Mensagem");
			nome = jsonResult.getString("Nome");
			matricula = jsonResult.getString("Matricula");
			vinculo = jsonResult.getString("Vinculo");
			curso = jsonResult.getString("Curso");


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(!erro.isEmpty()){
			Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG).show();
			return;
		}else{
			mensagem = nome+"\n"+matricula+"\n"+vinculo+"\n"+curso;
			Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
		}
		
		

		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);

	}


}
