package catalogo.reportes.core.catalogo.db;

import java.util.List;
import java.util.Optional;

import common.rondanet.catalogo.core.entity.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.joda.time.DateTime;

import common.rondanet.catalogo.core.entity.Categoria;
import common.rondanet.catalogo.core.entity.Producto;
import catalogo.reportes.core.catalogo.repository.ICategoriaRepository;
import catalogo.reportes.core.catalogo.repository.IEmpresaRepository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class CategoriasDAO {

	Logger logger = LogManager.getLogger(CategoriasDAO.class);

	@Autowired
	ICategoriaRepository categoriaRepository;

	@Autowired
	IEmpresaRepository empresaRepository;

	private final MongoOperations mongoOperations;

	public CategoriasDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, ICategoriaRepository categoriaRepository, IEmpresaRepository empresaRepository) {
		this.mongoOperations = mongoOperations;
		this.categoriaRepository = categoriaRepository;
		this.empresaRepository = empresaRepository;
	}

	public List<Categoria> getAll() {
		return categoriaRepository.findAll();
	}

	/**
	 * Devuelve un {@link Optional} de tipo {@link Categoria} con la
	 * {@link Categoria} con el <b>null</null> pasado por parámetros.
	 * 
	 * @param id
	 * @return {@link Optional} de tipo {@link Categoria}
	 */
	public Optional<Categoria> findById(String id) {
		return categoriaRepository.findById(id);
	}

	/**
	 * Actualida el {@link DateTime} fecha de edicion de la {@link Categoria} pasada como parámetro
	 * @param toUpdate
	 */
	public void update(Categoria toUpdate) {
		toUpdate.setFechaEdicion();
		categoriaRepository.save(toUpdate);
	}

	/**
	 * Devuelve una {@link List}<{@link Categoria}> donde {@link String} key pasado como parámetro sea igual al {@link String} value pasado como parámetro
	 * @param key {@link String}
	 * @param value {@link String}
	 * @return {@link List}<{@link Categoria}>
	 */
	public List<Categoria> findByKey(String key, String value) {
		Query query = new Query();
		query.addCriteria(Criteria.where(key).is(value));
		List<Categoria> categorias = mongoOperations.find(query, Categoria.class);

		return categorias;
	}

	public Categoria addCategory(Empresa empresa, Producto product) {
		Categoria division = new Categoria();
		division.setEmpresa(empresa);
		division.setSempresa(empresa.getSId());
		division.setNombre(product.getDivision());
		division = categoriaRepository.save(division);
		division.setSId(division.getId());
		categoriaRepository.save(division);

		Categoria linea = new Categoria();
		linea.setEmpresa(empresa);
		linea.setSempresa(empresa.getSId());
		linea.setNombre(product.getLinea());
		linea.setPadre(division);
		linea.setSpadre(division.getId());
		linea = categoriaRepository.save(linea);
		linea.setSId(linea.getId());
		return categoriaRepository.save(linea);
	}

	public void updateCategoria(Producto product) {
		Optional<Categoria> linea = categoriaRepository.findById(product.getCategoria().getId()) ;
		linea.get().setNombre(product.getLinea());
		if(linea.get().getSpadre() == null){
			Categoria division = new Categoria();
			division.setNombre(product.getDivision());
			division.setEmpresa(linea.get().getEmpresa());
			division.setSempresa(linea.get().getEmpresa().getId());
			division = categoriaRepository.save(division);
			division.setSId(division.getId());
			categoriaRepository.save(division);
			linea.get().setPadre(division);
			linea.get().setSpadre(division.getSpadre());
			categoriaRepository.save(linea.get());
		} else {
			Optional<Categoria> division = categoriaRepository.findById(linea.get().getSpadre()) ;
			division.get().setNombre(product.getDivision());
			categoriaRepository.save(division.get());
			categoriaRepository.save(linea.get());
		}

	}

	public void delete(Categoria categoria){
		categoriaRepository.delete(categoria);
	}

}
