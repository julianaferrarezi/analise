package br.unesp.academico.analise.jsf;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean(name = "graficos")
@ViewScoped
public class Graficos {
	private static final long serialVersionUID = 8927538487452737559L;

	private BarChartModel   graficoBarras;
	private Graduados       graduados;

	@PostConstruct
	public void init(){
	}
	
	private ChartSeries serieTipoExists(String tipo) {
		for (ChartSeries serieTipo : graficoBarras.getSeries()) {
			if (serieTipo.getLabel().equals(tipo)) {
				return serieTipo;
			}
		}
		return null;
	}
	
	public void gerarGrafico(Map<String, Number> quantidadePorCurso, Map<String, Map<String, Number>> quantidadePorTipo) {
		ChartSeries serieCursos = new ChartSeries();
		int maximo = 0;
		graduados = new Graduados();
		graficoBarras = new BarChartModel();
		
		if (quantidadePorCurso.size() == 0) {
			serieCursos.set("-", 0);
			graficoBarras.addSeries(serieCursos);
			return;
		}
		
		for (Map.Entry<String, Number> quantidade : quantidadePorCurso.entrySet()) {
			serieCursos.set(quantidade.getKey(), quantidade.getValue());
			maximo = (quantidade.getValue().intValue() > maximo) ? quantidade.getValue().intValue() : maximo;
		}
		serieCursos.setLabel("Total por curso");
		graficoBarras.addSeries(serieCursos);
		
		for (Map.Entry<String, Map<String, Number>> quantidade : quantidadePorTipo.entrySet()) {
			ChartSeries serieTipo = new ChartSeries();
			serieTipo.setLabel(quantidade.getKey());
			for (Map.Entry<String, Number> quant : quantidade.getValue().entrySet()) {
				serieTipo.set(quant.getKey(), quant.getValue());
				maximo = (quant.getValue().intValue() > maximo) ? quant.getValue().intValue() : maximo; 
			}
			graficoBarras.addSeries(serieTipo);
		}
		
		Axis eixoX  = graficoBarras.getAxis(AxisType.X);
		eixoX.setTickAngle(30);
		eixoX.setLabel("Cursos");
		
		Axis eixoY = graficoBarras.getAxis(AxisType.Y);
		eixoY.setTickFormat("%d");
		eixoY.setMax(maximo + 26);
		eixoY.setMin(0);
		
		graficoBarras.setLegendPosition("ne");
		graficoBarras.setShowPointLabels(true);
		graficoBarras.setMouseoverHighlight(false);
		graficoBarras.setShowDatatip(false);
	}
	
	public BarChartModel getGrafico(Map<String, Number> quantidadePorCurso, Map<String, Map<String, Number>> quantidadePorTipo) {
		gerarGrafico(quantidadePorCurso, quantidadePorTipo);
		return graficoBarras;
	}
}