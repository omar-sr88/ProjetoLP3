package com.nti.SigaaBiblio.model;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.activities.SituacaoUsuarioActivity;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Historico {
	
	public static LinearLayout TabelaHistorico(Activity activity, String biblioteca, String dataEmprestimo,
			String dataRenovacao, String dataDevolucao, Boolean renovavel, String informacoes){
		
		LinearLayout ll1 = new LinearLayout(activity);
        ll1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ll1.setOrientation(LinearLayout.VERTICAL);
        
        /***************    Componentes do Layout */
        
        // Componente 1
        LinearLayout ll2 = new LinearLayout(activity);
        LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llp2.setMargins(5, 5, 5, 5);
        ll2.setLayoutParams(llp2);
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView tv1 = new TextView(activity);
        LinearLayout.LayoutParams ltv1 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        ltv1.weight = (float) 0.50;
        tv1.setLayoutParams(ltv1);
        tv1.setText("Biblioteca");
        tv1.setTextSize(15);
        tv1.setTypeface(null, Typeface.BOLD);
        
        TextView tv2 = new TextView(activity);
        LinearLayout.LayoutParams ltv2 = new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
        ltv2.weight = (float) 0.50;
        tv2.setText(biblioteca);
        tv2.setLayoutParams(ltv2);
        
        ll2.addView(tv1);
        ll2.addView(tv2);
        ll1.addView(ll2);
        
        // Componente 2
        LinearLayout ll3 = new LinearLayout(activity);
        LinearLayout.LayoutParams llp3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        llp3.setMargins(5, 5, 5, 5);
        ll3.setOrientation(LinearLayout.HORIZONTAL);
        ll3.setLayoutParams(llp3);
        
        TextView tv3 = new TextView(activity);
        LinearLayout.LayoutParams ltv3 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        ltv3.weight = (float) 0.50;
        tv3.setLayoutParams(ltv3);
        tv3.setText("Data de Empréstimo");
        tv3.setTextSize(15);
        tv3.setTypeface(null, Typeface.BOLD);
        
        TextView tv4 = new TextView(activity);
        LinearLayout.LayoutParams ltv4 = new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
        ltv4.weight = (float) 0.50;
        tv4.setText(dataEmprestimo);
        tv4.setLayoutParams(ltv4);
        
        ll3.addView(tv3);
        ll3.addView(tv4);
        ll1.addView(ll3);
        
        // Componente 3
        LinearLayout ll4 = new LinearLayout(activity);
        LinearLayout.LayoutParams llp4 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llp4.setMargins(5, 5, 5, 5);
        ll4.setOrientation(LinearLayout.HORIZONTAL);
        ll4.setLayoutParams(llp4);
        
        TextView tv5 = new TextView(activity);
        LinearLayout.LayoutParams ltv5 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        ltv5.weight = (float) 0.50;
        tv5.setLayoutParams(ltv5);
        tv5.setText("Data de Renovação");
        tv5.setTextSize(15);
        tv5.setTypeface(null, Typeface.BOLD);
        
        TextView tv6 = new TextView(activity);
        LinearLayout.LayoutParams ltv6 = new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
        ltv6.weight = (float) 0.50;
        tv6.setText(dataRenovacao);
        tv6.setLayoutParams(ltv6);
        
        ll4.addView(tv5);
        ll4.addView(tv6);
        ll1.addView(ll4);
        
        //Componente 4
        
        LinearLayout ll5 = new LinearLayout(activity);
        LinearLayout.LayoutParams llp5 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llp5.setMargins(5, 5, 5, 5);
        ll5.setOrientation(LinearLayout.HORIZONTAL);
        ll5.setLayoutParams(llp5);
        
        TextView tv7 = new TextView(activity);
        LinearLayout.LayoutParams ltv7 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        ltv7.weight = (float) 0.50;
        tv7.setLayoutParams(ltv7);
        tv7.setText("Data de Devolução");
        tv7.setTextSize(15);
        tv7.setTypeface(null, Typeface.BOLD);
        
        TextView tv8 = new TextView(activity);
        LinearLayout.LayoutParams ltv8 = new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
        ltv8.weight = (float) 0.50;
        tv8.setText(dataDevolucao);
        tv8.setLayoutParams(ltv8);
        
        ll5.addView(tv7);
        ll5.addView(tv8);
        ll1.addView(ll5);
        
        //Componente 5
        
        LinearLayout ll6 = new LinearLayout(activity);
        LinearLayout.LayoutParams llp6 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llp6.setMargins(5, 5, 5, 5);
        ll6.setOrientation(LinearLayout.HORIZONTAL);
        ll6.setLayoutParams(llp6);
        
        TextView tv9 = new TextView(activity);
        LinearLayout.LayoutParams ltv9 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        ltv9.weight = (float) 0.50;
        tv9.setLayoutParams(ltv9);
        tv9.setText("Renovável");
        tv9.setTextSize(15);
        tv9.setTypeface(null, Typeface.BOLD);
        
        ImageView im1 = new ImageView(activity);
        LinearLayout.LayoutParams ltv10 = new LinearLayout.LayoutParams(25,25);
        ltv10.weight = (float) 0.50;
        ltv10.gravity = Gravity.LEFT;
        im1.setLayoutParams(ltv10);
        im1.setImageResource(renovavel ? R.drawable.check : R.drawable.not_available);
        
        ll6.addView(tv9);
        ll6.addView(im1);
        ll1.addView(ll6);
        
        //Componente 6
        
        TextView tv10 = new TextView(activity);
        LinearLayout.LayoutParams ltv11 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ltv8.weight = (float) 0.50;
        tv10.setLayoutParams(ltv11);
        tv10.setText(informacoes);
        tv10.setTextSize(18);
        tv10.setTypeface(null, Typeface.BOLD);
        
        ll1.addView(tv10);
        
        
        
        return ll1;
	}
	
}
