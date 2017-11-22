package br.unesp.academico.analise.jsf;

import java.text.SimpleDateFormat;
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

import org.primefaces.event.SelectEvent;
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
	private String sexo = "Todos";
	private List<AlunoGraduacaoVO> alunos = null;
	private Date entrada;
	private Date saida;
	private boolean ativarEntrada;
	private boolean ativarSaida;
	
	private BarChartModel   barModel;
	private LineChartModel  lineModelMatriculados;
	private DonutChartModel donutModelTipoIngresso;
	private DonutChartModel donutModelSexosPorUnidade;
	private DonutChartModel donutModelSexosPorCurso;
	
	private BarChartModel   barraAlunos;

	@PostConstruct
	public void init(){
		academicoService = AcademicoService.getInstance();
		unidadeUniversitariaService = new UnidadeUniversitariaService();
		cursosSelecionados = new ArrayList<String>();
		tiposSelecionados = new ArrayList<String>();
		situacoes = new ArrayList<String>();
		alunos = new ArrayList<AlunoGraduacaoVO>();
		
		setAtivarEntrada(false);
		setAtivarSaida(false);
		
		
	}
	
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
	
	public String getUnidadeUniversitaria() {
		return unidadeUniversitaria;
	}
	
	public void setUnidadeUniversitaria(String unidade) {
		unidadeUniversitaria = unidade;
	}
	
	public List<CursoVO> getCursos() {
		if(unidadeUniversitaria != null && !unidadeUniversitaria.isEmpty()) {
			cursos = new ArrayList<CursoVO>();
			CursoVO curso = new CursoVO();
			curso.setId(0L);
			curso.setNome("Todos");
			cursos.add(curso);
			cursos.addAll(academicoService.getCursosPorUnidade(unidadeUniversitaria));
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
	
	public List<String> getTiposIngresso() {
		tiposIngresso = new ArrayList<String>();
		if(alunos != null && !alunos.isEmpty()) {
			for(AlunoGraduacaoVO a : alunos) {
				if(!tiposIngresso.contains(a.getTipoIngresso())) {
					tiposIngresso.add(a.getTipoIngresso());
				}
			}
		}
		return tiposIngresso;
	}
	
	public List<String> getTiposSelecionados() {
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
		System.out.println(entrada);
	}
	
	public Date getSaida() {
		return saida;
	}
	
	public void setSaida(Date saida) {
		this.saida = saida;
		System.out.println(saida);
	}
	
	public boolean isAtivarEntrada() {
		return ativarEntrada;
	}
	
	public void setAtivarEntrada(boolean ativarEntrada) {
		this.ativarEntrada = ativarEntrada;
	}
	
	public boolean isAtivarSaida() {
		return ativarSaida;
	}
	
	public void setAtivarSaida(boolean ativarSaida) {
		this.ativarSaida = ativarSaida;
	}
	
	
	/*public void formatarData() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.format(entrada);
	}*/
	
	
	public BarChartModel getBarModel() { return barModel; }
	
	public LineChartModel getLineModelMatriculados() { return lineModelMatriculados; }

	public DonutChartModel getDonutModelTipoIngresso() { return donutModelTipoIngresso; }
	
	public DonutChartModel getDonutModelSexosPorUnidade() { return donutModelSexosPorUnidade; }
	
	public DonutChartModel getDonutModelSexosPorCurso() { return donutModelSexosPorCurso; }
	
	public BarChartModel getBarraAlunos() {
		novoBarraAlunos();
		return barraAlunos;
	}
	
	public void novoBarraAlunos() {
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
		barModel.setTitle("Gráfico de barras / 2016");
		Axis xAxis = barModel.getAxis(AxisType.X);
		xAxis.setLabel("Unidade universitária");
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
		donutModelTipoIngresso.setTitle("Gráfico de tipos de ingresso");
		donutModelTipoIngresso.setLegendPosition("w");
		donutModelTipoIngresso.setShowDataLabels(true);
		donutModelTipoIngresso.setDataFormat("percent");
	}
	
	/*private void createDonutModelSexosPorUnidade() {
		donutModelSexosPorUnidade = initDonutModelSexosPorUnidade();
		donutModelSexosPorUnidade.setTitle("Gráfico de donut");
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
		alunoVO.setNome("Antônio");
		alunoVO.setEstadoCivil("casado");
		alunoVO.setMatriculado(true);
		listaAlunos.add(alunoVO);
		
		alunoVO.setNome("Carlos");
		alunoVO.setEstadoCivil("casado");
		alunoVO.setMatriculado(true);
		listaAlunos.add(alunoVO);
		
		alunoVO.setNome("José");
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