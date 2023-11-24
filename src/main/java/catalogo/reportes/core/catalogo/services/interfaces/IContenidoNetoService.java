package catalogo.reportes.core.catalogo.services.interfaces;

import catalogo.reportes.core.catalogo.exceptions.ServiceException;
import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Producto;
import common.rondanet.clasico.core.catalogo.models.Image;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IContenidoNetoService {

    List<Producto> obtenerUnidadesDeContenidoNeto(
            List<common.rondanet.clasico.core.afiliados.models.Producto> productosAfiliado
    );
}

