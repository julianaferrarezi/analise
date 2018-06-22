package br.unesp.academico.analise.jsf;

//import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.chart.LineChartModel;

import br.unesp.academico.analise.services.AcademicoService;
import br.unesp.academico.analise.services.UnidadeUniversitariaService;
import br.unesp.exception.ServiceValidationException;
import br.unesp.graduacao.api.v2.beans.AlunoGraduacaoVO;
import br.unesp.graduacao.api.v2.beans.CursoVO;
import br.unesp.graduacao.api.v2.beans.UnidadeUniversitariaVO;

@ManagedBean(name = "graficosMB")
@ViewScoped
public class GraficosMB {
	private static final long serialVersionUID = 8927538487452737559L;

	private AcademicoService academicoService = null;
	private UnidadeUniversitariaService unidadeUniversitariaService = null;	
	
	private List<String> unidadesUniversitarias;
	private String unidadeUniversitaria;
	private List<CursoVO> cursos;
	private List<String> cursosSelecionados;
	private List<String> tiposIngresso;
	private List<String> tiposSelecionados;
	private List<AlunoGraduacaoVO> alunos = null;
	
	private Date dataEntrada; //usado em alunos graduados
	private Long anoSaida; //usado em alunos graduados
	private String sexo = "Ambos"; //usado em alunos graduados
	
	
	//**********
	class TabelaColunas {
		Map<String, String> mapNomeTipo = new HashMap<String, String>(); //Tipo de Ingresso / Nome do tipo
		Map<Object, Number> mapCursoQuant = new HashMap<Object, Number>(); //Nome do curso / Quantidade
		
		TabelaColunas(String tipoIngresso, ChartSeries serie) {
			mapNomeTipo.put("Tipo de ingresso", tipoIngresso);
			for (Map.Entry<Object, Number> entry : serie.getData().entrySet()) {
				mapCursoQuant.put(entry.getKey(), entry.getValue());
			}
		}
		
	}
	private List<TabelaColunas> dadosTabela;
	private List<String> colunasTabela;
	
	public List<TabelaColunas> getDadosTabela() {
		return dadosTabela;
	}
	public List<String> getColunasTabela() {
		return colunasTabela;
	}
	
	
	Map<String, ChartSeries> tipoSerie;
	public Map<String, ChartSeries> getTipoSerie() {
		return tipoSerie;
	}
	
	//**********
	
	
	
	private BarChartModel   barraAlunosGraduados;
	private BarChartModel   barraAlunosMatriculados;

	@PostConstruct
	public void init(){
		academicoService = AcademicoService.getInstance();
		unidadeUniversitariaService = new UnidadeUniversitariaService();
		cursosSelecionados = new ArrayList<String>();
		tiposIngresso = new ArrayList<String>();
		tiposSelecionados = new ArrayList<String>();
		alunos = new ArrayList<AlunoGraduacaoVO>();
	}
	
	//Validate
	public boolean validarBotoes() {
		if(unidadeUniversitaria == null || unidadeUniversitaria.isEmpty() || cursosSelecionados.isEmpty() || anoSaida == 0) {
			FacesContext contexto = FacesContext.getCurrentInstance();
			contexto.addMessage(null, new FacesMessage("Atenção", "Preencha os campos Unidade universitária, Cursos, Ano de saída"));
			return false;
		}
		return true;
	}
	
	// Search for graduated students
	public void getAlunosGraduados() {
		if(validarBotoes()) {
			alunos = null;
			alunos = new ArrayList<AlunoGraduacaoVO>();
			
			tiposSelecionados = new ArrayList<String>();
			dataEntrada = null;
			sexo = "Ambos";
			org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("formAlunosGraduados:tiposIngressoGraduados");
			org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("formAlunosGraduados:dataEntrada");
			org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("formAlunosGraduados:sexos");
			
			for(String curso : cursosSelecionados) {
				Long idCurso = Long.parseLong(curso);
				System.out.println("Id:" + idCurso + " Ano: " + anoSaida);
				alunos.addAll(academicoService.getAlunosGraduados(idCurso, anoSaida));
			}
			
			System.out.println("Quantidade: " + alunos.size());
			org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("formAlunosGraduados:tiposIngressoGraduados");
		}
	}
	
	// Search for registered students
	public void getAlunosMatriculados() {
		if(validarBotoes()) {
			alunos = null;
			alunos = new ArrayList<AlunoGraduacaoVO>();
			
			tiposSelecionados = new ArrayList<String>();
			dataEntrada = null;
			sexo = "Ambos";
			org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("formAlunosGraduados:tiposIngressoGraduados");
			org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("formAlunosGraduados:dataEntrada");
			org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("formAlunosGraduados:sexos");
			
			for(String curso : cursosSelecionados) {
				Long idCurso = Long.parseLong(curso);
				System.out.println("Id:" + idCurso + " Ano: " + anoSaida);
				alunos.addAll(academicoService.getAlunosMatriculados(idCurso));
			}
			
			System.out.println("Quantidade: " + alunos.size());
			org.primefaces.context.DefaultRequestContext.getCurrentInstance().update("formAlunosGraduados:tiposIngressoGraduados");
		}
	}
	
	
	// Returns the admission types of the students
	public List<String> getTiposIngresso() {
		tiposIngresso = new ArrayList<String>();
		for(AlunoGraduacaoVO aluno : alunos) {
			String tipo = aluno.getTipoIngresso();
			if(!tiposIngresso.contains(tipo))
				tiposIngresso.add(tipo);
		}
		return tiposIngresso;
	}
	
	// Returns the list of university units
	public List<String> getUnidadesUniversitarias() {
		unidadesUniversitarias = new ArrayList<String>();
		try {
			List<br.unesp.academico.analise.vo.UnidadeUniversitariaVO> listaUnidade = unidadeUniversitariaService.listarUnidades();
			for (br.unesp.academico.analise.vo.UnidadeUniversitariaVO unidadeUniversitariaVO : listaUnidade) {
				unidadesUniversitarias.add(unidadeUniversitariaVO.getNomeAbreviado());
			}
		} catch (ServiceValidationException e) {
			e.printStackTrace();
		}
		
		return (unidadesUniversitarias);
	}
	
	// Get the selected university unit
	public String getUnidadeUniversitaria() {
		return unidadeUniversitaria;
	}
	
	// Set the selected university unit
	public void setUnidadeUniversitaria(String unidade) {
		unidadeUniversitaria = unidade;
	}
	
	// Returns the available courses
	public List<CursoVO> getCursos() {
		if(unidadeUniversitaria != null && !unidadeUniversitaria.isEmpty()) {
			cursos = academicoService.getCursosPorUnidade(unidadeUniversitaria);
		}
		
		return (cursos);
	}
	
	public void setCursos(List<CursoVO> cursos) {
		this.cursos = cursos;
	}
	
	public List<String> getCursosSelecionados() {
		return cursosSelecionados;
	}
	
	public void setCursosSelecionados(List<String> cursosSelecionadosA) {
		cursosSelecionados = cursosSelecionadosA;
	}
	
	public List<String> getTiposSelecionados() {
		/*List<AlunoGraduacaoVO> alunos = new ArrayList<AlunoGraduacaoVO>();
		for(String idCurso : cursosSelecionados) {
			Long idLong = Long.parseLong(idCurso);
			alunos.addAll(academicoService.getAlunosGraduados(idLong, anoSaida));
		}*/
		for(AlunoGraduacaoVO aluno : alunos) {
			String tipo = aluno.getTipoIngresso();
			tiposIngresso.add(tipo);
		}
		return tiposSelecionados;
	}
	
	public void setTiposSelecionados (List<String> tiposSelecionados) { this.tiposSelecionados = tiposSelecionados; }
	
	public String getSexo() { return sexo; }
	public void setSexo(String s) { sexo = s; }
	
	public Date getDataEntrada() { return dataEntrada; }
	public void setDataEntrada(Date dataEntrada) { this.dataEntrada = dataEntrada; }
	
	public Long getAnoSaida() { return anoSaida; }
	public void setAnoSaida(Long anoSaida) { this.anoSaida = anoSaida; }
	
	public BarChartModel getBarraAlunosGraduados() {
		barraAlunosGraduados();
		return barraAlunosGraduados;
	}
	
	public BarChartModel getBarraAlunosMatriculados() {
		barraAlunosMatriculados();
		return barraAlunosMatriculados;
	}
	
	//Graduados - obg: unidade, curso, saída / opc: tipo de ingresso, primeira matrícula
	
	//Alunos graduados - obrigatórios: unidade universitária, curso, ano de saída - opcionais: tipos de ingresso, data de primeira matrícula
	public void barraAlunosGraduados() {
		barraAlunosGraduados = null;
		ChartSeries serieCursos = new ChartSeries();
		Map<String, ChartSeries> tipoSerie = new HashMap<String, ChartSeries>();
		
		if(!cursosSelecionados.isEmpty() && anoSaida != null && anoSaida != 0) {
			
			barraAlunosGraduados = new BarChartModel();
			int maximo = 0;
				
			for(AlunoGraduacaoVO aluno : alunos) {
				boolean contar = true;
				boolean contarTipo = false;
				String tipoIngresso = "";
					
				Long idCurso = aluno.getIdCurso();
				String nomeCurso = academicoService.getNomeCurso(idCurso);
					
				if(!tiposSelecionados.isEmpty()) {
					tipoIngresso = aluno.getTipoIngresso();
					if(tiposSelecionados.contains(tipoIngresso))
						contarTipo = true;
				}
				if(dataEntrada != null) {
					Date primeiraMatricula = aluno.getDataPrimeiraMatricula();
					if(primeiraMatricula.before(dataEntrada))
						contar = false;
				}
				if(!sexo.equals("Ambos")) {
					String sexoAluno = aluno.getSexo();
					if(!sexoAluno.equals(sexo))
						contar = false;
				}
					
				if(contar) {
						
					Number somaC = 1;
					if(serieCursos.getData().containsKey(nomeCurso)) {
						somaC = serieCursos.getData().get(nomeCurso).intValue() + 1;
					}
					serieCursos.set(nomeCurso, somaC);
						
					if(contarTipo) {
						ChartSeries serie;
						if(tipoSerie.containsKey(tipoIngresso)) {
							serie = tipoSerie.get(tipoIngresso);
							Number soma = 1;
							if(serie.getData().containsKey(nomeCurso)) {
								soma = serie.getData().get(nomeCurso).intValue() + 1;
							}
							serie.set(nomeCurso, soma);
						}
						else {
							serie = new ChartSeries();
							serie.setLabel(tipoIngresso);
							for(String curso : cursosSelecionados) {
								String c = academicoService.getNomeCurso(Long.parseLong(curso));
								serie.set(c, 0);
							}
							serie.set(nomeCurso, 1);
						}
						tipoSerie.put(tipoIngresso, serie);
					}
						
					if(somaC.intValue() > maximo) {
						maximo = somaC.intValue();
					}	
				}
			}
			
			serieCursos.setLabel("Total de alunos");
			barraAlunosGraduados.addSeries(serieCursos);
			barraAlunosGraduados.setTitle("Quantidade de alunos graduados");
			
			dadosTabela = new ArrayList<TabelaColunas>();//*********************
			this.tipoSerie = tipoSerie;//*************2
			if(!tipoSerie.isEmpty()) {
				for(Map.Entry<String, ChartSeries> entrada : tipoSerie.entrySet()) {
					barraAlunosGraduados.addSeries(entrada.getValue());
					dadosTabela.add(new TabelaColunas(entrada.getKey(), entrada.getValue()));//*********************
				}
			}
			
			//********************FAZER EM NOVA FUNÇÃO
			colunasTabela = new ArrayList<String>();
			colunasTabela.add("Tipo de ingresso");
			for(String idCurso : cursosSelecionados) {
				colunasTabela.add(academicoService.getNomeCurso(Long.parseLong(idCurso)));
			}
			
			//********************
			
			Axis eixoX  = barraAlunosGraduados.getAxis(AxisType.X);
			eixoX.setTickAngle(30);
			eixoX.setLabel("Cursos");
			
			Axis eixoY = barraAlunosGraduados.getAxis(AxisType.Y);
			eixoY.setTickFormat("%d");
			eixoY.setMax(maximo + 26);
			eixoY.setMin(0);
			
			barraAlunosGraduados.setLegendPosition("ne");
			barraAlunosGraduados.setShowPointLabels(true);
			barraAlunosGraduados.setMouseoverHighlight(false);
			barraAlunosGraduados.setShowDatatip(false);
		}
		else {
			barraAlunosGraduados = new BarChartModel();
			ChartSeries serie = new ChartSeries();
			serie.set("-", 0);
			barraAlunosGraduados.addSeries(serie);
		}
	}
	
	//Alunos matriculados - obrigatórios: unidade universitária, curso - opcionais: tipos de ingresso, sexo, data de primeira matrícula
		public void barraAlunosMatriculados() {
			barraAlunosMatriculados = null;
			ChartSeries serieCursos = new ChartSeries();
			Map<String, ChartSeries> tipoSerie = new HashMap<String, ChartSeries>();
			
			if(!cursosSelecionados.isEmpty()) {
				
				barraAlunosMatriculados = new BarChartModel();
				int maximo = 0;
					
				for(AlunoGraduacaoVO aluno : alunos) {
					boolean contar = true;
					boolean contarTipo = false;
					String tipoIngresso = "";
						
					Long idCurso = aluno.getIdCurso();
					String nomeCurso = academicoService.getNomeCurso(idCurso);
						
					if(!tiposSelecionados.isEmpty()) {
						tipoIngresso = aluno.getTipoIngresso();
						if(tiposSelecionados.contains(tipoIngresso))
							contarTipo = true;
					}
					if(dataEntrada != null) {
						Date primeiraMatricula = aluno.getDataPrimeiraMatricula();
						if(primeiraMatricula.before(dataEntrada))
							contar = false;
					}
					if(!sexo.equals("Ambos")) {
						String sexoAluno = aluno.getSexo();
						if(!sexoAluno.equals(sexo))
							contar = false;
					}
						
					if(contar) {
							
						Number somaC = 1;
						if(serieCursos.getData().containsKey(nomeCurso)) {
							somaC = serieCursos.getData().get(nomeCurso).intValue() + 1;
						}
						serieCursos.set(nomeCurso, somaC);
							
						if(contarTipo) {
							ChartSeries serie;
							if(tipoSerie.containsKey(tipoIngresso)) {
								serie = tipoSerie.get(tipoIngresso);
								Number soma = 1;
								if(serie.getData().containsKey(nomeCurso)) {
									soma = serie.getData().get(nomeCurso).intValue() + 1;
								}
								serie.set(nomeCurso, soma);
							}
							else {
								serie = new ChartSeries();
								serie.setLabel(tipoIngresso);
								for(String curso : cursosSelecionados) {
									String c = academicoService.getNomeCurso(Long.parseLong(curso));
									serie.set(c, 0);
								}
								serie.set(nomeCurso, 1);
							}
							tipoSerie.put(tipoIngresso, serie);
						}
							
						if(somaC.intValue() > maximo) {
							maximo = somaC.intValue();
						}	
					}
				}
				
				serieCursos.setLabel("Total de alunos");
				barraAlunosMatriculados.addSeries(serieCursos);
				barraAlunosMatriculados.setTitle("Quantidade de alunos matriculados");
				
				dadosTabela = new ArrayList<TabelaColunas>();//*********************
				this.tipoSerie = tipoSerie;//*************2
				if(!tipoSerie.isEmpty()) {
					for(Map.Entry<String, ChartSeries> entrada : tipoSerie.entrySet()) {
						barraAlunosMatriculados.addSeries(entrada.getValue());
						dadosTabela.add(new TabelaColunas(entrada.getKey(), entrada.getValue()));//*********************
					}
				}
				
				//********************FAZER EM NOVA FUNÇÃO
				colunasTabela = new ArrayList<String>();
				colunasTabela.add("Tipo de ingresso");
				for(String idCurso : cursosSelecionados) {
					colunasTabela.add(academicoService.getNomeCurso(Long.parseLong(idCurso)));
				}
				
				//********************
				
				Axis eixoX  = barraAlunosMatriculados.getAxis(AxisType.X);
				eixoX.setTickAngle(30);
				eixoX.setLabel("Cursos");
				
				Axis eixoY = barraAlunosMatriculados.getAxis(AxisType.Y);
				eixoY.setTickFormat("%d");
				eixoY.setMax(maximo + 26);
				eixoY.setMin(0);
				
				barraAlunosMatriculados.setLegendPosition("ne");
				barraAlunosMatriculados.setShowPointLabels(true);
				barraAlunosMatriculados.setMouseoverHighlight(false);
				barraAlunosMatriculados.setShowDatatip(false);
			}
			else {
				barraAlunosMatriculados = new BarChartModel();
				ChartSeries serie = new ChartSeries();
				serie.set("-", 0);
				barraAlunosMatriculados.addSeries(serie);
			}
		}

	public String alunos(){
		return "alunos.xhtml";
	}
	
	public String sexosalunos() {
		return "sexosalunos.xhtml";
	}
	
	public String TiposIngresso() {
		return "tiposingresso.xhtml";
	}
	
	public String quantidadeAlunos() {
		return "quantidadealunos.xhtml";
	}
	
	public String alunosGraduados() {
		return "alunosgraduados.xhtml";
	}
	
	public String alunosEvadidos() {
		return "alunosevadidos.xhtml";
	}
	
	public String alunosMatriculados() {
		return "alunosmatriculados.xhtml";
	}
	
	
}