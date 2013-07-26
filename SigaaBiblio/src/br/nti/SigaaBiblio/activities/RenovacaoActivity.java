package br.nti.SigaaBiblio.activities;

import java.util.ArrayList;
import java.util.List;

import br.nti.SigaaBiblio.model.Emprestimo;

import com.nti.SigaaBiblio.R;
import com.nti.SigaaBiblio.R.layout;
import com.nti.SigaaBiblio.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

public class RenovacaoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_renovacao);
		
		ListView listaLivros = (ListView) findViewById(R.id.listViewResultados);
		
		/*
		 * A variavel livros_emprestados se tornará uma lista quando
		 * a parte de conexão com o servidor for implementada 
		 */
		
		String[] livros_emprestados = new String[] { "Código do livro\nAutor\nTítulo\n" +
				"Ano\n"+"Data de empréstimo, data de devolução",
				"Código do livro\nAutor\nTítulo\n" +
						"Ano\n"+"Data de empréstimo, data de devolução","Código do livro\nAutor\nTítulo\n" +
								"Ano\n"+"Data de empréstimo, data de devolução","Código do livro\nAutor\nTítulo\n" +
										"Ano\n"+"Data de empréstimo, data de devolução","Código do livro\nAutor\nTítulo\n" +
												"Ano\n"+"Data de empréstimo, data de devolução",};
		
		       ArrayList<Emprestimo> lista_para_adapter = new ArrayList<Emprestimo>();
		       
		       for (int i = 0; i < livros_emprestados.length; ++i) {
		    	   lista_para_adapter.add(new Emprestimo(livros_emprestados[i]));
		       }
		       ArrayAdapter<Emprestimo> adapter = new EmprestimoAdapter(this,lista_para_adapter);
		       //ArrayAdapter<String> listViewAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, lista_para_adapter);	 
		       listaLivros.setAdapter(adapter);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.renovacao, menu);
		return true;
	}
	
	private class EmprestimoAdapter extends ArrayAdapter<Emprestimo> {

		  private  List<Emprestimo> lista_emprestimos;

		  public EmprestimoAdapter(Context context, List<Emprestimo> lista) {
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
		              Emprestimo element = (Emprestimo) viewHolder.checkbox.getTag();
		              element.setSelected(buttonView.isChecked());

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
