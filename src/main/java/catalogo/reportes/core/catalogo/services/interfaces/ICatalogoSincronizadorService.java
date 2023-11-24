package catalogo.reportes.core.catalogo.services.interfaces;

import common.rondanet.catalogo.core.entity.ListaDeVenta;
import catalogo.reportes.core.catalogo.resources.GruposYEmpresas;
import common.rondanet.clasico.core.afiliados.models.Empresa;
import common.rondanet.clasico.core.afiliados.models.Ubicacion;
import common.rondanet.clasico.core.catalogo.models.*;

import java.util.List;

public interface ICatalogoSincronizadorService {

    public void sincronizarEmpresas(List<Empresa> empresasASincronizar);

    public void sincronizarUbicaciones(List<Ubicacion> ubicacionesASincronizar);

    void enviarPorcientoDeActualizacionGrupos(String mensaje);

    void actualizarGruposAntesDeSincronizar(common.rondanet.catalogo.core.entity.Empresa empresa, String gln);

    void actualizarGruposDespuesDeSincronizar(common.rondanet.catalogo.core.entity.Empresa empresa, String gln);

    void eliminarGruposNoActualizados(common.rondanet.catalogo.core.entity.Empresa empresa);

    public void sincronizarProductos(List<ProductoGtin> productosASincronizar);

    public void sincronizarGrupos(List<Grupo> gruposASincronizar, common.rondanet.catalogo.core.entity.Empresa empresa);

    ListaDeVenta obtenerListaDeVenta(common.rondanet.catalogo.core.entity.Ubicacion ubicacion, String gln, common.rondanet.catalogo.core.entity.Empresa empresa);

    void sincronizarProductosEliminadosDelCatalogoViejo();

    public void sincronizarListaPrecio(List<ListaPrecio> listaPrecios);

    void sincronizarVisibilidad(List<VisibilidadProducto> visibilidadProductosASincronizar, ListaDeVenta listaDeVenta, String gln, long totalRegistros);

    void sincronizarVisibilidadProductos(List<VisibilidadProducto> visibilidadProductosASincronizar, common.rondanet.catalogo.core.entity.Empresa empresa, GruposYEmpresas gruposYEmpresas, long totalRegistros);

    void verificarVisibilidad();
}
