package catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces;

import common.rondanet.clasico.core.catalogo.models.ListaPrecio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ICatalogoViejoListaPrecioService {

    public List<ListaPrecio> GetAll(Date fechaDeActualizacion, int limit, int offset);

    List<BigDecimal> getAllGroupByGln();

    List<String> getAllByGlnGroupByTarget(String gln);

    List<String> getAllByGlnAndTargetGroupByMoneda(String gln, String target);

    Date getUltimaFechaVigenciaByGlnAndTargetAndMoneda(String gln, String target, String moneda);

    List<ListaPrecio> getAllByGlnAndTargetAndMonedaAndFechaVigencia(String gln, String target, String moneda, Date vigencia);

    public int getTotal(Date fechaDeActualizacion);
}
