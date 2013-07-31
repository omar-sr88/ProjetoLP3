package br.ufrn.sigaa.biblioteca.android_service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.arq.negocio.AbstractProcessador;
import br.ufrn.arq.usuarios.UserAutenticacao;
import br.ufrn.arq.util.UFRNUtils;
import br.ufrn.comum.dao.ServidorDAO;
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
		usuarioDao = AbstractProcessador.getDAO(UsuarioDao.class, null); //Inicia Conexão
		UsuarioGeral userGeral = usuarioDao.findByLogin(login);

		if(userGeral == null){
			error = "Usuário não existe";
		}
		else if(!UserAutenticacao.autenticaUsuario(request, login.toLowerCase(), senha, false)){ //False para verificar Dados Criptografados
			error = "Senha Inválida";
		}else{


			user = usuarioDao.findByPrimaryKey(userGeral.getId());
			usuarioDao.close(); // Encerra conexão

			UsuarioBibliotecaDao usuarioBiblioDao = 
					AbstractProcessador.getDAO(UsuarioBibliotecaDao.class, null); //Inicia Conexão
			UsuarioBiblioteca usuarioBiblioteca;

			/**
			 * Carrega informações do Usuario
			 */


			List<UsuarioBiblioteca> contasUsuarioBiblioteca = 
					usuarioBiblioDao.findUsuarioBibliotecaAtivoByPessoa( user.getPessoa().getId());
			usuarioBiblioteca = UsuarioBibliotecaUtil.recuperaUsuarioNaoQuitadosAtivos(contasUsuarioBiblioteca);
			usuarioBiblioDao.close(); //Encerra Conexão

			if(usuarioBiblioteca == null){
				error = "O usuário não possui Vinculos com a biblioteca";
			}else{

				//CAPTURAR INFORMAÇÕES DO USUARIO

				map.put("Nome", user.getNome());
				map.put("isAluno", String.valueOf(usuarioBiblioteca.getVinculo().isVinculoAluno()));
				map.put("IdUsuarioBiblioteca", String.valueOf(usuarioBiblioteca.getId()));
				map.put("Foto", userGeral.getIdFoto() == null? "/img/no_picture.png":
					"/verFoto?idArquivo="+userGeral.getIdFoto()+"&key="+UFRNUtils.generateArquivoKey(userGeral.getIdFoto()));


				//Identificação vinculo
				if(usuarioBiblioteca.getVinculo().isVinculoAluno()){
					//INFORMAÇÕES DE DISCENTE

					discenteDao = AbstractProcessador.getDAO(DiscenteDao.class, null); //Inicia Conexão
					DiscenteAdapter discente = discenteDao.findByPK(usuarioBiblioteca.getIdentificacaoVinculo());

					map.put("Matricula", String.valueOf(discente.getMatricula()));
					map.put("Curso", discente.getCurso().getDescricao());

					discenteDao.close();//Encerra Conexão
				}else{
					//INFORMAÇÕES DO SERVIDOR

					map.put("Unidade", user.getUnidade().getNome());

				}

			}
		}
		map.put("Error", error);
	}
}
