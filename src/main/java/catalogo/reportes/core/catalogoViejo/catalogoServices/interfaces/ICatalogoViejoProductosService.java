package catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces;

import common.rondanet.clasico.core.catalogo.models.ProductoGtin;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ICatalogoViejoProductosService {

    public List<ProductoGtin> GetAll(Date fechaDeActualizacion, int limit, int offset);

    List<ProductoGtin> findAllByGln(String gln, Date fechaDeActualizacion);

    List<ProductoGtin> findAllByGln(String gln, Date fechaDeActualizacion, int limit, int offset);

    List<ProductoGtin> findAllByGln(String gln, int limit, int offset);

    List<BigDecimal> findAllGroupByGln();

    int totalDeProductosConGTIN13(String gln);

    int totalDeProductosConGTIN14(String gln);

    Date ultimaFechaDeActualizacion(String gln);

    public int getTotal(Date fechaDeActualizacion);

    public int getTotalProductosVisibles(String gln, List<String> glns);
}
