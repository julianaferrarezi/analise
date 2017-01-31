package br.unesp.academico.analise.services;

import java.io.Serializable;
import java.util.List;

import br.unesp.core.ConfigHelper;
import br.unesp.graduacao.api.v2.beans.AlunoGraduacaoBasicoVO;
import br.unesp.graduacao.api.v2.beans.AlunoGraduacaoVO;
import br.unesp.graduacao.api.v2.client.AlunosGraduacaoClient;
import br.unesp.graduacao.api.v2.client.ClientFactory;
import br.unesp.graduacao.api.v2.client.CursosClient;

public class AcademicoService {
	
	private AlunosGraduacaoClient alunosGraduacaoClient;
	
    public static AcademicoService getInstance() {
        // Usando a V2, não dá para fazer o service Singleton! 
        // O Resource dos Clientes mantém url no histórico.
    	AcademicoService instance = new AcademicoService(); 
    	instance.alunosGraduacaoClient =  ClientFactory.create(AlunosGraduacaoClient.class, ConfigHelper.get().getString("academico.api"), ConfigHelper.get().getString("academico.token"));
    	
    	return instance;
    }
    
    public AlunoGraduacaoVO getAlunoPorEmail(String email){
    	AlunoGraduacaoVO alunoVO = null;
        try {
            List<AlunoGraduacaoBasicoVO> alunos = alunosGraduacaoClient.filter(null, 2L, false, null, email.trim(), null);
            if (alunos != null && !alunos.isEmpty()){
                //É possível que exista mais de um registro ativo por aluno!
                //Ex: Aluno matriculado em outro curso como Intercâmbio Nacional ou Aluno Especial.
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
    
    public AlunoGraduacaoVO getAlunoGraduacaoVO(Long idAluno){
        AlunoGraduacaoVO alunoVO = alunosGraduacaoClient.get(idAluno);
        return alunoVO;
    }
	
}