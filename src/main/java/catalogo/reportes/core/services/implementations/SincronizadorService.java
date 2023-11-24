package catalogo.reportes.core.services.implementations;

import catalogo.reportes.ReportesApplication;
import catalogo.reportes.core.services.interfaces.ISincronizadorService;
import catalogo.reportes.core.utils.login.Login;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SincronizadorService implements ISincronizadorService {

	Logger logger = LogManager.getLogger(ReportesApplication.class);

	private RestTemplate clientesService;

	@Value("${API_URL}")
	String url;

	@Value("${API_URL_PEDIDOS}")
	String urlPedidos;

	@Value("${API_USER}")
	String apiUser;

	@Value("${API_PASSWORD}")
	String apiPassword;

	public SincronizadorService() {
		this.clientesService = new RestTemplate();
	}

	@Override
	public boolean enviarListaDeVentaQueSeEstaActualizando(String listaDeVentaGln) {
		boolean ocurrioAlgunError = false;
		try {
			clientesService = new RestTemplate();
			String sincronizadorEmpresasUrl = url + "/sincronizador/actualizandoListasDeventa/" + listaDeVentaGln;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<List<?>> requestEntity = new HttpEntity<>(headers);
			ResponseEntity<String> response = clientesService.postForEntity(sincronizadorEmpresasUrl, requestEntity, String.class);
		} catch (Exception e) {
			ocurrioAlgunError = true;
			System.out.println(e);
			logger.log(Level.ERROR,"A ocurrido un error el enviar la lista de venta que se esta actualizando" + e.getStackTrace());
		}
		return ocurrioAlgunError;
	}

	@Override
	public boolean enviarProductoQueFueActualizado(String idProducto) {
		boolean ocurrioAlgunError = false;
		try {
			clientesService = new RestTemplate();
			String sincronizadorEmpresasUrl = url + "/sincronizador/actualizandoProductos/" + idProducto;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<List<?>> requestEntity = new HttpEntity<>(headers);
			ResponseEntity<String> response = clientesService.postForEntity(sincronizadorEmpresasUrl, requestEntity, String.class);
		} catch (Exception e) {
			ocurrioAlgunError = true;
			System.out.println(e);
			logger.log(Level.ERROR,"A ocurrido un error el enviar el id del producto que fue actualizado" + e.getStackTrace());
		}
		return ocurrioAlgunError;
	}

	@Override
	public boolean enviarNotificacionAlActualizarGrupos(String porcientoDeActualizacion) {
		boolean ocurrioAlgunError = false;
		try {
			clientesService = new RestTemplate();
			String sincronizadorEmpresasUrl = url + "/sincronizador/actualizandoGrupos/" + porcientoDeActualizacion;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<List<?>> requestEntity = new HttpEntity<>(headers);
			ResponseEntity<String> response = clientesService.postForEntity(sincronizadorEmpresasUrl, requestEntity, String.class);
		} catch (Exception e) {
			ocurrioAlgunError = true;
			System.out.println(e);
			logger.log(Level.ERROR,"A ocurrido un error el enviar la lista de venta que se esta actualizando" + e.getStackTrace());
		}
		return ocurrioAlgunError;
	}

	public String login(){
		try {
			String loginUrl = url + "/auth/login";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			Login body = new Login(apiUser, apiPassword);
			HttpEntity<Login> requestEntity = new HttpEntity<>(body, headers);
			ResponseEntity<String> response = clientesService.postForEntity(loginUrl, requestEntity, String.class);
			String responseBody = response.getBody();
			JsonObject convertedObject = new Gson().fromJson(responseBody, JsonObject.class);
			JsonObject jsonObject = convertedObject.getAsJsonObject("data");
			return jsonObject.get("token").getAsString();
		} catch (Exception e) {
			System.out.println(e);
			logger.log(Level.ERROR,"A ocurrido un error al loguearse en Catalogo: " + e.getStackTrace());
		}
		return "";
	}

}
