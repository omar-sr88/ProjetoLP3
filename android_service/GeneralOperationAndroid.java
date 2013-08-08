package br.ufrn.sigaa.biblioteca.android_service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.ufrn.arq.dao.GenericDAOImpl;
import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.arq.negocio.AbstractProcessador;
import br.ufrn.arq.usuarios.UserAutenticacao;
import br.ufrn.arq.util.UFRNUtils;
import br.ufrn.comum.dominio.UsuarioGeral;
import br.ufrn.sigaa.arq.dao.DiscenteDao;
import br.ufrn.sigaa.arq.dao.UsuarioDao;
import br.ufrn.sigaa.arq.dao.biblioteca.UsuarioBibliotecaDao;
import br.ufrn.sigaa.biblioteca.circulacao.dominio.UsuarioBiblioteca;
import br.ufrn.sigaa.biblioteca.circulacao.negocio.ObtemVinculoUsuarioBibliotecaFactory;
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
		else if(!UserAutenticacao.autenticaUsuario(request, login.toLowerCase(), senha, false)){ //False para verificar Dados Criptografados
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
				map.put("isAluno", String.valueOf(usuarioBiblioteca.getVinculo().isVinculoAluno()));
				map.put("IdUsuarioBiblioteca", String.valueOf(usuarioBiblioteca.getId()));
				map.put("Foto", userGeral.getIdFoto() == null? "/img/no_picture.png":
					"/verFoto?idArquivo="+userGeral.getIdFoto()+"&key="+UFRNUtils.generateArquivoKey(userGeral.getIdFoto()));
				
				//Calcula total de emprestimos em aberto
				int emprestimosAbertos = 0;
				try {
					ResultSet rs = AbstractProcessador.getDAO(UsuarioBibliotecaDao.class, null).getConnection().prepareStatement("SELECT COUNT(*) FROM biblioteca.emprestimo WHERE" +
							                            " situacao = 1 AND id_usuario_biblioteca = "+usuarioBiblioteca.getId()).executeQuery();
					while(rs.next()){
						emprestimosAbertos = rs.getInt(1);
					}
						
				} catch (SQLException e) {
					e.printStackTrace();
				}
				map.put("EmprestimosAbertos",String.valueOf(emprestimosAbertos));
				
				//Verifica se o usu�rio pode realizar empr�stimos de acordo com o seu perfil
				Boolean podeRealizarEmprestimo = usuarioBiblioteca.getVinculo().isVinculoAluno()? 
						new ObtemVinculoUsuarioBibliotecaFactory().getEstrategiaVinculo().
						montaInformacoesVinculoDiscenteBiblioteca(user.getPessoa().getId()).get(0).isPodeFazerEmprestimos():
							new ObtemVinculoUsuarioBibliotecaFactory().getEstrategiaVinculo().
							montaInformacoesVinculoServidorBiblioteca(user.getPessoa().getId()).get(0).isPodeFazerEmprestimos();
						
				map.put("PodeRealizarEmprestimo", String.valueOf(podeRealizarEmprestimo));
				
				
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

					map.put("Unidade", user.getUnidade().getNome());

				}

			}
		}
		map.put("Error", error);
	}
	
	
	//Verificar se � necess�rios adicionar Request
	
	public static void consultaAcervoLivros(String titulo, String autor, String assunto){}
	public static void consultaAcervoArtigos(String titulo, String autor){}
	public static void renovacaoEmprestimo(String login, String senha, String senhaBiblioteca){} // Definir demais parametros para empr�stimos: id_livro
	public static void minhaSituacao(String login, String senha){}
	public static void meusEmprestimos(){}
	
	
}
