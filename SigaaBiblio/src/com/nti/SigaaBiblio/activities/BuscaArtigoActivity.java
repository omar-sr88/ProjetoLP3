package com.nti.SigaaBiblio.activities;

import com.nti.SigaaBiblio.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BuscaArtigoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busca_artigo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busca_artigo, menu);
		return true;
	}

}
