package com.nti.SigaaBiblio.activities;

import com.nti.SigaaBiblio.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements OnClickListener {
	
	Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login = (Button)findViewById(R.id.button1);
		
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
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
		
	}
	

}