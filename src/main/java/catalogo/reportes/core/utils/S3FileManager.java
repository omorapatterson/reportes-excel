package catalogo.reportes.core.utils;

import catalogo.reportes.ReportesConfiguration;
import catalogo.reportes.core.catalogo.db.ParamsDAO;
import common.rondanet.catalogo.core.entity.Param;
import catalogo.reportes.core.config.CatalogoConfiguration;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class S3FileManager implements IS3FileManager {

    Logger logger = LogManager.getLogger(S3FileManager.class);

    private String bucketName;

    private String bucketUrl;

    private String keyId;

    private String key;

    private AWSCredentials credentials;

    @Autowired
    private ParamsDAO paramsDAO;

    private ReportesConfiguration configuration;

    public S3FileManager(ReportesConfiguration configuration, ParamsDAO paramsDAO) {
        this.configuration = configuration;
        this.paramsDAO = paramsDAO;
        this.bucketName = this.configuration.getConfiguracionDespliegue().getBucket();
        this.bucketUrl = this.configuration.getConfiguracionDespliegue().getBucketUrl();
        this.key = this.configuration.getConfiguracionDespliegue().getS3().getS3Key();
        this.keyId = this.configuration.getConfiguracionDespliegue().getS3().getS3Id();
        this.credentials = new BasicAWSCredentials(keyId, key);
    }

    @Override
    public InputStream getFile(String url) {
        try {
            AmazonS3 s3Client = this.crearConexion();
            S3Object object = s3Client.getObject(bucketName, url);
            InputStream file = object.getObjectContent();
            return file;
        } catch (AmazonServiceException e) {
            logger.log(Level.ERROR, "uploadFile Error:", e.getMessage(), ExceptionUtils.getStackTrace(e));
        } catch (SdkClientException e) {
            logger.log(Level.ERROR, "uploadFile Error:", e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public String uploadFile(String contentType, ByteArrayInputStream byteArray, long contentLength, String url) {
        try {
            AmazonS3 s3Client = this.crearConexion();
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, url));
            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(contentType);
            objectMetaData.setContentLength(contentLength);
            s3Client.putObject(new PutObjectRequest(bucketName, url, byteArray, objectMetaData)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            logger.log(Level.ERROR, "uploadFile Error:", e.getMessage(), ExceptionUtils.getStackTrace(e));
        } catch (SdkClientException e) {
            logger.log(Level.ERROR, "uploadFile Error:", e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
        return bucketUrl + "/" + url;
    }

    public AmazonS3 crearConexion(){
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(obtenerRegion())
                .withCredentials(new AWSStaticCredentialsProvider(this.credentials)).build();
    }

    String obtenerRegion(){
        String region = "us-east-2";
        Param param = paramsDAO.findByNombre(Propiedades.AWS_REGION);
        if(param != null){
            region = param.getNombre();
        }
        return region;
    }

}