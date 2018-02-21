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
	private List<String> situacoes;
	private String situacao = null;
	//private String sexo = "Todos";
	private List<AlunoGraduacaoVO> alunos = null;
	private Date entrada;
	private Date saida;
	
	
	private Date dataEntrada; //usado em alunos graduados
	private Long anoSaida; //usado em alunos graduados
	private String sexo = "Ambos"; //usado em alunos graduados
	
	private boolean ativarEntrada;
	private boolean ativarSaida;
	
	private BarChartModel   barModel;
	private LineChartModel  lineModelMatriculados;
	private DonutChartModel donutModelTipoIngresso;
	private DonutChartModel donutModelSexosPorUnidade;
	private DonutChartModel donutModelSexosPorCurso;
	private BarChartModel barraAlunos;
	
	private BarChartModel   barraAlunosGraduados;

	@PostConstruct
	public void init(){
		academicoService = AcademicoService.getInstance();
		unidadeUniversitariaService = new UnidadeUniversitariaService();
		cursosSelecionados = new ArrayList<String>();
		tiposIngresso = new ArrayList<String>();
		tiposSelecionados = new ArrayList<String>();
		situacoes = new ArrayList<String>();
		alunos = new ArrayList<AlunoGraduacaoVO>();
		
		setAtivarEntrada(false);
		setAtivarSaida(false);
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
			for(String curso : cursosSelecionados) {
				Long idCurso = Long.parseLong(curso);
				System.out.println("Id:" + idCurso + " Ano: " + anoSaida);
				alunos.addAll(academicoService.getAlunosGraduados(idCurso, anoSaida));
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
		List<AlunoGraduacaoVO> alunos = new ArrayList<AlunoGraduacaoVO>();
		for(String idCurso : cursosSelecionados) {
			Long idLong = Long.parseLong(idCurso);
			alunos.addAll(academicoService.getAlunosGraduados(idLong, anoSaida));
		}
		for(AlunoGraduacaoVO aluno : alunos) {
			String tipo = aluno.getTipoIngresso();
			tiposIngresso.add(tipo);
		}
		return tiposSelecionados;
	}
	
	public void setTiposSelecionados (List<String> tiposSelecionados) {
		this.tiposSelecionados = tiposSelecionados;
	}
	
	public List<String> getSituacoes() {
		if(alunos != null && !alunos.isEmpty()) {
			for(AlunoGraduacaoVO a : alunos) {
				if(!situacoes.contains(a.getSituacaoAtual())) {
					situacoes.add(a.getSituacaoAtual());
				}
			}
		}
		return situacoes;
	}
	
	public String getSituacao() {
		return situacao;
	}
	
	public void setSituacao(String sit) {
		situacao = sit;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String s) {
		sexo = s;
	}
	
	public void adicionaAlunos() {
		for(String curso : cursosSelecionados) {
			alunos.addAll(academicoService.getAlunosPorCurso(Long.parseLong(curso)));
		}
	}
	
	public void todosCursos() {
		if(cursosSelecionados.contains("0")) {
			cursosSelecionados = new ArrayList<String>();
			for(CursoVO curso : cursos) {
				if(curso.getId() != 0L)
					cursosSelecionados.add(curso.getId().toString());
			}
		}
		adicionaAlunos();
	}
	
	public Date getEntrada() {
		return entrada;
	}
	
	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}
	
	public Date getSaida() {
		return saida;
	}
	
	public void setSaida(Date saida) {
		this.saida = saida;
	}
	
	public boolean isAtivarEntrada() {
		return ativarEntrada;
	}
	
	public void setAtivarEntrada(boolean ativarEntrada) {
		if(ativarEntrada) {
			Calendar hoje = Calendar.getInstance();
			hoje.set(Calendar.HOUR_OF_DAY, 0);
			entrada = hoje.getTime();
		}
		this.ativarEntrada = ativarEntrada;
	}
	
	public boolean isAtivarSaida() {
		return ativarSaida;
	}
	
	public void setAtivarSaida(boolean ativarSaida) {
		if(ativarSaida) {
			Calendar hoje = Calendar.getInstance();
			hoje.set(Calendar.HOUR_OF_DAY, 0);
			saida = hoje.getTime();
		}
		this.ativarSaida = ativarSaida;
	}
	
	
	public Date getDataEntrada() { return dataEntrada; }
	public void setDataEntrada(Date dataEntrada) { this.dataEntrada = dataEntrada; }
	
	public Long getAnoSaida() { return anoSaida; }
	public void setAnoSaida(Long anoSaida) { this.anoSaida = anoSaida; }
	
	
	public BarChartModel getBarModel() { return barModel; }
	
	public LineChartModel getLineModelMatriculados() { return lineModelMatriculados; }

	public DonutChartModel getDonutModelTipoIngresso() { return donutModelTipoIngresso; }
	
	public DonutChartModel getDonutModelSexosPorUnidade() { return donutModelSexosPorUnidade; }
	
	public DonutChartModel getDonutModelSexosPorCurso() { return donutModelSexosPorCurso; }
	
	public BarChartModel getBarraAlunosGraduados() {
		barraAlunosGraduados();
		return barraAlunosGraduados;
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
			
			if(!tipoSerie.isEmpty()) {
				for(Map.Entry<String, ChartSeries> entrada : tipoSerie.entrySet()) {
					barraAlunosGraduados.addSeries(entrada.getValue());
				}
			}
			
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
	
	
	
	public void novoBarraAlunos() {
		System.out.println("Chamou");
		barraAlunos = null;
		ChartSeries serieCursos = new ChartSeries();
		
		if(!cursosSelecionados.isEmpty()) {
			barraAlunos = new BarChartModel();
			alunos = new ArrayList<AlunoGraduacaoVO>();
			int max = 0;
			for(String curso : cursosSelecionados) {
				List<AlunoGraduacaoVO> alunos = academicoService.getAlunosPorCurso(Long.parseLong(curso));
				List<AlunoGraduacaoVO> al = new ArrayList<AlunoGraduacaoVO>();
				Number resultado = 0;
				
				for(AlunoGraduacaoVO a : alunos) {
					int ok = 1;
					if(situacao != null) {
						if(!a.getSituacaoAtual().equals(situacao))
							ok = 0;
					}
					if(!sexo.equals("Todos")) {
						if(!a.getSexo().equals(sexo))
							ok = 0;
					}
					
					Date entradaCurso = a.getDataPrimeiraMatricula();
					Date saidaCurso = a.getDataSaidaCurso();
					
					if(entrada != null && ativarEntrada == true) {
						if(entradaCurso.before(entrada))
							ok = 0;
					}
					
					if(ok != 0) {
						if(saidaCurso != null && ativarSaida == true) {
							if(entradaCurso.after(saida))
								ok = 0;
						}
					}
					
					if(ok == 1)
						al.add(a);
				}
				
				resultado = al.size();
				
				if(resultado.intValue() > max) {
					max = resultado.intValue();
				}
				
				CursoVO c = academicoService.getCurso(Long.parseLong(curso));
				serieCursos.set(c.getNome(), resultado);
				if((this.alunos).isEmpty()) {
					this.alunos = academicoService.getAlunosPorCurso(Long.parseLong(curso));
				}
				else {
					(this.alunos).addAll(academicoService.getAlunosPorCurso(Long.parseLong(curso)));
				}
			}
			
			serieCursos.setLabel("Total de alunos");
			barraAlunos.addSeries(serieCursos);
			barraAlunos.setTitle("Quantidade de alunos");
			Axis eixoX = barraAlunos.getAxis(AxisType.X);
			eixoX.setTickAngle(30);
			eixoX.setLabel("Cursos");
			Axis eixoY = barraAlunos.getAxis(AxisType.Y);
			eixoY.setTickFormat("%d");
			eixoY.setMax(max + 26);
			eixoY.setMin(0);
			barraAlunos.setLegendPosition("ne");
			barraAlunos.setShowPointLabels(true);
			barraAlunos.setMouseoverHighlight(false);
			barraAlunos.setShowDatatip(false);
			
			if(!tiposSelecionados.isEmpty()) {
				Map<String, ChartSeries> selecionados = new HashMap<String, ChartSeries>();
				
				for(AlunoGraduacaoVO a : alunos) {
					//if(tiposSelecionados.contains(a.getTipoIngresso()) && a.getSituacaoAtual().equals(situacao)) {
					int ok = 1;
					if(!tiposSelecionados.contains(a.getTipoIngresso()))
						ok = 0;
					if(situacao != null) {
						if(!situacao.equals(a.getSituacaoAtual()))
							ok = 0;
					}
					if(!sexo.equals("Todos")) {
						if(!sexo.equals(a.getSexo()))
							ok = 0;
					}
					Date entradaCurso = a.getDataPrimeiraMatricula();
					Date saidaCurso = a.getDataSaidaCurso();
					
					if(entrada != null && ativarEntrada == true) {
						if(entradaCurso.before(entrada))
							ok = 0;
					}
					
					if(ok != 0) {
						if(saidaCurso != null && ativarSaida == true) {
							if(entradaCurso.after(saida))
								ok = 0;
						}
					}
					if(ok == 1) {
						if(selecionados.containsKey(a.getTipoIngresso())) {
							ChartSeries s = selecionados.get(a.getTipoIngresso());
							CursoVO c = academicoService.getCurso(a.getIdCurso());
							Number numero = s.getData().get(c.getNome());
							numero = numero.intValue() + 1;
							
							if(numero.intValue() > max) {
								max = numero.intValue();
								eixoY.setMax(max + 26);
							}
							
							s.set(c.getNome(), numero);
							selecionados.replace(a.getTipoIngresso(), s);
						}
						else {
							ChartSeries s = new ChartSeries();
							s.setLabel(a.getTipoIngresso());
							CursoVO c = academicoService.getCurso(a.getIdCurso());
							
							for(Map.Entry<Object, Number> curso : serieCursos.getData().entrySet()) {
								s.set(curso.getKey(), 0);
							}
							
							s.set(c.getNome(), 1);
							selecionados.put(a.getTipoIngresso(), s);
						}
					}
				}
				for(Map.Entry<String, ChartSeries> series : selecionados.entrySet()) {
					barraAlunos.addSeries(series.getValue());
				}
			}
		}
		else {
			barraAlunos = new BarChartModel();
			ChartSeries serieCursos2 = new ChartSeries();
			serieCursos2.set("-", 0);
			barraAlunos.addSeries(serieCursos2);
		}
	}
	
	private void createBarModel() {	
		barModel = initBarModel();
		barModel.setTitle("GrÃ¡fico de barras / 2016");
		Axis xAxis = barModel.getAxis(AxisType.X);
		xAxis.setLabel("Unidade universitÃ¡ria");
		Axis yAxis = barModel.getAxis(AxisType.Y);
		yAxis.setLabel("Quantidade de alunos");
		yAxis.setMin(0);
		yAxis.setMax(6000);
	}
	
	private void createLineModelMatriculados() {
		lineModelMatriculados = initLineModelMatriculados();
		Axis eixoX = lineModelMatriculados.getAxis(AxisType.X);
		eixoX.setLabel("Tempo");
		eixoX.setTickFormat("%d");
		eixoX.setTickCount(4);
		Axis eixoY = lineModelMatriculados.getAxis(AxisType.Y);
		eixoY.setLabel("Quantidade");
		eixoY.setMin(0);
		eixoY.setMax(100);
	}
	
	private void createDonutModelTipoIngresso() {
		donutModelTipoIngresso = initDonutModelTipoIngresso();
		donutModelTipoIngresso.setTitle("GrÃ¡fico de tipos de ingresso");
		donutModelTipoIngresso.setLegendPosition("w");
		donutModelTipoIngresso.setShowDataLabels(true);
		donutModelTipoIngresso.setDataFormat("percent");
	}
	
	/*private void createDonutModelSexosPorUnidade() {
		donutModelSexosPorUnidade = initDonutModelSexosPorUnidade();
		donutModelSexosPorUnidade.setTitle("GrÃ¡fico de donut");
		donutModelSexosPorUnidade.setLegendPosition("e");
		donutModelSexosPorUnidade.setShowDataLabels(true);
		donutModelSexosPorUnidade.setDataFormat("value");
	}*/
	
	private void createDonutModelSexosPorCurso() {
		donutModelSexosPorCurso = initDonutModelSexosPorCurso();
		donutModelSexosPorCurso.setTitle("Masculino/feminino");
		donutModelSexosPorCurso.setLegendPosition("e");
		donutModelSexosPorCurso.setShowDataLabels(true);
		donutModelSexosPorCurso.setDataFormat("value");
	}
	
	//************************************************************************************************************
	
	private BarChartModel initBarModel() {
		BarChartModel model = new BarChartModel();
		Long idUnidade;
		int quantidade;
		
		ChartSeries serieUnidades = new ChartSeries();
		
		idUnidade = getUnidadeUniversitaria("FAAC").getId();
		quantidade = getAlunosPorUnidade(idUnidade).size();
		serieUnidades.set("FAAC", quantidade);
		
		idUnidade = getUnidadeUniversitaria("FEB").getId();
		quantidade = getAlunosPorUnidade(idUnidade).size();
		serieUnidades.set("FEB", quantidade);
		
		idUnidade = getUnidadeUniversitaria("FC").getId();
		quantidade = getAlunosPorUnidade(idUnidade).size();
		serieUnidades.set("FC", quantidade);
		
		
		
		model.addSeries(serieUnidades);
		
		return model;
	}
	
	private LineChartModel initLineModelMatriculados() {
		LineChartModel model = new LineChartModel();
		ChartSeries serieAlunos = new ChartSeries();
		
		Map<Integer, Number> anos = new LinkedHashMap<Integer, Number>();
		int anoEntrada;
		int anoSaida;
		
		Calendar primeiraMatricula = new GregorianCalendar();
		Calendar saidaCurso = new GregorianCalendar();
		List<AlunoGraduacaoVO> alunos = academicoService.getAlunosPorCurso(16L);
		
		for(AlunoGraduacaoVO aluno : alunos) {
			if(aluno.getSituacaoAtual().equals("Aluno Regularmente Matriculado")) {
				primeiraMatricula.setTime(aluno.getDataPrimeiraMatricula());
				anoEntrada = primeiraMatricula.get(Calendar.YEAR);
				
				saidaCurso.setTime(aluno.getDataPrimeiraMatricula());
				anoSaida = saidaCurso.get(Calendar.YEAR);
				
				if((anoEntrada >= 2010)&&(anoSaida <= 2013)) {
					if(anos.containsKey(anoEntrada)) {
						anos.put(anoEntrada, anos.get(anoEntrada).intValue() + 1);
					}
					else {
						anos.put(anoEntrada, 1);
					}
				}
			}
		}
		
		for(Map.Entry<Integer, Number> ano : anos.entrySet()) {
			serieAlunos.set(ano.getKey(), ano.getValue());
		}
		
		model.addSeries(serieAlunos);
		return model;
	}
	
	private DonutChartModel initDonutModelTipoIngresso() {
		DonutChartModel model = new DonutChartModel();
		Map<String, Number> serieTipoIngresso = new LinkedHashMap<String, Number>();
		Number i = 0;

		List<AlunoGraduacaoVO> alunosUnidade = academicoService.getAlunosPorCurso(16L);
		for(AlunoGraduacaoVO a : alunosUnidade) {
			serieTipoIngresso.put(a.getTipoIngresso(), 0);
		}
		for(AlunoGraduacaoVO a : alunosUnidade) {
			i = serieTipoIngresso.get(a.getTipoIngresso());
			i = i.intValue() + 1;
			serieTipoIngresso.put(a.getTipoIngresso(), i);
		}
		model.addCircle(serieTipoIngresso);
		return model;
	}
	
	/*private DonutChartModel initDonutModelSexosPorUnidade() {
		DonutChartModel model = new DonutChartModel();
		int m = 0, f = 0;
		
		Map<String, Number> circulo = new LinkedHashMap<String, Number>();
		
		for(String sigla : uSelSexos) {
			List<CursoVO> cursosUnidade = academicoService.getCursosPorUnidade(sigla);
			for(CursoVO c : cursosUnidade) {
				if(Arrays.asList(cSelSexos).contains(c.getNome())) {
					Long idCurso = c.getId();
					List<AlunoGraduacaoVO> alunos = academicoService.getAlunosPorCurso(idCurso);
					for(AlunoGraduacaoVO a : alunos) {
						if(a.getSexo().equals("Masculino"))
							m++;
						else
							f++;
					}
				}
			}
		}
		circulo.put("Masculino", m);
		circulo.put("Feminino", f);
		model.addCircle(circulo);
		return model;
	}*/
	
	private DonutChartModel initDonutModelSexosPorCurso() {
		DonutChartModel model = new DonutChartModel();
		List<CursoVO> cursosSelecionados;
		int m = 0;
		int f = 0;
		
		Map<String, Number> circulo = new LinkedHashMap<String, Number>();
		
		/*for(CursoVO curso : cursosSelecionados) {
			List<AlunoGraduacaoVO> alunos = academicoService.getAlunosPorCurso(curso.getId());
		}
		
		for(AlunoGraduacaoVO aluno : alunos) {
			if(aluno.getSexo().equals("Masculino")) {
				m++;
			}
			else {
				f++;
			}
		}*/
		circulo.put("Masculino", m);
		circulo.put("Feminino", f);
		
		model.addCircle(circulo);
		
		return model;
		
	}
	
	//************************************************************************************************************
	
	/*public void verGraficoSexosPorUnidade(ActionEvent actionEvent) {
		createDonutModelSexosPorUnidade();
	}*/
	
	public void verGraficoSexosPorCurso(ActionEvent actionEvent) {
		createDonutModelSexosPorCurso();
	}
	
	public void verGraficoTipoIngresso(ActionEvent actionEvent) {
		createDonutModelTipoIngresso();
	}
	
	public void verGraficoMatriculados(ActionEvent actionEvent) {
		createLineModelMatriculados();
	}
	
	//************************************************************************************************************

	public List<AlunoGraduacaoVO> getAlunosPorUnidade(Long idUnidade) { //Retorna lista de alunos de unidade
		List<AlunoGraduacaoVO> unidadeAlunos = academicoService.getAlunosPorUnidade(idUnidade, 2016, 2);
		return unidadeAlunos;
	}
	
	public UnidadeUniversitariaVO getUnidadeUniversitaria(String sigla) { //Retorna unidade
		UnidadeUniversitariaVO unidade = academicoService.getUnidade(sigla);
		return unidade;
	}
	
	public List<CursoVO> getCursosPorUnidade(String sigla) {
		List<CursoVO> cursos = academicoService.getCursosPorUnidade(sigla);
		return cursos;
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
	
	/*public AlunoGraduacaoVO getAlunoVO() {
	String email = "phms.1998@gmail.com";
	alunoVO = academicoService.getAlunoPorEmail(email);
	return alunoVO;
}*/

/*	public AlunoGraduacaoVO getAlunoVO() {
	if (alunoVO == null) {
		//String email = "phms.1998@gmail.com";
		academicoService = new AcademicoService();
		//alunoVO = academicoService.getAlunoPorEmail("phms.1998@gmail.com");
		
		alunoVO.setDataPrimeiraMatricula(dataPrimeiraMatricula);
		
		alunoVO.setDataPrimeiraMatricula(new Date());
		
	Calendar c = new GregorianCalendar();
	c.set(GregorianCalendar.MONTH, 7);
	alunoVO.setDataPrimeiraMatricula(c.getTime());
		
		listaAlunos = new ArrayList<AlunoGraduacaoVO>();
		alunoVO = new AlunoGraduacaoVO();
		alunoVO.setNome("AntÃ´nio");
		alunoVO.setEstadoCivil("casado");
		alunoVO.setMatriculado(true);
		listaAlunos.add(alunoVO);
		
		alunoVO.setNome("Carlos");
		alunoVO.setEstadoCivil("casado");
		alunoVO.setMatriculado(true);
		listaAlunos.add(alunoVO);
		
		alunoVO.setNome("JosÃ©");
		alunoVO.setEstadoCivil("solteiro");
		alunoVO.setMatriculado(true);
		listaAlunos.add(alunoVO);
		
		alunoVO.setNome("Rafaela");
		alunoVO.setEstadoCivil("solteiro");
		alunoVO.setMatriculado(false);
		listaAlunos.add(alunoVO);
	}
	
	return alunoVO;
}*/
	
}