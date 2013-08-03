package br.nti.SigaaBiblio.activities;

import org.json.JSONException;
import org.json.JSONObject;

import Connection.ConnectJSON;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
		
		//verifica
		if(Prefs.getLembrar(this)){
			String log,senha;
			log = "";
			senha = "";
			if(getPreferences(MODE_PRIVATE).contains("login"))			
				log = getPreferences(MODE_PRIVATE).getString("login", "");
			
			if(getPreferences(MODE_PRIVATE).contains("senha"))
				senha = getPreferences(MODE_PRIVATE).getString("senha", "");
			
			etLogin.setText(log);
			etSenha.setText(senha);
			
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
		try {
			con.execute(etLogin.getText().toString().trim(),
					ConnectJSON.getMd5Hash(etSenha.getText().toString().trim()));
			jsonResult = con.get();

		} catch(Exception ex){
			ex.printStackTrace();
			return;
		}
		jsonResult = con.getJsonResult();


		/**
		 * Atributos
		 */
		Intent intent = new Intent(this, MenuActivity.class);
				
		String erro ="";
		String mensagem = "";

		String idUsuarioBiblioteca = "";
		String nome = "";
		String matricula = "";
		boolean isAluno = false;
		String curso = "";
		String urlFoto = "";
		String unidade = "";


		//
		try {

			erro = jsonResult.getString("Error");
			mensagem = jsonResult.getString("Mensagem");

			intent.putExtra("Nome", nome = jsonResult.getString("Nome"));
			intent.putExtra("IdUsuarioBiblioteca",idUsuarioBiblioteca = jsonResult.getString("IdUsuarioBiblioteca"));
			intent.putExtra("isAluno",isAluno = Boolean.valueOf(jsonResult.getString("isAluno")));//Se False: Servidor		
			intent.putExtra("Foto", urlFoto = ConnectJSON.SISTEMA+jsonResult.getString("Foto"));
			
			
			if(isAluno){
				intent.putExtra("Matricula", matricula = jsonResult.getString("Matricula"));
				intent.putExtra("Curso",curso = jsonResult.getString("Curso"));
			}else{
				intent.putExtra("Unidade",unidade = jsonResult.getString("Unidade"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(!erro.isEmpty()){
			Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG).show();
			return;
		}else{
			mensagem = nome+"\n"+matricula+"\n"+String.valueOf(isAluno?"Aluno":"Servidor "+unidade)+"\n"+curso;
			Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
		}

		/**
		 * URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
			Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			imageView.setImageBitmap(bmp);
		 */

		if(Prefs.getLembrar(this)){
			String login = etLogin.getText().toString().trim();
			String senha = etSenha.getText().toString().trim();
			
			if(!login.isEmpty() && login!=null && !senha.isEmpty() && senha!=null){	
				getPreferences(MODE_PRIVATE).edit().putString("login", login).commit();
				getPreferences(MODE_PRIVATE).edit().putString("senha", ConnectJSON.getMd5Hash(senha)).commit();		
			}
			
		}
		startActivity(intent);

	}
	
	


}
