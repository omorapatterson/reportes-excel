package catalogo.reportes.core.catalogoViejo.catalogoServices.implementations;

import catalogo.reportes.ReportesConfiguration;
import catalogo.reportes.core.catalogoViejo.catalogoRepositories.ICatalogoViejoProductoGtinRepository;
import catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces.ICatalogoViejoProductosService;
import catalogo.reportes.core.utils.IEmailHelper;
import common.rondanet.clasico.core.catalogo.models.ProductoGtin;
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
public class CatalogoViejoProductosService implements ICatalogoViejoProductosService {

	Logger logger = LogManager.getLogger(CatalogoViejoProductosService.class);

	private ReportesConfiguration sincronizadorConfiguration;

	@Autowired
	IEmailHelper emailHelper;

	@Autowired
    ICatalogoViejoProductoGtinRepository productoRepository;

	public CatalogoViejoProductosService(ReportesConfiguration sincronizadorConfiguration) {
		this.sincronizadorConfiguration = sincronizadorConfiguration;
	}

	@Override
	public List<ProductoGtin> GetAll(Date fechaDeActualizacion, int limit, int offset) {
		List<ProductoGtin> productos = new ArrayList<>();
		try {
			productos = this.productoRepository.findAll(fechaDeActualizacion, limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Productos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return productos;
	}

	@Override
	public List<ProductoGtin> findAllByGln(String gln, Date fechaDeActualizacion) {
		List<ProductoGtin> productos = new ArrayList<>();
		try {
			productos = this.productoRepository.findAllByGln(gln, fechaDeActualizacion);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Productos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return productos;
	}

	@Override
	public List<ProductoGtin> findAllByGln(String gln, Date fechaDeActualizacion, int limit, int offset) {
		List<ProductoGtin> productos = new ArrayList<>();
		try {
			productos = this.productoRepository.findAllByGln(gln, fechaDeActualizacion, limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Productos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return productos;
	}

	@Override
	public List<ProductoGtin> findAllByGln(String gln, int limit, int offset) {
		List<ProductoGtin> productos = new ArrayList<>();
		try {
			productos = this.productoRepository.findAllByGln(gln, limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Productos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return productos;
	}

	@Override
	public List<BigDecimal> findAllGroupByGln() {
		List<BigDecimal> productosGroupByGln = new ArrayList<>();
		try {
			productosGroupByGln = this.productoRepository.findAllGroupByGln();
		} catch (Exception e) {
			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return productosGroupByGln;
	}

	@Override
	public int totalDeProductosConGTIN13(String gln) {
		int  totalDeProductosConGTIN13 = 0;
		try {
			totalDeProductosConGTIN13 = this.productoRepository.totalDeProductosConGTIN13(gln);
		} catch (Exception e) {
			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return totalDeProductosConGTIN13;
	}

	@Override
	public int totalDeProductosConGTIN14(String gln) {
		int  totalDeProductosConGTIN14 = 0;
		try {
			totalDeProductosConGTIN14 = this.productoRepository.totalDeProductosConGTIN14(gln);
		} catch (Exception e) {
			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return totalDeProductosConGTIN14;
	}

	@Override
	public Date ultimaFechaDeActualizacion(String gln) {
		Date ultimaFechaDeActualizacion = null;
		try {
			ultimaFechaDeActualizacion = this.productoRepository.ultimaFechaDeActualizacion(gln);
		} catch (Exception e) {
			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return ultimaFechaDeActualizacion;
	}

	@Override
	public int getTotal(Date fechaDeActualizacion){
		int total = 0;
		try {
			total = this.productoRepository.getTotal(fechaDeActualizacion);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Productos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return total;
	}

	@Override
	public int getTotalProductosVisibles(String gln, List<String> glns){
		int total = 0;
		try {
			total = this.productoRepository.getTotalProductosVisibles(gln, glns);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Productos", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return total;
	}

}
