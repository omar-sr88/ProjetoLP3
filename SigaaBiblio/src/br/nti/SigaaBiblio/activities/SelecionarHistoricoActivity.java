package br.nti.SigaaBiblio.activities;

import java.text.DateFormat;
import java.util.Calendar;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SelecionarHistoricoActivity extends Activity {

	EditText inputDataInicial;
	EditText inputDataFinal;
	ImageButton dataInicialButton;
	ImageButton dataFinalButton;
	Button pesquisar;
	DateFormat formatDateTime;
	Calendar dateTime;
	DatePickerDialog.OnDateSetListener dataInicioListener;
	DatePickerDialog.OnDateSetListener dataFimListener;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selecionar_historico);
		pesquisar = (Button) findViewById(R.id.consultarHistorico);
		dataInicialButton = (ImageButton) findViewById(R.id.calendario_img1);
		dataFinalButton = (ImageButton) findViewById(R.id.calendario_img2);
		inputDataInicial = (EditText) findViewById(R.id.dataInicialText);
		inputDataFinal= (EditText) findViewById(R.id.editTextLoginUsuario);

		//obtem a instancia do calendario
		formatDateTime=DateFormat.getDateTimeInstance();
		dateTime=Calendar.getInstance();

		//listeners dos dialog picker  

		dataInicioListener=new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int ano, int mes,int dia) {
					// TODO Auto-generated method stub
					inputDataInicial.setText(""+dia+"-"+mes+"-"+ano);
				}
			};

		dataFimListener=new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int ano, int mes,int dia) {
					// TODO Auto-generated method stub
				inputDataFinal.setText(""+dia+"-"+mes+"-"+ano);

				}
			};



		dataInicialButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(SelecionarHistoricoActivity.this, dataInicioListener, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();


			}
		});

		dataFinalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(SelecionarHistoricoActivity.this, dataFimListener, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();

			}
		});
		
		pesquisar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelecionarHistoricoActivity.this, HistoricoEmprestimosActivity.class );
				startActivity(intent);

			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selecionar_historico, menu);
		return true;
	}





}