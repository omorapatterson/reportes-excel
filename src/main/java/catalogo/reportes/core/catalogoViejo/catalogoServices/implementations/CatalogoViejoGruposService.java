package catalogo.reportes.core.catalogoViejo.catalogoServices.implementations;

import catalogo.reportes.ReportesApplication;
import catalogo.reportes.core.catalogoViejo.catalogoRepositories.ICatalogoViejoGrupoRepository;
import catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces.ICatalogoViejoGruposService;
import catalogo.reportes.core.utils.IEmailHelper;
import common.rondanet.clasico.core.catalogo.models.Grupo;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CatalogoViejoGruposService implements ICatalogoViejoGruposService {

	Logger logger = LogManager.getLogger(CatalogoViejoGruposService.class);

	@Autowired
	IEmailHelper emailHelper;

	@Autowired
	ICatalogoViejoGrupoRepository grupoRepository;

	@Override
	public List<Grupo> getAll() {
		List<Grupo> grupos = new ArrayList<>();
		try {
			grupos = this.grupoRepository.findAll();
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Grupos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return grupos;
	}

	@Override
	public List<Grupo> findAllByGln(String gln) {
		List<Grupo> grupos = new ArrayList<>();
		try {
			grupos = this.grupoRepository.findAllByGln(gln);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Grupos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return grupos;
	}

	@Override
	public List<Grupo> findAllByGln(String gln, int limit, int offet) {
		List<Grupo> grupos = new ArrayList<>();
		try {
			grupos = this.grupoRepository.findAllByGln(gln, limit, offet);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Grupos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return grupos;
	}

	@Override
	public List<String> findAllByGlnGroupByNombre(String gln) {
		List<String> grupos = new ArrayList<>();
		try {
			grupos = this.grupoRepository.findAllByGlnGroupByNombre(gln);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Grupos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return grupos;
	}

	@Override
	public List<BigDecimal> getAllGroupByGln() {
		List<BigDecimal> gruposByGlns = new ArrayList<>();
		try {
			gruposByGlns = this.grupoRepository.findAllGroupByGln();
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Grupos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return gruposByGlns;
	}

	@Override
	public int getTotal(Date fechaDeActualizacion){
		int total = 0;
		try {
			total = this.grupoRepository.getTotal(fechaDeActualizacion);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Grupos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return total;
	}

}
