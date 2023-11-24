package catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces;

import common.rondanet.clasico.core.catalogo.models.ActualizarVisibilidad;
import common.rondanet.clasico.core.catalogo.models.VisibilidadProducto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ICatalogoViejoVisibilidadService {

    public List<VisibilidadProducto> GetAll(Date fechaDeActualizacion, int limit, int offset);

    List<VisibilidadProducto> findAllByGln(String gln, int limit, int offset);

    List<VisibilidadProducto> findAllByGln(String gln);

    List<VisibilidadProducto> findAllProductosPublicoByGln(String gln);

    List<VisibilidadProducto> findAllProductosPublicoByGln(String gln, int limit, int offset);

    List<VisibilidadProducto> convertirVisibilidad(String gln, List<String> cppDeProductosPublicos);

    public List<String> GetAllGln(Date fechaDeActualizacion, int limit, int offset);

    List<BigDecimal> findAllGroupByGln();

    public List<ActualizarVisibilidad> GetAllGroupByGrupoAndGln(Date fechaDeActualizacion, int limit, int offset);

    public int getTotal();

    int countAllProductosPublicosByGln(String gln);

    int countAllProductosPrivadosByGln(String gln);

    int totalDeProductos(String gln);
}
