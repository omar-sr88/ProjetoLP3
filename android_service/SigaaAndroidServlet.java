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


		String inputString = request.getParameter("sigaaLogin");
		String error = "";
		String msg = "";
		Map <String,String> map = new HashMap<String,String>();

		try{
			JSONObject inputValues = new JSONObject(inputString);
			String login = inputValues.getString("Login");
			String senha = inputValues.getString("Senha");

			if(!login.isEmpty() && !senha.isEmpty()){
				GeneralOperationAndroid.validaLogin(login,senha,map,request);
			}		

		}catch(Exception ex){
			ex.printStackTrace();
			error = "\nException";
		}
		//map.put....

		map.put("Error",map.get("Error")+error);
		map.put("Mensagem", msg);
		PrintWriter out = response.getWriter();
		out.println(new JSONObject(map));
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
