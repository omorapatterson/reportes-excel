package catalogo.reportes.core.catalogoViejo.catalogoServices.implementations;

import catalogo.reportes.ReportesApplication;
import catalogo.reportes.core.catalogoViejo.catalogoRepositories.ICatalogoViejoImageRepository;
import catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces.ICatalogoViejoImagenesService;
import catalogo.reportes.core.utils.IEmailHelper;
import common.rondanet.clasico.core.catalogo.models.Image;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CatalogoViejoImagenesService implements ICatalogoViejoImagenesService {

	Logger logger = LogManager.getLogger(CatalogoViejoImagenesService.class);

	@Value("${API_URL}")
	String apiUrl;

	@Autowired
	IEmailHelper emailHelper;

	@Autowired
    ICatalogoViejoImageRepository imageRepository;

	@Override
	public List<Image> findAllByGln(Date fechaDeActualizacion, String gln) {
		List<Image> imagenes = new ArrayList<>();
		try {
			imagenes = this.imageRepository.findAllByGln(gln, fechaDeActualizacion);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Imagenes", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return imagenes;
	}

	@Override
	public List<Image> findAllByGln(Date fechaDeActualizacion, String gln, int limit, int offet) {
		List<Image> imagenes = new ArrayList<>();
		try {
			imagenes = this.imageRepository.findAllByGln(gln, fechaDeActualizacion, limit, offet);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Imagenes", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return imagenes;
	}

	@Override
	public List<Image> findAllImagenesByGln(String gln) {
		List<Image> imagenes = new ArrayList<>();
		try {
			imagenes = this.imageRepository.findAllImagenesByGln(gln);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Imagenes", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return imagenes;
	}


	@Override
	public List<Image> findAllImagenesByGln(String gln, int limit, int offet) {
		List<Image> imagenes = new ArrayList<>();
		try {
			imagenes = this.imageRepository.findAllImagenesByGln(gln, limit, offet);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Imagenes", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return imagenes;
	}

	@Override
	public List<BigDecimal> findAllGroupByGln(Date fechaDeActualizacion) {
		List<BigDecimal> groupsByGln = new ArrayList<>();
		try {
			groupsByGln =  this.imageRepository.findAllGroupByGln(fechaDeActualizacion);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Imagenes", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return groupsByGln;
	}

	@Override
	public List<BigDecimal> findAllImagenesGroupByGln() {
		List<BigDecimal> groupsByGln = new ArrayList<>();
		try {
			groupsByGln =  this.imageRepository.findAllImagenesGroupByGln();
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Imagenes", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return groupsByGln;
	}
}