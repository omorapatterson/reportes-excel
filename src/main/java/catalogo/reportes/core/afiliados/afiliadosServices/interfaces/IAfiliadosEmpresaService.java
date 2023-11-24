package catalogo.reportes.core.afiliados.afiliadosServices.interfaces;

import common.rondanet.clasico.core.afiliados.models.Empresa;
import common.rondanet.clasico.core.afiliados.models.Ubicacion;

import java.util.Date;
import java.util.List;

public interface IAfiliadosEmpresaService {

    public List<Empresa> GetAll( Date fechaDeActualizacion, int limit, int offset);

    public int getTotal(Date fechaDeActualizacion);

    Empresa obtenerEmpresaPorGln(String gln);

    Empresa obtenerEmpresaPorCodigoInterno(String codigoInterno);

    List<Ubicacion> obtenerEmpresaPorGln();
}
