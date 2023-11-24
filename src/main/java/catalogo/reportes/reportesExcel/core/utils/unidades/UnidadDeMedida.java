package catalogo.reportes.reportesExcel.core.utils.unidades;

import java.util.HashMap;

public class UnidadDeMedida {
	public static final HashMap<String, String> unidadesDeMedida = new HashMap<String, String>(){{
		put("grs", "g");
		put("grss", "g");
		put("gr", "g");
		put("g", "g");
		put("gramos", "g");
		put("mg", "mg");
		put("kgs", "kg");
		put("kgss", "kg");
		put("kg", "kg");
		put("k", "kg");
		put("kilos", "kg");
		put("kilo", "kg");
		put("c.c", "cc");
		put("cc", "cc");
		put("cm3", "cc");
		put("lt", "l");
		put("lts", "l");
		put("l", "l");
		put("litro", "l");
		put("litros", "l");
		put("unidad", "ea");
		put("unidades", "ea");
		put("unid", "ea");
		put("uno", "ea");
		put("uni", "ea");
		put("un", "ea");
		put("unida", "ea");
		put("undiad", "ea");
		put("unitaria", "ea");
		put("uniadad", "ea");
		put("umidad", "ea");
		put("unitario", "ea");
		put("uniddad", "ea");
		put("unidda", "ea");
		put("unidadades", "ea");
		put("u", "ea");
		put("pzas", "ea");
		put("piezas", "ea");
		put("pcs", "ea");
		put("sachets", "ea");
		put("estuche", "ea");
		put("productos", "ea");
		put("producto", "ea");
		put("tostadas", "ea");
		put("saquitos", "ea");
		put("horma", "ea");
		put("bot", "ea");
		put("u", "ea");
		put("oz", "oz");
		put("sobres", "ea");
		put("sobre", "ea");
		put("ml", "ml");
		put("porcion", "ea");
		put("porciones", "ea");
		put("cm3", "cm3");
		put("lbs", "lb");
		put("lb", "lb");
		put("tabletas", "ea");
		put("caj", "ea");
		put("mts", "ea");
		put("mm", "ea");
		put("cms", "ea");
		put("sp", "ea");
		put("ftb", "ea");
		put("comprimidos", "ea");
		put("comp", "ea");
		put("hojas", "ea");
		put("formularios", "ea");
		put("ampollas", "ea");
		put("ampolla", "ea");
		put("amp", "ea");
		put("sistemas", "ea");
		put("capsulas", "ea");
		put("cápsulas", "ea");
		put("cap", "ea");
		put("blister", "ea");
		put("par", "ea");
		put("bolsita", "ea");
		put("bolsitas", "ea");
		put("bolsas", "ea");
		put("bolsa", "ea");
		put("test", "ea");
		put("tests", "ea");
		put("fetas", "ea");
		put("barritas", "ea");
		put("botellas", "ea");
		put("potes", "ea");
		put("lata", "ea");
		put("cl", "cl");
		put("ovulos", "ea");
		put("kit", "ea");
		put("ap", "ap");
		put("fco", "ea");
		put("paquete", "ea");
		put("atado", "ea");
		put("promocion", "ea");
		put("docena", "ea");
		put("decena", "ea");
		put("pcs", "ea");
		put("pote", "ea");
		put("set", "ea");
		put("metro", "ea");
		put("supositorios", "ea");
		put("sachet", "ea");
		put("platito", "ea");
		put("ui", "ui");
		put("rollo", "ea");
		put("rollos", "ea");
		put("ovillo", "ea");
		put("ea", "ea");
		put("x", "x");
	}};

	public static String obtenerUnidadDeMedidaEstandar(String unidadDeMedida) {
		unidadDeMedida = unidadDeMedida != null ? unidadDeMedida.toLowerCase() : "";
		String unidad = unidadesDeMedida.get(unidadDeMedida.toLowerCase());
		return unidad;
	}
}