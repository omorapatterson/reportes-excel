package catalogo.reportes.core.afiliados.afiliadosServices.interfaces;

import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Producto;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Optional;

public interface IAfiliadosProductosService {

    XSSFWorkbook obtenerProductosDeAfiliados(XSSFWorkbook xSSFWorkbookProductos, int rows);

    XSSFWorkbook obtenerProductosDeAfiliadosEmpresa(
            XSSFWorkbook xSSFWorkbookProductos,
            common.rondanet.clasico.core.afiliados.models.Empresa empresaAfiliado,
            Optional<Empresa> optionalEmpresa
    );

    XSSFWorkbook obtenerProductosDeAfiliadosEmpresaConProblemasEnContenidoNeto(
            XSSFWorkbook xSSFWorkbookProductos,
            Empresa empresa
    );
}
