package catalogo.reportes.core.afiliados.afiliadosServices.interfaces;

import java.util.Date;

public interface IAfiliadosSincronizadorService {

    public boolean sincronizar(Date FechaDeActualizacion);

    public boolean sincronizarEmpresas(Date fechaDeActualizacion, String token);

    public boolean sincronizarUbicaciones(Date fechaDeActualizacion, String token);
}
