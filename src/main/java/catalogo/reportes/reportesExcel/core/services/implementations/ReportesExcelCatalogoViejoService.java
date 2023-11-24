package catalogo.reportes.reportesExcel.core.services.implementations;

import catalogo.reportes.ReportesConfiguration;

import catalogo.reportes.core.afiliados.afiliadosServices.interfaces.IAfiliadosEmpresaService;
import catalogo.reportes.core.catalogo.db.EmpresasDAO;
import catalogo.reportes.core.catalogo.db.UbicacionesDAO;
import common.rondanet.catalogo.core.entity.Ubicacion;
import catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces.ICatalogoViejoProductosService;
import catalogo.reportes.reportesExcel.core.services.interfaces.IReportesExcelCatalogoViejoService;

import catalogo.reportes.reportesExcel.core.utils.excelUtility.ExcelUtilityReporteEmpresas;
import common.rondanet.clasico.core.afiliados.models.Empresa;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReportesExcelCatalogoViejoService implements IReportesExcelCatalogoViejoService {

	@Autowired
	ICatalogoViejoProductosService catalogoViejoProductosService;

	@Autowired
	IAfiliadosEmpresaService afiliadosEmpresaService;

	@Autowired
	ExcelUtilityReporteEmpresas excelUtilityReporteEmpresas;

	@Autowired
	EmpresasDAO empresasDAO;

	@Autowired
	UbicacionesDAO ubicacionesDAO;

	public ReportesExcelCatalogoViejoService(ReportesConfiguration configuration) {

	}

	@Override
	public String reporteEmpresas(){
		List<BigDecimal> empresasConProductos = this.catalogoViejoProductosService.findAllGroupByGln();
		XSSFWorkbook xSSFWorkbook = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
		for (BigDecimal glnBigDecimal : empresasConProductos) {
			String glnEmpresa = String.valueOf(glnBigDecimal);
			Empresa empresa = afiliadosEmpresaService.obtenerEmpresaPorGln(glnEmpresa);
			int totalDeProductosConGTIN13 = this.catalogoViejoProductosService.totalDeProductosConGTIN13(glnEmpresa);
			int totalDeProductosConGTIN14 = this.catalogoViejoProductosService.totalDeProductosConGTIN14(glnEmpresa);
			Date ultimaFechaDeActualizacion = this.catalogoViejoProductosService.ultimaFechaDeActualizacion(glnEmpresa);
			xSSFWorkbook = excelUtilityReporteEmpresas.agregarEmpresa(xSSFWorkbook, empresa, glnEmpresa, ultimaFechaDeActualizacion, totalDeProductosConGTIN13, totalDeProductosConGTIN14);

		}
		crearReporte(xSSFWorkbook);
		return "Se ha generado el reporte";
	}

	@Override
	public String reporteEmpresasPorGln(){
		List<common.rondanet.clasico.core.afiliados.models.Ubicacion> ubicaciones = afiliadosEmpresaService.obtenerEmpresaPorGln();
		XSSFWorkbook xSSFWorkbook = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
		for (common.rondanet.clasico.core.afiliados.models.Ubicacion ubicacion : ubicaciones) {
			Empresa empresa = afiliadosEmpresaService.obtenerEmpresaPorCodigoInterno(ubicacion.getCodigoInternoEmpresa());
			xSSFWorkbook = excelUtilityReporteEmpresas.agregarEmpresa(xSSFWorkbook, empresa, ubicacion.getId().getCodigo(), new Date(), 0, 0);
		}
		crearReporte(xSSFWorkbook);
		return "Se ha generado el reporte";
	}

	@Override
	public String reporteEmpresasConProductosVisibles(String rut){
		Optional<common.rondanet.catalogo.core.entity.Empresa> optionalEmpresa = empresasDAO.findByRutEmpresa(rut);
		if(optionalEmpresa.isPresent()) {
			List<Ubicacion> ubicaciones = ubicacionesDAO.buscarUbicacionesDeEmpresa(optionalEmpresa.get());
			List<String> codigoDeUbicaciones = Arrays.asList(ubicaciones.stream().map(Ubicacion::getCodigo).toArray(String[]::new));
			List<BigDecimal> empresasConProductos = this.catalogoViejoProductosService.findAllGroupByGln();
			XSSFWorkbook xSSFWorkbook = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
			for (BigDecimal glnBigDecimal : empresasConProductos) {
				String glnEmpresa = String.valueOf(glnBigDecimal);
				Empresa empresa = afiliadosEmpresaService.obtenerEmpresaPorGln(glnEmpresa);
				if(empresa != null) {
					int totalDeProductosVisibles = this.catalogoViejoProductosService.getTotalProductosVisibles(glnEmpresa, codigoDeUbicaciones);
					if (totalDeProductosVisibles > 0) {
						Date ultimaFechaDeActualizacion = this.catalogoViejoProductosService.ultimaFechaDeActualizacion(glnEmpresa);
						xSSFWorkbook = excelUtilityReporteEmpresas.agregarEmpresa(xSSFWorkbook, empresa, glnEmpresa, ultimaFechaDeActualizacion, totalDeProductosVisibles, totalDeProductosVisibles);
					}
				}
			}
			crearReporte(xSSFWorkbook);
		}
		return "Se ha generado el reporte";
	}

	public void crearReporte(XSSFWorkbook xSSFWorkbook){
		try {
			FileOutputStream out = new FileOutputStream(new File("reportes/reporteEmpresas.xlsx"));
			xSSFWorkbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
