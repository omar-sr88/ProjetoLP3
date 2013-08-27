package Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import br.nti.SigaaBiblio.activities.BuscaLivroActivity;
import br.nti.SigaaBiblio.activities.ResultadoBuscaActivity;
import br.nti.SigaaBiblio.model.Artigo;
import br.nti.SigaaBiblio.model.Biblioteca;
import br.nti.SigaaBiblio.model.Livro;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class JSONOperations implements Operations {

	@Override
	public ArrayList<Biblioteca> listarBibliotecas() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		String jsonString;
		map.put("Operacao", String.valueOf(Operations.LISTAR_BIBLIOTECAS));
		JSONObject inputsJson = new JSONObject(map);
		JSONObject resposta;
		String bibliotecas="";
		ArrayList<Biblioteca> bibliotecasLista = new ArrayList<Biblioteca>();
		try {
			jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
			resposta = new JSONObject(jsonString);
			bibliotecas = resposta.getString("Bibliotecas");
			JSONObject bibliotecasJson = new JSONObject(bibliotecas);
			bibliotecasLista.add(new Biblioteca("0", "Buscar em Todas as Unidades")); //desativa o filtro da biblioteca lá no servidor
			Iterator<String> keys = bibliotecasJson.keys(); //descobre as chaves que são os ids das bibliotecas
			while(keys.hasNext()){
				String key=keys.next();
				bibliotecasLista.add(new Biblioteca(key, bibliotecasJson.getString(key)));
				//Log.d("MARCILIO_DEBUG", "isso é uma key "+key);
			}
			Log.d("MARCILIO_DEBUG", bibliotecas);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return bibliotecasLista;
	}
	


	@Override
	public ArrayList consultarAcervoLivro(String... parametrosConsulta) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		String jsonString;
		map.put("Operacao", String.valueOf(Operations.CONSULTAR_ACERVO_LIVRO));
		map.put("IdBiblioteca",parametrosConsulta[0]);
		map.put("TituloBusca", parametrosConsulta[1]);
		map.put("AutorBusca", parametrosConsulta[2]);
		map.put("AssuntoBusca", parametrosConsulta[3]);
		JSONObject inputsJson = new JSONObject(map);
		JSONObject resposta;
		String livros="";
		ArrayList<Livro> listaLivros = new ArrayList<Livro>();
		
		try {
			jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
			resposta = new JSONObject(jsonString);
			livros =resposta.getString("Livros");
			JSONObject livrosJson = new JSONObject(livros);
		
			
			String autor="", titulo="", edicao="", ano="", quantidade="", registroSistema="";
			Iterator<String> keys = livrosJson.keys(); //descobre as chaves que são os ids das bibliotecas
			if(keys.hasNext())
				while(keys.hasNext()){
					String key=keys.next();
					JSONObject livroJSON = livrosJson.getJSONObject(key);
					autor = JSONObject.NULL.equals(livroJSON.get("Autor"))?"":livroJSON.getString("Autor");
					titulo = JSONObject.NULL.equals(livroJSON.get("Titulo"))?"":livroJSON.getString("Titulo");
					edicao = JSONObject.NULL.equals(livroJSON.get("Edicao"))?"":livroJSON.getString("Edicao");
					ano = JSONObject.NULL.equals(livroJSON.get("Ano"))?"":livroJSON.getString("Ano");
					quantidade = JSONObject.NULL.equals(livroJSON.get("QuantidadeAtivos"))?"":livroJSON.getString("QuantidadeAtivos");
					registroSistema = key;
					
					Livro livro = new Livro(autor,titulo,edicao,ano,quantidade,registroSistema);
					listaLivros.add(livro);
					//Log.d("MARCILIO_DEBUG", "isso é uma key "+key);
				}
				else{
					return null;
				}
				//Log.d("MARCILIO_DEBUG",livros);//ou Artigos
		} catch (Exception ex){ ex.printStackTrace();}
		
		
		return listaLivros;
	}

	//
	@Override
	public ArrayList consultarAcervoArtigo(String... parametrosConsulta) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		String jsonString;
		map.put("Operacao", String.valueOf(Operations.CONSULTAR_ACERVO_LIVRO));
		map.put("IdBiblioteca",parametrosConsulta[0]);
		map.put("TituloBusca", parametrosConsulta[1]);
		map.put("AutorBusca", parametrosConsulta[2]);
		map.put("AssuntoBusca", parametrosConsulta[3]);
		JSONObject inputsJson = new JSONObject(map);
		JSONObject resposta;
		String artigosRaw="";
		ArrayList<Artigo> listaArtigos = new ArrayList<Artigo>();
		
		String autor="", titulo="", palavrasChave="";
		

		try {
			jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
			resposta = new JSONObject(jsonString);
			artigosRaw =resposta.getString("Artigos");
			JSONObject artigosJSON = new JSONObject(artigosRaw);
			Iterator<String> keys = artigosJSON.keys();
			if(keys.hasNext())
				while(keys.hasNext()){
						String key=keys.next();
					
						JSONObject artigoJSON = artigosJSON.getJSONObject(key);
						autor = JSONObject.NULL.equals(artigoJSON.get("Autor"))?"-":artigoJSON.getString("Autor");
						titulo = JSONObject.NULL.equals(artigoJSON.get("Titulo"))?"-":artigoJSON.getString("Titulo");
						palavrasChave = JSONObject.NULL.equals(artigoJSON.get("Assunto"))?"-":artigoJSON.getString("Assunto");
						String[] pChaves = palavrasChave.split("\\#\\$\\&");
						String palavraChaveFinal="";
						for(String c : pChaves){
							palavraChaveFinal+=c+"; ";
						}
						Artigo artigo = new Artigo(autor,titulo,palavraChaveFinal);
						listaArtigos.add(artigo);
				}
				else
					listaArtigos=null;
			//Log.d("MARCILIO_DEBUG",livros);//ou Artigos
			}catch (Exception ex){ ex.printStackTrace();}
			
		return listaArtigos;
	}



	@Override
	public Livro informacoesLivro(String... pararametrosLivro) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		String jsonString;
		map.put("Operacao", String.valueOf(Operations.INFORMACOES_EXEMPLAR_ACERVO));
		map.put("IdDetalhes", pararametrosLivro[0]);					
		JSONObject inputsJson = new JSONObject(map);
		Livro livro=null;
		String registro="", numero="", titulo="",subtitulo="",assunto="",autor="",autorSecundario="",publicacao="",editora="",ano="",notas="";
		
		try {
			jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
			JSONObject resposta = new JSONObject(jsonString);
			Iterator<String> keys = resposta.keys();
			if(keys.hasNext()){
				
			}
				
			
			Log.d("MARCILIO_DEBUG", resposta.toString());
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	
}//end class