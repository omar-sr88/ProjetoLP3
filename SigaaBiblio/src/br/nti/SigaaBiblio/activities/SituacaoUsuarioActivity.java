package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import br.nti.SigaaBiblio.model.Emprestimo;
import br.nti.SigaaBiblio.model.Historico;
import br.nti.SigaaBiblio.model.Usuario;

import com.nti.SigaaBiblio.R;



public class SituacaoUsuarioActivity extends Activity {
	ViewFlipper page;
	TextView emprestimosAbertos;
    TextView podeEmprestimo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_situacao_usuario);
		
		emprestimosAbertos = (TextView)findViewById(R.id.textViewSituacaoUsuario1);
		podeEmprestimo = (TextView)findViewById(R.id.textViewSituacaoUsuario2);
		
		page = (ViewFlipper)findViewById(R.id.viewFlipper1);	      
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
        
        
        Bundle bund = getIntent().getExtras();
        ArrayList<Emprestimo> lista = (ArrayList<Emprestimo>) bund.get("Emprestimos");
        podeEmprestimo.setText(bund.getString("Mensagem"));
        emprestimosAbertos.setText("Total de Empréstimos em Aberto: "+Usuario.INSTANCE.getUserVinculo().getTotalEmprestimosAbertos());
        
        LinearLayout l1;
        for(Emprestimo emp : lista){
        	l1 = Historico.TabelaHistorico(this, emp.getBiblioteca(), emp.getDataEmprestimo(), emp.getDataRenovacao(),
        			emp.getDataDevolucao(), emp.isRenovavel(), emp.getInformacoesLivro());
        	page.addView(l1);
//        	l1 = Historico.TabelaHistorico(activity, biblioteca, dataEmprestimo, dataRenovacao, dataDevolucao, renovavel, informacoes)
        }
        // Conteudo das tabelas que serão exibidas
        
        //
        
	}
	
	
	// Implementacao da mudanca entre tabelas
	//
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