package catalogo.reportes.core.afiliados.afiliadosServices.interfaces;

import common.rondanet.clasico.core.afiliados.models.Ubicacion;

import java.util.Date;
import java.util.List;

public interface IAfiliadosUbicacionService {

    public List<Ubicacion> GetAll(Date fechaDeActualizacion, int limit, int offset);

}
