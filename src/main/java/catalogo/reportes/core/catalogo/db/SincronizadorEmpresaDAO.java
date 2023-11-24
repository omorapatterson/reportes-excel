package catalogo.reportes.core.catalogo.db;

import catalogo.reportes.core.afiliados.afiliadosRepositories.IAfiliadosEmpresaRepository;
import catalogo.reportes.core.afiliados.afiliadosRepositories.IAfiliadosUbicacionRepository;
import catalogo.reportes.core.pedidos.services.interfaces.IDatosAfiliadosService;
import common.rondanet.catalogo.core.entity.*;
import common.rondanet.clasico.core.afiliados.models.Empresa;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SincronizadorEmpresaDAO {

    Logger logger = LogManager.getLogger(SincronizadorEmpresaDAO.class);

    @Autowired
    BajasDAO bajasDAO;

    @Autowired
    EmpresasDAO empresasDAO;

    @Autowired
    ParamsDAO paramsDAO;

    @Autowired
    UsuariosDAO usuariosDAO;

    @Autowired
    UsuarioEmpresaDAO usuarioEmpresaDAO;

    @Autowired
    RolesDAO rolesDAO;

    IDatosAfiliadosService datosAfiliadosService;

    @Autowired
    IAfiliadosEmpresaRepository afiliadosEmpresaRepository;

    @Autowired
    IAfiliadosUbicacionRepository afiliadosUbicacionRepository;

    List<Rol> rolesEmpresa = new ArrayList<Rol>();

    boolean actualizarRoles = false;

    public SincronizadorEmpresaDAO(
            EmpresasDAO empresasDAO, UsuariosDAO usuariosDAO,
            UsuarioEmpresaDAO usuarioEmpresaDAO,
            RolesDAO rolesDAO,
            ParamsDAO paramsDAO,
            IDatosAfiliadosService datosAfiliadosService
    ) {
        this.empresasDAO = empresasDAO;
        this.usuariosDAO = usuariosDAO;
        this.datosAfiliadosService = datosAfiliadosService;
        this.usuarioEmpresaDAO = usuarioEmpresaDAO;
        this.rolesDAO = rolesDAO;
        this.rolesEmpresa = this.rolesDAO.findAll();
        this.paramsDAO = paramsDAO;
    }

    public common.rondanet.catalogo.core.entity.Empresa obtenerDatosDeEmpresaEmpresa(Empresa empresaASincronizar) {
        common.rondanet.catalogo.core.entity.Empresa empresa = null;
        try {
            empresa = actualizarEmpresa(empresaASincronizar);
        } catch (Exception e) {
            logger.log(Level.INFO, "Ocurrió un error al sincronizar las empresas. Error: " + e.getMessage());
        }
        return empresa;
    }

    public common.rondanet.catalogo.core.entity.Empresa actualizarEmpresa(
            Empresa empresaASincronizar
    ){
        common.rondanet.catalogo.core.entity.Empresa empresa = new common.rondanet.catalogo.core.entity.Empresa();
        empresa.setRut(empresaASincronizar.getRuc());
        empresa.setCodigoInterno(empresaASincronizar.getId().getCodigoInternoEmpresa());
        empresa.setRazonSocial(empresaASincronizar.getRazonSocial());
        empresa.setNombre(empresaASincronizar.getNombreEmpresa());
        empresa.setEmail(empresaASincronizar.getEmail());
        if (empresaASincronizar.getFechaDeBaja() == null) {
            empresa.setActivo(true);
            empresa.setEliminado(false);
        } else {
            empresa.setActivo(false);
            empresa.setEliminado(true);
        }
        empresa.setValidado(true);
        empresa.setEliminado(false);
        empresa.setFechaCreacion(new DateTime(empresaASincronizar.getFechaIngreso()));
        empresa.setFechaEdicion(new DateTime());
        actualizarPrefijosDeLaEmpresa(empresa, empresaASincronizar);
        actualizarDatosDeAfiliadosRondanet(empresa, empresaASincronizar);
        obtenerUbicacion(empresa);
        return empresa;
    }

    public void obtenerUbicacion(
            common.rondanet.catalogo.core.entity.Empresa empresa
    ) {
        List<common.rondanet.clasico.core.afiliados.models.Ubicacion> ubicaciones = afiliadosUbicacionRepository.obtenerUbicaciones(empresa.getCodigoInterno());
        for (common.rondanet.clasico.core.afiliados.models.Ubicacion ubicacion: ubicaciones) {
            if (ubicacion.getTipoUbicacion().equals("E")) {
                empresa.setGln(ubicacion.getId().getCodigo());
                break;
            }

        }
    }

    public void actualizarPrefijosDeLaEmpresa(
            common.rondanet.catalogo.core.entity.Empresa empresa,
            Empresa empresaASincronizar
    ) {
        Set<String> prefijosEmpresa = this.afiliadosEmpresaRepository.findAllPrefijosEmpresa(empresaASincronizar.getRuc());
        empresa.setPrefijos(prefijosEmpresa);
    }

    public void actualizarDatosDeAfiliadosRondanet(
            common.rondanet.catalogo.core.entity.Empresa empresa,
            Empresa empresaASincronizar
    ) {
        empresa.setUtilizaRondanet(!empresaASincronizar.getUsaEdi().toLowerCase().contains("n"));
        empresa.setSubcuenta(empresaASincronizar.getSubcuenta());
        empresa.setCodigoLocalidad(empresaASincronizar.getCodigoLocalidad());
        empresa.setCodigoPostal(empresaASincronizar.getCodigoPostal());
        empresa.setDireccion(empresaASincronizar.getDireccion());
        empresa.setTelefono(empresaASincronizar.getTelefono());
        empresa.setFax(empresaASincronizar.getFax());
        empresa.setRepresentanteEmpresa(empresaASincronizar.getRepresentanteEmpresa());
        empresa.setResponsableCodificacion(empresaASincronizar.getResponsableCodificacion());
        empresa.setContactoEdi(empresaASincronizar.getContactoEdi());
        empresa.setCargoDelRepresentante(empresaASincronizar.getCargoDelRepresentante());
        empresa.setCargoDelResponsable(empresaASincronizar.getCargoDelResponsable());
        empresa.setCargoDelContanctoEdi(empresaASincronizar.getCargoDelContanctoEdi());
        empresa.setUsaEdi(empresaASincronizar.getUsaEdi());
        empresa.setFormatoEnQueRecibe(empresaASincronizar.getFormatoEnQueRecibe());
        empresa.setEmpresaReceptoraDeReclamosDeFacturasAPagar(empresaASincronizar.getEmpresaReceptoraDeReclamosDeFacturasAPagar());
        empresa.setRamaDeActividad(empresaASincronizar.getRamaDeActividad());
        empresa.setFechaIngreso(empresaASincronizar.getFechaIngreso());
        empresa.setTipoAfiliado(empresaASincronizar.getTipoAfiliado());
        empresa.setPublica(empresaASincronizar.getPublica());
        empresa.setDatosParaEtiqueta(empresaASincronizar.getDatosParaEtiqueta());
        empresa.setComentario(empresaASincronizar.getComentario());
        empresa.setPaginaWeb(empresaASincronizar.getPaginaWeb());
        empresa.setFechaDeBaja(empresaASincronizar.getFechaDeBaja() != null ? new DateTime(empresaASincronizar.getFechaDeBaja()) : null);
        empresa.setCausaDeLaBaja(empresaASincronizar.getCausaDeLaBaja());
        empresa.setMesDeVencimiento(empresaASincronizar.getMesDeVencimiento());
        empresa.setCondFacturacionEdiClasico(empresaASincronizar.getCondFacturacionEdiClasico());
        empresa.setTipoDeCuota(empresaASincronizar.getTipoDeCuota());
        empresa.setTipoCuotaDeEmpresa(empresaASincronizar.getTipoCuotaDeEmpresa());
        empresa.setCuotMin(empresaASincronizar.getCuotMin());
        empresa.setCantMin(empresaASincronizar.getCantMin());
        empresa.setPrecioPorCuotaAdicional(empresaASincronizar.getPrecioPorCuotaAdicional());
        empresa.setDescuentoEspecialSobreProductos(empresaASincronizar.getDescuentoEspecialSobreProductos());
        empresa.setDescuentoEspecialSobreElTotal(empresaASincronizar.getDescuentoEspecialSobreElTotal());
        empresa.setMonedaFacturacion(empresaASincronizar.getMonedaFacturacion());
        empresa.setMesesFactura(empresaASincronizar.getMesesFactura());
        empresa.setCuotaPorUsoPOC(empresaASincronizar.getCuotaPorUsoPOC());
        empresa.setCuotaPorAutoingresoDeOC(empresaASincronizar.getCuotaPorAutoingresoDeOC());
        empresa.setCuotaPorDistribucionDeListasDePrecios(empresaASincronizar.getCuotaPorDistribucionDeListasDePrecios());

    }

}
