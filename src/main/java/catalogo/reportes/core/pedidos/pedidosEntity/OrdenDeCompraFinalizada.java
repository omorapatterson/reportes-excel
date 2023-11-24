package catalogo.reportes.core.pedidos.pedidosEntity;

import common.rondanet.catalogo.core.entity.Entidad;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "OrdenDeCompraFinalizada")
public class OrdenDeCompraFinalizada extends Entidad {

	private String empresa;

	private String proveedor;

	private String proveedorGln;

	private String usuarioEmpresa;

	private String plantillaId;

	private String lugarEntregaGln;

	private String numeroOrden;

	private String comentarios;

	private String moneda;

	private String mensaje;

	private boolean fueCreada;

	private String errores;

	public OrdenDeCompraFinalizada() {
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getProveedorGln() {
		return proveedorGln;
	}

	public void setProveedorGln(String proveedorGln) {
		this.proveedorGln = proveedorGln;
	}

	public String getUsuarioEmpresa() {
		return usuarioEmpresa;
	}

	public void setUsuarioEmpresa(String usuarioEmpresa) {
		this.usuarioEmpresa = usuarioEmpresa;
	}

	public String getPlantillaId() {
		return plantillaId;
	}

	public void setPlantillaId(String plantillaId) {
		this.plantillaId = plantillaId;
	}

	public String getLugarEntregaGln() {
		return lugarEntregaGln;
	}

	public void setLugarEntregaGln(String lugarEntregaGln) {
		this.lugarEntregaGln = lugarEntregaGln;
	}

	public String getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isFueCreada() {
		return fueCreada;
	}

	public void setFueCreada(boolean fueCreada) {
		this.fueCreada = fueCreada;
	}

	public String getErrores() {
		return errores;
	}

	public void setErrores(String errores) {
		this.errores = errores;
	}
}