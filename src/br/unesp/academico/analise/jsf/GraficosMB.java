package br.unesp.academico.analise.jsf;

//import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.chart.LineChartModel;

import br.unesp.academico.analise.services.AcademicoService;
import br.unesp.graduacao.api.v2.beans.AlunoGraduacaoVO;
import br.unesp.graduacao.api.v2.beans.CursoVO;
import br.unesp.graduacao.api.v2.beans.UnidadeUniversitariaVO;

@ManagedBean(name = "graficosMB")
@ViewScoped
public class GraficosMB {
	private static final long serialVersionUID = 8927538487452737559L;
	
	
	//Variaveis de conexao ao AcademicoService
	private AcademicoService academicoService = null;
	
	//Variaveis de dados
	private String[] cSelSexos = {"Sistemas de Informação"};
	private String[] uSelSexos = {"FC"};
	private String[] cSelTipoIngresso = {"Sistemas de Informação"};
	private String[] uSelTipoIngresso = {"FC"};
	private List<String> lcSexos;
	private List<String> lcTipoIngresso;
	private List<String> luSexos;
	private List<String> luTipoIngresso;
	
	
	private String[] sexosUnidadesSelecionadas;
	
	private List<String> unidadesUniversitarias;
	private String unidadeUniversitaria = "FC";
	
	private List<CursoVO> cursos;
	private List<String> cursosSelecionados;
	
	
	
	//Variaveis graficas
	private BarChartModel barModel;
	private LineChartModel lineModelMatriculados;
	private DonutChartModel donutModelTipoIngresso;
	private DonutChartModel donutModelSexosPorUnidade;
	private DonutChartModel donutModelSexosPorCurso;
	

	@PostConstruct
	public void init(){

		//System.out.println(getAlunoVO().getNome());
		
		academicoService = AcademicoService.getInstance();
		createBarModel();
		createDonutModelSexosPorUnidade();
		createDonutModelTipoIngresso();
		createLineModelMatriculados();
		createDonutModelSexosPorCurso();
		
		/*String nome = getAlunoVO().getNome();
		System.out.println(nome);
		
		createBarModel(nome,"Idade");
		createLineModel("", "");
		tipos = new ArrayList<String>();
		tipos.add("Barras");
		tipos.add("Setores");
		tipos.add("Linhas");
		eixosX = new ArrayList<String>();
		eixosX.add("Nome");
		eixosX.add("Anos");
		eixosX.add("Estado civil");
		//eixosY = new ArrayList<String>();
		//eixosY.add("Idade");
		//eixosY.add("Quantidade de matriculados");
		this.eixoX = "Nome";*/
	}
	
	//************************************************************************************************************
	
	public List<String> getUnidadesUniversitarias() {
		unidadesUniversitarias = new ArrayList<String>();
		unidadesUniversitarias.add("FAAC");
		unidadesUniversitarias.add("FEB");
		unidadesUniversitarias.add("FC");
		return (unidadesUniversitarias);
	}
	
	public String getUnidadeUniversitaria() {
		return unidadeUniversitaria;
	}
	
	public void setUnidadeUniversitaria(String unidade) {
		unidadeUniversitaria = unidade;
	}
	
	public List<CursoVO> getCursos() {
		cursos = getCursosPorUnidade(unidadeUniversitaria);
		return (cursos);
	}
	
	public void setCursos(List<CursoVO> cursos) {
		this.cursos = cursos;
	}
	
	public List<String> getCursosSelecionados() {
		return (cursosSelecionados);
	}
	
	public void setCursosSelecionadso(List<String> cursosSelecionados) {
		this.cursosSelecionados = cursosSelecionados;
	}
	
	
	
	public String[] getcSelSexos() { return cSelSexos; }
	public void setcSelSexos(String[] cSelSexos) { this.cSelSexos = cSelSexos; }
	
	public String[] getuSelSexos() { return uSelSexos; }
	public void setuSelSexos(String[] uSelSexos) { this.uSelSexos = uSelSexos; }
	
	public List<String> getlcSexos() {
		lcSexos = new ArrayList<String>();
		List<CursoVO> cursosUnidade = null;
		if(Arrays.asList(uSelSexos).contains("FAAC")) {
			cursosUnidade = academicoService.getCursosPorUnidade("FAAC");
			for(CursoVO c : cursosUnidade) {
				lcSexos.add(c.getNome());
			}
		}
		if(Arrays.asList(uSelSexos).contains("FEB")) {
			cursosUnidade = academicoService.getCursosPorUnidade("FEB");
			for(CursoVO c : cursosUnidade) {
				lcSexos.add(c.getNome());
			}
		}
		if(Arrays.asList(uSelSexos).contains("FC")) {
			cursosUnidade = academicoService.getCursosPorUnidade("FC");
			for(CursoVO c : cursosUnidade) {
				lcSexos.add(c.getNome());
			}
		}
		return lcSexos;
	}
	
	public List<String> getluSexos() {
		luSexos = new ArrayList<String>();
		luSexos.add("FAAC");
		luSexos.add("FEB");
		luSexos.add("FC");
		return luSexos;
	}

	
	
	//************************************************************************************************************
	
	public BarChartModel getBarModel() {return barModel;}
	
	public LineChartModel getLineModelMatriculados() {return lineModelMatriculados;}

	public DonutChartModel getDonutModelTipoIngresso() {return donutModelTipoIngresso;}
	
	public DonutChartModel getDonutModelSexosPorUnidade() {return donutModelSexosPorUnidade;}
	
	public DonutChartModel getDonutModelSexosPorCurso() {return donutModelSexosPorCurso;}
	
	//************************************************************************************************************

	private void createBarModel() {	
		barModel = initBarModel();
		barModel.setTitle("Gráfico de barras / 2016");
	/*private void createBarModel(String eixoX, String eixoY) {
		barModel = initBarModel(eixoX,eixoY);
		barModel.setTitle("Grï¿½fico de barras");*/
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
	
	private void createDonutModelSexosPorUnidade() {
		donutModelSexosPorUnidade = initDonutModelSexosPorUnidade();
		donutModelSexosPorUnidade.setTitle("Gráfico de donut");
		donutModelSexosPorUnidade.setLegendPosition("e");
		donutModelSexosPorUnidade.setShowDataLabels(true);
		donutModelSexosPorUnidade.setDataFormat("value");
	}
	
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
	/*private void createLineModel(String eixoX, String eixoY) {
		lineModel = initLineModel();
		lineModel.setTitle("Grï¿½fico de linhas");
		Axis xAxis = lineModel.getAxis(AxisType.X);
		xAxis.setLabel(eixoX);
		Axis yAxis = lineModel.getAxis(AxisType.Y);
		yAxis.setLabel(eixoY);
		yAxis.setMin(10);
		yAxis.setMax(50);*/
	}
	
	private DonutChartModel initDonutModelSexosPorUnidade() {
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
		/*if(eixoX.equals("Nome")) {
			ChartSeries serieNome = new ChartSeries();
			serieNome.set("Antï¿½nio", 10);
			serieNome.set("Carlos", 10);
			serieNome.set("Edson", 20);
			model.addSeries(serieNome);*/
		}
		circulo.put("Masculino", m);
		circulo.put("Feminino", f);
		model.addCircle(circulo);
		return model;
	}
	
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
	
	public void verGraficoSexosPorUnidade(ActionEvent actionEvent) {
		createDonutModelSexosPorUnidade();
	}
	
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
		List<AlunoGraduacaoVO> unidadeAlunos = academicoService.getAlunosPorUnidade(idUnidade);
		return unidadeAlunos;
	}
	
	public UnidadeUniversitariaVO getUnidadeUniversitaria(String sigla) { //Retorna unidade
		UnidadeUniversitariaVO unidade = academicoService.getUnidade(sigla);
		return unidade;
	}
	
	public List<CursoVO> getCursosPorUnidade(String sigla) {
		List<CursoVO> cursos = academicoService.getCursosPorUnidade(sigla);
		return cursos;
	/*public AlunoGraduacaoVO getAlunoVO() {
		if (alunoVO == null) {
			//String email = "phms.1998@gmail.com";
//			academicoService = new AcademicoService();
			alunoVO = academicoService.getAlunoPorEmail("phms.1998@gmail.com");
			
//			alunoVO.setDataPrimeiraMatricula(dataPrimeiraMatricula);
			
//			alunoVO.setDataPrimeiraMatricula(new Date());
			
//		Calendar c = new GregorianCalendar();
//		c.set(GregorianCalendar.MONTH, 7);
//		alunoVO.setDataPrimeiraMatricula(c.getTime());
			
			listaAlunos = new ArrayList<AlunoGraduacaoVO>();
			//alunoVO = new AlunoGraduacaoVO();
			//alunoVO.setNome("Antï¿½nio");
			//alunoVO.setEstadoCivil("casado");
			//alunoVO.setMatriculado(true);
			//listaAlunos.add(alunoVO);
			
			//alunoVO.setNome("Carlos");
			//alunoVO.setEstadoCivil("casado");
			//alunoVO.setMatriculado(true);
			//listaAlunos.add(alunoVO);
			
			//alunoVO.setNome("Josï¿½");
			//alunoVO.setEstadoCivil("solteiro");
			//alunoVO.setMatriculado(true);
			//listaAlunos.add(alunoVO);
			
			//alunoVO.setNome("Rafaela");
			//alunoVO.setEstadoCivil("solteiro");
			//alunoVO.setMatriculado(false);
			//listaAlunos.add(alunoVO);
		}
		
		return alunoVO;*/
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