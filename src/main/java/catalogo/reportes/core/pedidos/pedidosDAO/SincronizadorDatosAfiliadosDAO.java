package catalogo.reportes.core.pedidos.pedidosDAO;

import catalogo.reportes.core.pedidos.pedidosEntity.DatosAfiliados;
import catalogo.reportes.core.pedidos.services.interfaces.IDatosAfiliadosService;
import common.rondanet.clasico.core.afiliados.models.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SincronizadorDatosAfiliadosDAO {

    @Autowired
    DatosAfiliadosDAO datosAfiliadosDAO;

    @Autowired
    IDatosAfiliadosService datosAfiliadosService;

    public void sincronizarDatosAfiliados(List<Empresa> empresasASincronizar) {
        List<DatosAfiliados> listDatosAfiliados = new ArrayList<>();
        for (Empresa empresaASincronizar: empresasASincronizar) {
            if (empresaASincronizar.getContrasenaRondanet() != null && empresaASincronizar.getRuc() != null && !empresaASincronizar.getRuc().equals("") && empresaASincronizar.getFechaDeBaja() == null) {
                try {
                    Optional<DatosAfiliados> optionalDatosAfiliados = datosAfiliadosDAO.findByRut(empresaASincronizar.getRuc());
                    DatosAfiliados datosAfiliados = getDatosAfiliados(empresaASincronizar, optionalDatosAfiliados);
                    listDatosAfiliados.add(datosAfiliados);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        datosAfiliadosDAO.saveAll(listDatosAfiliados);
    }

    private DatosAfiliados getDatosAfiliados(Empresa empresaASincronizar, Optional<DatosAfiliados> optionalDatosAfiliados) {
        DatosAfiliados datosAfiliados = new DatosAfiliados();
        if (optionalDatosAfiliados.isPresent()) {
            datosAfiliados = optionalDatosAfiliados.get();
        }
        datosAfiliados.setRut(empresaASincronizar.getRuc());
        datosAfiliados.setNroEmpresa(empresaASincronizar.getId().getCodigoInternoEmpresa());
        datosAfiliados.setPassword(empresaASincronizar.getContrasenaRondanet());
        return datosAfiliados;
    }
}
