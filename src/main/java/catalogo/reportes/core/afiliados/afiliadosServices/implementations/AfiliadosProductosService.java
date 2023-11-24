package catalogo.reportes.core.afiliados.afiliadosServices.implementations;

import catalogo.reportes.core.afiliados.afiliadosRepositories.IAfiliadosProductoRepository;
import catalogo.reportes.core.afiliados.afiliadosServices.interfaces.IAfiliadosProductosService;
import catalogo.reportes.core.catalogo.db.ProductosDAO;
import catalogo.reportes.core.catalogo.services.interfaces.IContenidoNetoService;
import catalogo.reportes.reportesExcel.core.utils.excelUtility.ExcelUtilityReporteEmpresas;
import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Producto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AfiliadosProductosService implements IAfiliadosProductosService {

	Logger logger = LogManager.getLogger(AfiliadosProductosService.class);

	@Autowired
	IAfiliadosProductoRepository afiliadosProductoRepository;

	@Autowired
	ExcelUtilityReporteEmpresas excelUtilityReporteEmpresas;

	@Autowired
	ProductosDAO productosDAO;

	@Autowired
	IContenidoNetoService contenidoNetoService;

	@Override
	public XSSFWorkbook obtenerProductosDeAfiliados(XSSFWorkbook xSSFWorkbookProductos, int rows) {
		rows = (rows * 100000);
		int cantidad = 0;
		boolean todosLosProductos = false;
		int rowNumber = 2;
		while (!todosLosProductos) {
			int to = rows + 100;
			cantidad += 100;
			List<common.rondanet.clasico.core.afiliados.models.Producto> productosAfiliado = afiliadosProductoRepository.findAll(rows + 1, to);
			if (productosAfiliado.size() > 0) {
				List<Producto> productos = new ArrayList<>();
				int rowNumber1 = excelUtilityReporteEmpresas.agregarProductos(xSSFWorkbookProductos, productos, rowNumber);
				rowNumber = rowNumber1;
			}
			rows = to;
			if (productosAfiliado.size() < 100 || cantidad >= 100000) {
				todosLosProductos = true;
			}
		}
		return xSSFWorkbookProductos;
	}

	@Override
	public XSSFWorkbook obtenerProductosDeAfiliadosEmpresa(
			XSSFWorkbook xSSFWorkbookProductos,
			common.rondanet.clasico.core.afiliados.models.Empresa empresaAfiliado,
			Optional<Empresa> optionalEmpresa
	) {
		int rows = 0;
		boolean todosLosProductos = false;
		int rowNumber = 2;
		while (!todosLosProductos) {
			int to = rows + 100;
			List<common.rondanet.clasico.core.afiliados.models.Producto> productosAfiliado = afiliadosProductoRepository.findAllProductosByEmpresa(
					empresaAfiliado.getId().getCodigoInternoEmpresa(),
					rows + 1,
					to
			);
			if (productosAfiliado.size() > 0) {
				List<Producto> productos = contenidoNetoService.obtenerUnidadesDeContenidoNeto(
						productosAfiliado
				);
				rowNumber = excelUtilityReporteEmpresas.agregarProductos(
						xSSFWorkbookProductos,
						productos,
						rowNumber
				);
			}
			rows = to;
			if (productosAfiliado.size() < 100 ) {
				todosLosProductos = true;
			}
		}
		return xSSFWorkbookProductos;
	}

	@Override
	public XSSFWorkbook obtenerProductosDeAfiliadosEmpresaConProblemasEnContenidoNeto(
			XSSFWorkbook xSSFWorkbookProductos,
			Empresa empresa
	) {
		int rows = 0;
		boolean todosLosProductos = false;
		int rowNumber = 2;
		List<Producto> productos = productosDAO.obtenerTodosLosProductosAfiliadosPorEmpresaConErroresEnContenidoNeto(empresa);
		excelUtilityReporteEmpresas.agregarProductos(
				xSSFWorkbookProductos,
				productos,
				rowNumber
		);
		return xSSFWorkbookProductos;
	}

}