package br.unesp.academico.analise.jsf;

//import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.BarChartModel;

import br.unesp.academico.analise.services.Model;
import br.unesp.graduacao.api.v2.beans.AlunoGraduacaoVO;
import br.unesp.graduacao.api.v2.beans.CursoVO;

@ManagedBean(name = "graduados")
@ViewScoped
public class Graduados {
	private static final long serialVersionUID = 8927538487452737559L;

	private Model 				   model;
	private Graficos               graficos;
	private String                 unidadeSelecionada;
	private List<String>           cursosSelecionados;
	private List<String>           tiposSelecionados;
	private List<AlunoGraduacaoVO> alunosSelecionados;
	private List<AlunoGraduacaoVO> alunosFiltrados;
	private Date 				   dataEntrada;
	private Long 				   anoSaida; 
	private String 				   sexo;
	
	private List<String> unidadesUniversitarias;
	private List<CursoVO> cursos;
	private List<String> tiposIngresso;
	private BarChartModel grafico;

	@PostConstruct
	public void init(){
		model = Model.getInstance();
		graficos = new Graficos();
		cursosSelecionados = new ArrayList<String>();
		tiposSelecionados = new ArrayList<String>();
		alunosSelecionados = new ArrayList<AlunoGraduacaoVO>();
		sexo = "Ambos";
	}
	
	public void getAlunos() {
		alunosSelecionados = new ArrayList<AlunoGraduacaoVO>();
		for (String idCurso : cursosSelecionados) {
			alunosSelecionados.addAll(model.getAlunosGraduados(Long.parseLong(idCurso), anoSaida));
		}
		RequestContext.getCurrentInstance().update("formAlunosGraduados:tiposIngresso");
		tiposSelecionados = new ArrayList<String>();
	}
	
	public String getUnidadeSelecionada() {
		return unidadeSelecionada;
	}

	public void setUnidadeSelecionada(String unidade) {
		unidadeSelecionada = unidade;
	}
	
	public List<String> getCursosSelecionados() {
		return cursosSelecionados;
	}
	
	public void setCursosSelecionados(List<String> cursos) {
		cursosSelecionados = cursos;
	}
	
	public List<String> getTiposSelecionados() {
		return tiposSelecionados;
	}
	
	public void setTiposSelecionados(List<String> tipos) {
		tiposSelecionados = tipos;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public Date getDataEntrada() {
		return dataEntrada;
	}
	
	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}
	
	public Long getAnoSaida() {
		return anoSaida;
	}
	
	public void setAnoSaida(Long anoSaida) {
		this.anoSaida = anoSaida;
	}
	
	public List<String> getUnidadesUniversitarias() {
		unidadesUniversitarias = model.getUnidadesUniversitarias();
		return unidadesUniversitarias;
	}
	
	public List<CursoVO> getCursos() {
		cursos = (unidadeSelecionada != null) ? model.getCursos(unidadeSelecionada) : new ArrayList<CursoVO>();
		return cursos;
	}
	
	public List<String> getTiposIngresso() {
		tiposIngresso = model.getTiposIngresso(alunosSelecionados);
		return tiposIngresso;
	}
	
	public Map<String, Number> getQuantidadePorCurso() {
		Map<String, Number> quantidade = new HashMap<String, Number>();
		
		for (String idCurso : cursosSelecionados) {
			CursoVO curso = model.getCurso(Long.parseLong(idCurso));
			quantidade.put(curso.getNome(), 0);
		}
		
		for (AlunoGraduacaoVO aluno : alunosFiltrados) {
			CursoVO curso = model.getCurso(aluno.getIdCurso());
			//Number soma = quantidade.containsKey(curso.getNome()) ? quantidade.get(curso.getNome()).intValue() + 1 : 1;
			Number soma = quantidade.get(curso.getNome()).intValue() + 1;
			quantidade.put(curso.getNome(), soma);
		}
		return quantidade;
	}
	
	public Map<String, Map<String, Number>> getQuantidadePorTipoIngresso() {
		Map<String, Map<String, Number>> quantidade = new HashMap<String, Map<String, Number>>();
		
		for (AlunoGraduacaoVO aluno : alunosFiltrados) {
			String tipo = aluno.getTipoIngresso();
			CursoVO curso = model.getCurso(aluno.getIdCurso());
			
			if (! tiposSelecionados.contains(tipo)) {
				continue;
			}
			
			if (quantidade.containsKey(tipo)) {
				Number soma = quantidade.get(tipo).containsKey(curso.getNome()) ? quantidade.get(tipo).get(curso.getNome()).intValue() + 1 : 1;
				quantidade.get(tipo).put(curso.getNome(), soma);
			} else {
				Map<String, Number> cursoQuantidade = new HashMap<String, Number>();
				for (String idCurso : cursosSelecionados) {
					CursoVO cursoSelecionado = model.getCurso(Long.parseLong(idCurso));
					Number valor = (cursoSelecionado.getId() == curso.getId()) ? 1 : 0;
					cursoQuantidade.put(cursoSelecionado.getNome(), valor);
				}
				quantidade.put(tipo, cursoQuantidade);
			}
		}
		return quantidade;
	}
	
	public void filtrar() {
		alunosFiltrados = new ArrayList<AlunoGraduacaoVO>();
		for (AlunoGraduacaoVO aluno : alunosSelecionados) {
			boolean conta = true;
			
			if (! sexo.equals("Ambos") && ! sexo.equals(aluno.getSexo())) {
				conta = false;
			}
			
			if (dataEntrada != null && aluno.getDataPrimeiraMatricula().before(dataEntrada)) {
				conta = false;
			}
			
			if (conta) {
				alunosFiltrados.add(aluno);
			}
		}
	}
	
	public BarChartModel getGrafico() {
		filtrar();
		Map<String, Number> quantidadePorCurso = getQuantidadePorCurso();
		Map<String, Map<String, Number>> quantidadePorTipoIngresso = getQuantidadePorTipoIngresso();
		
		grafico =  graficos.getGrafico(quantidadePorCurso, quantidadePorTipoIngresso);
		return grafico;
	}
	
	public String alunosGraduados() {
		return "alunosgraduados.xhtml";
	}
}