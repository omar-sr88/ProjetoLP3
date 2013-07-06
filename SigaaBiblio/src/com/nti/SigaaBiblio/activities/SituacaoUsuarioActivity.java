package com.nti.SigaaBiblio.activities;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.model.Historico;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class SituacaoUsuarioActivity extends Activity {
    ViewFlipper page;
    Animation animFlipInForeward;
    Animation animFlipOutForeward;
    Animation animFlipInBackward;
    Animation animFlipOutBackward;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_situacao_usuario);
		
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
        
        
        
        LinearLayout ll1 = Historico.TabelaHistorico(this,"Central","15/03/2013","30/03/2013","15/04/2013",true,"Informações do Livro: NOME; AUTOR");
        LinearLayout ll2 = Historico.TabelaHistorico(this,"Setorial","30/03/2013","15/04/2013","15/04/2013",false,"Informações do Livro: NOME; AUTOR");
        
       
        page.addView(ll1);
        page.addView(ll2);
        
	}
	
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