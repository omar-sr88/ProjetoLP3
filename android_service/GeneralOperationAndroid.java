package br.ufrn.sigaa.biblioteca.android_service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.arq.negocio.AbstractProcessador;
import br.ufrn.arq.usuarios.UserAutenticacao;
import br.ufrn.comum.dominio.UsuarioGeral;
import br.ufrn.sigaa.arq.dao.DiscenteDao;
import br.ufrn.sigaa.arq.dao.UsuarioDao;
import br.ufrn.sigaa.arq.dao.biblioteca.UsuarioBibliotecaDao;
import br.ufrn.sigaa.biblioteca.circulacao.dominio.UsuarioBiblioteca;
import br.ufrn.sigaa.biblioteca.util.UsuarioBibliotecaUtil;
import br.ufrn.sigaa.dominio.Usuario;
import br.ufrn.sigaa.ensino.dominio.DiscenteAdapter;

public class GeneralOperationAndroid {


	public static void validaLogin(String login, String senha,
			Map<String, String> map, HttpServletRequest request) throws ArqException, NegocioException {

		UsuarioDao usuarioDao = null;
		DiscenteDao discenteDao = null;
		String error = "";

		Usuario user = null;
		usuarioDao = AbstractProcessador.getDAO(UsuarioDao.class, null); //Inicia Conex�o
		UsuarioGeral userGeral = usuarioDao.findByLogin(login);

		if(userGeral == null){
			error = "Usu�rio n�o existe";
		}
		else if(!UserAutenticacao.autenticaUsuario(request, login.toLowerCase(), senha, true)){
			error = "Senha Inv�lida";
		}else{

			user = usuarioDao.findByPrimaryKey(userGeral.getId());
			usuarioDao.close(); // Encerra conex�o

			UsuarioBibliotecaDao usuarioBiblioDao = 
					AbstractProcessador.getDAO(UsuarioBibliotecaDao.class, null); //Inicia Conex�o
			UsuarioBiblioteca usuarioBiblioteca;

			/**
			 * Carrega informa��es do Usuario
			 */


			List<UsuarioBiblioteca> contasUsuarioBiblioteca = 
					usuarioBiblioDao.findUsuarioBibliotecaAtivoByPessoa( user.getPessoa().getId());
			usuarioBiblioteca = UsuarioBibliotecaUtil.recuperaUsuarioNaoQuitadosAtivos(contasUsuarioBiblioteca);
			usuarioBiblioDao.close(); //Encerra Conex�o

			if(usuarioBiblioteca == null){
				error = "O usu�rio n�o possui Vinculos com a biblioteca";
			}else{

				//CAPTURAR INFORMA��ES DO USUARIO

				map.put("Nome", user.getNome());
				map.put("Vinculo", usuarioBiblioteca.getVinculo().getDescricao());

				//Identifica��o vinculo
				if(usuarioBiblioteca.getVinculo().isVinculoAluno()){
					//INFORMA��ES DE DISCENTE

					discenteDao = AbstractProcessador.getDAO(DiscenteDao.class, null); //Inicia Conex�o
					DiscenteAdapter discente = discenteDao.findByPK(usuarioBiblioteca.getIdentificacaoVinculo());

					map.put("Matricula", String.valueOf(discente.getMatricula()));
					map.put("Curso", discente.getCurso().getDescricao());

					discenteDao.close();//Encerra Conex�o
				}else{
					//INFORMA��ES DO SERVIDOR
				}

			}
		}
		map.put("Error", error);
	}
}
