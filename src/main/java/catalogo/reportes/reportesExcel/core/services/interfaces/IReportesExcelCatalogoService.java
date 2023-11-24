package catalogo.reportes.reportesExcel.core.services.interfaces;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Producto;
import common.rondanet.catalogo.core.resources.Representacion;

import java.util.Date;
import java.util.List;

public interface IReportesExcelCatalogoService {

    String reporteEmpresasQueRealizanPedidosEnLaPlataformaNueva(String desde, String hasta);

    String reporteEmpresasQueRecibenPedidosDesdeLaPlataformaNueva(String desde, String hasta);

    String reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNueva(String desde, String hasta);

    String reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNuevaYNoRecibenPedidos(String desde, String hasta);

    String reporteEmpresasQueUsanLaPlataformaNueva(String desde, String hasta);

    void reporteProductosDeAfiliados(int rows);

    void reporteProductosDeAfiliadosPorEmpresa(
            boolean todosLosProductos,
            boolean usaRondanet
    );

    void reporteProductosDeAfiliadosPorEmpresaConErroresEnLosEmpaques();

    List<String> empresasDeAfiliados(long gtinLongitud);

    void reporteProductosDeAfiliadosPorEmpresaRut(String rut);

    void reporteEmpresaCatalogoConSoloProductosDeAfiliados();

    Representacion<List<Producto>> obtenerProductosDeAfiliadosPorEmpresaCodigoInterno(
            String codigoInterno,
            Date fechaDeActualizacion,
            long page,
            long limit,
            long gtinLongitud,
            boolean productosConEmpaque
    );

    Representacion<List<Producto>> obtenerProductosDeAfiliadosPorGtin(
            List<String> productosGtin
    );

    Empresa obtenerEmpresaAfiliados(
            String codigoInterno
    );

    List<common.rondanet.clasico.core.afiliados.models.Ubicacion> obtenerUbicacionesDeEmpresaAfiliados(
            String codigoInterno
    );

    List<common.rondanet.clasico.core.afiliados.models.Contacto> obtenerContactosDeEmpresaAfiliados(
            String codigoInterno
    );

    List<common.rondanet.clasico.core.afiliados.models.Cabezal> obtenerCabezalesDeEmpresaAfiliados(
            String codigoInterno
    );

    void reporteUsuariosEmpresa();

    void hacerLogin();
}
