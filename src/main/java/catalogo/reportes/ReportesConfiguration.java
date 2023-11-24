package catalogo.reportes;

import catalogo.reportes.core.catalogo.db.EmpresasDAO;
import catalogo.reportes.core.catalogo.db.UbicacionesDAO;
import common.rondanet.catalogo.core.entity.Param;
import catalogo.reportes.core.catalogo.repository.IParamRepository;
import catalogo.reportes.core.catalogo.utils.Propiedades;
import catalogo.reportes.core.utils.DespliegueConfiguration;
import catalogo.reportes.core.utils.S3Config;
import catalogo.reportes.core.utils.mandrill.MandrillConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ReportesConfiguration {
    @Autowired
    IParamRepository paramRepository;

    @Autowired
    EmpresasDAO empresasDAO;

    @Autowired
    UbicacionesDAO ubicacionesDAO;

    private DespliegueConfiguration configuracionDespliegue = new DespliegueConfiguration();

    public DespliegueConfiguration getConfiguracionDespliegue() {
        return this.configuracionDespliegue;
    }

    public void setConfiguracionDespliegue(DespliegueConfiguration despliegue) {
        this.configuracionDespliegue = despliegue;
    }

    @Value("${RUT_DE_EMPRESAS_A_SINCRONIZAR}")
    public String sempresasASincronizar;

    @Value("${RUT_DE_EMPRESAS_A_SINCRONIZAR_VISIBILIDAD}")
    public String sempresasASincronizarVisibilidad;

    @Value("${RUT_DE_EMPRESAS_A_SINCRONIZAR_HACIA_ATRAS}")
    public String sempresasASincronizarHaciaAtras;

    private List<String> empresasASincronizar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportesConfiguration)) return false;
        ReportesConfiguration that = (ReportesConfiguration) o;
        return Objects.equals(getConfiguracionDespliegue(), that.getConfiguracionDespliegue());
    }

    @PostConstruct
    public void init() {
        cargarConfiguracion();
    }

    public void cargarConfiguracion() {

        Param despliqueFrontend = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_FRONTEND).orElse(null);
        Param despliqueTerceros = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_TERCEROS).orElse(null);
        Param despliqueCors = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_CORS).orElse(null);
        Param despliqueBucket = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_BUCKET).orElse(null);
        Param despliqueBucketUrl = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_BUCKET_URL).orElse(null);

        Param s3Id = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_S3_S3Id).orElse(null);
        Param s3Api = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_S3_S3APIKEY).orElse(null);

        Param mandrillFromEmail = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_MANDRILL_MAIL).orElse(null);
        Param mandrillApiKey = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_MANDRILL_APIKEY).orElse(null);
        Param mandrillUrlFronted = paramRepository.findByNombreAndEliminadoIsFalse(Propiedades.DW_DESPLIEGUE_MANDRILL_URL).orElse(null);

        S3Config s3Config = new S3Config(s3Id.getValor(), s3Api.getValor());

        MandrillConfiguration mandrillConfiguration = new MandrillConfiguration(mandrillFromEmail.getValor(), mandrillApiKey.getValor(),mandrillUrlFronted.getValor());

        DespliegueConfiguration despliegueConfiguration = new DespliegueConfiguration(
                Boolean.valueOf(despliqueFrontend.getValor()),
                Boolean.valueOf(despliqueTerceros.getValor()),
                despliqueCors.getValor(),
                despliqueBucket.getValor(),
                despliqueBucketUrl.getValor(),
                mandrillConfiguration,
                s3Config
        );

        this.configuracionDespliegue = despliegueConfiguration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConfiguracionDespliegue());
    }

    @Override
    public String toString() {
        return "CatalogoConfiguration{" +
                ", configuracionDespliegue=" + configuracionDespliegue +
                '}';
    }

}
