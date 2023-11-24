package catalogo.reportes.core.afiliados.afiliadosServices.implementations;

import catalogo.reportes.core.afiliados.afiliadosRepositories.IAfiliadosEmpresaRepository;
import catalogo.reportes.core.afiliados.afiliadosRepositories.IAfiliadosUbicacionRepository;
import catalogo.reportes.core.afiliados.afiliadosServices.interfaces.IAfiliadosEmpresaService;
import catalogo.reportes.core.utils.IEmailHelper;
import common.rondanet.clasico.core.afiliados.models.Ubicacion;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import common.rondanet.clasico.core.afiliados.models.Empresa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AfiliadosEmpresaService implements IAfiliadosEmpresaService {

	Logger logger = LogManager.getLogger(AfiliadosEmpresaService.class);

	@Autowired
	IEmailHelper emailHelper;

	@Autowired
	IAfiliadosEmpresaRepository afiliadosEmpresaRepository;

	@Autowired
	IAfiliadosUbicacionRepository afiliadosUbicacionRepository;

	@Override
	public List<Empresa> GetAll(Date fechaDeActualizacion, int limit, int offset) {
		List<Empresa> empresas = new ArrayList<>();
		try {
			empresas = this.afiliadosEmpresaRepository.findAll(fechaDeActualizacion, limit, offset);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Empresa", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return empresas;
	}

	@Override
	public int getTotal(Date fechaDeActualizacion){
		int total = 0;
		try{
			total = this.afiliadosEmpresaRepository.getTotal(fechaDeActualizacion);
		} catch (Exception e) {
			this.emailHelper.sendErrorEmail("Empresa", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return total;
	}

	@Override
	public Empresa obtenerEmpresaPorGln(String gln){
		Empresa empresa = null;
		try{
			String codigoEmpresa = this.afiliadosUbicacionRepository.obtenerCodigoEmpresa(gln);
			empresa = this.afiliadosEmpresaRepository.obtenerEmpresaPorCodigoInterno(codigoEmpresa);
		} catch (Exception e) {
			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return empresa;
	}

	@Override
	public Empresa obtenerEmpresaPorCodigoInterno(String codigoInterno){
		Empresa empresa = null;
		try{
			empresa = this.afiliadosEmpresaRepository.findByCodigoInterno(codigoInterno);
		} catch (Exception e) {
			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return empresa;
	}

	@Override
	public List<Ubicacion> obtenerEmpresaPorGln(){
		List<Ubicacion> ubicaciones = new ArrayList<>();
		try{
			ubicaciones = this.afiliadosUbicacionRepository.obtenerUbicacionesPorGln();
		} catch (Exception e) {
			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return ubicaciones;
	}

}
