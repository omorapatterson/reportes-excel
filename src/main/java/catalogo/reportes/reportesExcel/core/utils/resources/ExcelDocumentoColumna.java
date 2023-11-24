package catalogo.reportes.reportesExcel.core.utils.resources;

import java.util.ArrayList;
import java.util.List;

public class ExcelDocumentoColumna {
    private int posicion;
    private String label;

    public ExcelDocumentoColumna(int posicion, String nombreCabecera) {
        this.posicion = posicion;
        this.label = nombreCabecera;
    }

    public static List<ExcelDocumentoColumna> cabecerasExcelParaReporteEmpresa(){
        List<ExcelDocumentoColumna> excelDocumentoColumnas = new ArrayList<>();
        excelDocumentoColumnas.add(new ExcelDocumentoColumna(0, "RUT"));
        excelDocumentoColumnas.add(new ExcelDocumentoColumna(1, "GLN"));
        excelDocumentoColumnas.add(new ExcelDocumentoColumna(2, "NOMBRE"));
        excelDocumentoColumnas.add(new ExcelDocumentoColumna(3, "FECHA ÚLTIMA ACTUALIZACIÓN"));
        excelDocumentoColumnas.add(new ExcelDocumentoColumna(4, "CANT PROD CON GTIN13"));
        excelDocumentoColumnas.add(new ExcelDocumentoColumna(5, "CANT PROD CON GTIN13"));
        return excelDocumentoColumnas;
    }



    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
