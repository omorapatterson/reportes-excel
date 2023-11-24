package catalogo.reportes.core.catalogo.db;

import java.util.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import catalogo.reportes.core.catalogo.repository.IEmpresaRepository;
import catalogo.reportes.core.catalogo.repository.IUserRepository;
import catalogo.reportes.core.catalogo.repository.IUsuarioEmpresaRepository;

@Component
public class UsuarioEmpresaDAO {

	Logger logger = LogManager.getLogger(UsuarioEmpresaDAO.class);

	@Autowired
	private IUsuarioEmpresaRepository usuarioEmpresaRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IEmpresaRepository empresaRepository;

	private final MongoOperations mongoOperations;

	public UsuarioEmpresaDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IUsuarioEmpresaRepository usuarioEmpresaRepository, IEmpresaRepository empresaRepository) {
		this.mongoOperations = mongoOperations;
		this.usuarioEmpresaRepository = usuarioEmpresaRepository;
		this.empresaRepository = empresaRepository;
	}

	/**
	 * Salva un {@link UsuarioEmpresa}
	 * @param e {@link UsuarioEmpresa}
	 * @return {@link UsuarioEmpresa}
	 */
	public UsuarioEmpresa save(UsuarioEmpresa e) {
		e = usuarioEmpresaRepository.save(e);
		e.setSId(e.getId());
		usuarioEmpresaRepository.save(e);

		logger.log(Level.INFO, "El método save() de la clase UsuarioEmpresaDAO fue ejecutado.");
		return e;
	}

	/**
	 * Devuelve el {@link UsuarioEmpresa} que coincida con los criterios pasados por
	 * parámetros.
	 *
	 * TODO: Evaluar si realmente es necesario chequear el Usuario si ya se está
	 * pasando el id del {@link UsuarioEmpresa}. Es este el lugar correcto para
	 * hacer esa verificación??
	 *
	 * @param idUsuario {@link String}
	 * @param idEmpresa {@link String}
	 * @return {@link Optional}<{@link UsuarioEmpresa}>
	 */
	public List<UsuarioEmpresa> findByIdUsuarioAndIdEmpresa(String idUsuario, String idEmpresa) {
		List<UsuarioEmpresa> usuarioEmpresa = usuarioEmpresaRepository.findAllBySusuarioAndSempresa(idUsuario, idEmpresa);
		return usuarioEmpresa;
	}
	public List<UsuarioEmpresa> findByIdUsuario(String usuarioId) {
		List<UsuarioEmpresa> usuarioEmpresa = usuarioEmpresaRepository.findAllBySusuario(usuarioId);
		return usuarioEmpresa;
	}


	public List<UsuarioEmpresa> getAll() {
		return usuarioEmpresaRepository.findAll();
	}

	public List<UsuarioEmpresa> findAllByIdEmpresa(String idEmpresa) {
		return usuarioEmpresaRepository.findAllBySempresa(idEmpresa);
	}


	public void delete(UsuarioEmpresa usuarioEmpresa){
		this.usuarioEmpresaRepository.delete(usuarioEmpresa);
	}
}