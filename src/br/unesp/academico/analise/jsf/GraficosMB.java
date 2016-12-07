package br.unesp.academico.analise.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "graficosMB")
@ViewScoped
public class GraficosMB implements Serializable {
	private static final long serialVersionUID = 8927538487452737559L;

	@PostConstruct
	public void init(){
		
	}
	
	
	
	public String alunos(){
		
		return "alunos.xhtml";
	}
	

}
