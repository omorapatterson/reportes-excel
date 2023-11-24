package catalogo.reportes.core.catalogo.db;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import common.rondanet.catalogo.core.entity.*;
import catalogo.reportes.core.catalogo.repository.IListaDeVentaRepository;
import org.joda.time.DateTime;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Component
public class ListasDeVentaDAO {

	Logger logger = LogManager.getLogger(ListasDeVentaDAO.class);

	@Autowired
	IListaDeVentaRepository listaDeVentaRepository;

	@Autowired
	ListaDeVentaVisibilidadDAO listaDeVentaVisibilidadDAO;

	private final MongoOperations mongoOperations;

	public ListasDeVentaDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IListaDeVentaRepository listaDeVentaRepository, ListaDeVentaVisibilidadDAO listaDeVentaVisibilidadDAO) {
		this.mongoOperations = mongoOperations;
		this.listaDeVentaRepository = listaDeVentaRepository;
		this.listaDeVentaVisibilidadDAO = listaDeVentaVisibilidadDAO;
	}

	/**
	 * Devuelve una {@link ListaDeVenta} donde {@link String} id de la {@link ListaDeVenta} sea igual al {@link String} id pasado por parámetro
	 *
	 * @param id {@link String}
	 * @return {@link ListaDeVenta}
	 */
	public ListaDeVenta findById(String id) {
		Optional<ListaDeVenta> listaDeVenta = listaDeVentaRepository.findById(id);
		logger.log(Level.INFO, "El método findById() de la clase ListasDeVentaDAO fue ejecutado.");
		return listaDeVenta.isPresent() ? listaDeVenta.get() : null;
	}

	/**
	 * Guarda una {@link ListaDeVenta} pasada como parámetro modificando el {@link DateTime} de fechaEdición
	 *
	 * @param listaDeVenta {@link ListaDeVenta}
	 * @return {@link ListaDeVenta}
	 */
	public ListaDeVenta save(ListaDeVenta listaDeVenta) {
		listaDeVenta.setFechaEdicion();
		if(listaDeVenta.getFechaCreacion() == null){
			listaDeVenta.setFechaCreacion();
		}
		if(listaDeVenta.getSId() == null) {
			listaDeVenta = listaDeVentaRepository.save(listaDeVenta);
			listaDeVenta.setSId(listaDeVenta.getId());
		}
		logger.log(Level.INFO, "El método save() de la clase ListasDeVentaDAO fue ejecutado.");
		return listaDeVentaRepository.save(listaDeVenta);
	}

	/**
	 * Devuelve un {@link Optional}<{@link ListaDeVenta}> donde {@link String} id sea igual al {@link String} idListaVenta pasado por parámetro
	 *
	 * @param idListaVenta {@link String}
	 * @param idEmpresa    {@link String}, no se usa
	 * @return {@link Optional}<{@link ListaDeVenta}>
	 */
	public Optional<ListaDeVenta> findById(String idListaVenta, String idEmpresa) {
		Optional<ListaDeVenta> listaDeVenta = listaDeVentaRepository.findById(idListaVenta);
		logger.log(Level.INFO, "El método findById() de la clase ListasDeVentaDAO fue ejecutado.");
		return listaDeVenta;

	}

	/**
	 * Devuelve un {@link Optional}<{@link ListaDeVenta}> donde {@link String} id sea igual al {@link String} idListaVenta pasado por parámetro
	 *
	 * @param idUbicacion {@link String}, no se usa
	 * @return {@link Optional}<{@link ListaDeVenta}>
	 */
	public Optional<ListaDeVenta> findFirstBySubicacionAndNombre(String idUbicacion, String nombre) {
		Optional<ListaDeVenta> listaDeVenta = listaDeVentaRepository.findFirstBySubicacionAndNombre(idUbicacion, nombre);
		return listaDeVenta;

	}

	public Optional<ListaDeVenta> obtenerListaVentaByUbicacion(Ubicacion ubicacion) {
		Query query = new Query();
		query.addCriteria(Criteria.where("subicacion").is(ubicacion.getId()).andOperator(Criteria.where("eliminado").is(false)));
		List<ListaDeVenta> listasVenta = mongoOperations.find(query, ListaDeVenta.class);
		if (listasVenta.size() != 0){
			return Optional.of(listasVenta.get(0));
		}
		return Optional.ofNullable(null);
	}

	public List<ListaDeVenta> obtenerListasVentaPorUbicacion(Ubicacion ubicacion) {
		Query query = new Query();
		query.addCriteria(Criteria.where("subicacion").is(ubicacion.getId()).andOperator(Criteria.where("eliminado").is(false)));
		List<ListaDeVenta> listasVenta = mongoOperations.find(query, ListaDeVenta.class);
		return listasVenta;
	}

	public List<ListaDeVenta> getAll() {
		return listaDeVentaRepository.findAll();
	}

	/**
	 * Devuelve un {@link List}<{@link ListaDeVenta}> con todas las {@link ListaDeVenta} donde el {@link String} key y {@link String} value
	 * de {@link ListaDeVenta} sean igual al {@link String} key y {@link String} value pasado por parámetros
	 *
	 * @param key   {@link String}
	 * @param value {@link String}
	 * @return {@link List}<{@link ListaDeVenta}>
	 */
	public List<ListaDeVenta> findByKey(String key, String value) {
		Query query = new Query();
		query.addCriteria(Criteria.where(key).is(value).andOperator(Criteria.where("eliminado").is(false)));
		List<ListaDeVenta> listasVenta = mongoOperations.find(query, ListaDeVenta.class);
		logger.log(Level.INFO, "El método findByKey() de la clase ListasDeVentaDAO fue ejecutado.");
		return listasVenta;
	}

	public List<ListaDeVenta> obtenerTodasLasListasDeVentasActualizadas(DateTime fechaEdicion, Empresa empresa) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sempresa").is(empresa.getId()).andOperator(Criteria.where("fechaEdicion").gte(fechaEdicion)));
		List<ListaDeVenta> listasVenta = mongoOperations.find(query, ListaDeVenta.class);
		logger.log(Level.INFO, "El método findByKey() de la clase ListasDeVentaDAO fue ejecutado.");
		return listasVenta;
	}

	public List<ListaDeVenta> findAllByGrupo(String idGrupo) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sgrupos").in(idGrupo).andOperator(Criteria.where("eliminado").is(false)));
		List<ListaDeVenta> listasVenta = mongoOperations.find(query, ListaDeVenta.class);
		logger.log(Level.INFO, "El método findByKey() de la clase ListasDeVentaDAO fue ejecutado.");
		return listasVenta;
	}

	public List<String> obtenerTodasEmpresaQueTinenListasDeVentasActualizadas(DateTime fechaEdicion) {
		Aggregation productosAggregation = Aggregation.newAggregation(match(Criteria.where("eliminado").is(false).andOperator(Criteria.where("fechaEdicion").gte(fechaEdicion))), group("sempresa"));
		List<ListaDeVenta> listasVenta = mongoOperations.aggregate(productosAggregation, "ListaDeVenta", ListaDeVenta.class).getMappedResults();
		String[] sempresas = listasVenta.stream().map(ListaDeVenta::getId).toArray(String[]::new);
		return Arrays.asList(sempresas);
	}
}
