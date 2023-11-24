package catalogo.reportes.core.catalogo.db;

import java.util.List;
import java.util.Optional;


import common.rondanet.catalogo.core.entity.Empresa;
import catalogo.reportes.core.catalogo.exceptions.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import common.rondanet.catalogo.core.entity.Ubicacion;
import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import catalogo.reportes.core.catalogo.repository.IUbicacionRepository;


@Component
public class UbicacionesDAO {

	Logger logger = LogManager.getLogger(UbicacionesDAO.class);

	@Autowired
	IUbicacionRepository ubicacionRepository;

	private MongoOperations mongoOperations;

	public UbicacionesDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IUbicacionRepository ubicacionRepository) {
		this.mongoOperations = mongoOperations;
		this.ubicacionRepository = ubicacionRepository;
	}

	public Ubicacion update(Ubicacion e) throws ServiceException {
		Optional<Ubicacion> optional = this.existeUbicacion(e.getCodigo());
		if (!optional.isPresent())
			throw new ServiceException("No existe la Ubicación a actualizar");
		Ubicacion ubiBD = optional.get();
		ubiBD.update(e);
		return ubicacionRepository.save(ubiBD);
	}

	public Ubicacion save(Ubicacion ubicacion) throws ServiceException {
		ubicacion = ubicacionRepository.save(ubicacion);
		if(ubicacion.getSId() == null){
			ubicacion.setSId(ubicacion.getId());
			ubicacion = ubicacionRepository.save(ubicacion);
		}
		return ubicacion;
	}

	public Ubicacion delete(Ubicacion e) throws ServiceException {
		Optional<Ubicacion> optional = this.existeUbicacion(e.getCodigo());
		if (!optional.isPresent())
			throw new ServiceException("No existe la Ubicación a eliminar");
		Ubicacion ubiBD = optional.get();
		ubiBD.eliminar();
		return ubicacionRepository.save(ubiBD);
	}

	public Ubicacion findById(String id) {
		Optional<Ubicacion> ubicacion = ubicacionRepository.findById(id);
		if(ubicacion.isPresent())
			return ubicacion.get();
		return null;
	}

	/**
	 * Devuelve una {@link Optional}<{@link Ubicacion}> donde {@link String} codigo sea igual al {@link String} codigo
	 * pasado por parámetro
	 * @param codigo {@link}
	 * @return {@link Optional}<{@link Ubicacion}>
	 * @throws ServiceException
	 */
	public Optional<Ubicacion> existeUbicacion(String codigo) throws ServiceException {
		Optional<Ubicacion> ubicacion = this.findByKey("codigo", codigo);
		logger.log(Level.INFO, "El método existeUbicacion() de la clase UbicacionesDAO fue ejecutado.");
		return ubicacion;
	}

	/**
	 * Devuelve un {@link Optional}<{@link Ubicacion}> donde el {@link String} key y el {@link String} value de la {@link Ubicacion}
	 * sea igual al {@link String} key y el {@link String} value pasado por parámetro
	 * @param key {@link String}
	 * @param value {@link String}
	 * @return {@link Optional}<{@link Ubicacion}>
	 */
	public Optional<Ubicacion> findByKey(String key, String value) {
		return this.findByKey(key, value, null);
	}

	/**
	 * Devuelve un {@link Optional}<{@link Ubicacion}> donde el {@link String} key y el {@link String} value de la {@link Ubicacion}
	 * sea igual al {@link String} key y el {@link String} value pasado por parámetro y la {@link Ubicacion} no este eliminada
	 * <p>
	 *     Si el {@link UsuarioEmpresa} pasado por parametro es desigual a null se chequea qe la {@link Empresa} de la {@link Ubicacion}
	 *     sea igual a la {@link Empresa} del {@link UsuarioEmpresa} pasado por parámetro
	 * </p>
	 *
	 * @param key {@link String}
	 * @param value {@link String}
	 * @param usuarioEmpresa {@link UsuarioEmpresa}
	 * @return {@link Optional}<{@link Ubicacion}>
	 */
	public Optional<Ubicacion> findByKey(String key, String value, UsuarioEmpresa usuarioEmpresa) {
		Query query = new Query();
		List<Ubicacion> listaUbicaciones = mongoOperations.find(query.addCriteria(Criteria.where(key).is(value).andOperator(Criteria.where("eliminado").is(false))), Ubicacion.class);

		if (listaUbicaciones.size() != 0) {
			logger.log(Level.INFO, "El método findByKey() de la clase Ubicacion fue ejecutado.");
			return Optional.of(listaUbicaciones.get(0));
		}
		logger.log(Level.INFO, "El método findByKey() de la clase Ubicacion fue ejecutado.");
		return Optional.ofNullable(null);
	}

	public List<Ubicacion> getAll() {
		return ubicacionRepository.findAll();
	}

	/**
	 * Devuelve una {@link List}<{@link Ubicacion}> con las {@link Ubicacion} no marcadas como eliminadas.
	 * <p>
	 *     Si el {@link UsuarioEmpresa} pasado como parámetro es desigual a null, condiciona la lista con las
	 *     {@link Ubicacion} pertenecientes a la {@link Empresa} del {@link UsuarioEmpresa}
	 * </p>
	 * @param usuarioEmpresa {@link UsuarioEmpresa}
	 * @return {@link List}<{@link Ubicacion}>
	 */
	public List<Ubicacion> getAll(UsuarioEmpresa usuarioEmpresa) {
		Query query = new Query();
		List<Ubicacion> listaUbicaciones = mongoOperations.find(query.addCriteria(Criteria.where("sempresa").is(usuarioEmpresa.getEmpresa().getSId()).andOperator(Criteria.where("eliminado").is(false))), Ubicacion.class);
		logger.log(Level.INFO, "El método getAll() de la clase UbicacionesDAO fue ejecutado.");
		return listaUbicaciones;
	}

	public Optional<Ubicacion>buscarUbicacionEmpresa(String codigoUbicacion, Empresa empresa) {
		Query query = new Query();
		List<Ubicacion> listaUbicaciones = mongoOperations.find(query.addCriteria(Criteria.where("sempresa").is(empresa.getSId()).andOperator(Criteria.where("codigo").is(codigoUbicacion), Criteria.where("eliminado").is(false))), Ubicacion.class);

		if (listaUbicaciones.size() != 0)
			return Optional.of(listaUbicaciones.get(0));

		logger.log(Level.INFO, "El método perteneceEmpresa() de la clase UbicacionesDAO fue ejecutado.");
		return Optional.ofNullable(null);
	}

	public Ubicacion obtenerUbicacionDeEmpresa(Empresa empresa){
		Ubicacion ubicacionEmpresa = null;
		for (Ubicacion ubicacion: empresa.getUbicaciones()) {
			if(ubicacion.getTipo().equals("E")){
				ubicacionEmpresa = ubicacion;
				break;
			}
		}
		if(ubicacionEmpresa == null && empresa.getUbicaciones().size() > 0) {
			ubicacionEmpresa = empresa.getUbicaciones().iterator().next();
		}
		return ubicacionEmpresa;
	}

	public List<Ubicacion>buscarUbicacionesDeEmpresa(Empresa empresa) {
		Query query = new Query();
		List<Ubicacion> listaUbicaciones = mongoOperations.find(query.addCriteria(Criteria.where("sempresa").is(empresa.getSId()).andOperator(Criteria.where("tipo").is("E"), Criteria.where("eliminado").is(false))), Ubicacion.class);
		return listaUbicaciones;
	}

	public List<Ubicacion>buscarUbicacionesDeEmpresaODeposito(Empresa empresa) {
		Query query = new Query();
		List<Ubicacion> listaUbicaciones = mongoOperations.find(query.addCriteria(Criteria.where("sempresa").is(empresa.getSId()).andOperator(Criteria.where("eliminado").is(false)).orOperator(Criteria.where("tipo").is("E"), Criteria.where("tipo").is("D"))), Ubicacion.class);
		return listaUbicaciones;
	}

}