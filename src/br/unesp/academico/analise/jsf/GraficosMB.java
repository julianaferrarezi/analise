package br.unesp.academico.analise.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.unesp.academico.analise.services.AcademicoService;
import br.unesp.graduacao.api.v2.beans.AlunoGraduacaoVO;

@ManagedBean(name = "graficosMB")
@ViewScoped
public class GraficosMB implements Serializable {
	private static final long serialVersionUID = 8927538487452737559L;
	
	
	private AcademicoService academicoService = null;
	private AlunoGraduacaoVO alunoVO;
	private List<AlunoGraduacaoVO> listaAlunos;
	private BarChartModel barModel;
	private LineChartModel lineModel;
	private String tipo;
	private List<String> tipos;
	private String eixoX;
	private List<String> eixosX;
	private String eixoY;
	private List<String> eixosY;
	private int anoComeco;
	private int anoFim;
	

	@PostConstruct
	public void init(){
		System.out.println(getAlunoVO().getNome());
		
		academicoService = AcademicoService.getInstance();
		createBarModel(getAlunoVO().getNome(),"Idade");
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
		this.eixoX = "Nome";
	}
	
	public String getTipo(){return tipo;}
	public void setTipo(String tipo) {this.tipo = tipo;}
	public List<String> getTipos() {return tipos;}
	
	public String getEixoX() {return eixoX;}
	public void setEixoX(String eixoX) {this.eixoX = eixoX;}
	public List<String> getEixosX() {return eixosX;}
	
	public String getEixoY() {return eixoY;}
	public void setEixoY(String eixoY) {this.eixoY = eixoY;}
	public List<String> getEixosY() {
		if(this.eixoX.equals("Anos")) {
			eixosY = new ArrayList<String>();
			eixosY.add("Idade");
			eixosY.add("Quantidade de matriculados");
		}
		return eixosY;
	}
	
	public int getanoComeco() {return anoComeco;}
	public void setanoComeco(int anoComeco) {this.anoComeco = anoComeco;}
	
	public int getanoFim() {return anoFim;}
	public void setanoFim(int anoFim) {this.anoFim = anoFim;}
	
	public BarChartModel getBarModel() {return barModel;}
	
	public LineChartModel getLineModel() {return lineModel;}
	
	private void createBarModel(String eixoX, String eixoY) {
		barModel = initBarModel(eixoX,eixoY);
		barModel.setTitle("Gráfico de barras");
		Axis xAxis = barModel.getAxis(AxisType.X);
		xAxis.setLabel(eixoX);
		Axis yAxis = barModel.getAxis(AxisType.Y);
		yAxis.setLabel(eixoY);
		
		if(eixoY.equals("Idade")) {
			yAxis.setMin(18);
			yAxis.setMax(52);
		}
		if(eixoY.equals("Quantidade de matriculados")) {
			yAxis.setMin(100);
			yAxis.setMax(500);
		}
	}
	
	private void createLineModel(String eixoX, String eixoY) {
		lineModel = initLineModel();
		lineModel.setTitle("Gráfico de linhas");
		Axis xAxis = lineModel.getAxis(AxisType.X);
		xAxis.setLabel(eixoX);
		Axis yAxis = lineModel.getAxis(AxisType.Y);
		yAxis.setLabel(eixoY);
		yAxis.setMin(10);
		yAxis.setMax(50);
	}
	
	private BarChartModel initBarModel(String eixoX, String eixoY) {
		BarChartModel model = new BarChartModel();
		
		if(eixoX.equals("Nome")) {
			ChartSeries serieNome = new ChartSeries();
			serieNome.set("Antônio", 10);
			serieNome.set("Carlos", 10);
			serieNome.set("Edson", 20);
			model.addSeries(serieNome);
		}
		if(eixoX.equals("Anos")) {
			ChartSeries serieAnos = new ChartSeries();
			serieAnos.set("2016",5);
			serieAnos.set("2017", 1);
			model.addSeries(serieAnos);
		}
		if(eixoX.equals("Estado civil")) {
			ChartSeries serieEstadoCivil = new ChartSeries();
			serieEstadoCivil.set("Casado", 9);
			serieEstadoCivil.set("Solteiro", 1);
			model.addSeries(serieEstadoCivil);
		}
		return model;
	}
	
	private LineChartModel initLineModel() {
		LineChartModel model = new LineChartModel();
		ChartSeries serie = new ChartSeries();
		serie.set("2016", 1);
		model.addSeries(serie);
		return model;
	}
	
	public void verGrafico(ActionEvent actionEvent) {
		createBarModel(this.eixoX, this.eixoY);
		createLineModel(this.eixoX, this.eixoY);
	}
	
	public int getComecoProcura() {
		return anoComeco;
	}
	
	public void setComecoProcura(int anoComeco) {
		this.anoComeco =  anoComeco;
	}
	
	public int getFimProcura() {
		return anoFim;
	}
	
	public void setFimProcura(int anoFim) {
		this.anoFim = anoFim;
	}
	
	public AlunoGraduacaoVO getAlunoVO() {
		if (alunoVO == null) {
			//String email = "phms.1998@gmail.com";
			academicoService = new AcademicoService();
			alunoVO = academicoService.getAlunoPorEmail("phms.1998@gmail.com");
			
//			alunoVO.setDataPrimeiraMatricula(dataPrimeiraMatricula);
			
//			alunoVO.setDataPrimeiraMatricula(new Date());
			
//		Calendar c = new GregorianCalendar();
//		c.set(GregorianCalendar.MONTH, 7);
//		alunoVO.setDataPrimeiraMatricula(c.getTime());
			
			listaAlunos = new ArrayList<AlunoGraduacaoVO>();
			//alunoVO = new AlunoGraduacaoVO();
			//alunoVO.setNome("Antônio");
			//alunoVO.setEstadoCivil("casado");
			//alunoVO.setMatriculado(true);
			//listaAlunos.add(alunoVO);
			
			//alunoVO.setNome("Carlos");
			//alunoVO.setEstadoCivil("casado");
			//alunoVO.setMatriculado(true);
			//listaAlunos.add(alunoVO);
			
			//alunoVO.setNome("José");
			//alunoVO.setEstadoCivil("solteiro");
			//alunoVO.setMatriculado(true);
			//listaAlunos.add(alunoVO);
			
			//alunoVO.setNome("Rafaela");
			//alunoVO.setEstadoCivil("solteiro");
			//alunoVO.setMatriculado(false);
			//listaAlunos.add(alunoVO);
		}
		
		return alunoVO;
	}



	public String alunos(){
		
		
		return "alunos.xhtml";
	}
	

}