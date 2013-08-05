package br.nti.SigaaBiblio.persistence;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper{
	
	private static final String DB_NAME = "carros.db";
	private static final int DB_VER = 1;
	

	
	public DbManager(Context ctx){
		super(ctx, DB_NAME,null, DB_VER);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE PESQUISA ("+_ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"palavra TEXT NOT NULL);" +
				"" +
				"" +
				"CREATE TABLE LIVROS ("+_ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"titulo TEXT NOT NULL," +
				"data DATE);"    );	
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db	, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS PESQUISA");
		
	}
	
}
