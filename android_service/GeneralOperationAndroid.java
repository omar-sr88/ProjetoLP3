package br.ufrn.sigaa.biblioteca.android_service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.primefaces.json.JSONObject;

import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.DAOException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.arq.mensagens.MensagensArquitetura;
import br.ufrn.arq.negocio.AbstractProcessador;
import br.ufrn.arq.usuarios.UserAutenticacao;
import br.ufrn.arq.util.StringUtils;
import br.ufrn.arq.util.UFRNUtils;
import br.ufrn.comum.dominio.UsuarioGeral;
import br.ufrn.sigaa.arq.dao.DiscenteDao;
import br.ufrn.sigaa.arq.dao.UsuarioDao;
import br.ufrn.sigaa.arq.dao.biblioteca.ArtigoDePeriodicoDao;
import br.ufrn.sigaa.arq.dao.biblioteca.BibliotecaDao;
import br.ufrn.sigaa.arq.dao.biblioteca.EmprestimoDao;
import br.ufrn.sigaa.arq.dao.biblioteca.ExemplarDao;
import br.ufrn.sigaa.arq.dao.biblioteca.MaterialInformacionalDao;
import br.ufrn.sigaa.arq.dao.biblioteca.TituloCatalograficoDao;
import br.ufrn.sigaa.arq.dao.biblioteca.UsuarioBibliotecaDao;
import br.ufrn.sigaa.biblioteca.circulacao.dominio.Emprestimo;
import br.ufrn.sigaa.biblioteca.circulacao.dominio.UsuarioBiblioteca;
import br.ufrn.sigaa.biblioteca.circulacao.negocio.ObtemVinculoUsuarioBibliotecaFactory;
import br.ufrn.sigaa.biblioteca.dominio.Biblioteca;
import br.ufrn.sigaa.biblioteca.dominio.SituacaoUsuarioBiblioteca;
import br.ufrn.sigaa.biblioteca.processos_tecnicos.dominio.CacheEntidadesMarc;
import br.ufrn.sigaa.biblioteca.processos_tecnicos.dominio.MaterialInformacional;
import br.ufrn.sigaa.biblioteca.processos_tecnicos.pesquisa.dominio.CampoOrdenacaoConsultaAcervo;
import br.ufrn.sigaa.biblioteca.processos_tecnicos.pesquisa.dominio.GeraPesquisaTextual;
import br.ufrn.sigaa.biblioteca.processos_tecnicos.pesquisa.negocio.GeraPesquisaTextualFactory;
import br.ufrn.sigaa.biblioteca.util.UsuarioBibliotecaUtil;
import br.ufrn.sigaa.biblioteca.util.VerificaSituacaoUsuarioBibliotecaUtil;
import br.ufrn.sigaa.dominio.Usuario;
import br.ufrn.sigaa.ensino.dominio.DiscenteAdapter;

public class GeneralOperationAndroid {

	public static void validaLogin(String login, String senha,
			Map<String, String> map, HttpServletRequest request)
			throws ArqException, NegocioException {

		UsuarioDao usuarioDao = null;
		DiscenteDao discenteDao = null;
		String error = "";

		Usuario user = null;
		
		UsuarioGeral userGeral = verificaLoginSenha( login, senha);
				
		if (userGeral == null) {
			error = "Usuário não existe";
		} else if (!UserAutenticacao.autenticaUsuario(request,
				login.toLowerCase(), senha, false)) { // False para verificar
														// Dados Criptografados
			error = "Senha Inválida";
		} else {
			
			usuarioDao = AbstractProcessador.getDAO(UsuarioDao.class, null);
			user = usuarioDao.findByPrimaryKey(userGeral.getId());
			usuarioDao.close(); // Encerra conexão

			UsuarioBibliotecaDao usuarioBiblioDao = AbstractProcessador.getDAO(
					UsuarioBibliotecaDao.class, null); // Inicia Conexão
			UsuarioBiblioteca usuarioBiblioteca;

			/**
			 * Carrega informações do Usuario
			 */

			List<UsuarioBiblioteca> contasUsuarioBiblioteca = usuarioBiblioDao
					.findUsuarioBibliotecaAtivoByPessoa(user.getPessoa()
							.getId());
			usuarioBiblioteca = UsuarioBibliotecaUtil
					.recuperaUsuarioNaoQuitadosAtivos(contasUsuarioBiblioteca);
			usuarioBiblioDao.close(); // Encerra Conexão

			if (usuarioBiblioteca == null) {
				error = "O usuário não possui Vinculos com a biblioteca";
			} else {

				// CAPTURAR INFORMAÇÕES DO USUARIO

				map.put("Nome", user.getNome());
				map.put("isAluno", String.valueOf(usuarioBiblioteca
						.getVinculo().isVinculoAluno()));
				map.put("IdUsuarioBiblioteca",
						String.valueOf(usuarioBiblioteca.getId()));
				map.put("Foto",
						userGeral.getIdFoto() == null ? "/img/no_picture.png"
								: "/verFoto?idArquivo="
										+ userGeral.getIdFoto()
										+ "&key="
										+ UFRNUtils
												.generateArquivoKey(userGeral
														.getIdFoto()));

				// Calcula total de emprestimos em aberto
				int emprestimosAbertos = 0;
				try {
					ResultSet rs = AbstractProcessador
							.getDAO(UsuarioBibliotecaDao.class, null)
							.getConnection()
							.prepareStatement(
									"SELECT COUNT(*) FROM biblioteca.emprestimo WHERE"
											+ " situacao = 1 AND id_usuario_biblioteca = "
											+ usuarioBiblioteca.getId())
							.executeQuery();
					while (rs.next()) {
						emprestimosAbertos = rs.getInt(1);
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				map.put("EmprestimosAbertos",
						String.valueOf(emprestimosAbertos));

				// Verifica se o usuário pode realizar empréstimos de acordo com
				// o seu perfil
				Boolean podeRealizarEmprestimo = usuarioBiblioteca.getVinculo()
						.isVinculoAluno() ? new ObtemVinculoUsuarioBibliotecaFactory()
						.getEstrategiaVinculo()
						.montaInformacoesVinculoDiscenteBiblioteca(
								user.getPessoa().getId()).get(0)
						.isPodeFazerEmprestimos()
						: new ObtemVinculoUsuarioBibliotecaFactory()
								.getEstrategiaVinculo()
								.montaInformacoesVinculoServidorBiblioteca(
										user.getPessoa().getId()).get(0)
								.isPodeFazerEmprestimos();

				map.put("PodeRealizarEmprestimo",
						String.valueOf(podeRealizarEmprestimo));

				// Identificação vinculo
				if (usuarioBiblioteca.getVinculo().isVinculoAluno()) {
					// INFORMAÇÕES DE DISCENTE

					discenteDao = AbstractProcessador.getDAO(DiscenteDao.class,
							null); // Inicia Conexão
					DiscenteAdapter discente = discenteDao
							.findByPK(usuarioBiblioteca
									.getIdentificacaoVinculo());

					map.put("Matricula",
							String.valueOf(discente.getMatricula()));
					map.put("Curso", discente.getCurso().getDescricao());

					discenteDao.close();// Encerra Conexão
				} else {
					// INFORMAÇÕES DO SERVIDOR

					map.put("Unidade", user.getUnidade().getNome());

				}

			}
		}
		map.put("Error", error);
	}

	private static UsuarioGeral verificaLoginSenha(String login, String senha) throws DAOException {
		UsuarioDao usuarioDao = AbstractProcessador.getDAO(UsuarioDao.class, null); // Inicia
		UsuarioGeral userGeral = usuarioDao.findByLogin(login);
		usuarioDao.close();
		return userGeral;
		
	}

	public static void listaBibliotecas(Map<String, String> map) {
		try {
			Map<String, String> bibliotecas = new HashMap<String, String>();
			Collection<Biblioteca> bibliotecasAtivas = AbstractProcessador
					.getDAO(BibliotecaDao.class, null)
					.findAllBibliotecasInternasAtivas();

			for (Biblioteca b : bibliotecasAtivas)
				bibliotecas.put(String.valueOf(b.getId()),
						b.getDescricaoCompleta());
			
			JSONObject bibliotecaJSON = new JSONObject(bibliotecas);
			map.put("Bibliotecas", bibliotecaJSON.toString());
		} catch (DAOException e) {
			e.printStackTrace();
		};

	}

	// Buscar Artigos e Livros

	public static void pesquisarAcervo(Map<String, String> map, Integer idBiblioteca, String tituloBusca, String autorBusca, String assuntoBusca)
			throws DAOException {
		
		TituloCatalograficoDao daoTitulo = null;
		ArtigoDePeriodicoDao daoArtigo = null;
		GeraPesquisaTextual geradorPesquisa = new GeraPesquisaTextualFactory().getGeradorPesquisaTextual();
		List<CacheEntidadesMarc> resultadosBuscados = new ArrayList<CacheEntidadesMarc>();
		List<CacheEntidadesMarc> artigos = new ArrayList<CacheEntidadesMarc>();
		List<Integer> idsBibliotecasAcervoPublico = new ArrayList<Integer>();

		
		
		// Demais parâmetros da busca
		String localPublicacaoBusca = "";
		String editoraBusca = "";
		Integer anoInicialBusca = null;
		Integer anoFinalBusca = null;
		Integer idBibliotecaBusca = null;
		Integer idColecaoBusca = null;
		Integer idTipoMaterialBusca = null;

		String mensagem = null;
		
		Boolean buscarBusca           = false ;
		Boolean buscarAssunto 		  = true;
		Boolean buscarAutor 		  = true;
		Boolean buscarLocalPublicacao = false;
		Boolean buscarEditora 		  = false;
		Boolean buscarAno 			  = false;
		Boolean buscarBiblioteca      = true;
		Boolean buscarColecao 		  = false;
		Boolean buscarTipoMaterial    = false;
		Boolean buscarTitulo 		  = true;

		try {
			BibliotecaDao dao = AbstractProcessador.getDAO(BibliotecaDao.class,	null);
			idsBibliotecasAcervoPublico = dao.findIdsBibliotecaAcervoPublico();
			dao.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (StringUtils.isEmpty(tituloBusca) || tituloBusca.length() <= 2)
			buscarBusca = false;
		if (StringUtils.isEmpty(assuntoBusca) || assuntoBusca.length() <= 2)
			buscarAssunto = false;
		if (StringUtils.isEmpty(autorBusca) || autorBusca.length() <= 2)
			buscarAutor = false;
		if (StringUtils.isEmpty(localPublicacaoBusca) || localPublicacaoBusca.length() <= 2)
			buscarLocalPublicacao = false;
		if (StringUtils.isEmpty(editoraBusca) || editoraBusca.length() <= 2)
			buscarEditora = false;
		if (anoInicialBusca == null && anoFinalBusca == null)
			buscarAno = false;
		if (new Integer(-1).equals(idBibliotecaBusca))
			buscarBiblioteca = false;
		if (new Integer(-1).equals(idColecaoBusca))
			buscarColecao = false;
		if (new Integer(-1).equals(idTipoMaterialBusca))
			buscarTipoMaterial = false;

		if (!buscarTitulo)
			tituloBusca = null;
		if (!buscarAssunto)
			assuntoBusca = null;
		if (!buscarAutor)
			autorBusca = null;
		if (!buscarLocalPublicacao)
			localPublicacaoBusca = null;
		if (!buscarEditora)
			editoraBusca = null;
		if (!buscarAno) {
			anoInicialBusca = null;
			anoFinalBusca = null;
		}

		if (!buscarBiblioteca)
			idBibliotecaBusca = -1;
		if (!buscarColecao)
			idColecaoBusca = -1;
		if (!buscarTipoMaterial)
			idTipoMaterialBusca = -1;

		try {
			
			daoTitulo = AbstractProcessador.getDAO(	TituloCatalograficoDao.class, null);
			

			CampoOrdenacaoConsultaAcervo campoOrdenacao = CampoOrdenacaoConsultaAcervo.getCampoOrdenacao(1); // Ordenar por titulo

			// Buscar por Livros

			resultadosBuscados = daoTitulo.buscaMultiCampoPublica(
					geradorPesquisa, campoOrdenacao, tituloBusca, assuntoBusca,
					autorBusca, localPublicacaoBusca, editoraBusca,
					anoInicialBusca, anoFinalBusca, idBibliotecaBusca,
					idColecaoBusca, idTipoMaterialBusca, false,
					idsBibliotecasAcervoPublico);

			// geraResultadosPaginacao();
			
			daoTitulo.close();
			
			// ////////////REALIZA TAMBÉM A BUSCA DE ARTIGOS //////////////
			
			daoArtigo = AbstractProcessador.getDAO(ArtigoDePeriodicoDao.class, null);
			if (StringUtils.notEmpty(tituloBusca)
					|| StringUtils.notEmpty(autorBusca)
					|| StringUtils.notEmpty(assuntoBusca)) {

				artigos = daoArtigo
						.findAllArtigosResumidosAtivosByTituloAutorPalarvaChave(
								geradorPesquisa,
								CampoOrdenacaoConsultaAcervo
										.isCampoOrdenacaoArtigo(campoOrdenacao) ? campoOrdenacao
										: null, tituloBusca, autorBusca,
								assuntoBusca, true, idsBibliotecasAcervoPublico);

			}
			
			daoArtigo.close();
						
			Integer quantidadeTotalResultados = resultadosBuscados.size();
			
			if (quantidadeTotalResultados == 0 && artigos.size() == 0)
				mensagem = (MensagensArquitetura.BUSCA_SEM_RESULTADOS);

			if (Operations.LIMITE_RESULTADOS_LIVROS
					.compareTo(quantidadeTotalResultados) <= 0) {
				mensagem = ("A busca por livros resultou em um número muito grande de resultados, somente os "
						+ Operations.LIMITE_RESULTADOS_LIVROS
						+ " primeiros estão sendo mostrados.");
			}

			if (Operations.LIMITE_RESULTADOS_ARTIGOS.compareTo(artigos
					.size()) <= 0) {
				mensagem = ("A busca de artigos resultou em um número muito grande de resultados, somente os "
						+ Operations.LIMITE_RESULTADOS_ARTIGOS
						+ " primeiros estão sendo mostrados.");
			}
			
			map.put("Mensagem", mensagem);
			
			
			//Mapa dos livros
			Map<String,JSONObject> livros = new HashMap<String,JSONObject>();
			Map<String,String> cont;
			CacheEntidadesMarc liv;
			
			int quantidadeInteracaoLivro =  //Carregar o JSON apenas com a quantidade permitida pelo sistema, para não estourar o máximo.
					(resultadosBuscados.size() > Operations.LIMITE_RESULTADOS_LIVROS ? Operations.LIMITE_RESULTADOS_LIVROS : resultadosBuscados.size());
			for(int i = 0; i < quantidadeInteracaoLivro;i++){
				liv = resultadosBuscados.get(i);
				cont = new HashMap<String,String>();
				cont.put("Autor", liv.getAutor());
				cont.put("Titulo", liv.getTitulo());
				cont.put("Edicao", liv.getEdicao());
				cont.put("Ano", liv.getAno());
				cont.put("QuantidadeAtivos", String.valueOf(liv.getQuantidadeMateriaisAtivosTitulo()));
				livros.put(String.valueOf(liv.getId()), new JSONObject(cont));
			}
			
			//Adicao do mapa de livros para o mapa de retorno
			map.put("Livros", new JSONObject(livros).toString());
			
			// Mapa dos artigos
			
			//Mapa dos livros
			Map<String,JSONObject> artigosJSON = new HashMap<String,JSONObject>();
			
			int quantidadeInteracaoArtigo =  //Carregar o JSON apenas com a quantidade permitida pelo sistema, para não estourar o máximo.
					(resultadosBuscados.size() > Operations.LIMITE_RESULTADOS_ARTIGOS ? Operations.LIMITE_RESULTADOS_ARTIGOS : resultadosBuscados.size());
			for(int i = 0; i < quantidadeInteracaoArtigo;i++){
				liv = resultadosBuscados.get(i);
				cont = new HashMap<String,String>();
				cont.put("Autor", liv.getAutor());
				cont.put("Titulo", liv.getTitulo());
				cont.put("Edicao", liv.getEdicao());
				cont.put("Ano", liv.getAno());
				cont.put("QuantidadeAtivos", String.valueOf(liv.getQuantidadeMateriaisAtivosTitulo()));
				artigosJSON.put(String.valueOf(liv.getId()), new JSONObject(cont));
			}
			
			//Adicao do mapa de livros para o mapa de retorno
			map.put("Artigos", new JSONObject(livros).toString());
						

		} finally {

			if (daoTitulo != null)
				daoTitulo.close();
			if (daoArtigo != null)
				daoArtigo.close();
		}

	}
	
	
	// Situação do Usuario
	public static void minhaSituacao(String login, String senha, Map<String,String> map) {
		try {
			//Captura Informações do usuário
			UsuarioGeral userGeral = verificaLoginSenha(login, senha);		
			UsuarioDao usuarioDao = AbstractProcessador.getDAO(UsuarioDao.class, null);
			Usuario user = usuarioDao.findByPrimaryKey(userGeral.getId());
			usuarioDao.close(); // Encerra conexão
			
			UsuarioBibliotecaDao usuarioBiblioDao = AbstractProcessador.getDAO(
					UsuarioBibliotecaDao.class, null); // Inicia Conexão
	
			/**
			 * Carrega informações do Usuario Biblioteca
			 */
	
			List<UsuarioBiblioteca> contasUsuarioBiblioteca = usuarioBiblioDao.findUsuarioBibliotecaAtivoByPessoa(user.getPessoa().getId());
			UsuarioBiblioteca usuarioBiblioteca = UsuarioBibliotecaUtil.recuperaUsuarioNaoQuitadosAtivos(contasUsuarioBiblioteca);
			usuarioBiblioDao.close(); // Encerra Conexão
	
			EmprestimoDao emprestimoDao = AbstractProcessador.getDAO(EmprestimoDao.class, null);
			
			//Captura Emprestimos Ativos do usuário
			List<Emprestimo> emprestimos = emprestimoDao.findEmprestimosAtivosPorVinculoUsuario(usuarioBiblioteca);
			String situacao = verificarSituacaoUsuario(usuarioBiblioteca);
			
			//Captura informações dos emprestimos e carrega o map.
			Map<String,String> emprestimoMap;
			Map<String,String> emprestimosUsuarioMap = new HashMap<String,String>();
			MaterialInformacionalDao exemplarDao = AbstractProcessador.getDAO(MaterialInformacionalDao.class,null);
			TituloCatalograficoDao tituloDao = AbstractProcessador.getDAO(TituloCatalograficoDao.class,null);
			MaterialInformacional material;
			Object[] materialInfo;
			for(Emprestimo emp : emprestimos){				
				material = exemplarDao.findMaterialAtivoByCodigoBarras(emp.getMaterial().getCodigoBarras());
				materialInfo =(Object[]) tituloDao.findInformacoesResumidasDoTitulo(tituloDao.findIdTituloDoMaterial(material.getId()));

				emprestimoMap = new HashMap<String,String>();
				emprestimoMap.put("CodigoDeBarras", material.getCodigoBarras());
				emprestimoMap.put("Autor", (String)materialInfo[2]);
				emprestimoMap.put("Titulo",(String)materialInfo[1]);
				emprestimoMap.put("Ano", (String)materialInfo[3]);
				emprestimoMap.put("DataEmprestimo", emp.getDataEmprestimo().toString());
				emprestimoMap.put("DataRenovacao", emp.getDataRenovacao() == null ? "-" : emp.getDataRenovacao().toString());
				emprestimoMap.put("Devolucao", emp.getDataDevolucao() == null ? "-" : emp.getDataDevolucao().toString());
				emprestimoMap.put("Biblioteca", emp.getMaterial().getBiblioteca().getDescricao());
				emprestimoMap.put("Renovavel", emp.isPodeRenovar() && !emp.isAtrasado() ? "true" : "false");
				
				JSONObject emprestimoJSON = new JSONObject(emprestimoMap);
				emprestimosUsuarioMap.put(String.valueOf(emp.getId()), emprestimoJSON.toString());
			}			
			JSONObject emprestimosJSON = new JSONObject(emprestimosUsuarioMap);
			map.put("Emprestimos", emprestimosJSON.toString());
			map.put("Mensagem", situacao);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String verificarSituacaoUsuario(UsuarioBiblioteca usuarioBiblioteca) throws DAOException, ArqException,
			NegocioException {

		ArrayList<SituacaoUsuarioBiblioteca> situacoes = new ArrayList<SituacaoUsuarioBiblioteca>();

		boolean situacaoSemPendencias;
		if (usuarioBiblioteca != null) {

			String motivoBloqueio = VerificaSituacaoUsuarioBibliotecaUtil.getMotivoBloqueadoUsuario(usuarioBiblioteca);

			if (StringUtils.notEmpty(motivoBloqueio))
				situacoes.add(SituacaoUsuarioBiblioteca.ESTA_BLOQUEADO);

			situacoes.addAll(VerificaSituacaoUsuarioBibliotecaUtil.verificaUsuarioPossuiPunicoesBiblioteca(usuarioBiblioteca.getId()));
			situacoes
					.addAll(VerificaSituacaoUsuarioBibliotecaUtil
							.verificaUsuarioPossuiEmprestimosEmAbertoOUAtrasadosBiblioteca(usuarioBiblioteca.getId()));

			if (situacoes.isEmpty()) {
				situacoes.add(SituacaoUsuarioBiblioteca.SEM_PENDENCIA);
				situacaoSemPendencias = true;
			} else {
				situacaoSemPendencias = false;
			}
		} else { // Se não tem nenhum vínculo atual ativo, então teoricamente
					// não possui empréstimos ativos
			situacoes.add(SituacaoUsuarioBiblioteca.SEM_PENDENCIA);
			situacaoSemPendencias = true;
		}
		
		String situacao = "";
		for(SituacaoUsuarioBiblioteca s : situacoes)
			situacao+=s.getDescricaoCompleta()+" ";
		
		return situacao;
	}
		
	
	public static void informacoesExemplar(){
		try {
			ExemplarDao daoExemplares =  AbstractProcessador.getDAO(ExemplarDao.class, null);
			
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void renovacaoEmprestimo(String login, String senha,
			String senhaBiblioteca) {
	} // Definir demais parametros para empréstimos: id_livro

	
	

	public static void meusEmprestimos() {
	}

}
