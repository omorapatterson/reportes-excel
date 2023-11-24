package catalogo.reportes.reportesExcel.api;

import catalogo.reportes.core.security.IAuthenticationFacade;
import catalogo.reportes.core.utils.fechas.FechaUtils;
import catalogo.reportes.reportesExcel.core.services.interfaces.IReportesExcelCatalogoService;
import catalogo.reportes.reportesExcel.core.services.interfaces.IReportesExcelCatalogoViejoService;
import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Producto;
import common.rondanet.catalogo.core.resources.Representacion;
import common.rondanet.clasico.core.afiliados.models.Cabezal;
import common.rondanet.clasico.core.afiliados.models.Contacto;
import common.rondanet.clasico.core.afiliados.models.Ubicacion;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/afiliados")
public class AfiliadosController {
  Logger logger = LogManager.getLogger(AfiliadosController.class);

  private final IAuthenticationFacade authenticationFacade;

  private final IReportesExcelCatalogoViejoService reportesCatalogoViejoExcelService;

  @Autowired
  IReportesExcelCatalogoService reportesExcelService;

  public AfiliadosController(
    IAuthenticationFacade authenticationFacade, IReportesExcelCatalogoViejoService reportesExcelService) {
    this.authenticationFacade = authenticationFacade;
    this.reportesCatalogoViejoExcelService = reportesExcelService;
  }

  @GetMapping(value = "/empresas")
  public ResponseEntity<Representacion> empresasDeAfiliados(
          @RequestParam(value="gtinLongitud", defaultValue = "13") Long gtinLongitud
  ) {
    List<String> empresas  = reportesExcelService.empresasDeAfiliados(gtinLongitud);
    Representacion response = new Representacion(HttpStatus.SC_OK, empresas);
    return ok(response);
  }

  @GetMapping("/empresa/productos")
  public ResponseEntity<Representacion> obtenerProductosDeAfiliadosPorEmpresa(
          @RequestParam(value="codigoInterno",defaultValue = "") String codigoInterno,
          @RequestParam(value="page", defaultValue = "1") Long page,
          @RequestParam(value="limit", defaultValue = "25") Long limit,
          @RequestParam(value="fecha", defaultValue = "") String fecha,
          @RequestParam(value="gtinLongitud", defaultValue = "13") Long gtinLongitud,
          @RequestParam(value="productosConEmpaque", defaultValue = "true") boolean productosConEmpaque
  ) {
    Date fechaDeActualizacion = FechaUtils.obtenerFechaDate(fecha);
    Representacion reponse  = reportesExcelService.obtenerProductosDeAfiliadosPorEmpresaCodigoInterno(
            codigoInterno,
            fechaDeActualizacion,
            page,
            limit,
            gtinLongitud,
            productosConEmpaque
    );
    return ok(reponse);
  }

  @PostMapping("/empresa/productos/gtin")
  public ResponseEntity<Representacion> obtenerProductosDeAfiliadosPorGtin(
          @RequestBody() List<String> productosGtin
  ) {
    Representacion reponse  = reportesExcelService.obtenerProductosDeAfiliadosPorGtin(
            productosGtin
    );
    return ok(reponse);
  }

  @GetMapping("/empresa")
  public ResponseEntity<Representacion> obtenerEmpresa(
          @RequestParam(value="codigoInterno",defaultValue = "") String codigoInterno
  ) {
    Empresa empresa  = reportesExcelService.obtenerEmpresaAfiliados(
            codigoInterno
    );
    Representacion reponse = new Representacion(HttpStatus.SC_OK, empresa);
    return ok(reponse);
  }

  @GetMapping("/empresa/ubicaciones")
  public ResponseEntity<Representacion> obtenerUbicacionesDeEmpresaAfiliados(
          @RequestParam(value="codigoInterno",defaultValue = "") String codigoInterno
  ) {
    List<Ubicacion> ubicaciones  = reportesExcelService.obtenerUbicacionesDeEmpresaAfiliados(
            codigoInterno
    );
    Representacion reponse = new Representacion(HttpStatus.SC_OK, ubicaciones);
    return ok(reponse);
  }

  @GetMapping("/empresa/contactos")
  public ResponseEntity<Representacion> obtenerContactosDeEmpresaAfiliados(
          @RequestParam(value="codigoInterno",defaultValue = "") String codigoInterno
  ) {
    List<Contacto> contactos  = reportesExcelService.obtenerContactosDeEmpresaAfiliados(
            codigoInterno
    );
    Representacion reponse = new Representacion(HttpStatus.SC_OK, contactos);
    return ok(reponse);
  }

  @GetMapping("/empresa/cabezales")
  public ResponseEntity<Representacion> obtenerCabezalesDeEmpresaAfiliados(
          @RequestParam(value="codigoInterno",defaultValue = "") String codigoInterno
  ) {
    List<Cabezal> cabezales  = reportesExcelService.obtenerCabezalesDeEmpresaAfiliados(
            codigoInterno
    );
    Representacion reponse = new Representacion(HttpStatus.SC_OK, cabezales);
    return ok(reponse);
  }


}