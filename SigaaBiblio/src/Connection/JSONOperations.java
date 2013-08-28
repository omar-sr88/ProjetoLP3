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
import br.nti.SigaaBiblio.model.Emprestimo;
import br.nti.SigaaBiblio.model.Livro;
import br.nti.SigaaBiblio.model.Usuario;

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
					 //= key;
					registroSistema= JSONObject.NULL.equals(livroJSON.get("IdDetalhes"))?"-":livroJSON.getString("IdDetalhes");

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
		
		String autor="", titulo="", palavrasChave="",id="";
		

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
						id= JSONObject.NULL.equals(artigoJSON.get("IdDetalhes"))?"-":artigoJSON.getString("IdDetalhes");
						String[] pChaves = palavrasChave.split("\\#\\$\\&");
						String palavraChaveFinal="";
						for(String c : pChaves){
							palavraChaveFinal+=c+"; ";
						}
						Artigo artigo = new Artigo(autor,titulo,palavraChaveFinal,id);
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
			
				
			
			Log.d("MARCILIO_DEBUG", resposta.toString());
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}



	@Override
	public Artigo informacoesExemplarArtigo(String... pararametrosArtigo) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		String jsonString;
		map.put("Operacao", String.valueOf(Operations.INFORMACOES_EXEMPLAR_ARTIGO));
		map.put("IdDetalhes", pararametrosArtigo[0]);					
		JSONObject inputsJson = new JSONObject(map);
		Artigo artigo = null;
		String biblioteca="", codigoDeBarras="", localizacao="",situacao="",
				anoCronologico="",ano="",diaMes="",volume="",numero="",autorSecundario="",
				intervaloPaginas="",localPublicacao="", editora="",anoExemplar="",resumo="";
				
		
		try {
			jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
			JSONObject resposta = new JSONObject(jsonString);					
			
			biblioteca= JSONObject.NULL.equals(resposta.get("Biblioteca"))?"-":resposta.getString("Biblioteca");
			codigoDeBarras= JSONObject.NULL.equals(resposta.get("CodigoBarras"))?"-":resposta.getString("CodigoBarras");
			localizacao= JSONObject.NULL.equals(resposta.get("Localizacao"))?"-":resposta.getString("Localizacao");
			situacao= JSONObject.NULL.equals(resposta.get("Situacao"))?"-":resposta.getString("Situacao");
			anoCronologico= JSONObject.NULL.equals(resposta.get("AnoCronologico"))?"-":resposta.getString("AnoCronologico");
			ano= JSONObject.NULL.equals(resposta.get("Ano"))?"-":resposta.getString("Ano");
			diaMes= JSONObject.NULL.equals(resposta.get("DiaMes"))?"-":resposta.getString("DiaMes");
			volume= JSONObject.NULL.equals(resposta.get("Volume"))?"-":resposta.getString("Volume");
			numero= JSONObject.NULL.equals(resposta.get("Numero"))?"-":resposta.getString("Numero");
			autorSecundario= JSONObject.NULL.equals(resposta.get("AutorSecundario"))?"-":resposta.getString("AutorSecundario");
			intervaloPaginas= JSONObject.NULL.equals(resposta.get("IntervaloPaginas"))?"-":resposta.getString("IntervaloPaginas");
			localPublicacao= JSONObject.NULL.equals(resposta.get("LocalPublicacao"))?"-":resposta.getString("LocalPublicacao");
			editora= JSONObject.NULL.equals(resposta.get("Editora"))?"-":resposta.getString("Editora");
			anoExemplar= JSONObject.NULL.equals(resposta.get("AnoExemplar"))?"-":resposta.getString("AnoExemplar");
			resumo= JSONObject.NULL.equals(resposta.get("Resumo"))?"-":resposta.getString("Resumo");

			artigo = new Artigo(autorSecundario,intervaloPaginas,localPublicacao,editora,ano,
					  resumo,biblioteca,codigoDeBarras,localizacao,situacao,volume,numero,
					  anoCronologico,diaMes);
			Log.d("MARCILIO_DEBUG", resposta.toString());
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return artigo;
	}



	@Override
	public ArrayList<Emprestimo> consultarSituacao(String... parametrosUsuario) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		String jsonString;
		map.put("Operacao",
				String.valueOf(Operations.MINHA_SITUACAO));
		map.put("Login", parametrosUsuario[0]);
		map.put("Senha", parametrosUsuario[1]);
		ArrayList<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
		JSONObject inputsJson = new JSONObject(map);
		JSONObject resposta;
		
		try {
			jsonString = HttpUtils.urlContentPost(ConnectJSON.HOST, "sigaaAndroid", inputsJson.toString());
			resposta = new JSONObject(jsonString);
			parametrosUsuario[2] = resposta.getString("Mensagem"); //informa a mensagem
			resposta = new JSONObject(resposta.getString("Emprestimos"));
			//intent.putExtra("Mensagem", mensagem);
			String key = "" ;
			JSONObject content;
			
			Emprestimo emp;
			for(Iterator obj = resposta.keys(); obj.hasNext();){
				key = (String)obj.next();
				content = new JSONObject(resposta.getString(key));
				Log.d("IRON_DEBUG", content.toString());	
				emp = new Emprestimo(content.getString("CodigoDeBarras"),content.getString("Autor"),
						content.getString("Titulo"),content.getString("Ano"),content.getString("DataEmprestimo"), 
						content.getString("DataRenovacao"), content.getString("Devolucao"), "", content.getString("Biblioteca"),
						content.getBoolean("Renovavel"));
				emprestimos.add(emp);
				}
										
		
		
	} catch (Exception ex) {
		ex.printStackTrace();
	}


		
		return emprestimos;
	}
	
	
}//end class