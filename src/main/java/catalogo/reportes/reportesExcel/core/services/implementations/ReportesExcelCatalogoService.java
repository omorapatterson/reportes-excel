package catalogo.reportes.reportesExcel.core.services.implementations;

import catalogo.reportes.core.afiliados.afiliadosRepositories.*;
import catalogo.reportes.core.afiliados.afiliadosServices.interfaces.IAfiliadosEmpresaService;
import catalogo.reportes.core.afiliados.afiliadosServices.interfaces.IAfiliadosProductosService;
import catalogo.reportes.core.catalogo.db.*;
import catalogo.reportes.core.catalogo.services.implementations.ContenidoNetoService;
import catalogo.reportes.core.utils.login.Login;
import common.rondanet.catalogo.core.entity.*;
import catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces.ICatalogoViejoProductosService;
import catalogo.reportes.core.pedidos.pedidosDAO.OrdenDeCompraFinalizadaDAO;
import catalogo.reportes.core.pedidos.pedidosEntity.OrdenDeCompraFinalizada;
import catalogo.reportes.reportesExcel.core.services.interfaces.IReportesExcelCatalogoService;
import catalogo.reportes.reportesExcel.core.utils.excelUtility.ExcelUtilityReporteEmpresas;
import common.rondanet.catalogo.core.resources.Representacion;
import org.apache.http.HttpStatus;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Service
public class ReportesExcelCatalogoService implements IReportesExcelCatalogoService {

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

	@Autowired
	ListasDeVentaDAO listasDeVentaDAO;

	@Autowired
	OrdenDeCompraFinalizadaDAO ordenDeCompraFinalizadaDAO;

	@Autowired
	TraficoDeAplicacionDAO traficoDeAplicacionDAO;

	@Autowired
	UsuariosDAO usuariosDAO;

	@Autowired
	ProductosDAO productosDAO;

	@Autowired
	UsuarioEmpresaDAO usuarioEmpresaDAO;

	@Autowired
	IAfiliadosProductosService afiliadosProductosService;

	@Autowired
	IAfiliadosProductoRepository afiliadosProductoRepository;

	@Autowired
	IAfiliadosEmpresaRepository afiliadosEmpresaRepository;

	@Autowired
	IAfiliadosUbicacionRepository afiliadosUbicacionRepository;

	@Autowired
	IAfiliadosCabezalRepository afiliadosCabezalRepository;

	@Autowired
	IAfiliadosContactoRepository afiliadosContactoRepository;

	@Autowired
	SincronizadorEmpresaDAO sincronizadorEmpresaDAO;

	@Autowired
	ContenidoNetoService contenidoNetoService;

	@Autowired
	ParamsDAO paramsDAO;

	private RestTemplate clientesService;

	public ReportesExcelCatalogoService() {
		this.clientesService = new RestTemplate();
	}

	@Override
	public String reporteEmpresasQueRealizanPedidosEnLaPlataformaNueva(String desde, String hasta) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
		DateTime fechaInicial = formatter.parseDateTime(desde);
		DateTime fechaFinal = formatter.parseDateTime(hasta);

		List<OrdenDeCompraFinalizada> empresasEmitiendoOrdenesDeCompra = ordenDeCompraFinalizadaDAO.obtenerEmpresasEmitiendoOrdenesDeCompra(fechaInicial, fechaFinal);

			XSSFWorkbook xSSFWorkbook = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
			for (OrdenDeCompraFinalizada ordenDeCompraFinalizada : empresasEmitiendoOrdenesDeCompra) {
				common.rondanet.catalogo.core.entity.Empresa empresaEmitiendo = empresasDAO.findById(ordenDeCompraFinalizada.getId());
				if(empresaEmitiendo != null) {
					xSSFWorkbook = excelUtilityReporteEmpresas.agregarEmpresasEmitiendoOrdenesDeCompra(xSSFWorkbook, empresaEmitiendo);
				}
			}
			crearReporte(xSSFWorkbook, "reporteEmpresasQueRealizanPedidosEnLaPlataformaNueva");
		return "Se ha generado el reporte";
	}

	@Override
	public String reporteEmpresasQueRecibenPedidosDesdeLaPlataformaNueva(String desde, String hasta) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
		DateTime fechaInicial = formatter.parseDateTime(desde);
		DateTime fechaFinal = formatter.parseDateTime(hasta);

		List<OrdenDeCompraFinalizada> empresasEmitiendoOrdenesDeCompra = ordenDeCompraFinalizadaDAO.obtenerEmpresasQueRecibenOrdenesDeCompra(fechaInicial, fechaFinal);

		XSSFWorkbook xSSFWorkbook = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
		for (OrdenDeCompraFinalizada ordenDeCompraFinalizada : empresasEmitiendoOrdenesDeCompra) {
			try {
				common.rondanet.catalogo.core.entity.Empresa empresaEmitiendo = empresasDAO.findById(ordenDeCompraFinalizada.getId());
				if (empresaEmitiendo != null) {
					xSSFWorkbook = excelUtilityReporteEmpresas.agregarEmpresasEmitiendoOrdenesDeCompra(xSSFWorkbook, empresaEmitiendo);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		crearReporte(xSSFWorkbook, "reporteEmpresasQueRecibenPedidosDesdeLaPlataformaNueva");
		return "Se ha generado el reporte";
	}

	@Override
	public String reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNueva(String desde, String hasta) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
		DateTime fechaInicial = formatter.parseDateTime(desde);
		DateTime fechaFinal = formatter.parseDateTime(hasta);

		List<String> empresasId = listasDeVentaDAO.obtenerTodasEmpresaQueTinenListasDeVentasActualizadas(fechaInicial);

		XSSFWorkbook xSSFWorkbook = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
		for (String empresaId : empresasId) {
			try {
				common.rondanet.catalogo.core.entity.Empresa empresaEmitiendo = empresasDAO.findById(empresaId);
				if (empresaEmitiendo != null) {
					xSSFWorkbook = excelUtilityReporteEmpresas.agregarEmpresasEmitiendoOrdenesDeCompra(xSSFWorkbook, empresaEmitiendo);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		crearReporte(xSSFWorkbook, "reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNueva");
		return "Se ha generado el reporte";
	}

	@Override
	public String reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNuevaYNoRecibenPedidos(String desde, String hasta) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
		DateTime fechaInicial = formatter.parseDateTime(desde);
		DateTime fechaFinal = formatter.parseDateTime(hasta);

		List<OrdenDeCompraFinalizada> empresasEmitiendoOrdenesDeCompra = ordenDeCompraFinalizadaDAO.obtenerEmpresasQueRecibenOrdenesDeCompra(fechaInicial, fechaFinal);

		String[] sempresas = empresasEmitiendoOrdenesDeCompra.stream().map(OrdenDeCompraFinalizada::getId).toArray(String[]::new);
		List<String> empresasEmitiendoOrdenesDeCompraId = Arrays.asList(sempresas);

		List<String> empresasId = listasDeVentaDAO.obtenerTodasEmpresaQueTinenListasDeVentasActualizadas(fechaInicial);

		XSSFWorkbook xSSFWorkbook = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
		for (String empresaId : empresasId) {
			try {
				if (empresasEmitiendoOrdenesDeCompraId.indexOf(empresaId) == -1) {
					common.rondanet.catalogo.core.entity.Empresa empresaEmitiendo = empresasDAO.findById(empresaId);
					if (empresaEmitiendo != null) {
						xSSFWorkbook = excelUtilityReporteEmpresas.agregarEmpresasEmitiendoOrdenesDeCompra(xSSFWorkbook, empresaEmitiendo);
					}
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		crearReporte(xSSFWorkbook, "reporteEmpresasQueTienenListasDeVentaEnLaPlataformaNuevaYNoRecibenPedidos");
		return "Se ha generado el reporte";
	}

	@Override
	public String reporteEmpresasQueUsanLaPlataformaNueva(String desde, String hasta) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
		DateTime fechaInicial = formatter.parseDateTime(desde);
		DateTime fechaFinal = formatter.parseDateTime(hasta);

		List<TraficoDeAplicacion> listaTraficoDeAplicacion = traficoDeAplicacionDAO.obtenerTodosLosUsuariosQueEntraronALaAplicacion(
				fechaInicial,
				fechaFinal
		);

		Set<Empresa> empresas = obtenerEmpresas(
				listaTraficoDeAplicacion,
				fechaInicial,
				fechaFinal
		);
		XSSFWorkbook xSSFWorkbook = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
		for (Empresa empresa : empresas) {
			xSSFWorkbook = excelUtilityReporteEmpresas.agregarEmpresasEmitiendoOrdenesDeCompra(xSSFWorkbook, empresa);
		}
		crearReporte(xSSFWorkbook, "reporteEmpresasQueUsanLaPlataformaNueva");
		return "Se ha generado el reporte";
	}

	public Set<Empresa> obtenerEmpresas(
			List<TraficoDeAplicacion> listaTraficoDeAplicacion,
			DateTime fechaInicial,
			DateTime fechaFinal
	) {
		Set<Empresa> empresas = new HashSet<>();
		for (TraficoDeAplicacion traficoDeAplicacion: listaTraficoDeAplicacion) {
			Usuario usuario = usuariosDAO.findById(traficoDeAplicacion.getId());
			Optional<TraficoDeAplicacion> optionalTraficoDeAplicacion = traficoDeAplicacionDAO.obtenerTraficoDeAplicacionPorUsuario(
					traficoDeAplicacion.getId(),
					fechaInicial,
					fechaFinal
			);
			if (usuario != null) {
				List<UsuarioEmpresa> usuarioEmpresas = usuarioEmpresaDAO.findByIdUsuario(usuario.getId());
				for (UsuarioEmpresa usuarioEmpresa: usuarioEmpresas) {
					Empresa empresa = empresasDAO.findById(usuarioEmpresa.getSempresa());
					if(empresa != null) {
						empresa.setFechaCreacion(optionalTraficoDeAplicacion.get().getFechaCreacion());
						empresas.add(empresa);
					}
				}
			}
		}
		return empresas;
	}

	@Override
	public void reporteProductosDeAfiliados(int rows) {
		XSSFWorkbook xSSFWorkbookProductos = excelUtilityReporteEmpresas.obtenerExcelParaReporteProductos();
		xSSFWorkbookProductos = afiliadosProductosService.obtenerProductosDeAfiliados(
				xSSFWorkbookProductos,
				rows
		);
		crearReporte(xSSFWorkbookProductos, "reporteProductosAfiliados");
	}

	@Override
	public void reporteProductosDeAfiliadosPorEmpresa(
			boolean todosLosProductos,
			boolean usaRondanet
	) {
		List<String> empresas = afiliadosProductoRepository.findGroupByEmpresa(13);

		if (!todosLosProductos) {
			if (usaRondanet) {
				List<String> empresasQueUsanRondanet = afiliadosEmpresaRepository.findAllByUsaRondanet();
				empresasQueUsanRondanet = filtrarEmpresas(empresas, empresasQueUsanRondanet);
				empresas = filtrarEmpresasQueNoTenganProductosEnCatalogo(empresasQueUsanRondanet);
			} else {
				List<String> empresasQueNoUsanRondanet = afiliadosEmpresaRepository.findAllByNoUsaRondanet();
				empresas = filtrarEmpresas(empresas, empresasQueNoUsanRondanet);
			}
		}

		for (String empresa: empresas) {
			XSSFWorkbook xSSFWorkbookProductos = excelUtilityReporteEmpresas.obtenerExcelParaReporteProductos();
			String nombreEmpresa = empresa;
			common.rondanet.clasico.core.afiliados.models.Empresa empresaAfiliado = afiliadosEmpresaRepository.obtenerEmpresaPorCodigoInterno(empresa);
			Optional<Empresa> empresaRondanet = Optional.empty();
			if (empresaAfiliado != null) {
				nombreEmpresa = empresaAfiliado.getRuc() + "-" + empresaAfiliado.getRazonSocial();
				empresaRondanet = empresasDAO.findByRutEmpresa(empresaAfiliado.getRuc());
			}
			xSSFWorkbookProductos = afiliadosProductosService.obtenerProductosDeAfiliadosEmpresa(
					xSSFWorkbookProductos,
					empresaAfiliado,
					empresaRondanet
			);
			crearReporte(xSSFWorkbookProductos, nombreEmpresa + "-reporteProductosAfiliados");
		}
	}

	@Override
	public void reporteProductosDeAfiliadosPorEmpresaConErroresEnLosEmpaques() {
		List<Producto> productosConErroresEnLosEmpaques = new ArrayList<>();
		productosConErroresEnLosEmpaques = productosDAO.obtenerTodasLasEmpresasProductosAfiliadosConErroresEnContenidoNeto();
		for (Producto producto: productosConErroresEnLosEmpaques) {
			Empresa empresa = empresasDAO.findById(producto.getId());
			XSSFWorkbook xSSFWorkbookProductos = excelUtilityReporteEmpresas.obtenerExcelParaReporteProductos();
			xSSFWorkbookProductos = afiliadosProductosService.obtenerProductosDeAfiliadosEmpresaConProblemasEnContenidoNeto(
					xSSFWorkbookProductos,
					empresa
			);
			String nombreEmpresa = empresa.getRut() + "-" + empresa.getRazonSocial();
			crearReporte(xSSFWorkbookProductos, nombreEmpresa + "-reporteProductosAfiliados");
		}
	}

	public List<String> filtrarEmpresas(
			List<String> todasLasEmpresasEnAfiliados,
			List<String> empresasQueUsanRondanet
	) {
		List<String> empresasFiltradas = new ArrayList<>();
		for (String empresa: empresasQueUsanRondanet) {
			if (todasLasEmpresasEnAfiliados.contains(empresa)) {
				empresasFiltradas.add(empresa);
			}
		}
		return empresasFiltradas;
	}

	public List<String> filtrarEmpresasQueNoTenganProductosEnCatalogo(
			List<String> empresasQueUsanRondanet
	) {
		List<String> empresasFiltradas = new ArrayList<>();
		for (String empresa: empresasQueUsanRondanet) {
			common.rondanet.clasico.core.afiliados.models.Empresa empresaAfiliados = afiliadosEmpresaRepository.findByCodigoInterno(empresa);
			Optional<Empresa> optionalEmpresa = empresasDAO.findByRutEmpresa(empresaAfiliados.getRuc());
			if (optionalEmpresa.isPresent()) {
				long totalDeProductos = productosDAO.findAllByEmpresaTotalDeProductos(optionalEmpresa.get().getId());
				if (totalDeProductos == 0) {
					empresasFiltradas.add(empresa);
				}
			}

		}
		return empresasFiltradas;
	}

	@Override
	public List<String> empresasDeAfiliados(long gtinLongitud) {
		List<String> empresas = afiliadosProductoRepository.findGroupByEmpresa(gtinLongitud);
		return empresas;
	}

	@Override
	public void reporteProductosDeAfiliadosPorEmpresaRut(String rut) {
		common.rondanet.clasico.core.afiliados.models.Empresa empresaAfiliado = afiliadosEmpresaRepository.findByRut(rut);
		if (empresaAfiliado != null) {
			XSSFWorkbook xSSFWorkbookProductos = excelUtilityReporteEmpresas.obtenerExcelParaReporteProductos();
			String nombreEmpresa = empresaAfiliado.getId().getCodigoInternoEmpresa();
			Optional<Empresa> empresaRondanet = Optional.empty();
			if (empresaAfiliado != null) {
				nombreEmpresa = empresaAfiliado.getRuc() + "-" + empresaAfiliado.getRazonSocial();
				empresaRondanet = empresasDAO.findByRutEmpresa(empresaAfiliado.getRuc());
			}
			xSSFWorkbookProductos = afiliadosProductosService.obtenerProductosDeAfiliadosEmpresa(
					xSSFWorkbookProductos,
					empresaAfiliado,
					empresaRondanet
			);
			crearReporte(xSSFWorkbookProductos, nombreEmpresa + "-reporteProductosAfiliados");
		}
	}

	@Override
	public void reporteEmpresaCatalogoConSoloProductosDeAfiliados() {
		List<String> empresasConProductosDeAfiliados = productosDAO.obtenerTodasLasEmpresasProductosAfiliados();
		List<String> empresasConProductosDeCatalogo = productosDAO.obtenerTodasLasEmpresasProductosCatalogo();
		List<String> empresasConSoloProductosDeAfiliados = new ArrayList<>();
		for (String empresaId: empresasConProductosDeAfiliados) {
			if (!empresasConProductosDeCatalogo.contains(empresaId)) {
				empresasConSoloProductosDeAfiliados.add(empresaId);
			}
		}
		List<Empresa> empresas = empresasDAO.finAllByIdIn(empresasConSoloProductosDeAfiliados);
		XSSFWorkbook xSSFWorkbook = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
		for (Empresa empresa : empresas) {
			xSSFWorkbook = excelUtilityReporteEmpresas.agregarEmpresasEmitiendoOrdenesDeCompra(xSSFWorkbook, empresa);
		}
		crearReporte(xSSFWorkbook, "reporteEmpresasQueSoloTienenProductosDeAfialidos");
	}

	@Override
	public Representacion<List<Producto>> obtenerProductosDeAfiliadosPorEmpresaCodigoInterno(
			String codigoInterno,
			Date fechaDeActualizacion,
			long page,
			long limit,
			long gtinLongitud,
			boolean productosConEmpaque
	) {
		List<Producto> productos = new ArrayList<>();
		long total = 0;
		if (fechaDeActualizacion != null) {
			if (productosConEmpaque) {
				total = afiliadosProductoRepository.countAllProductosByEmpresaPorFechaAndEmpaque(codigoInterno, gtinLongitud, fechaDeActualizacion);
			} else {
				total = afiliadosProductoRepository.countAllProductosByEmpresaPorFecha(codigoInterno, gtinLongitud, fechaDeActualizacion);
			}
		} else {
			if (productosConEmpaque) {
				total = afiliadosProductoRepository.countAllProductosByEmpresaConFechasAndEmpaque(codigoInterno, gtinLongitud);
			} else {
				total = afiliadosProductoRepository.countAllProductosByEmpresaConFechas(codigoInterno, gtinLongitud);
			}
		}

		limit = (limit < 0 || limit > 100) ? 25 : limit;
		int from = (int) ((page > 0 ? page - 1 : page) * limit) + 1;
		int to = (int) ((page < 0 ? 1 : page) * limit);

		List<common.rondanet.clasico.core.afiliados.models.Producto> productosAfiliado = new ArrayList<>();

		if (fechaDeActualizacion != null) {
			if (productosConEmpaque) {
				productosAfiliado = afiliadosProductoRepository.findAllProductosByEmpresaPorFechaAndEmpaque(
						codigoInterno,
						gtinLongitud,
						fechaDeActualizacion,
						from,
						to
				);
			} else {
				productosAfiliado = afiliadosProductoRepository.findAllProductosByEmpresaPorFecha(
						codigoInterno,
						gtinLongitud,
						fechaDeActualizacion,
						from,
						to
				);
			}
		} else {
			if (productosConEmpaque) {
				productosAfiliado = afiliadosProductoRepository.findAllProductosByEmpresaConFechaAndEmpaque(
						codigoInterno,
						gtinLongitud,
						from,
						to
				);
			} else {
				productosAfiliado = afiliadosProductoRepository.findAllProductosByEmpresaConFecha(
						codigoInterno,
						gtinLongitud,
						from,
						to
				);
			}
		}

		if (productosAfiliado.size() > 0) {
			productos = contenidoNetoService.obtenerUnidadesDeContenidoNeto(
					productosAfiliado
			);
		}

		Representacion<List<Producto>> representacion = new Representacion<>(
				HttpStatus.SC_OK,
				productos
		);

		representacion.setPage(page);
		representacion.setLimit(limit);
		representacion.setTotal((long) total);
		return representacion;
	}


	@Override
	public Representacion<List<Producto>> obtenerProductosDeAfiliadosPorGtin(
			List<String> productosGtin
	) {
		List<Producto> productos = new ArrayList<>();

		List<common.rondanet.clasico.core.afiliados.models.Producto> productosAfiliado = new ArrayList<>();

		productosAfiliado = afiliadosProductoRepository.findAllByGtin(
				productosGtin
		);

		long total = productosAfiliado.size();

		if (productosAfiliado.size() > 0) {
			productos = contenidoNetoService.obtenerUnidadesDeContenidoNeto(
					productosAfiliado
			);
		}

		Representacion<List<Producto>> representacion = new Representacion<>(
				HttpStatus.SC_OK,
				productos
		);

		representacion.setPage(1L);
		representacion.setLimit(25L);
		representacion.setTotal((long) total);
		return representacion;
	}

	@Override
	public Empresa obtenerEmpresaAfiliados(
			String codigoInterno
	) {
		common.rondanet.clasico.core.afiliados.models.Empresa empresaAfiliado = null;
		empresaAfiliado = afiliadosEmpresaRepository.findByCodigoInternoConFecha(codigoInterno);
		if (empresaAfiliado == null) {
			empresaAfiliado = afiliadosEmpresaRepository.findByRutConFecha(codigoInterno);
		}
		Empresa empresa = sincronizadorEmpresaDAO.obtenerDatosDeEmpresaEmpresa(empresaAfiliado);

		return empresa;
	}

	@Override
	public List<common.rondanet.clasico.core.afiliados.models.Ubicacion> obtenerUbicacionesDeEmpresaAfiliados(
			String codigoInterno
	) {
		List<common.rondanet.clasico.core.afiliados.models.Ubicacion> ubicacionesEmpresaAfiliado = new ArrayList<>();
		ubicacionesEmpresaAfiliado = afiliadosUbicacionRepository.obtenerUbicaciones(codigoInterno);
		if (ubicacionesEmpresaAfiliado == null) {
			common.rondanet.clasico.core.afiliados.models.Empresa empresaAfiliado = afiliadosEmpresaRepository.findByRut(codigoInterno);
			ubicacionesEmpresaAfiliado = afiliadosUbicacionRepository.obtenerUbicaciones(empresaAfiliado.getId().getCodigoInternoEmpresa());

		}
		return ubicacionesEmpresaAfiliado;
	}

	@Override
	public List<common.rondanet.clasico.core.afiliados.models.Contacto> obtenerContactosDeEmpresaAfiliados(
			String codigoInterno
	) {
		List<common.rondanet.clasico.core.afiliados.models.Contacto> contactos = new ArrayList<>();
		contactos = afiliadosContactoRepository.obtenerContactosEmpresa(codigoInterno);
		return contactos;
	}

	@Override
	public List<common.rondanet.clasico.core.afiliados.models.Cabezal> obtenerCabezalesDeEmpresaAfiliados(
			String codigoInterno
	) {
		List<common.rondanet.clasico.core.afiliados.models.Cabezal> cabezales = new ArrayList<>();
		cabezales = afiliadosCabezalRepository.obtenerCabezalesEmpresa(codigoInterno);
		return cabezales;
	}

	@Override
	public void reporteUsuariosEmpresa() {
		List<Empresa> empresas = empresasDAO.getAllIdEmpresas();
		XSSFWorkbook xSSFWorkbookUsuariosEmpresa = excelUtilityReporteEmpresas.obtenerExcelParaReporteEmpresas();
		for (Empresa empresa: empresas) {
			empresa = empresasDAO.findById(empresa.getId());
			if (empresa != null) {
				//xSSFWorkbookUsuariosEmpresa = excelUtilityReporteEmpresas.agregarEmpresasEmitiendoOrdenesDeCompra(xSSFWorkbookUsuariosEmpresa, empresa);
				List<UsuarioEmpresa> usuarioEmpresas = usuarioEmpresaDAO.findAllByIdEmpresa(empresa.getId());
				for (UsuarioEmpresa usuarioEmpresa: usuarioEmpresas) {
					Usuario usuario = usuariosDAO.findById(usuarioEmpresa.getSusuario());
					if (usuario != null && !usuario.getEsAdministradorSistema() && usuario.getEmail() != null && usuario.getEmail().contains("@")) {
						xSSFWorkbookUsuariosEmpresa = excelUtilityReporteEmpresas.agregarUsuarioEmpresas(xSSFWorkbookUsuariosEmpresa, empresa, usuario);
					}
				}
			}
		}
		crearReporte(xSSFWorkbookUsuariosEmpresa, "reporteUsuariosEmpresa");
	}

	@Override
	public void hacerLogin() {
		List<Producto> productos = productosDAO.findAllByMarcaIsEmpty();
		for (Producto producto: productos) {
			producto = productosDAO.findById(producto.getId());
			if (producto != null && (producto.getMarca() == null || producto.getMarca().isEmpty())) {
				producto.setMarca("No proporcionada");
			}
		}
	}

	public void login(String usuario, String password) {
		try {
			String loginUrl = "https://api.desarrollo.catalogo.rondanet.com/auth/login";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			Login body = new Login(
					usuario,
					password
			);
			HttpEntity<Login> requestEntity = new HttpEntity<>(body, headers);
			clientesService.postForEntity(loginUrl, requestEntity, String.class);
			ResponseEntity<String> response = clientesService.postForEntity(loginUrl, requestEntity, String.class);
			String responseBody = response.getBody();
		} catch (Exception exception) {
			// logger.log(Level.ERROR,"A ocurrido un error al loguearse en Catalogo: " + exception.getStackTrace());
		}
	}

	public void crearReporte(XSSFWorkbook xSSFWorkbook, String name){
		try {
			FileOutputStream out = new FileOutputStream(new File("reportes/" + name + ".xlsx"));
			xSSFWorkbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
