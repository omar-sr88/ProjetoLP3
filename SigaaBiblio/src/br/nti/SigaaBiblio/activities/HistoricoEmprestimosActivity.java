package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import br.nti.SigaaBiblio.layouts.HistoricoLayout;
import br.nti.SigaaBiblio.model.Emprestimo;

import com.nti.SigaaBiblio.R;

public class HistoricoEmprestimosActivity extends Activity {

	Button retornaPesquisaButton;
	Button retornaMenuButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_historico_emprestimos);
		page = (ViewFlipper)findViewById(R.id.viewFlipper2);	      
		animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
        animFlipOutForeward = AnimationUtils.loadAnimation(this, R.anim.flipout);
        animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
        animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);	
        page.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});
        
		retornaMenuButton = (Button) findViewById(R.id.retornarMenu);
		retornaPesquisaButton = (Button) findViewById(R.id.retornarPesquisa);	
		Intent intent = getIntent();

		ArrayList<Emprestimo> listaHistorico = (ArrayList<Emprestimo>) intent.getExtras().get("Historico");
				
		LinearLayout table;
		for (Emprestimo historico : listaHistorico) {

			table = HistoricoLayout.TabelaHistorico(this,
					historico.getTipoEmprestimo(),
					historico.getDataEmprestimo(),
					historico.getDataRenovacao(),
					historico.getPrazoDevolucao(),
					historico.getDataDevolucao(),
					historico.getInformacoes());
			
			
			page.addView(table);
		}
		
		
		retornaMenuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HistoricoEmprestimosActivity.this, MenuActivity.class );
				startActivity(intent);

			}
		});
		retornaPesquisaButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HistoricoEmprestimosActivity.this, SelecionarHistoricoActivity.class );
				startActivity(intent);

			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.historico_emprestimos, menu);
		return true;
	}
	
	
	
	// Implementacao da mudanca entre tabelas
		//
		ViewFlipper page;
		Animation animFlipInForeward;
	    Animation animFlipOutForeward;
	    Animation animFlipInBackward;
	    Animation animFlipOutBackward;
		
		private void SwipeRight(){
	    	page.setInAnimation(animFlipInBackward);
			page.setOutAnimation(animFlipOutBackward);
			page.showPrevious();
	    }
	    
	    private void SwipeLeft(){
	    	page.setInAnimation(animFlipInForeward);
			page.setOutAnimation(animFlipOutForeward);
			page.showNext();
	    }
	    
	    @Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
	    	return gestureDetector.onTouchEvent(event);
		}

		SimpleOnGestureListener simpleOnGestureListener 
	    = new SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {

				float sensitvity = 50;
				if((e1.getX() - e2.getX()) > sensitvity){
					SwipeLeft();
				}else if((e2.getX() - e1.getX()) > sensitvity){
					SwipeRight();
				}
				
				return true;
			}
	    	
	    };
	    
	    GestureDetector gestureDetector
		= new GestureDetector(simpleOnGestureListener);

}
