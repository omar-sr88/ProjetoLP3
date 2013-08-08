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
				
				//Verifica se o usuário pode realizar empréstimos de acordo com o seu perfil
				Boolean podeRealizarEmprestimo = usuarioBiblioteca.getVinculo().isVinculoAluno()? 
						new ObtemVinculoUsuarioBibliotecaFactory().getEstrategiaVinculo().
						montaInformacoesVinculoDiscenteBiblioteca(user.getPessoa().getId()).get(0).isPodeFazerEmprestimos():
							new ObtemVinculoUsuarioBibliotecaFactory().getEstrategiaVinculo().
							montaInformacoesVinculoServidorBiblioteca(user.getPessoa().getId()).get(0).isPodeFazerEmprestimos();
						
				map.put("PodeRealizarEmprestimo", String.valueOf(podeRealizarEmprestimo));
				
				
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
	
	
	//Verificar se é necessários adicionar Request
	
	public static void consultaAcervoLivros(String titulo, String autor, String assunto){}
	public static void consultaAcervoArtigos(String titulo, String autor){}
	public static void renovacaoEmprestimo(String login, String senha, String senhaBiblioteca){} // Definir demais parametros para empréstimos: id_livro
	public static void minhaSituacao(String login, String senha){}
	public static void meusEmprestimos(){}
	
	
}
