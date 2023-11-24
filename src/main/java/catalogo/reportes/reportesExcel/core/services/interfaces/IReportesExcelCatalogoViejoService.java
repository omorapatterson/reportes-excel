package catalogo.reportes.reportesExcel.core.services.interfaces;

public interface IReportesExcelCatalogoViejoService {

    String reporteEmpresas();

    String reporteEmpresasPorGln();

    String reporteEmpresasConProductosVisibles(String rut);

}
