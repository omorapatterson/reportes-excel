package catalogo.reportes.core.catalogoViejo.catalogoServices.interfaces;

import common.rondanet.clasico.core.catalogo.models.Grupo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ICatalogoViejoGruposService {

    public List<Grupo> getAll();

    public List<Grupo> findAllByGln(String gln);

    List<Grupo> findAllByGln(String gln, int limit, int offet);

    List<String> findAllByGlnGroupByNombre(String gln);

    List<BigDecimal> getAllGroupByGln();

    public int getTotal(Date fechaDeActualizacion);
}
