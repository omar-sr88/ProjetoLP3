package br.nti.SigaaBiblio.persistence;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import br.nti.SigaaBiblio.model.LivroResumido;

public class Repositorio implements IRepositorio {

	DbManager  manager;
	public Repositorio(Context ctx){
		manager = new DbManager(ctx);
	}
	
	@Override
	public boolean salvarPesquisa(String texto) {
		SQLiteDatabase db =  manager.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("palavra", texto);				
		if(db.insertOrThrow("pesquisa", null, values) >1)
			return true;		
			
		return false;
	}

	@Override
	public boolean salvarLivros(List<LivroResumido> livros) {
		
		//teste com tamanho da lista local e do BD nti
		int tamLocal = tamanhoListaLivros();
		int tamRemoto = livros.size();
		
		int i = tamRemoto - tamLocal ;
		
		SQLiteDatabase db =  manager.getWritableDatabase();
		db.beginTransaction();
		for(;i<tamRemoto-1;i++){
		
			ContentValues values = new ContentValues();
			CharSequence data = DateFormat.format("dd/MM/yyyy", livros.get(i).getData());
			values.put("titulo", livros.get(i).getTitulo());
			values.put("data", data.toString());			
			db.insert("livros", null, values);					
		}
		
		db.setTransactionSuccessful();
		db.endTransaction();
		return true;
	}

	@Override
	public List<String> listarPalavras() {
		List<String> lista = new ArrayList<String>();
		SQLiteDatabase db =  manager.getReadableDatabase();
		Cursor c = db.rawQuery("select texto from pesquisa",null);
		
		if (c != null)
        {
		 if(c.moveToFirst())
         do{     	
        	 lista.add((c.getString(0)));      	 
            }while (c.moveToNext());
		 
         c.close();
         return lista;
        }
				
		return null;
	}

	@Override
	public List<LivroResumido> listarLivros() {
		LivroResumido livro;
		List<LivroResumido> lista = new ArrayList<LivroResumido>();
		SQLiteDatabase db =  manager.getReadableDatabase();
		Cursor c = db.rawQuery("select titulo, data from livros",null);
		
		if (c != null)
        {
		 if(c.moveToFirst())
         do
            {
        	 livro = new LivroResumido();
        	 livro.setTitulo((c.getString(0)));
        	 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        	 try{
        	 java.util.Date d =  sdf.parse(c.getString(1));
        	 livro.setData(new java.sql.Date(d.getYear(),d.getMonth()+1,d.getDay()));
        	 }catch (Exception e){
        		 System.out.println("Erro na conversao da data");
        	 }     	
        	     	
        	 lista.add(livro);
        	 
            }while (c.moveToNext());
		 
         c.close();
         return lista;
        }
				
		return null;
		
	}

	@Override
	public int tamanhoListaLivros() {
		SQLiteDatabase db =  manager.getReadableDatabase();
		Cursor c = db.rawQuery("select count(_id from livros)",null);
		
		if (c != null)
		 if(c.moveToFirst())
			 return c.getInt(0);
		
		return 0;
	}

}
