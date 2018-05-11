package br.unesp.academico.analise.services;

import java.util.ArrayList;
import java.util.List;

import br.unesp.core.ConfigHelper;
import br.unesp.exception.ServiceValidationException;
import br.unesp.graduacao.api.v2.beans.AlunoGraduacaoVO;
import br.unesp.graduacao.api.v2.beans.CursoVO;
import br.unesp.graduacao.api.v2.beans.UnidadeUniversitariaVO;
import br.unesp.graduacao.api.v2.client.AlunosGraduacaoClient;
import br.unesp.graduacao.api.v2.client.ClientFactory;
import br.unesp.graduacao.api.v2.client.CursosClient;
import br.unesp.graduacao.api.v2.client.UnidadesUniversitariasClient;

public class Model {
	
	private AlunosGraduacaoClient        alunosGraduacaoClient;
	private UnidadesUniversitariasClient unidadesUniversitariasClient;
	private CursosClient                 cursosClient;
	
    public static Model getInstance() {
        // Usando a V2, nÃ£o dÃ¡ para fazer o service Singleton! 
        // O Resource dos Clientes mantÃ©m url no histÃ³rico.
    	Model instance = new Model(); 
    	instance.alunosGraduacaoClient =  ClientFactory.create(AlunosGraduacaoClient.class, ConfigHelper.get().getString("academico.api"), ConfigHelper.get().getString("academico.token"));
    	instance.unidadesUniversitariasClient = ClientFactory.create(UnidadesUniversitariasClient.class, ConfigHelper.get().getString("academico.api"), ConfigHelper.get().getString("academico.token"));
    	instance.cursosClient = ClientFactory.create(CursosClient.class, ConfigHelper.get().getString("academico.api"), ConfigHelper.get().getString("academico.token"));
    	return instance;
    }
    
    public List<String> getUnidadesUniversitarias() {
    	List<br.unesp.academico.analise.vo.UnidadeUniversitariaVO> listaUnidade = null;
    	UnidadeUniversitariaService unidadeUniversitariaService = new UnidadeUniversitariaService();
    	List<String> unidadesUniversitarias = new ArrayList<String>();
    	
		try {
			listaUnidade = unidadeUniversitariaService.listarUnidades();
			for (br.unesp.academico.analise.vo.UnidadeUniversitariaVO unidadeUniversitariaVO : listaUnidade) {
				unidadesUniversitarias.add(unidadeUniversitariaVO.getNomeAbreviado());
			}
		} catch (ServiceValidationException e) {
			e.printStackTrace();
		}
    	
    	//unidadesUniversitarias.add("FC");
		
		return unidadesUniversitarias;
    }
    
    public List<CursoVO> getCursos(String siglaUnidade) {
    	Long idUnidade = getUnidade(siglaUnidade).getId();
    	return unidadesUniversitariasClient.getCursos(idUnidade);
    	
    	/*List<CursoVO> cursos = new ArrayList<CursoVO>();
    	CursoVO curso1 = new CursoVO();
    	curso1.setId(1L);
    	curso1.setNome("Sistemas de informação");
    	CursoVO curso2 = new CursoVO();
    	curso2.setId(2L);
    	curso2.setNome("Ciências da computação");
    	cursos.add(curso1);
    	cursos.add(curso2);
    	return cursos;*/
    }

    private UnidadeUniversitariaVO getUnidade(String siglaUnidade) {
    	return unidadesUniversitariasClient.getBySigla(siglaUnidade);
    }
    
    public CursoVO getCurso(Long idCurso) {
    	return cursosClient.get(idCurso);
    	
    	/*CursoVO curso1 = new CursoVO();
    	curso1.setId(1L);
    	curso1.setNome("Sistemas de informação");
    	CursoVO curso2 = new CursoVO();
    	curso2.setId(2L);
    	curso2.setNome("Ciências da computação");
    	if (idCurso == 1L) return curso1;
    	else return curso2;*/
    }
    
    public List<AlunoGraduacaoVO> getAlunosGraduados(Long idCurso, Long ano) {
    	return cursosClient.getAlunosGraduacaoFormados(idCurso, ano);
    	
    	/*AlunoGraduacaoVO aluno1 = new AlunoGraduacaoVO();
    	aluno1.setIdCurso(1L);
    	aluno1.setSexo("Masculino");
    	aluno1.setTipoIngresso("Concurso Vestibular");
    	AlunoGraduacaoVO aluno2 = new AlunoGraduacaoVO();
    	aluno2.setIdCurso(2L);
    	aluno2.setNome("Feminino");
    	aluno2.setTipoIngresso("Transferência interna");
    	List<AlunoGraduacaoVO> alunos = new ArrayList<AlunoGraduacaoVO>();
    	alunos.add(aluno1);
    	alunos.add(aluno2);
    	return alunos;*/
    }
    
    public List<AlunoGraduacaoVO> getAlunosMatriculados(Long idCurso) {
    	return cursosClient.getAlunosGraduacaoMatriculados(idCurso);
    }
    
    public List<String> getTiposIngresso(List<AlunoGraduacaoVO> listaAlunos) {
    	List<String> tiposIngresso = new ArrayList<String>();
    	
    	for (AlunoGraduacaoVO aluno : listaAlunos) {
    		String tipoIngresso = aluno.getTipoIngresso();
    		if (! tiposIngresso.contains(tipoIngresso)) {
    			tiposIngresso.add(tipoIngresso);
    		}
    	}
    	return tiposIngresso;
    }
}