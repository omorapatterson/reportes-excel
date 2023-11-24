package catalogo.reportes.core.pedidos.services.implementations;

import catalogo.reportes.core.pedidos.pedidosDAO.SincronizadorDatosAfiliadosDAO;
import catalogo.reportes.core.pedidos.services.interfaces.IPedidosSincronizadorService;
import common.rondanet.clasico.core.afiliados.models.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PedidosSincronizadorService implements IPedidosSincronizadorService {

	@Autowired
	SincronizadorDatosAfiliadosDAO sincronizadorDatosAfiliadosDAO;

	public void sincronizarDatosAfiliados(List<Empresa> empresasASincronizar) {
			sincronizadorDatosAfiliadosDAO.sincronizarDatosAfiliados(empresasASincronizar);
	}
}
