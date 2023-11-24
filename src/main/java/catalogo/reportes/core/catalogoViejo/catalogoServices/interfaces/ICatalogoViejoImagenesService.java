package catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces;

import common.rondanet.clasico.core.catalogo.models.Image;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ICatalogoViejoImagenesService {

    List<Image> findAllByGln(Date fechaDeActualizacion, String gln);

    List<Image> findAllByGln(Date fechaDeActualizacion, String gln, int limit, int offet);

    List<Image> findAllImagenesByGln(String gln);

    List<Image> findAllImagenesByGln(String gln, int rows, int to);

    List<BigDecimal> findAllGroupByGln(Date fechaDeActualizacion);

    List<BigDecimal> findAllImagenesGroupByGln();
}
