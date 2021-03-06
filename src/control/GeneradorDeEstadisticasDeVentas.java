package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import dao.CarritoDao;
import modelo.Carrito;

public class GeneradorDeEstadisticasDeVentas {
	
ArrayList <Carrito> historial = new ArrayList<Carrito>();
    
    public GeneradorDeEstadisticasDeVentas (ArrayList <Carrito> historial){
        this.historial = historial;
    }
    
    public boolean  graficoNumVentasSemana (String ruta){
        return saveJPG(crearModeloSem(numVentasSemana()), ruta, "Compras por d�a de la semana", "D�a", "Compras");
    }
    
    public boolean graficoGanadoPorDia (String ruta){
        return saveJPG(crearModeloSem(mediaVendidoDiaSem()), ruta, "Media por compra en los d�as de la semana",
                "D�a", "Media ($)");
    }
    
    public boolean porcentajeProductosVendidosMes (String ruta){
        return saveJPG(crearModeloMes(porcentajeProductosVendidosMes()), ruta,
                "Porcentaje de productos vendidos por mes", "Mes", "Porcentaje (%)");
    }
    
    private int [] numVentasSemana (){
        int [] numVentasSemana = new int [8];
        Calendar cal = Calendar.getInstance(Tools.getLocale());
        for (int i = 0; i < historial.size(); i++){
            String [] fecha = historial.get(i).getFecha().split("-");
            cal.set(Integer.valueOf(fecha[0]), Integer.valueOf(fecha[1]), Integer.valueOf(fecha[2]));
            numVentasSemana[cal.get(Calendar.DAY_OF_WEEK)] += 1;
        }
        return numVentasSemana;
    }
    
    private double [] mediaVendidoDiaSem (){
        double [] priceDiasSemana = new double [8];
        int [] comprasPerDay = new int [8];
        Calendar cal = Calendar.getInstance(Tools.getLocale());
        for (int i = 0; i < historial.size(); i++){
            String [] fecha = historial.get(i).getFecha().split("-");
            cal.set(Integer.valueOf(fecha[0]), Integer.valueOf(fecha[1]), Integer.valueOf(fecha[2]));
            priceDiasSemana [cal.get(Calendar.DAY_OF_WEEK)] += historial.get(i).getPrecio();
            comprasPerDay[cal.get(Calendar.DAY_OF_WEEK)]++;
        }
        
        for (int i = 1; i <=7; i++){
            if (comprasPerDay[i] != 0){
                priceDiasSemana [i] = priceDiasSemana [i] / (double)comprasPerDay [i];
            }
        }
        return priceDiasSemana;
    }      
    
    private double [] porcentajeProductosVendidosMes (){
        int [] productosEnMes = new int [12];
        double [] resultados = new double [12];
        int productosVendidos = 0;
        int productosCompra = 0;
        Calendar cal = Calendar.getInstance(Tools.getLocale());
        for (int i = 0; i < historial.size(); i++){
            productosCompra = CarritoDao.getInstance().getDetailsCartRecord(historial.get(i).getCodigo()).size();
            productosVendidos += productosCompra;
            String [] fecha = historial.get(i).getFecha().split("-");
            cal.set(Integer.valueOf(fecha[0]), Integer.valueOf(fecha[1]), Integer.valueOf(fecha[2]));
            productosEnMes [cal.get(Calendar.MONTH)] += productosCompra;
        }
        for (int i = 0; i <=11; i++){
            resultados [i] = ((double)productosEnMes[i] / (double)productosVendidos) * 100;
        }
        
        return resultados;
    }
    
    private DefaultCategoryDataset crearModeloMes (double [] datos){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(datos[1], "Enero", "Ene");
        dataset.setValue(datos[2], "Febrero", "Feb");
        dataset.setValue(datos[3], "Marzo", "Mar");
        dataset.setValue(datos[4], "Abril", "Abr");
        dataset.setValue(datos[5], "Mayo", "May");
        dataset.setValue(datos[6], "Junio", "Jun");
        dataset.setValue(datos[7], "Julio", "Jul");
        dataset.setValue(datos[8], "Agosto", "Ago");
        dataset.setValue(datos[9], "Septiembre", "Sept");
        dataset.setValue(datos[10], "Octubre", "Oct");
        dataset.setValue(datos[11], "Noviembre", "Nov");
        dataset.setValue(datos[0], "Diciembre", "Dic");
        
        return dataset;
    }
    
    private DefaultCategoryDataset crearModeloSem (int [] datos){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(datos[5], "Lunes", "L");
        dataset.setValue(datos[6], "Martes", "M");
        dataset.setValue(datos[7], "Mi�rcoles", "X");
        dataset.setValue(datos[1], "Jueves", "J");
        dataset.setValue(datos[2], "Viernes", "V");
        dataset.setValue(datos[3], "S�bado", "S");
        dataset.setValue(datos[4], "Domingo", "D");
        return dataset;
    }
    
    private DefaultCategoryDataset crearModeloSem (double [] datos){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(datos[5], "Lunes", "L");
        dataset.setValue(datos[6], "Martes", "M");
        dataset.setValue(datos[7], "Mi�rcoles", "X");
        dataset.setValue(datos[1], "Jueves", "J");
        dataset.setValue(datos[2], "Viernes", "V");
        dataset.setValue(datos[3], "S�bado", "S");
        dataset.setValue(datos[4], "Domingo", "D");
        
        return dataset;
    }
    
    private boolean saveJPG (DefaultCategoryDataset dataset, String ruta, String titulo,
            String ejeX, String ejeY){
        try {
            JFreeChart chart = ChartFactory.createBarChart3D(titulo, ejeX, ejeY, dataset,
                    PlotOrientation.VERTICAL, true, true, false);
            ChartUtilities.saveChartAsJPEG(new File(ruta), chart, 500, 300);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(GeneradorDeEstadisticasDeVentas.class.getName()).log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

}
