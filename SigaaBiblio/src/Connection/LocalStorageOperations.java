package Connection;

import java.util.ArrayList;

import org.json.JSONObject;
import org.w3c.dom.ls.LSInput;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.nti.SigaaBiblio.model.Artigo;
import br.nti.SigaaBiblio.model.Biblioteca;
import br.nti.SigaaBiblio.model.Emprestimo;
import br.nti.SigaaBiblio.model.Livro;
import br.nti.SigaaBiblio.persistence.RepositorioLocalSigaa;

public class LocalStorageOperations implements Operations {

	private Context context;
	
	public LocalStorageOperations(Context context){
		this.context=context;
	}
	
	@Override
	public ArrayList<Biblioteca> listarBibliotecas() {
		// TODO Auto-generated method stub
		
		SQLiteDatabase sqLite = new  RepositorioLocalSigaa(context).getReadableDatabase();
		String tabela="biblioteca";
		String[] colunas = {"id","nome"};
		
		Cursor resultados = sqLite.query(tabela,colunas,null,null,null,null,null);
		ArrayList<Biblioteca> lista = new ArrayList<Biblioteca>();
		if (resultados.getCount() > 0){
		    resultados.moveToFirst();
		
		do{
			
			lista.add(new Biblioteca(resultados.getString(resultados.getColumnIndex("id")),
					resultados.getString(resultados.getColumnIndex("nome"))));
		}while(resultados.moveToNext());
	}
		resultados.close();
		sqLite.close();
		return lista;
	}

	@Override
	public ArrayList<Livro> consultarAcervoLivro(String... parametrosConsulta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Artigo> consultarAcervoArtigo(String... parametrosConsulta) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase sqLite = new  RepositorioLocalSigaa(context).getReadableDatabase();
		String tabela="artigo";
		String[] colunas = {"autor","titulo","palavrasChave"};
		String where="";
		where+= parametrosConsulta[0].equals("")?"":"autor LIKE ?%";
		where+=parametrosConsulta[1].equals("")?"":"titulo = ?";
		where+=parametrosConsulta[2].equals("")?"": "palavrasChave = ?";
		
		return null;
	}

	@Override
	public Livro informacoesLivro(String... pararametrosLivro) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public Artigo informacoesExemplarArtigo(String... pararametrosArtigo) {
		// TODO Auto-generated method stub
		SQLiteDatabase sqLite = new  RepositorioLocalSigaa(context).getReadableDatabase();
		String tabela="artigo";
		String[] colunas = {"id" , "autor", "titulo", "palavrasChave", "autoresSecundarios", "paginas", "localPublicacao", "editora", "ano", "resumo", "biblioteca", "codigoDeBarras",
				"localizacao", "situacao", "volume", "numero", "edicao", "anoCronologico", "diaMes"};
		String where = "id = ?";
		String args[] = {""+pararametrosArtigo[0]};
		Cursor resultados = sqLite.query(tabela,colunas,where,args,null,null,null);
		Artigo artigo = null;
		String biblioteca="", codigoDeBarras="", localizacao="",situacao="",
				anoCronologico="",ano="",diaMes="",volume="",numero="",autorSecundario="",
				intervaloPaginas="",localPublicacao="", editora="",anoExemplar="",resumo="";
		
		if (resultados.getCount() > 0){
			resultados.moveToFirst();
			//localPublicacao, editora, ano, resumo, biblioteca, codigoDeBarras, localizacao, situacao, volume, numero, edicao, anoCronologico, diaMes
			biblioteca= resultados.getString(resultados.getColumnIndex("biblioteca"));
			codigoDeBarras= resultados.getString(resultados.getColumnIndex("codigoDeBarras"));
			localizacao= resultados.getString(resultados.getColumnIndex("localizacao"));
			situacao= resultados.getString(resultados.getColumnIndex("situacao"));
			anoCronologico= resultados.getString(resultados.getColumnIndex("anoCronologico"));
			ano= resultados.getString(resultados.getColumnIndex("ano"));
			diaMes= resultados.getString(resultados.getColumnIndex("diaMes"));
			volume= resultados.getString(resultados.getColumnIndex("volume"));
			numero= resultados.getString(resultados.getColumnIndex("biblioteca"));
			autorSecundario= resultados.getString(resultados.getColumnIndex("autoresSecundarios"));
			intervaloPaginas= resultados.getString(resultados.getColumnIndex("paginas"));
			localPublicacao= resultados.getString(resultados.getColumnIndex("localPublicacao"));
			editora= resultados.getString(resultados.getColumnIndex("editora"));
			resumo= resultados.getString(resultados.getColumnIndex("resumo"));
			
			artigo = new Artigo(autorSecundario,intervaloPaginas,localPublicacao,editora,ano,
					  resumo,biblioteca,codigoDeBarras,localizacao,situacao,volume,numero,
					  anoCronologico,diaMes);
			
		}
		
		
		return artigo;

	}

	@Override
	public ArrayList<Emprestimo> consultarSituacao(String... parametrosUsuario) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase sqLite = new  RepositorioLocalSigaa(context).getReadableDatabase();
		
		String tabela = "usuario";
		String[] colunas = {};
		String[] userAutenticacao = {parametrosUsuario[0],parametrosUsuario[1]};
		//Cursor resultados = sqLite.query("usuario","idUsuarioBiblioteca","login = ? AND senha = ?",userAutenticacao,null,null,null);
//		
//		
//		String tabela="emprestimo";
//		String[] colunas = {"codigoBarras" , "autor", "titulo", "ano", "dataEmprestimo", "dataRenovacao", "prazoDevolucao", "biblioteca", "renovavel"};
//		
//		
//		
//		String codigoBarras="",autor="",titulo="",ano="",dataEmprestimo="",dataRenovacao="",devolucao="",biblioteca="";
//		Boolean renovavel=false;
//		
//		emp = new Emprestimo(content.getString("CodigoDeBarras"),content.getString("Autor"),
//		content.getString("Titulo"),content.getString("Ano"),content.getString("DataEmprestimo"), 
//		content.getString("DataRenovacao"), content.getString("Devolucao"), "", content.getString("Biblioteca"),
//		content.getBoolean("Renovavel"));
//		emprestimos.add(emp);
//
//		map.put("Login", parametrosUsuario[0]);
//		map.put("Senha", parametrosUsuario[1]);
//		ArrayList<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
		
		return null;
	}

	@Override
	public ArrayList<Emprestimo> consultarEmprestimosRenovaveis(
			String... parametrosUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Emprestimo> historicoEmprestimos(
			String... parametrosUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String renovarEmprestimo(String... parametrosEmprestimos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String realizarLogin(String... parametrosDoUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

}
