package catalogo.reportes.core.catalogo.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import common.rondanet.catalogo.core.entity.Empresa;
import catalogo.reportes.core.utils.Acciones;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import common.rondanet.catalogo.core.entity.Grupo;
import common.rondanet.catalogo.core.entity.Producto;
import common.rondanet.catalogo.core.entity.ProductoAccion;
import catalogo.reportes.core.catalogo.repository.IEmpresaRepository;
import catalogo.reportes.core.catalogo.repository.IProductoAccionRepository;

@Component
public class ProductosAccionesDAO {
	@SuppressWarnings("unused")
	Logger logger = LogManager.getLogger(ProductosAccionesDAO.class);

	@Autowired
	IProductoAccionRepository productoAccionRepository;

	@Autowired
	IEmpresaRepository empresaRepository;

	@Autowired
	EmpresasDAO empresasDAO;

	private final MongoOperations mongoOperations;

	public ProductosAccionesDAO(@Qualifier("mongoTemplateCatalogo") MongoOperations mongoOperations, IProductoAccionRepository productoAccionRepository, IEmpresaRepository empresaRepository) {
		this.mongoOperations = mongoOperations;
		this.productoAccionRepository = productoAccionRepository;
		this.empresaRepository = empresaRepository;
	}

	/**
	 * Devuelve un {@link ProductoAccion} donde el {@link String} id de {@link ProductoAccion} sea igual al {@link String} id
	 * pasado por parámetro
	 * @param id {@link String}
	 * @return {@link ProductoAccion}
	 */
	public ProductoAccion findById(String id) {
		Optional<ProductoAccion> productoAccion = productoAccionRepository.findById(id);
		logger.log(Level.INFO, "El método findById() de la clase ProductosAccionesDAO fue ejecutado.");
		return productoAccion.isPresent() ? productoAccion.get() : null;
	}

	public ProductoAccion save(ProductoAccion accion) {
		accion.setFechaEdicion();
		accion = productoAccionRepository.save(accion);
		return accion;
	}

	public List<ProductoAccion> saveAll(List<ProductoAccion> acciones) {
		acciones = productoAccionRepository.saveAll(acciones);
		return acciones;
	}

	/**
	 * Actualiza y salva un {@link ProductoAccion} pasado por parámetro
	 * @param productoAccion {@link ProductoAccion}
	 * @return {@link ProductoAccion}
	 */
	public ProductoAccion update(ProductoAccion productoAccion) {
		productoAccion.setFechaEdicion();
		logger.log(Level.INFO, "El método update() de la clase ProductosAccionesDAO fue ejecutado.");
		return productoAccionRepository.save(productoAccion);
	}

	/**
	 * Elimina un {@link ProductoAccion} pasado por parámetro
	 * @param productoAccion {@link ProductoAccion}
	 * @return {@link ProductoAccion}
	 */
	public void delete(ProductoAccion productoAccion) {
		productoAccionRepository.delete(productoAccion);
	}

	public void deleteAll(List<ProductoAccion> productosAccions) {
		productoAccionRepository.deleteAll(productosAccions);
	}

	public void salvarAccion(Empresa proveedor, Empresa empresa, Producto producto){
		try {
			if(empresa.getUtilizaApiDeAcciones() != null && empresa.getUtilizaApiDeAcciones()) {
				ProductoAccion productoAccion = this.buscarAccionPorProveedorEmpresaProducto(proveedor.getId(), empresa.getId(), producto.getId());
				if (productoAccion == null) {
					productoAccion = new ProductoAccion(proveedor, empresa, producto);
					productoAccion.setAccion(Acciones.POST);
					productoAccion.setFechaCreacion();
				} else {
					if (!productoAccion.getRecibido()) {
						if (productoAccion.getAccion().equals(Acciones.DELETE)) {
							productoAccion.setAccion(Acciones.UPDATE);
						}
					} else {
						if (productoAccion.getAccion().equals(Acciones.DELETE)) {
							productoAccion.setAccion(Acciones.POST);
						} else {
							productoAccion.setAccion(Acciones.UPDATE);
						}
						productoAccion.setFechaRecibido(null);
					}
				}
				productoAccion.setFechaEdicion();
				productoAccion.setRecibido(false);
				productoAccion.setActualizada(true);
				save(productoAccion);
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, "El método salvarAccion() de la clase ProductosAccionesDAO dio Error" + e.getMessage() + " getStackTrace: " + e.getStackTrace().toString());
		}
		logger.log(Level.INFO, "El método salvarAccion() de la clase ProductosAccionesDAO fue ejecutado.");
	}

	public void setearAccionesAntesDeActualizarLaVisivilidad(Producto producto){
		Query query = new Query();
		query.addCriteria(Criteria.where("productoId").is(producto.getId()));
		List<ProductoAccion> listaAcciones = mongoOperations.find(query, ProductoAccion.class);
		for (ProductoAccion productoAccion: listaAcciones) {
			productoAccion.setFechaEdicion();
			productoAccion.setActualizada(false);
		}
		saveAll(listaAcciones);
		logger.log(Level.INFO, "Se ejecutó el método actualizarAcciones() de la clase ProductosAccionesDAO");
	}

	public List<ProductoAccion> findAccionesNoActualizadas(Producto producto, int page, int limit){
		Query query = new Query();
		query.addCriteria(Criteria.where("productoId").is(producto.getId()).andOperator(Criteria.where("actualizada").is(false)));
		query.limit(limit);
		query.skip((page - 1) * limit);
		List<ProductoAccion> listaAcciones = mongoOperations.find(query, ProductoAccion.class);
		return listaAcciones;
	}

	public void eliminarAccionesNoActualizadas(Producto producto) {
		int rows = 0;
		boolean todasLasAcciones = false;
		while (!todasLasAcciones) {
			int to = rows + 100;
			List<ProductoAccion> acciones = findAccionesNoActualizadas(producto, rows + 1, to);
			if (acciones.size() > 0) {
				eliminarAccionesNoActualizadas(acciones);
			}
			rows = to;
			if (acciones.size() < 100) {
				todasLasAcciones = true;
			}
		}
		System.gc();
	}

	public void eliminarAccionesNoActualizadas(List<ProductoAccion> acciones){
		List<ProductoAccion> accionesParaEliminar = new ArrayList<>();
		List<ProductoAccion> accionesParaActualizar = new ArrayList<>();
		for (ProductoAccion productoAccion : acciones) {
			if (!productoAccion.getActualizada()) {
				if (productoAccion.getRecibido()) {
					if (productoAccion.getAccion().equals(Acciones.DELETE)) {
						accionesParaEliminar.add(productoAccion);
					} else {
						productoAccion = actualizarAccion(productoAccion, Acciones.DELETE);
						accionesParaActualizar.add(productoAccion);
					}
				} else {
					if (!productoAccion.getAccion().equals(Acciones.DELETE)) {
						productoAccion = actualizarAccion(productoAccion, Acciones.DELETE);
						accionesParaActualizar.add(productoAccion);
					}
				}
			}
		}
		saveAll(accionesParaActualizar);
		deleteAll(accionesParaEliminar);
	}

	@Deprecated
	public void actualizarAccionYSalvar(ProductoAccion productoAccion, String accion){
		productoAccion.setAccion(accion);
		productoAccion.setRecibido(false);
		productoAccion.setFechaRecibido(null);
		productoAccion.setFechaEdicion();
		save(productoAccion);
	}

	public ProductoAccion actualizarAccion(ProductoAccion productoAccion, String accion){
		productoAccion.setAccion(accion);
		productoAccion.setRecibido(false);
		productoAccion.setFechaRecibido(null);
		productoAccion.setFechaEdicion();
		return productoAccion;
	}

	public ProductoAccion buscarAccionPorProveedorEmpresaProducto(String proveedorId, String empresaId, String productoId){
		Query query = new Query();
		query.addCriteria(Criteria.where("proveedorId").is(proveedorId).andOperator(Criteria.where("empresaId").is(empresaId), Criteria.where("productoId").is(productoId)));
		List<ProductoAccion> productosAcciones = mongoOperations.find(query, ProductoAccion.class);
		return productosAcciones.isEmpty() ? null : productosAcciones.get(0);
	}

	public void salvarAccionGrupo( Empresa proveedor, Grupo grupoEmpresas, Producto producto){
		Set<Empresa> empresas = grupoEmpresas.getEmpresas();
		for (Empresa empresa: empresas) {
			//empresa = empresasDAO.findById(empresa.getId());
			if(empresa != null) {
				salvarAccion(proveedor, empresa, producto);
			}
		}
		logger.log(Level.INFO, "El método salvarAccionGrupo() de la clase ProductosAccionesDAO fue ejecutado.");
	}

	public void actualizarAccionesProductoPrivado(Empresa proveedor, Producto producto){
		Set<Empresa> empresas = producto.getEmpresasConVisibilidad();
		Set<Grupo> grupos = producto.getGruposConVisibilidad();
		for (Empresa empresa: empresas) {
			if(empresa != null) {
				salvarAccion(proveedor, empresa, producto);
			}
		}
		for (Grupo grupo: grupos) {
			salvarAccionGrupo(proveedor, grupo, producto);
		}
		logger.log(Level.INFO, "El método actualizarAccionesProductoVisibles() de la clase ProductosAccionesDAO fue ejecutado.");
	}

	public void actualizarAccionesProductoVisibilidad(Empresa proveedor, Producto producto){
		Set<Empresa> empresas = producto.getEmpresasConVisibilidad();
		Set<Grupo> grupos = producto.getGruposConVisibilidad();
		for (Empresa empresa: empresas) {
			salvarAccionVisibilidad(proveedor, empresa, producto);
		}
		for (Grupo grupo: grupos) {
			salvarAccionVisibilidad(proveedor, grupo, producto);
		}
		logger.log(Level.INFO, "El método actualizarAccionesProductoVisibles() de la clase ProductosAccionesDAO fue ejecutado.");
	}

	public void salvarAccionVisibilidad( Empresa proveedor, Grupo grupoEmpresas, Producto producto){
		Set<Empresa> empresas = grupoEmpresas.getEmpresas();
		for (Empresa empresa: empresas) {
			salvarAccionVisibilidad(proveedor, empresa, producto);
		}
		logger.log(Level.INFO, "El método salvarAccionGrupo() de la clase ProductosAccionesDAO fue ejecutado.");
	}

	public void salvarAccionVisibilidad(Empresa proveedor, Empresa empresa, Producto producto){
		try {
			if(empresa.getUtilizaApiDeAcciones() != null && empresa.getUtilizaApiDeAcciones()) {
				ProductoAccion productoAccion = this.buscarAccionPorProveedorEmpresaProducto(proveedor.getId(), empresa.getId(), producto.getId());
				if (productoAccion == null) {
					productoAccion = new ProductoAccion(proveedor, empresa, producto);
					productoAccion.setAccion(Acciones.POST);
					productoAccion.setFechaCreacion();
				}
				productoAccion.setActualizada(true);
				save(productoAccion);
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, "El método salvarAccionVisibilidad() de la clase ProductosAccionesDAO dio Error" + e.getStackTrace());
		}
		logger.log(Level.INFO, "El método salvarAccionVisibilidad() de la clase ProductosAccionesDAO fue ejecutado.");
	}

	public void actualizarAccionesAlEliminarUnProducto(Producto producto){
		Query query = new Query();
		query.addCriteria(Criteria.where("productoId").is(producto.getId()));
		List<ProductoAccion> listaAcciones = mongoOperations.find(query, ProductoAccion.class);
		for (ProductoAccion productoAccion: listaAcciones) {
			if(productoAccion.getRecibido() && !productoAccion.getAccion().equals(Acciones.DELETE)){
				productoAccion.setAccion(Acciones.DELETE);
				productoAccion.setRecibido(false);
				productoAccion.setFechaRecibido(null);
				save(productoAccion);
			} else if(!productoAccion.getRecibido()) {
				if(productoAccion.getAccion().equals(Acciones.POST)){
					delete(productoAccion);
				} else {
					productoAccion.setAccion(Acciones.DELETE);
					productoAccion.setRecibido(false);
					productoAccion.setFechaRecibido(null);
					save(productoAccion);
				}
			}
		}
		logger.log(Level.INFO, "Se ejecutó el método actualizarAcciones() de la clase ProductosAccionesDAO");
	}
}