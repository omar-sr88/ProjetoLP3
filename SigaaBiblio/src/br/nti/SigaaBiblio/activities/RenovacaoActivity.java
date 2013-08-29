package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import br.nti.SigaaBiblio.model.Biblioteca;
import br.nti.SigaaBiblio.model.Emprestimo;
import br.nti.SigaaBiblio.model.Usuario;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import Connection.Operations;
import Connection.OperationsFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RenovacaoActivity extends Activity {

	Map<String,Boolean> renovar;
	Map<String,String> keysEmprestimos;
	String resposta;
	ArrayList<EmprestimoAdapterUtils> lista_para_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_renovacao);
		
		
		renovar= new HashMap<String, Boolean>();
		keysEmprestimos= new HashMap<String, String>();
		
		 Bundle bund = getIntent().getExtras();
	     ArrayList<Emprestimo> emprestimos = (ArrayList<Emprestimo>) bund.get("EmprestimosRenovaveis");
	     ArrayList<String> lista = new ArrayList<String>();
	     ListView listaLivros = (ListView) findViewById(R.id.listViewResultados);
	     
	     if(emprestimos==null){
	    	
	    	 lista.add("Você não possui empréstimos ativos renováveis");
	    	 ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, lista);
	    	 listaLivros.setAdapter(adapter);
	    	 Button renovar = (Button) findViewById(R.id.renovarEmprestimo);
	    	 renovar.setEnabled(false);
	    	 
	     }else{
		    	 for(Emprestimo e : emprestimos){
			    	 lista.add(e.toString());
			    	 keysEmprestimos.put(e.toString(), e.getCodigoLivro());
			    	 renovar.put(e.getCodigoLivro(),false); 
			     	}
		    	 
		    	 lista_para_adapter = new ArrayList<EmprestimoAdapterUtils>();
			       
			       for(String emprestimo : lista){
			    	   lista_para_adapter.add(new EmprestimoAdapterUtils(emprestimo));
			       	}
			       
			       ArrayAdapter<EmprestimoAdapterUtils> adapter = new EmprestimoAdapter(this,lista_para_adapter);			       	 
			       listaLivros.setAdapter(adapter);

	     	}//end else
	     
	     
	     
		
		
		       	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.renovacao, menu);
		return true;
	}
	
	
	/*
	 * Renovacao dos emprestimos
	 */
	
	public void renovar(View e){
		final ProgressDialog pd = new ProgressDialog(RenovacaoActivity.this);
		pd.setMessage("Processando...");
		pd.setTitle("Aguarde");
		pd.setIndeterminate(false);
	
	
		
		new AsyncTask<Void,Void,Void>(){

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd.show();
			}
			
			
			@Override
			protected Void doInBackground(Void... arg0) {
					Operations json = new OperationsFactory().getOperation(OperationsFactory.REMOTA);
					String usuario = Usuario.INSTANCE.getLogin();
					String senha = Usuario.INSTANCE.getSenha();
					
					Set<String> id = renovar.keySet();
					for (String chave : id)  
			        {  
			            if(chave != null){
			            	if(renovar.get(chave)){ //tem que renovar
			            	   resposta = json.renovarEmprestimo(usuario,senha,chave);
			            	   Log.d("MARCILIO_DEBUG", "ariaria: "+resposta);
			            	}
			            }
			                  
			        }  
					
					//Log.d("MARCILIO_DEBUG", ""+bibliotecas);
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void v) {
				// TODO Auto-generated method stub
				super.onPostExecute(v);
				if(pd!= null && pd.isShowing())
					pd.dismiss();
			}
			
			}.execute();
			
			
			Toast.makeText(getApplicationContext(), "Emprestimo Renovado com Sucesso", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(RenovacaoActivity.this, MenuActivity.class );
			startActivity(intent);
	}
	
	
	
	
	
	
	
	
	private class EmprestimoAdapter extends ArrayAdapter<EmprestimoAdapterUtils> {

		  private  List<EmprestimoAdapterUtils> lista_emprestimos;

		  public EmprestimoAdapter(Context context, List<EmprestimoAdapterUtils> lista) {
		    super(context, R.layout.emprestimos_layout, lista);
		    
		    this.lista_emprestimos = lista;
		  }

		  private class ViewHolder {
		    protected TextView text;
		    protected CheckBox checkbox;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    View view = null;
		    if (convertView == null) {
		    	  LayoutInflater vi = (LayoutInflater)getSystemService(
		    			     Context.LAYOUT_INFLATER_SERVICE);
		      view = vi.inflate(R.layout.emprestimos_layout, null);
		      final ViewHolder viewHolder = new ViewHolder();
		      viewHolder.text = (TextView) view.findViewById(R.id.label);
		      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
		      viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

		            @Override
		            public void onCheckedChanged(CompoundButton buttonView,
		                boolean isChecked) {
		              EmprestimoAdapterUtils element = (EmprestimoAdapterUtils) viewHolder.checkbox.getTag();
		              element.setSelected(buttonView.isChecked());
		             //Aqui
		             String idEmprestimo =keysEmprestimos.get(element.getDados());
		 	    	 renovar.put(idEmprestimo,buttonView.isChecked());
		             //Toast.makeText(getApplicationContext(), ""+renovar.get(idEmprestimo), Toast.LENGTH_SHORT).show();

		            }
		          });
		      view.setTag(viewHolder);
		      viewHolder.checkbox.setTag(lista_emprestimos.get(position));
		    } else {
		      view = convertView;
		      ((ViewHolder) view.getTag()).checkbox.setTag(lista_emprestimos.get(position));
		    }
		    ViewHolder holder = (ViewHolder) view.getTag();
		    holder.text.setText(lista_emprestimos.get(position).getDados());
		    holder.checkbox.setChecked(lista_emprestimos.get(position).isSelected());
		    return view;
		  }
		} 

}
