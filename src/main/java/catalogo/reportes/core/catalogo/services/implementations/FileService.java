package catalogo.reportes.core.catalogo.services.implementations;

import catalogo.reportes.ReportesConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import common.rondanet.clasico.core.catalogo.models.Image;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import catalogo.reportes.core.catalogo.db.EmpresasDAO;
import catalogo.reportes.core.catalogo.db.ParamsDAO;
import catalogo.reportes.core.catalogo.db.ProductosDAO;
import catalogo.reportes.core.catalogo.db.UbicacionesDAO;
import common.rondanet.catalogo.core.entity.*;
import catalogo.reportes.core.catalogo.services.interfaces.IFileService;
import catalogo.reportes.core.catalogo.utils.Propiedades;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService implements IFileService {

	Logger logger = LogManager.getLogger(FileService.class);

	@Autowired
	private ParamsDAO paramsDAO;
	@Autowired
	private ReportesConfiguration configuration;
	@Autowired
	private ProductosDAO productosDAO;
	@Autowired
	EmpresasDAO empresasDAO;
	@Autowired
	UbicacionesDAO ubicacionesDAO;

	public FileService(ParamsDAO paramsDAO, ProductosDAO productosDAO, ReportesConfiguration configuration) {
		this.paramsDAO = paramsDAO;
		this.configuration = configuration;
		this.productosDAO = productosDAO;
	}

	@Override
	public boolean SaveFileByGlnBussisnes(String gln, List<Image> images) throws IOException {
		boolean ocurrioAlgunErrorAlActualizar = false;
		Optional<Ubicacion> optionalUbicacion = ubicacionesDAO.findByKey("codigo", gln);
		Param bucket = paramsDAO.findByNombre(Propiedades.DW_DESPLIEGUE_BUCKET);
		String nombreBucket = bucket != null ? bucket.getValor() : "";
		List<String> result = new ArrayList<>();
		if(optionalUbicacion.isPresent()) {
			Empresa empresa = optionalUbicacion.get().getEmpresa();
			String rut = empresa.getRut();
			/*if (this.configuration.empresasASincronizar(rut)) {
				for (Image image : images) {
					InputStream inputStream = new ByteArrayInputStream(image.getImagen());
					String cpp = image.getId().getCpp();
					String mediaType = "image/" + image.getExtension();
					Producto producto = this.productosDAO.getProductByBussisnesAndCpp(empresa.getId(), cpp.replace(" ", ""));
					if (producto != null) {
						UUID uuid = UUID.randomUUID();
						if (producto.getFoto() != null) {
							deleteFile(producto.getFoto(), nombreBucket);
							producto.setFoto("No se actualizo la foto");
						}
						String location = rut + "/productos/img/" + producto.getCpp() + "_" + producto.getGtin() + "_" + uuid.toString() + "." + image.getExtension();
						try {
							SaveFile(inputStream, mediaType, location, nombreBucket);
							producto.setFoto(location);
							result.add(location);
						} catch (Exception exception) {
							ocurrioAlgunErrorAlActualizar = true;
							logger.log(Level.ERROR, "Ocurrió un error al guardar la imagen en el bucket.");
						}
						this.productosDAO.update(producto);
					}
				}
			}*/
		}
		logger.log(Level.INFO, "El método SaveFileByRutBussisnes() de la clase FileService fue ejecutado.");
		return ocurrioAlgunErrorAlActualizar;
	}

	private String SaveFile(InputStream uploadedInputStream, String mediaType, String location, String bucketName) throws Exception{
		Param key = paramsDAO.findByNombre(Propiedades.DW_S3_S3APIKEY);
		Param keyId = paramsDAO.findByNombre(Propiedades.DW_S3_S3Id);
		AWSCredentials credentials = new BasicAWSCredentials(keyId.getValor(), key.getValor());
		@SuppressWarnings("deprecation")
		AmazonS3 s3Client = new AmazonS3Client(credentials);
		ObjectMetadata objectMetaData = new ObjectMetadata();
		objectMetaData.setContentType(mediaType);
		s3Client.putObject(new PutObjectRequest(bucketName, location, uploadedInputStream, objectMetaData)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		logger.log(Level.INFO, "El método SaveFile() de la clase FileService fue ejecutado.");
		return location;
	}

	public void deleteFile(String location, String bucketName) {
		try {
			Param key = paramsDAO.findByNombre(Propiedades.DW_S3_S3APIKEY);
			Param keyId = paramsDAO.findByNombre(Propiedades.DW_S3_S3Id);
			AWSCredentials credentials = new BasicAWSCredentials(keyId.getValor(), key.getValor());
			@SuppressWarnings("deprecation")
			AmazonS3 s3Client = new AmazonS3Client(credentials);
			s3Client.deleteObject(new DeleteObjectRequest(bucketName, location));
		} catch (SdkClientException e) {
			logger.error("A ocurrido un error al borrar la imagen del bucket: " + e.getStackTrace());
		}
	}

	@Override
	public boolean verificarQueExisteLaImagen(String bucketName, String location) {
		boolean existe = false;
		try {
			Param key = paramsDAO.findByNombre(Propiedades.DW_S3_S3APIKEY);
			Param keyId = paramsDAO.findByNombre(Propiedades.DW_S3_S3Id);
			AWSCredentials credentials = new BasicAWSCredentials(keyId.getValor(), key.getValor());
			@SuppressWarnings("deprecation")
			AmazonS3 s3Client = new AmazonS3Client(credentials);
			existe = s3Client.doesObjectExist(bucketName, location);
		} catch (Exception e) {
			logger.error("A ocurrido verificarQueExisteLaImagen: " + e.getStackTrace());
		}
		return existe;
	}
}