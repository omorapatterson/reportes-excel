package catalogo.reportes.reportesExcel.api;

import catalogo.reportes.core.security.IAuthenticationFacade;
import catalogo.reportes.reportesExcel.core.services.interfaces.IReportesExcelCatalogoService;
import catalogo.reportes.reportesExcel.core.services.interfaces.IReportesExcelCatalogoViejoService;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/reportesExcel")
public class ReportesExcelController {
  Logger logger = LogManager.getLogger(ReportesExcelController.class);

  private final IAuthenticationFacade authenticationFacade;

  private final IReportesExcelCatalogoViejoService reportesCatalogoViejoExcelService;

  @Autowired
  IReportesExcelCatalogoService reportesExcelService;

  public ReportesExcelController(
    IAuthenticationFacade authenticationFacade, IReportesExcelCatalogoViejoService reportesExcelService) {
    this.authenticationFacade = authenticationFacade;
    this.reportesCatalogoViejoExcelService = reportesExcelService;
  }

  @PostMapping("/reportesEmpresas")
  public ResponseEntity<String> reportesEmpresas() {
    this.reportesCatalogoViejoExcelService.reporteEmpresas();
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reportesEmpresasPorGln")
  public ResponseEntity<String> reportesEmpresasPorGln() {
    this.reportesCatalogoViejoExcelService.reporteEmpresasPorGln();
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reportesEmpresasConProductosVisibles")
  public ResponseEntity<String> reportesEmpresasConProductosVisibles(@RequestParam(defaultValue = "") String rut) {
    this.reportesCatalogoViejoExcelService.reporteEmpresasConProductosVisibles(rut);
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteEmpresasQueRealizanPedidosEnLaPlataformaNueva")
  public ResponseEntity<String> reporteEmpresasQueRealizanPedidosEnLaPlataformaNueva(
          @RequestParam(defaultValue = "01-01-2021") String fachaInicio,
          @RequestParam(defaultValue = "12-12-2021") String fechaFinal
  ) {
    this.reportesExcelService.reporteEmpresasQueRealizanPedidosEnLaPlataformaNueva(fachaInicio, fechaFinal);
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteEmpresasQueRecibenPedidosDesdeLaPlataformaNueva")
  public ResponseEntity<String> reporteEmpresasQueRecibenPedidosDesdeLaPlataformaNueva(
          @RequestParam(defaultValue = "01-01-2021") String fachaInicio,
          @RequestParam(defaultValue = "12-12-2021") String fechaFinal
  ) {
    this.reportesExcelService.reporteEmpresasQueRecibenPedidosDesdeLaPlataformaNueva(fachaInicio, fechaFinal);
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNueva")
  public ResponseEntity<String> reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNueva() {
    this.reportesExcelService.reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNueva("01-01-2020", "31-12-2022");
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNuevaYNoRecibenPedidos")
  public ResponseEntity<String> reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNuevaYNoRecibenPedidos() {
    this.reportesExcelService.reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNuevaYNoRecibenPedidos("01-01-2020", "31-12-2022");
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteEmpresasQueUsanLaPlataformaNueva")
  public ResponseEntity<String> reporteEmpresasQueUsanLaPlataformaNueva(
          @RequestParam(defaultValue = "01-01-2021") String fachaInicio,
          @RequestParam(defaultValue = "12-12-2021") String fechaFinal
  ) {
    this.reportesExcelService.reporteEmpresasQueUsanLaPlataformaNueva(fachaInicio, fechaFinal);
    return ok("Se ha generado el reporte empresas que usan la plataforma nueva");
  }

  @PostMapping("/reporteProductosDeAfiliados")
  public ResponseEntity<String> reporteProductosDeAfiliados(
          @Parameter(description = "") @RequestParam(defaultValue = "0") int rows
  ) {
    this.reportesExcelService.reporteProductosDeAfiliados(rows);
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteProductosDeAfiliadosPorEmpresa")
  public ResponseEntity<String> reporteProductosDeAfiliadosPorEmpresa(
          @RequestParam(value="todosLosProductos",defaultValue = "true") boolean todosLosProductos,
          @RequestParam(value="usaRondanet",defaultValue = "false") boolean usaRondanet
  ) {
    this.reportesExcelService.reporteProductosDeAfiliadosPorEmpresa(
            todosLosProductos,
            usaRondanet
    );
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteProductosDeAfiliadosPorEmpresaConErroresEnLosEmpaques")
  public ResponseEntity<String> reporteProductosDeAfiliadosPorEmpresaConErroresEnLosEmpaques() {
    this.reportesExcelService.reporteProductosDeAfiliadosPorEmpresaConErroresEnLosEmpaques();
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteProductosDeAfiliadosPorEmpresaRut")
  public ResponseEntity<String> reporteProductosDeAfiliadosPorEmpresaRut(
          @RequestParam(defaultValue = "") String rut
  ) {
    this.reportesExcelService.reporteProductosDeAfiliadosPorEmpresaRut(rut);
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteEmpresaCatalogoConSoloProductosDeAfiliados")
  public ResponseEntity<String> reporteEmpresaCatalogoConSoloProductosDeAfiliados(
  ) {
    this.reportesExcelService.reporteEmpresaCatalogoConSoloProductosDeAfiliados();
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/empresasDeAfiliados")
  public ResponseEntity<String> empresasDeAfiliados() {
    this.reportesExcelService.empresasDeAfiliados(13);
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/reporteUsuariosEmpresa")
  public ResponseEntity<String> reporteUsuariosEmpresa() {
    this.reportesExcelService.reporteUsuariosEmpresa();
    return ok("Se ha generado el reporte");
  }

  @PostMapping("/hacerLogin")
  public ResponseEntity<String> hacerLogin() {
    this.reportesExcelService.hacerLogin();
    return ok("Se ha generado el reporte");
  }

}