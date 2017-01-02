package br.unesp.academico.analise.jsf;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.DonutChartModel;

import br.unesp.academico.analise.services.AcademicoService;
import br.unesp.graduacao.api.v1.beans.AlunoGraduacaoVO;

@ManagedBean(name = "graficosMB")
@ViewScoped
public class GraficosMB implements Serializable {
	private static final long serialVersionUID = 8927538487452737559L;
	
	
	private AcademicoService academicoService = null;
	private DonutChartModel donutModel1;
	private AlunoGraduacaoVO alunoVO;

	@PostConstruct
	public void init(){
		academicoService = AcademicoService.getInstance();
		

		alunoVO = academicoService.getAlunoPorEmail("phms.1998@gmail.com");
		
		createDonutModels();
	}
	
	
	
	public DonutChartModel getDonutModel1() {
		return donutModel1;
	}



	private void createDonutModels() {
        donutModel1 = initDonutModel();
        donutModel1.setTitle("Donut Chart");
        donutModel1.setLegendPosition("w");
	}
	
	private DonutChartModel initDonutModel() {
        DonutChartModel model = new DonutChartModel();
         
        Map<String, Number> circle1 = new LinkedHashMap<String, Number>();
        circle1.put("Brand 1", 150);
        circle1.put("Brand 2", 400);
        circle1.put("Brand 3", 200);
        circle1.put("Brand 4", 10);
        model.addCircle(circle1);
         
        Map<String, Number> circle2 = new LinkedHashMap<String, Number>();
        circle2.put("Brand 1", 540);
        circle2.put("Brand 2", 125);
        circle2.put("Brand 3", 702);
        circle2.put("Brand 4", 421);
        model.addCircle(circle2);
         
        Map<String, Number> circle3 = new LinkedHashMap<String, Number>();
        circle3.put("Brand 1", 40);
        circle3.put("Brand 2", 325);
        circle3.put("Brand 3", 402);
        circle3.put("Brand 4", 421);
        model.addCircle(circle3);
         
        return model;
    }
	
	public AlunoGraduacaoVO getAlunoVO() {
		return alunoVO;
	}



	public String alunos(){
		
		
		return "alunos.xhtml";
	}
	

}
