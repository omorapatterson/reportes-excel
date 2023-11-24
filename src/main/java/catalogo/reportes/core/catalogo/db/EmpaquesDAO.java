package catalogo.reportes.core.catalogo.db;

import java.util.List;
import java.util.Optional;


import common.rondanet.catalogo.core.entity.Empresa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import common.rondanet.catalogo.core.entity.Empaque;
import common.rondanet.catalogo.core.entity.UsuarioEmpresa;
import catalogo.reportes.core.catalogo.exceptions.ModelException;
import catalogo.reportes.core.catalogo.repository.IEmpaqueRepository;
import catalogo.reportes.core.catalogo.repository.IEmpresaRepository;

@Component
public class EmpaquesDAO {

	Logger logger = LogManager.getLogger(EmpaquesDAO.class);

	@Autowired
	private IEmpresaRepository empresaRepository;

	@Autowired
	private IEmpaqueRepository empaqueRepository;

	private final MongoOperations mongoOperations;

	public EmpaquesDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IEmpaqueRepository empaqueRepository) {
		this.mongoOperations = mongoOperations;
		this.empaqueRepository = empaqueRepository;
	}

	public Optional<Empaque> findById(String id) throws ModelException {
		return this.findById(id, null);
	}

	/**
	 * <p>
	 * Devuelve el {@link Empaque} con el Id pasado por parámetros.
	 * </p>
	 * <p>
	 * Para encontrarlo verifica dos condiciones:
	 * <p>
	 * - Si viene un {@link UsuarioEmpresa} verifica que el {@link Empaque}
	 * efectivamente pertenezca a la {@link Empresa} pasada por parámetros
	 * </p>
	 * <p>
	 * - Si no viene un {@link UsuarioEmpresa} devuelve cualquier {@link Empaque}
	 * que tenga el Id pasado por parámetros
	 * </p>
	 * </p>
	 * 
	 * @param id {@link String}
	 * @param ue {@link UsuarioEmpresa}
	 * @return {@link Optional}<{@link Empresa}>
	 * @throws ModelException
	 */
	public Optional<Empaque> findById(String id, UsuarioEmpresa ue) throws ModelException {
		if (id == null)
			throw new ModelException("Debe especificar un Id válido para seleccionar un Empaque");

		Optional<Empaque> empaque;

		if (ue != null) {
			empaque = empaqueRepository.findBySidAndSempresa(id, ue.getEmpresa().getSId());
		}else{
			empaque = empaqueRepository.findById(id);
		}

		return empaque;
	}

	public Empaque save(Empaque empaque) {
		if(empaque.getFechaCreacion() == null){
			empaque.setFechaCreacion();
		}
		if(empaque.getSId() == null) {
			empaque = empaqueRepository.save(empaque);
			empaque.setSId(empaque.getId());
		}
		empaque = empaqueRepository.save(empaque);
		empaque.setSId(empaque.getId());
		return empaqueRepository.save(empaque);
	}

	public void delete(Empaque empaque){
		empaqueRepository.delete(empaque);
	}

	public List<Empaque> getAll(){
		return empaqueRepository.findAll();
	}
}