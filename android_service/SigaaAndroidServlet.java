package br.ufrn.sigaa.biblioteca.android_service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.json.JSONObject;

@SuppressWarnings("serial")
public class SigaaAndroidServlet extends HttpServlet {

	/**
	 * Recebe Requisição
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String inputString = request.getParameter("sigaaAndroid");
		String error = "";
		Map<String, String> map = new HashMap<String, String>();

		try {
			JSONObject inputValues = new JSONObject(inputString);			
			int operation = inputValues.getInt("Operacao");
			String login;
			String senha;
			switch(operation){			
				case Operations.LOGIN:
					login = inputValues.getString("Login");
					senha = inputValues.getString("Senha");
					GeneralOperationAndroid.validaLogin(login, senha, map, request);
					break;
				case Operations.LISTAR_BIBLIOTECAS:
					GeneralOperationAndroid.listaBibliotecas(map);
					break;
				case Operations.CONSULTAR_ACERVO_ARTIGO:
				case Operations.CONSULTAR_ACERVO_LIVRO:
					int idBiblioteca = inputValues.getInt("IdBiblioteca");
					String tituloBusca = inputValues.getString("TituloBusca");
					String autorBusca = inputValues.getString("AutorBusca");
					String assuntoBusca = inputValues.getString("AssuntoBusca");
					GeneralOperationAndroid.pesquisarAcervo(map, idBiblioteca, tituloBusca, autorBusca, assuntoBusca);
					break;
				
				case Operations.MINHA_SITUACAO:
					login = inputValues.getString("Login");
					senha = inputValues.getString("Senha");
					GeneralOperationAndroid.minhaSituacao(login, senha,map);
					break;
					
				case Operations.MEUS_EMPRESTIMOS:
					login = inputValues.getString("Login");
					senha = inputValues.getString("Senha");
					java.util.Date Inicio = inputValues.getString("Inicio").isEmpty() ? null : (java.util.Date)inputValues.get("Inicio"); 
					java.util.Date Fim = inputValues.getString("Fim").isEmpty() ? null : (java.util.Date)inputValues.get("Fim");
					GeneralOperationAndroid.historicoEmprestimos(login, senha, Inicio,Fim, map);
					break;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			error = "\nException";
		}
		// map.put....

		map.put("Error", map.get("Error") + error);
		map.put("Mensagem", map.get("Mensagem")==null?"":map.get("Mensagem"));
		PrintWriter out = response.getWriter();
		JSONObject mapJSON = new JSONObject(map);
		out.println(mapJSON);
	}

}
