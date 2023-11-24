package catalogo.reportes.core.pedidos.pedidosDAO;

import catalogo.reportes.core.catalogo.exceptions.ModelException;
import catalogo.reportes.core.pedidos.pedidosEntity.DatosAfiliados;
import catalogo.reportes.core.pedidos.pedidosRepository.IDatosAfiliadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;

@Component
public class DatosAfiliadosDAO {

	@Autowired
	private IDatosAfiliadosRepository datosAfiliadosRepository;

	private final MongoOperations mongoOperations;

	public DatosAfiliadosDAO(@Qualifier("mongoTemplatePedidos") MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public Optional<DatosAfiliados> findByRut(String rut) {
		Query query = new Query();
		List<DatosAfiliados> datosAfiliados = mongoOperations.find(query.addCriteria(Criteria.where("rut").is(rut).andOperator(Criteria.where("eliminado").is(false))), DatosAfiliados.class);
		if (datosAfiliados.size() > 0)
			return Optional.of(datosAfiliados.get(0));
		return Optional.ofNullable(null);
	}

	public Optional<DatosAfiliados> findByNroEmpresa(String nroEmpresa) {
		Query query = new Query();
		List<DatosAfiliados> datosAfiliados = mongoOperations.find(query.addCriteria(Criteria.where("nro_empresa").is(nroEmpresa).andOperator(Criteria.where("eliminado").is(true))), DatosAfiliados.class);
		if (datosAfiliados.size() > 0)
			return Optional.of(datosAfiliados.get(0));
		return Optional.ofNullable(null);
	}

	public DatosAfiliados findById(String id) {
		return datosAfiliadosRepository.findById(id).get();
	}

	public void delete(DatosAfiliados datos) {
		if (datos.getId() != null) {
			datos.eliminar();
			datosAfiliadosRepository.save(datos);
		}
	}

	public DatosAfiliados upsert(DatosAfiliados e) throws ModelException {
		Optional<DatosAfiliados> optional = this.findByRut(e.getRut());
		if (!optional.isPresent())
			return datosAfiliadosRepository.save(e);

		DatosAfiliados datos = optional.get();

		datos.nroEmpresa(e.getNroEmpresa());
		datos.setPassword(e.getPassword());

		return datosAfiliadosRepository.save(datos);
	}

	public DatosAfiliados save(DatosAfiliados datosAfiliados) {
		return datosAfiliadosRepository.save(datosAfiliados);
	}

	public List<DatosAfiliados> saveAll(List<DatosAfiliados> listDatosAfiliados) {
		return datosAfiliadosRepository.saveAll(listDatosAfiliados);
	}


}
