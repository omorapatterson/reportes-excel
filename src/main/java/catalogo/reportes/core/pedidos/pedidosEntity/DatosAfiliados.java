package catalogo.reportes.core.pedidos.pedidosEntity;

import common.rondanet.catalogo.core.entity.Entidad;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;

@Document(collection = "DatosAfiliados")
public class DatosAfiliados extends Entidad {

	@Column(name = "rut")
	@Indexed(direction = IndexDirection.ASCENDING)
	private String rut;

	@Column(name = "nro_empresa")
	private String nroEmpresa;

	@Column(name = "password")
	private String password;

	public DatosAfiliados() {
	}

	public DatosAfiliados(String rut, String nroEmpresa, String password) {
		this.rut = rut;
		this.nroEmpresa = nroEmpresa;
		this.password = password;
	}

	public String getRut() {
		return this.rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getNroEmpresa() {
		return this.nroEmpresa;
	}

	public void setNroEmpresa(String nroEmpresa) {
		this.nroEmpresa = nroEmpresa;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DatosAfiliados rut(String rut) {
		this.rut = rut;
		return this;
	}

	public DatosAfiliados nroEmpresa(String nroEmpresa) {
		this.nroEmpresa = nroEmpresa;
		return this;
	}

	public DatosAfiliados password(String password) {
		this.password = password;
		return this;
	}

	@Override
	public String toString() {
		return "{" + " rut='" + getRut() + "'" + ", nroEmpresa='" + getNroEmpresa() + "'" + ", password='"
				+ getPassword() + "'" + "}";
	}

}
