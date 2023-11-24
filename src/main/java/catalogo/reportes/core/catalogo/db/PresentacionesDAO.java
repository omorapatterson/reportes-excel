package catalogo.reportes.core.catalogo.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import common.rondanet.catalogo.core.entity.Presentacion;
import catalogo.reportes.core.catalogo.repository.IPresentacionRepository;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class PresentacionesDAO {

	Logger logger = LogManager.getLogger(PresentacionesDAO.class);

	@Autowired
	IPresentacionRepository presentacionRepository;

	private final MongoOperations mongoOperations;

	public PresentacionesDAO(IPresentacionRepository presentacionRepository, @Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations) {
		this.presentacionRepository = presentacionRepository;
		this.mongoOperations = mongoOperations;
	}

	public List<Presentacion> getAll() {
		return presentacionRepository.findAll();
	}

	public Optional<Presentacion> findByName(String nombre) {
		Query query = new Query();
		query.addCriteria(Criteria.where("nombre").is(nombre));
		List<Presentacion> presentaciones = mongoOperations.find(query, Presentacion.class);

		if (presentaciones.isEmpty()) {
			logger.log(Level.INFO, "El método findByName() de la clase PresentacionesDAO fue ejecutado.");
			return Optional.ofNullable(null);
		}
		logger.log(Level.INFO, "El método findByName() de la clase PresentacionesDAO fue ejecutado.");
		return Optional.of(presentaciones.get(0));
	}

}