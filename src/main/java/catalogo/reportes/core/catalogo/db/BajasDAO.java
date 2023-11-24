package catalogo.reportes.core.catalogo.db;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import common.rondanet.catalogo.core.entity.Baja;
import catalogo.reportes.core.catalogo.repository.IBajaRepository;
import org.joda.time.DateTime;

@Component
public class BajasDAO {

	Logger logger = LogManager.getLogger(BajasDAO.class);

	@Autowired
	IBajaRepository bajaRepository;

	BajasDAO(IBajaRepository bajaRepository){
		this.bajaRepository = bajaRepository;
	}

	public Baja findById(String id) {
		return bajaRepository.findById(id).get();
	}

	/**
	 * Inserta una {@link Baja} y establece el {@link DateTime} de la fecha de edición y de creación
	 * @param toAdd {@link Baja}
	 * @return {@link Baja}
	 */
	public Baja insert(Baja toAdd) {
		toAdd.setFechaCreacion();
		toAdd.setFechaEdicion();
		logger.log(Level.INFO, "El método insert() de la clase BajasDAO fue ejecutado.");
		return bajaRepository.save(toAdd);
	}

	/**
	 * Actualiza el {@link DateTime} fecha de edicion con la fecha del instante
	 * de la actualización de una {@link Baja} y la actualiza la {@link Baja}
	 * @param toUpdate
	 */
	public void update(Baja toUpdate) {
		toUpdate.setFechaEdicion();
		bajaRepository.save(toUpdate);
		logger.log(Level.INFO, "El método update() de la clase BajasDAO fue ejecutado.");
	}
}
