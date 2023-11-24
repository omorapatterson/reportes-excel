package catalogo.reportes.core.catalogo.db;

import java.util.*;


import catalogo.reportes.core.catalogo.pedidosQuery.ListaDeVentaQuerys;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import common.rondanet.catalogo.core.entity.*;
import catalogo.reportes.core.catalogo.repository.*;
import org.joda.time.DateTime;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Component
public class ProductosDAO{

	Logger logger = LogManager.getLogger(ProductosDAO.class);

	@Autowired
	IProductoRepository productoRepository;

	@Autowired
	IEmpresaRepository empresaRepository;

	@Autowired
	EmpresasDAO empresasDAO;

	@Autowired
	IGrupoRepository grupoRepository;

	@Autowired
	IUserRepository userRepository;

	@Autowired
	ListasDeVentaDAO listasDeVentaDAO;

	@Autowired
	ListaDeVentaVisibilidadDAO listaDeVentaVisibilidadDAO;

	private final MongoOperations mongoOperations;

	public ProductosDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IProductoRepository productoRepository, IEmpresaRepository empresaRepository,
						IUserRepository userRepository, EmpresasDAO empresasDAO, IGrupoRepository grupoRepository) {
		this.mongoOperations = mongoOperations;
		this.productoRepository = productoRepository;
		this.empresaRepository = empresaRepository;
		this.userRepository = userRepository;
		this.empresasDAO = empresasDAO;
		this.grupoRepository = grupoRepository;
	}

	/**
	 * Devuelve un {@link Producto} donde {@link String} id del {@link Producto} sea igual al {@link String} id pasado por parámetro
	 * @param id {@link String}
	 * @return {@link Producto}
	 */
	public Producto findById(String id) {
		Optional<Producto> producto = productoRepository.findById(id);
		logger.log(Level.INFO, "El método findById() de la clase ProductosDAO fue ejecutado.");
		return producto.isPresent() ? producto.get() : null;
	}

	/**
	 * Devuelve un {@link Optional}<{@link Producto}> donde el {@link String} cpp y el {@link String} sempresa de {@link Producto}
	 * sea igual al {@link String} cpp y el {@link String} empresaId pasado por parámetro.
	 * @param empresaId {@link String}
	 * @param cpp {@link String}
	 * @return {@link Optional}<{@link Producto}>
	 */
	public Optional<Producto> findByIdEmpresaAndCpp(String empresaId, String cpp) {
		logger.log(Level.INFO, "El metodo findByIdEmpresaAndCpp() de la clase ProductosDAO fue ejecutado.");
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresaId).andOperator(Criteria.where("cpp").is(cpp)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		if (productos.size() > 0)
			return Optional.of(productos.get(0));
		return Optional.ofNullable(null);
	}

	public List<Producto> getAll() {
		List<Producto> listaProductos = productoRepository.findAll();
		return listaProductos;
	}

	public List<Producto> findByKey(String key, String value) {
		Query query = new Query();
		List<Producto> productos = new ArrayList<>();
		try {
			query.addCriteria(Criteria.where("eliminado").is(false).orOperator(Criteria.where(key).is(value), Criteria.where("key").is(Long.parseLong(value))));
			productos = mongoOperations.find(query, Producto.class);
		} catch (NumberFormatException e) {
			query.addCriteria(Criteria.where("eliminado").is(false).orOperator(Criteria.where(key).is(value)));
			productos = mongoOperations.find(query, Producto.class);
		}
		return productos;
	}

	public Producto getProductByBussisnesAndCpp(String empresaId, String cpp) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresaId).andOperator(Criteria.where("cpp").is(cpp), Criteria.where("eliminado").is(false)));
		Producto producto = mongoOperations.findOne(query, Producto.class);
		return producto;
	}

	public Producto getProductByBussisnesAndGtin(String empresaId, String gtin) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresaId).andOperator(Criteria.where("gtin").is(gtin), Criteria.where("eliminado").is(false)));
		Producto producto = mongoOperations.findOne(query, Producto.class);
		return producto;
	}

	public List<Producto> findAllByEmpresa(String empresaId) {
		Aggregation productosAggregation = Aggregation.newAggregation(match(Criteria.where("sempresa").is(empresaId).andOperator(Criteria.where("eliminado").is(false)).orOperator(Criteria.where("tipo").is(null), Criteria.where("tipo").is("retail"))), group("sid"));
		List<Producto> productos = mongoOperations.aggregate(productosAggregation, "Producto", Producto.class).getMappedResults();
		return productos;
	}

	public long findAllByEmpresaTotalDeProductos(String empresaId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresaId).andOperator(Criteria.where("eliminado").is(false)));
		long total = mongoOperations.count(query, Producto.class);
		return total;
	}

	public List<Producto> findAllByMarcaIsEmpty() {
		Aggregation productosAggregation = Aggregation.newAggregation(match(Criteria.where("marca").is("").andOperator(Criteria.where("marca").is(null))), group("sid"));
		List<Producto> productos = mongoOperations.aggregate(productosAggregation, "Producto", Producto.class).getMappedResults();
		return productos;
	}

	public List<Producto> findAllByGtin(String gtin) {
		Query query = new Query();
		query.addCriteria(Criteria.where("gtin").is(gtin).andOperator(Criteria.where("eliminado").is(false)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		return productos;
	}

	/**
	 * Busca un único {@link Producto} dado un código <code>GTIN</code>.
	 * @param gtin Código GTIN por el cual se busca el {@link Producto} .
	 * @return {@link Optional}<{@link Producto}>
	 */
	public Optional<Producto> buscarProductoPrincipalPorGtinEnTodasLasEmpresas(String gtin) {
		Query query = new Query();
		query.addCriteria(Criteria.where("eliminado").is(false).andOperator(Criteria.where("gtin").is(gtin), Criteria.where("esPrincipal").is(true)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		return Optional.ofNullable(productos.isEmpty() ? null : productos.get(0));
	}

	public boolean esPrincipal(String gtin) {
		Query query = new Query();
		query.addCriteria(Criteria.where("eliminado").is(false).andOperator(Criteria.where("gtin").is(gtin), Criteria.where("esPrincipal").is(true)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		return productos.size() == 0;
	}

	public List<Producto> obtenerTodosLosProductosActualizados(DateTime fechaEdicion, Empresa empresa) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresa.getId()).andOperator(Criteria.where("fechaEdicion").gte(fechaEdicion)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		logger.log(Level.INFO, "El método findByKey() de la clase ListasDeVentaDAO fue ejecutado.");
		return productos;
	}

	public List<Producto> obtenerTodosLosProductosAfiliadosPorEmpresaConErroresEnContenidoNeto(Empresa empresa) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresa.getId()).andOperator(
				Criteria.where("esProductoDeAfiliados").gte(true),
				Criteria.where("visibilidadPorGrupo").gte(true))
		);
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		logger.log(Level.INFO, "El método findByKey() de la clase ListasDeVentaDAO fue ejecutado.");
		return productos;
	}

	public List<Producto> obtenerTodasLasEmpresasProductosAfiliadosConErroresEnContenidoNeto() {
		Aggregation productosVisiblesListaDeVentaAggregation = Aggregation.newAggregation(match(
				Criteria.where("esProductoDeAfiliados").is(true).andOperator(
						Criteria.where("visibilidadPorGrupo").gte(true)
		)), group("sempresa"));
		List<Producto> productos = mongoOperations.aggregate(productosVisiblesListaDeVentaAggregation, "Producto", Producto.class).getMappedResults();
		return productos;
	}

	public List<String> obtenerTodasLasEmpresasProductosAfiliados() {
		Aggregation productosVisiblesListaDeVentaAggregation = Aggregation.newAggregation(match(
				Criteria.where("eliminado").is(false).andOperator(
						Criteria.where("esProductoDeAfiliados").ne(null),
						Criteria.where("esProductoDeAfiliados").is(true)
				)), group("sempresa"));
		List<Producto> productos = mongoOperations.aggregate(
				productosVisiblesListaDeVentaAggregation,
				"Producto",
				Producto.class
		).getMappedResults();
		List<String> empresasConProductosDeAfiliados = Arrays.asList(productos.stream().map(Producto::getId).toArray(String[]::new));
		return empresasConProductosDeAfiliados;
	}

	public List<String> obtenerTodasLasEmpresasProductosCatalogo() {
		Aggregation productosVisiblesListaDeVentaAggregation = Aggregation.newAggregation(match(
				Criteria.where("eliminado").is(false).andOperator(
						Criteria.where("esProductoDeAfiliados").is(null)
				)), group("sempresa"));
		List<Producto> productos = mongoOperations.aggregate(
				productosVisiblesListaDeVentaAggregation,
				"Producto",
				Producto.class
		).getMappedResults();
		List<String> empresasConProductosDeCatalogo = Arrays.asList(productos.stream().map(Producto::getId).toArray(String[]::new));
		return empresasConProductosDeCatalogo;
	}

	public List<Producto> findAllByGrupo(String idGrupo) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sgruposConVisibilidad").in(idGrupo).andOperator(Criteria.where("eliminado").is(false)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		logger.log(Level.INFO, "El método findByKey() de la clase ListasDeVentaDAO fue ejecutado.");
		return productos;
	}

	public long getTotalDeProductosVisiblesEnListaDeVenta(Empresa empresa, ListaDeVenta listaDeVenta) {
		Query productosQuery = getVisibleByBussinesOnSaleListQuery(empresa, listaDeVenta);
		long total = mongoOperations.count(productosQuery, Producto.class);
		return total;
	}

	public Query getVisibleByBussinesOnSaleListQuery(Empresa empresa, ListaDeVenta lv){
		Aggregation productosVisiblesListaDeVentaAggregation = Aggregation.newAggregation(match(Criteria.where("slistaDeVenta").is(lv.getSId()).andOperator(Criteria.where("eliminado").is(false)).orOperator(Criteria.where("esPublico").is(true), Criteria.where("sempresasConVisibilidad").in(empresa.getSId()), Criteria.where("sgruposConVisibilidad").in(empresa.getSempresaGrupos()))), group("sproducto"));
		List<ListaDeVentaVisibilidad> listaDeVentaVisibilidadProductos = mongoOperations.aggregate(productosVisiblesListaDeVentaAggregation, "ListaDeVentaVisibilidad", ListaDeVentaVisibilidad.class).getMappedResults();
		Set<String> productos = new HashSet<>();
		for (ListaDeVentaVisibilidad listaDeVentaVisibilidad: listaDeVentaVisibilidadProductos) {
			productos.add(listaDeVentaVisibilidad.getId());
		}

		Query productosQuery;
		productosQuery = new BasicQuery(ListaDeVentaQuerys.MOSTRAR_TODOS);

		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("sid").in(productos), Criteria.where("eliminado").is(false)).orOperator(Criteria.where("esPublico").is(true), Criteria.where("esPrivado").is(true));
		productosQuery.addCriteria(criteria);
		return productosQuery;
	}

	public Optional<Producto> findProductoPrincipal(String gtin) {
		Query query = new Query();
		query.addCriteria(Criteria.where("gtin").is(gtin).andOperator(Criteria.where("eliminado").is(false)));
		List<Producto> productos = mongoOperations.find(query, Producto.class);
		for (Producto producto: productos) {
			if (producto.getEsPrincipal()) {
				return Optional.of(producto);
			}
		}
		if (productos.size() != 0)
			return Optional.of(productos.get(0));
		return Optional.ofNullable(null);
	}
}
