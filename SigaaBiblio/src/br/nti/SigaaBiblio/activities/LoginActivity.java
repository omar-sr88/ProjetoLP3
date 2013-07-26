package br.nti.SigaaBiblio.activities;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import android.widget.Toast;

import com.nti.SigaaBiblio.R;

public class LoginActivity extends Activity implements OnClickListener {
	
	Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login = (Button)findViewById(R.id.consultarHistorico);
		
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
			con.execute();
			jsonResult = con.get(10,TimeUnit.SECONDS);
	
		} catch(Exception ex){
			ex.printStackTrace();
			return;
		}
		jsonResult = con.getJsonResult();
		String erro ="";
		try {
			
			erro = jsonResult.getString("Error");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!erro.isEmpty()){
			Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG).show();
			return;
		}
		
		
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
		
	}
	

}
