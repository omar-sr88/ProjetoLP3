package br.ufrn.sigaa.biblioteca.android_service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.json.JSONObject;

import br.ufrn.arq.negocio.AbstractProcessador;
import br.ufrn.arq.usuarios.UserAutenticacao;
import br.ufrn.comum.dominio.UsuarioGeral;
import br.ufrn.sigaa.arq.dao.UsuarioDao;
import br.ufrn.sigaa.arq.dao.DiscenteDao;
import br.ufrn.sigaa.arq.dao.biblioteca.UsuarioBibliotecaDao;
import br.ufrn.sigaa.biblioteca.circulacao.dominio.UsuarioBiblioteca;
import br.ufrn.sigaa.biblioteca.util.UsuarioBibliotecaUtil;
import br.ufrn.sigaa.dominio.Usuario;
import br.ufrn.sigaa.ensino.dominio.DiscenteAdapter;
import br.ufrn.sigaa.pessoa.dominio.Discente;

@SuppressWarnings("serial")
public class SigaaAndroidServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String inputString = "Default";
		inputString = request.getParameter("sigaaLogin");    
		Map <String,String> map = new HashMap<String,String>();
		String login = "";
		String senha = "";
		String error = "";
		String msg = "";
		Usuario user = null;

		try {
			JSONObject inputValues = new JSONObject(inputString);
			login = inputValues.getString("Login");
			senha = inputValues.getString("Senha");


			//Resposta


			//			UsuarioDAO dao = DAOFactory.getInstance().getDAO(UsuarioDAO.class);
			UsuarioDao dao = AbstractProcessador.getDAO(UsuarioDao.class, null);

			UsuarioGeral userGeral = dao.findByLogin(login);

			if(userGeral == null){
				error = "Usuário não existe";
			}
			else if(!UserAutenticacao.autenticaUsuario(request, login.toLowerCase(), senha, true)){
				error = "Senha Inválida";

			}else{
				msg = "Usuario Existe!";
				UsuarioBibliotecaDao daoBiblio = AbstractProcessador.getDAO(UsuarioBibliotecaDao.class, null);
				UsuarioBiblioteca usuarioBiblioteca;

				/**
				 * Carrega informações do Usuário
				 */
				user = dao.findByPrimaryKey(userGeral.getId());
				List<UsuarioBiblioteca> contasUsuarioBiblioteca = daoBiblio.findUsuarioBibliotecaAtivoByPessoa( user.getPessoa().getId());
				usuarioBiblioteca = UsuarioBibliotecaUtil.recuperaUsuarioNaoQuitadosAtivos(contasUsuarioBiblioteca);

				if(usuarioBiblioteca == null){
					error = "O usuário não possui Vinculos com a biblioteca";
				}else{
					map.put("Nome", user.getPessoa().getNome());
					map.put("Vinculo", usuarioBiblioteca.getVinculo().getDescricao());

					if(usuarioBiblioteca.getVinculo().isVinculoAluno()){
						//CAPTURAR INFORMAÇÕES DISCENTE
						//Identificação vinculo
						DiscenteDao discenteDao = AbstractProcessador.getDAO(DiscenteDao.class, null);
						DiscenteAdapter discente = discenteDao.findByPK(usuarioBiblioteca.getIdentificacaoVinculo());

						map.put("Matricula", String.valueOf(discente.getMatricula()));
						map.put("Curso", discente.getCurso().getDescricao());

					}else{
						//INFORMAÇÕES DO SERVIDOR
					}
				}

			}


		} catch (Exception e) {  // NullPointerException and JSONException
			e.printStackTrace();
			error = "Exception";
		}

		//map.put....
		map.put("Error",error);
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
