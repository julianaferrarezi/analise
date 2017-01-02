package br.unesp.academico.analise.services;

import br.unesp.core.AbstractBasicService;
import br.unesp.core.ConfigHelper;
import br.unesp.core.Log4jWrapper;
import br.unesp.graduacao.api.v1.beans.AlunoGraduacaoVO;
import br.unesp.graduacao.api.v1.client.AlunosGraduacaoClient;
import br.unesp.graduacao.api.v1.client.ClientFactory;
import br.unesp.graduacao.api.v1.client.CursosClient;

public class AcademicoService extends AbstractBasicService {

	private static AcademicoService instance = new AcademicoService();
    private AlunoGraduacaoVO alunoGraduacaoVO = null;
	
    private Log4jWrapper log = new Log4jWrapper(AcademicoService.class, null);
      
	public AcademicoService() {
		try {
			
		} catch (Exception ex) {
            log.fatal("Services não instanciados: ".concat(AcademicoService.class.getName()), ex);
        }
		
	}
	
//	public UnidadeUniversitaria buscarUnidadeUniversitariaPorPessoaFisica(PessoaFisica pessoaFisica) {
//		
//		if (alunoGraduacaoVO == null) {
//			alunoGraduacaoVO = getAlunoGraduacaoAtivoPorPessoaFisica(pessoaFisica);
//		}
//		
//		if (alunoGraduacaoVO != null && alunoGraduacaoVO.isMatriculado()) {
//			CursoVO cursoVO = getCursosClient().get(alunoGraduacaoVO.getIdCurso());
//			return commonService.buscarUnidadeUniversitariaPorId(cursoVO.getIdUnidadeUniversitaria());
//		} else {
//			return null;
//		}
//	}
	
//	private AlunoGraduacaoVO getAlunoGraduacaoAtivoPorPessoaFisica(PessoaFisica pessoaFisica) {
//		AlunoGraduacaoVO ag = getAlunosGraduacaoClient().getByEmail(pessoaFisica.getPessoa().getEmail());
//        return ag != null && ag.isMatriculado() ? ag : null;
//    }
    
//    public String getRegistroAlunoGraduacaoAtivo(PessoaFisica pessoaFisica) {
//    	String ra = null;
//    	    	
//    	if (alunoGraduacaoVO == null) {
//    		alunoGraduacaoVO = getAlunoGraduacaoAtivoPorPessoaFisica(pessoaFisica);
//    	}
//    	
//    	if (alunoGraduacaoVO != null) {
//    		ra = alunoGraduacaoVO.getRegistroAcademico();
//    	}
//    	return ra;
//    }
	
	public AlunoGraduacaoVO getAlunoPorEmail(String email) {
		return getAlunosGraduacaoClient().getByEmail(email);
	}
    
    private AlunosGraduacaoClient getAlunosGraduacaoClient(){
    	return ClientFactory.create(AlunosGraduacaoClient.class, ConfigHelper.get().getString("academico.api"));
    }
    
    private CursosClient getCursosClient(){
    	return ClientFactory.create(CursosClient.class, ConfigHelper.get().getString("academico.api"));
    }
    
    public static final AcademicoService getInstance() {
		if (instance == null)
			instance = new AcademicoService();
		
		return instance;
	}
	
}