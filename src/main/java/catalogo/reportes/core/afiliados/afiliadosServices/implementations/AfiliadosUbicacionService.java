package catalogo.reportes.core.afiliados.afiliadosServices.implementations;

import catalogo.reportes.ReportesApplication;
import catalogo.reportes.core.afiliados.afiliadosRepositories.IAfiliadosUbicacionRepository;
import catalogo.reportes.core.afiliados.afiliadosServices.interfaces.IAfiliadosUbicacionService;
import catalogo.reportes.core.utils.IEmailHelper;
import common.rondanet.clasico.core.afiliados.models.Ubicacion;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AfiliadosUbicacionService implements IAfiliadosUbicacionService {

	Logger logger = LogManager.getLogger(AfiliadosUbicacionService.class);

	@Autowired
	IEmailHelper emailHelper;

	@Autowired
	IAfiliadosUbicacionRepository afiliadosUbicacionRepository;

	@Override
	public List<Ubicacion> GetAll(Date fechaDeActualizacion, int limit, int offset) {
		List<Ubicacion> ubicaciones = new ArrayList<>();
		try{
			ubicaciones = this.afiliadosUbicacionRepository.findAll(fechaDeActualizacion, limit, offset);
		 }catch (Exception e) {
			this.emailHelper.sendErrorEmail("Ubicacion", e);

			logger.log(Level.ERROR,"Posible Error de conexion a la base de datos" + e.getMessage() +" "+ e.getStackTrace());
		}
		return ubicaciones;
	}

}
