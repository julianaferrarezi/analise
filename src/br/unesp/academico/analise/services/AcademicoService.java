package br.unesp.academico.analise.services;

import java.util.ArrayList;
import java.util.List;

import br.unesp.core.ConfigHelper;
import br.unesp.graduacao.api.v2.beans.AlunoGraduacaoBasicoVO;
import br.unesp.graduacao.api.v2.beans.AlunoGraduacaoVO;
import br.unesp.graduacao.api.v2.beans.CursoVO;
import br.unesp.graduacao.api.v2.beans.UnidadeUniversitariaVO;
import br.unesp.graduacao.api.v2.client.AlunosGraduacaoClient;
import br.unesp.graduacao.api.v2.client.ClientFactory;
import br.unesp.graduacao.api.v2.client.CursosClient;
import br.unesp.graduacao.api.v2.client.UnidadesUniversitariasClient;

public class AcademicoService {
	
	private AlunosGraduacaoClient alunosGraduacaoClient;
	private UnidadesUniversitariasClient unidadesUniversitariasClient;
	private CursosClient cursosClient;
	
    public static AcademicoService getInstance() {
        // Usando a V2, n√£o d√° para fazer o service Singleton! 
        // O Resource dos Clientes mant√©m url no hist√≥rico.
    	AcademicoService instance = new AcademicoService(); 
    	instance.alunosGraduacaoClient =  ClientFactory.create(AlunosGraduacaoClient.class, ConfigHelper.get().getString("academico.api"), ConfigHelper.get().getString("academico.token"));
    	instance.unidadesUniversitariasClient = ClientFactory.create(UnidadesUniversitariasClient.class, ConfigHelper.get().getString("academico.api"), ConfigHelper.get().getString("academico.token"));
    	instance.cursosClient = ClientFactory.create(CursosClient.class, ConfigHelper.get().getString("academico.api"), ConfigHelper.get().getString("academico.token"));
    	return instance;
    }
    
    public AlunoGraduacaoVO getAlunoPorEmail(String email){
    	AlunoGraduacaoVO alunoVO = null;
        try {
            List<AlunoGraduacaoBasicoVO> alunos = alunosGraduacaoClient.filter(null, 2L, false, null, email.trim(), null);
            if (alunos != null && !alunos.isEmpty()){
                //√â poss√≠vel que exista mais de um registro ativo por aluno!
                //Ex: Aluno matriculado em outro curso como Interc√¢mbio Nacional ou Aluno Especial.
                if (alunos.size() == 1){
                    return getAlunoGraduacaoVO(alunos.iterator().next().getId());
                }
                for (AlunoGraduacaoBasicoVO alunoBasico : alunos){
                    AlunoGraduacaoVO aluno = getAlunoGraduacaoVO(alunoBasico.getId());
                    if (aluno.getTipoIngresso().equals("Concurso Vestibular")){
                        alunoVO = aluno;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alunoVO;
    }
    
    public List<AlunoGraduacaoVO> getAlunosPorUnidade(Long idUnidade, int ano, int semestre){
    	List<AlunoGraduacaoVO> unidade = null;
    	try {
    		unidade = unidadesUniversitariasClient.getCursandoDisciplinasPorUnidadeUniversitariaAnoSemestre(idUnidade, ano, semestre);
    		return unidade;
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return unidade;
	}
    
    public UnidadeUniversitariaVO getUnidade(String sigla) {
    	UnidadeUniversitariaVO unidade = unidadesUniversitariasClient.getBySigla(sigla);
    	return unidade;
    }
    
    public List<CursoVO> getCursosPorUnidade(String sigla) {
    	Long idUnidade = getUnidade(sigla).getId();
    	List<CursoVO> cursos = unidadesUniversitariasClient.getCursos(idUnidade);
    	
    	//REMOVER
    	/*List<CursoVO> cursos = new ArrayList<CursoVO>();
    	CursoVO c1 = new CursoVO();
    	c1.setId(16L);
    	c1.setNome("Sistemas de InformaÁ„o");
    	cursos.add(c1);*/
    	
    	return cursos;
    }
    
    public CursoVO getCurso(Long idCurso) {
    	CursoVO curso = cursosClient.get(idCurso);
    	return curso;
    }
    
    public List<AlunoGraduacaoVO> getAlunosPorCurso(Long idCurso) {
    	List<AlunoGraduacaoVO> alunos = cursosClient.getAlunosGraduacaoMatriculados(idCurso);
    	return alunos;
    }
    
    public String getNomeCurso(Long idCurso) {
    	CursoVO curso = getCurso(idCurso);
    	return curso.getNome();
    	
    	//REMOVER
    	//return "Sistemas de InformaÁ„o";
    }
    
    
    
    //AlunosGraduados
    public List<AlunoGraduacaoVO> getAlunosGraduados(Long idCurso, Long ano) {
    	List<AlunoGraduacaoVO> alunos = cursosClient.getAlunosGraduacaoFormados(idCurso, ano);
    	
    	//REMOVER
    	/*List<AlunoGraduacaoVO> alunos = new ArrayList<AlunoGraduacaoVO>();
    	AlunoGraduacaoVO a1 = new AlunoGraduacaoVO();
    	a1.setIdCurso(16L);
    	a1.setTipoIngresso("Concurso Vestibular");
    	AlunoGraduacaoVO a2 = new AlunoGraduacaoVO();
    	a2.setIdCurso(16L);
    	a2.setTipoIngresso("Concurso Vestibular");
    	alunos.add(a1);
    	alunos.add(a2);*/
    	
    	return alunos;
    }
    
    //AlunosEvadidos
    public List<AlunoGraduacaoVO> getAlunosEvadidos(Long idCurso) {
    	List<AlunoGraduacaoVO> alunos = cursosClient.getAlunosGraduacaoMatriculados(idCurso);
    	return alunos;
    }
    
    //AlunosMatriculados
    public List<AlunoGraduacaoVO> getAlunosMatriculados(Long idCurso) {
    	List<AlunoGraduacaoVO> alunos = cursosClient.getAlunosGraduacaoMatriculados(idCurso);
    	return alunos;
    }
     
    
    /*public int getAlunosPorUnidade(String email){
    	//List<AlunoGraduacaoBasicoVO> alunos = null;
        try {
        	List<AlunoGraduacaoBasicoVO> alunos = alunosGraduacaoClient.filter(null, 2L, true, null, null, null);
        	return (alunos.size());
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }*/
    
    public AlunoGraduacaoVO getAlunoGraduacaoVO(Long idAluno){
        AlunoGraduacaoVO alunoVO = alunosGraduacaoClient.get(idAluno);
        return alunoVO;
    }
	
}