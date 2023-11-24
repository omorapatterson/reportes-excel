package catalogo.reportes.core.catalogoViejo.catalogoServices.implementations;

import catalogo.reportes.ReportesApplication;
import catalogo.reportes.core.catalogoViejo.catalogoRepositories.ICatalogoViejoListaPrecioRepository;
import catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces.ICatalogoViejoListaPrecioService;
import catalogo.reportes.core.utils.IEmailHelper;
import common.rondanet.clasico.core.catalogo.models.ListaPrecio;
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
public class CatalogoViejoListaPrecioService implements ICatalogoViejoListaPrecioService {

	Logger logger = LogManager.getLogger(CatalogoViejoListaPrecioService.class);

	@Autowired
	IEmailHelper emailHelper;

	@Autowired
    ICatalogoViejoListaPrecioRepository listaPrecioRepository;

	@Override
	public List<ListaPrecio> GetAll(Date fechaDeActualizacion, int limit, int offset) {
		List<ListaPrecio> precios = new ArrayList<>();
		try {
			precios = this.listaPrecioRepository.findAll(fechaDeActualizacion, limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("ListaDePrecios", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return precios;
	}

	@Override
	public List<BigDecimal> getAllGroupByGln() {
		List<BigDecimal> gruposByGlns = new ArrayList<>();
		try {
			gruposByGlns = this.listaPrecioRepository.findAllGroupByGln();
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("ListaDePrecios", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return gruposByGlns;
	}

	@Override
	public List<String> getAllByGlnGroupByTarget(String gln)  {
		List<String> gruposByGlns = new ArrayList<>();
		try {
		gruposByGlns = this.listaPrecioRepository.findAllByGlnGroupByTarget(gln);
		} catch (Exception e) {
		this.emailHelper.sendErrorEmail("ListaDePrecios", e);

		logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return gruposByGlns;
	}

	@Override
	public List<String> getAllByGlnAndTargetGroupByMoneda(String gln, String target) {
		List<String> modedas = new ArrayList<>();
		try {
			modedas = this.listaPrecioRepository.findAllByGlnAndTargetGroupByMoneda(gln, target);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("ListaDePrecios", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return modedas;
	}

	@Override
	public Date getUltimaFechaVigenciaByGlnAndTargetAndMoneda(String gln, String target, String moneda) {
		Date fechaVigencia = null;
		try {
			fechaVigencia = this.listaPrecioRepository.findUltimaFechaVigenciaByGlnAndTargetAndMoneda(gln, target, moneda);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("ListaDePrecios", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return fechaVigencia;
	}

	@Override
	public List<ListaPrecio> getAllByGlnAndTargetAndMonedaAndFechaVigencia(String gln, String target, String moneda, Date vigencia) {
		List<ListaPrecio> listaPrecios = new ArrayList<>();
		try {
			listaPrecios = this.listaPrecioRepository.findAllByGlnAndTargetAndMonedaAndFechaVigencia(gln, target, moneda, vigencia);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("ListaDePrecios", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return listaPrecios;
	}

	@Override
	public int getTotal(Date fechaDeActualizacion){
		int total = 0;
		try {
			total = this.listaPrecioRepository.getTotal(fechaDeActualizacion);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("ListaDePrecios", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return total;
	}

}
