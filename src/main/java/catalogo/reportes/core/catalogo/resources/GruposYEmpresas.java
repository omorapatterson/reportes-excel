package catalogo.reportes.core.catalogo.resources;


import common.rondanet.catalogo.core.entity.Empresa;
import common.rondanet.catalogo.core.entity.Grupo;

import java.util.HashSet;
import java.util.Set;

public class GruposYEmpresas {

	private Set<Empresa> empresasConVisibilidad = new HashSet<>();

	private Set<String> sempresasConVisibilidad = new HashSet<>();

	private Set<Grupo> gruposConVisibilidad = new HashSet<>();

	private Set<String> sgruposConVisibilidad = new HashSet<>();

	public GruposYEmpresas() {
	}

	public Set<Empresa> getEmpresasConVisibilidad() {
		return empresasConVisibilidad;
	}

	public void setEmpresasConVisibilidad(Set<Empresa> empresasConVisibilidad) {
		this.empresasConVisibilidad = empresasConVisibilidad;
	}

	public Set<String> getSempresasConVisibilidad() {
		return sempresasConVisibilidad;
	}

	public void setSempresasConVisibilidad(Set<String> sempresasConVisibilidad) {
		this.sempresasConVisibilidad = sempresasConVisibilidad;
	}

	public Set<Grupo> getGruposConVisibilidad() {
		return gruposConVisibilidad;
	}

	public void setGruposConVisibilidad(Set<Grupo> gruposConVisibilidad) {
		this.gruposConVisibilidad = gruposConVisibilidad;
	}

	public Set<String> getSgruposConVisibilidad() {
		return sgruposConVisibilidad;
	}

	public void setSgruposConVisibilidad(Set<String> sgruposConVisibilidad) {
		this.sgruposConVisibilidad = sgruposConVisibilidad;
	}
}
