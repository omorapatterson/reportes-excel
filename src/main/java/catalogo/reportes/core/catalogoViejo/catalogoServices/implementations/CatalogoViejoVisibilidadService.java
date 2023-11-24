package catalogo.reportes.core.catalogoViejo.catalogoServices.implementations;

import catalogo.reportes.ReportesApplication;
import catalogo.reportes.core.catalogoViejo.catalogoRepositories.IActualizarVisibilidadRepository;
import catalogo.reportes.core.catalogoViejo.catalogoRepositories.ICatalogoViejoVisibilidadRepository;
import catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces.ICatalogoViejoVisibilidadService;
import catalogo.reportes.core.utils.IEmailHelper;
import common.rondanet.clasico.core.catalogo.models.ActualizarVisibilidad;
import common.rondanet.clasico.core.catalogo.models.VisibilidadProducto;
import common.rondanet.clasico.core.catalogo.models.VisibilidadProductoId;
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
public class CatalogoViejoVisibilidadService implements ICatalogoViejoVisibilidadService {

	Logger logger = LogManager.getLogger(CatalogoViejoVisibilidadService.class);

	@Autowired
	IEmailHelper emailHelper;

	@Autowired
	ICatalogoViejoVisibilidadRepository visibilidadRepository;

	@Autowired
	IActualizarVisibilidadRepository actualizarVisibilidadRepository;

	@Override
	public List<VisibilidadProducto> GetAll(Date fechaDeActualizacion, int limit, int offset) {
		List<VisibilidadProducto> visibilidadProductos = new ArrayList<>();
		try {
			visibilidadProductos = this.visibilidadRepository.findAll(limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return visibilidadProductos;
	}



	@Override
	public List<VisibilidadProducto> findAllByGln(String gln) {
		List<VisibilidadProducto> visibilidadProductos = new ArrayList<>();
		try {
			visibilidadProductos = this.visibilidadRepository.findAllByGln(gln);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return visibilidadProductos;
	}

	@Override
	public List<VisibilidadProducto> findAllByGln(String gln, int limit, int offset) {
		List<VisibilidadProducto> visibilidadProductos = new ArrayList<>();
		try {
			visibilidadProductos = this.visibilidadRepository.findAllByGln(gln, limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return visibilidadProductos;
	}

	@Override
	public List<VisibilidadProducto> findAllProductosPublicoByGln(String gln){
		List<String> visibilidadProductosPublicos = new ArrayList<>();
		try {
			visibilidadProductosPublicos = this.visibilidadRepository.findAllProductosPublicoByGln(gln);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return convertirVisibilidad(gln, visibilidadProductosPublicos);
	}

	@Override
	public List<VisibilidadProducto> findAllProductosPublicoByGln(String gln, int limit, int offset){
		List<String> visibilidadProductosPublicos = new ArrayList<>();
		try {
			visibilidadProductosPublicos = this.visibilidadRepository.findAllProductosPublicoByGln(gln, limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return convertirVisibilidad(gln, visibilidadProductosPublicos);
	}

	@Override
	public List<VisibilidadProducto> convertirVisibilidad(String gln, List<String> cppDeProductosPublicos){
		List<VisibilidadProducto> visibilidadProductos = new ArrayList<>();
		for (String cpp: cppDeProductosPublicos) {
			VisibilidadProducto visibilidadProducto = new VisibilidadProducto();
			VisibilidadProductoId visibilidadProductoId = new VisibilidadProductoId(gln, cpp, "TODOS_PUBLICOS");
			visibilidadProducto.setId(visibilidadProductoId);
			visibilidadProductos.add(visibilidadProducto);
		}
		return visibilidadProductos;
	}

	@Override
	public List<String> GetAllGln(Date fechaDeActualizacion, int limit, int offset) {
		List<String> glns = new ArrayList<>();
		try {
			glns = this.visibilidadRepository.findAllGln(limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return glns;
	}

	@Override
	public List<BigDecimal> findAllGroupByGln() {
		List<BigDecimal> groupsByGln = new ArrayList<>();
		try {
			groupsByGln = this.visibilidadRepository.findAllGroupByGln();
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return groupsByGln;
	}

	@Override
	public List<ActualizarVisibilidad> GetAllGroupByGrupoAndGln(Date fechaDeActualizacion, int limit, int offset){
		List<ActualizarVisibilidad> visibilidadList = new ArrayList<>();
		try {
			visibilidadList = this.actualizarVisibilidadRepository.findAllGroupByGrupoAndGln(limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return visibilidadList;
	}

	@Override
	public int getTotal(){
		int total = 0;
		try {
			total = this.visibilidadRepository.countAllProductosPrivadosByGln();
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return total;
	}

	@Override
	public int countAllProductosPublicosByGln(String gln){
		int total = 0;
		try {
			total = this.visibilidadRepository.countAllProductosPublicosByGln(gln);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return total;
	}

	@Override
	public int countAllProductosPrivadosByGln(String gln){
		int total = 0;
		try {
			total = this.visibilidadRepository.countAllProductosPrivadosByGln(gln);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return total;
	}

	@Override
	public int totalDeProductos(String gln){
		int total = 0;
		try {
			int totalProductosPrivados = this.visibilidadRepository.countAllProductosPrivadosByGln(gln);
			int totalProductosPublicos = this.visibilidadRepository.countAllProductosPublicosByGln(gln);
			total = totalProductosPrivados + totalProductosPublicos;
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Visibilidad", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return total;
	}



}
