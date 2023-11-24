package catalogo.reportes.core.catalogo.services.implementations;

import catalogo.reportes.core.afiliados.afiliadosRepositories.IAfiliadosProductoRepository;
import catalogo.reportes.core.catalogo.db.ProductosDAO;
import catalogo.reportes.core.catalogo.services.interfaces.IContenidoNetoService;
import catalogo.reportes.core.utils.S3FileManager;
import catalogo.reportes.core.utils.validador.NumbersValidators;
import catalogo.reportes.reportesExcel.core.utils.unidades.UnidadDeMedida;
import common.rondanet.catalogo.core.entity.Empaque;
import common.rondanet.catalogo.core.entity.Presentacion;
import common.rondanet.catalogo.core.entity.Producto;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContenidoNetoService implements IContenidoNetoService {

    Logger logger = LogManager.getLogger(ContenidoNetoService.class);

    @Autowired
    IAfiliadosProductoRepository afiliadosProductoRepository;

    @Autowired
    ProductosDAO productosDAO;

    @Override
    public List<Producto> obtenerUnidadesDeContenidoNeto(
            List<common.rondanet.clasico.core.afiliados.models.Producto> productosAfiliado
    ) {
        List<Producto> productos = new ArrayList<>();
        for (common.rondanet.clasico.core.afiliados.models.Producto productoAfiliado : productosAfiliado) {
            Producto producto = obtenerProducto(productoAfiliado);
            if (producto.getGtin().length() < 14) {
                obtenerEmpaques(producto, productoAfiliado.getCodigoEmpresa());
            }

            productos.add(producto);
            agregarFechas(producto, productoAfiliado);

        }
        return productos;
    }


    public String obtenerContenidoNeto(String descripcion) {
        String unidadDeMedidaContenidoNeto = "";
        try {
            descripcion = descripcion.toLowerCase().replace(".x", ". x");
            HashMap<Integer, String> numeros = obtenerNumerosEnteros(descripcion);
            HashMap<Integer, String> palabras = obtenerPalabras(descripcion);

            List<Integer> posiciones = new ArrayList<>();
            agregarLugares(numeros, posiciones);
            agregarLugares(palabras, posiciones);

            HashMap<Integer, String> palabrasYNumeros = new HashMap<>();
            palabrasYNumeros.putAll(numeros);
            palabrasYNumeros.putAll(palabras);

            Collections.sort(posiciones);

            boolean loUltimoFueUnNumero = false;
            for (Integer posicion : posiciones) {
                String palabraONumero = palabrasYNumeros.get(posicion);
                if (esUnaPalabra(palabraONumero)) {
                    String unidadDeMedida = UnidadDeMedida.obtenerUnidadDeMedidaEstandar(palabraONumero);
                    if (unidadDeMedida != null) {
                        if (unidadDeMedidaContenidoNeto.isEmpty() && !unidadDeMedida.equals("x")) {
                            unidadDeMedidaContenidoNeto = "1 " + unidadDeMedida;
                        } else if (unidadDeMedida.equals("x")) {
                            unidadDeMedidaContenidoNeto += " " + unidadDeMedida;
                        } else {
                            unidadDeMedidaContenidoNeto += " " + (loUltimoFueUnNumero ? "" : " + 1 ") + unidadDeMedida;
                        }
                        loUltimoFueUnNumero = false;
                    }
                } else if (!esUnaPalabra(palabraONumero)) {
                    String antesDelNumero = descripcion.substring(0, posicion);
                    int index = antesDelNumero.indexOf("+");
                    if (index > -1) {
                        if (unidadDeMedidaContenidoNeto.isEmpty()) {
                            unidadDeMedidaContenidoNeto = "1 ea + " + palabraONumero;
                        } else {
                            unidadDeMedidaContenidoNeto += (loUltimoFueUnNumero ? " ea + ": " + ") + palabraONumero;
                        }
                    } else {
                        unidadDeMedidaContenidoNeto += (loUltimoFueUnNumero ? " ea ": " ") + palabraONumero;
                    }
                    loUltimoFueUnNumero = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unidadDeMedidaContenidoNeto;
    }

    public boolean esUnaPalabra(String palabra) {
        boolean esUnaPalabra = palabra.matches("^[^0-9]*$");
        return esUnaPalabra;
    }

    public List<Integer> agregarLugares(HashMap<Integer, String> palabras, List<Integer> numeros) {
        for (Map.Entry<Integer, String> entry : palabras.entrySet()) {
            numeros.add(entry.getKey());
        }
        return numeros;
    }

    public String obtenerUnidadesDeContenidoNeto(common.rondanet.clasico.core.afiliados.models.Producto productoAfiliado) {
        String descripcion = productoAfiliado.getDescripcion();
        String presentacion = productoAfiliado.getPresentacion();
        String unidadesDeContenidonetoDescripcion = obtenerContenidoNeto(descripcion);
        String unidadesDeContenidonetoPresentacion = obtenerContenidoNeto(presentacion);
        return !unidadesDeContenidonetoPresentacion.isEmpty() ? unidadesDeContenidonetoPresentacion : unidadesDeContenidonetoDescripcion;
    }

    public Producto obtenerProducto(
            common.rondanet.clasico.core.afiliados.models.Producto productoAfiliado
    ) {
        String unidadesDeContenidoneto = obtenerUnidadesDeContenidoNeto(productoAfiliado);
        Producto producto = new Producto();
        producto.setGtin(productoAfiliado.getId().getCodigo());
        producto.setCpp(productoAfiliado.getId().getCodigo());
        producto.setDescripcion(productoAfiliado.getDescripcion());
        if (
                productoAfiliado.getMarca() != null &&
                        !productoAfiliado.getMarca().isEmpty()
        ) {
            producto.setMarca(productoAfiliado.getMarca());
        }
        producto.setLinea(productoAfiliado.getFamilia());
        Presentacion presentacion = new Presentacion();
        presentacion.setNombre(
                productoAfiliado.getDescripcion() +
                        " --- " +
                        productoAfiliado.getPresentacion()
        );
        producto.setPresentacion(presentacion);
        if (productoAfiliado.getFechaDeBaja() != null) {
            producto.eliminar();
        }
        agregarUnidadDeContenidoNeto(producto, unidadesDeContenidoneto);
        return producto;
    }

    public Producto obtenerEmpaques(Producto producto, String empresa) {
        try {
            List<common.rondanet.clasico.core.afiliados.models.Producto> empaquesAfiliado = afiliadosProductoRepository.findEmpaqueProducto(
                    producto.getGtin()
            );
            if (!empaquesAfiliado.isEmpty()) {
                Set<Empaque> empaques = new HashSet<>();
                for (common.rondanet.clasico.core.afiliados.models.Producto empaqueAfiliado: empaquesAfiliado) {
                    Empaque empaque = new Empaque();
                    empaque.setGtin(empaqueAfiliado.getId().getCodigo());
                    empaque.setId(empaqueAfiliado.getId().getCodigo());
                    String unidadesDeContenidoneto = obtenerUnidadesDeContenidoNeto(empaqueAfiliado);
                    empaque.setCpp(producto.getCpp());
                    String cantidad = "1";
                    HashMap<Integer, String> numeros = obtenerNumerosEnteros(unidadesDeContenidoneto);
                    for (Map.Entry<Integer, String> entry : numeros.entrySet()) {
                        cantidad = entry.getValue();
                        break;
                    }
                    BigDecimal numero = NumbersValidators.obtenerNumero(cantidad);
                    empaque.setCantidad(numero);
                    empaque.setFechaCreacion(empaqueAfiliado.getFechaDeAlta() != null ? new DateTime(empaqueAfiliado.getFechaDeAlta()): null);
                    empaque.setClasificacion("Caja");
                    if (empaqueAfiliado.getFechaDeBaja() != null) {
                        empaque.eliminar();
                    }
                    empaques.add(empaque);
                }
                producto.setEmpaques(empaques);
            }
        } catch (Exception e) {
            logger.log(Level.ERROR,"Error al obtenerEmpaques para GTIN: " + producto.getGtin() + " --- " + e.getMessage() + " " + e.getStackTrace());
        }
        return producto;
    }

    public void agregarUnidadDeMendidaAlEmpaque(Empaque empaque, String unidadesDeContenidoneto) {
        try {
            unidadesDeContenidoneto = unidadesDeContenidoneto.trim();
            String[] contenidoNetoPrincipal = unidadesDeContenidoneto.split(" ");
            BigDecimal contenido = NumbersValidators.obtenerNumero(contenidoNetoPrincipal[0]);
            String unidadDeMedida = contenidoNetoPrincipal[1];
            empaque.setPesoBruto(contenido);
            empaque.setUnidadMedida(unidadDeMedida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarUnidadDeContenidoNeto(
            Producto producto,
            String unidadesDeContenidoneto
    ) {
        try {
            String[] contenidoNetoParaMultiplicar = unidadesDeContenidoneto.split("x");
            String contenidoNetoDespuesDeMultiplicar = "";
            if (contenidoNetoParaMultiplicar.length > 1) {
                contenidoNetoDespuesDeMultiplicar = contenidoNetoParaMultiplicar[1].trim();
            }

            String contenidoNetoDespuesDeSumar = "";
            if (!contenidoNetoDespuesDeMultiplicar.equals("")) {
                contenidoNetoDespuesDeSumar = sumarContenidoNeto(contenidoNetoDespuesDeMultiplicar);
            } else {
                contenidoNetoDespuesDeSumar = sumarContenidoNeto(unidadesDeContenidoneto);
            }

            agregarContenidoNetoAlProducto(producto, contenidoNetoDespuesDeSumar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarContenidoNetoAlProducto(Producto producto, String unidadesDeContenidoneto) {
        String contenidoNetoParaAgregar = unidadesDeContenidoneto.trim();
        int indexMultiplicar = unidadesDeContenidoneto.indexOf("x");
        int indexSumar = unidadesDeContenidoneto.indexOf("+");
        if (indexMultiplicar > -1) {
            String contenidoAMultiplicar = unidadesDeContenidoneto.substring(indexMultiplicar + 2);
            contenidoNetoParaAgregar = contenidoNetoParaSumar(contenidoAMultiplicar);
        } else if (indexSumar > -1) {
            contenidoNetoParaAgregar = contenidoNetoParaSumar(unidadesDeContenidoneto);
        }

        int indexOfEa = contenidoNetoParaAgregar.indexOf("ea");
        if (indexSumar == -1 && indexOfEa > -1) {
            String[] contenidoNeto = contenidoNetoParaAgregar.split("ea");
            if (contenidoNeto.length > 1) {
                contenidoNetoParaAgregar = contenidoNeto[contenidoNeto.length -1].trim();
            }
        }

        HashMap<Integer, String> palabras = obtenerPalabras(contenidoNetoParaAgregar);
        if (palabras.isEmpty()) {
            if (indexSumar == -1) {
                String[] contenidoNeto = contenidoNetoParaAgregar.split("ea");
                String ultimo = contenidoNeto[contenidoNeto.length -1].trim();
                contenidoNetoParaAgregar = unidadesDeContenidoneto.substring(unidadesDeContenidoneto.indexOf(ultimo));
            } else {
                contenidoNetoParaAgregar = unidadesDeContenidoneto;
            }
        }
        contenidoNetoParaAgregar = contenidoNetoParaAgregar.replace(",", ".");

        if (!contenidoNetoParaAgregar.equals("")) {
            contenidoNetoParaAgregar = contenidoNetoParaAgregar.trim();
            String[] contenidoNetoPrincipal = contenidoNetoParaAgregar.split(" ");
            BigDecimal contenido = new BigDecimal(0);
            String unidadDeMedida = "";
            int index = 1;
            BigDecimal verificarQueUnidadDeMedidaNoSeaNumero = NumbersValidators.obtenerNumero(contenidoNetoPrincipal[index]);
            try {
                while (verificarQueUnidadDeMedidaNoSeaNumero != null) {
                    index++;
                    verificarQueUnidadDeMedidaNoSeaNumero = NumbersValidators.obtenerNumero(contenidoNetoPrincipal[index]);
                }
                contenido = NumbersValidators.obtenerNumero(contenidoNetoPrincipal[index - 1]);
                unidadDeMedida = contenidoNetoPrincipal[index];

                if (unidadDeMedida.toLowerCase().contains("oz")) {
                    contenido = contenido.divide(new BigDecimal(16));
                    unidadDeMedida = "lb";
                }

                if (unidadDeMedida.toLowerCase().contains("mg")) {
                    contenido = contenido.divide(new BigDecimal(1000));
                    unidadDeMedida = "g";
                }

                producto.setContenidoNeto(contenido);
                producto.setUnidadMedida(unidadDeMedida);
            } catch (Exception e) {
                //
            }
        }
    }

    public String contenidoNetoParaSumar(String contenidoASumar) {
        int indexSumar = contenidoASumar.indexOf("+");
        String contenidoNetoParaAgregar = contenidoASumar.substring(0, indexSumar);
        return contenidoNetoParaAgregar;
    }

    public String sumarContenidoNeto(String contenidoNeto) {
        String[] contenidoNetoParaSumar = contenidoNeto.split("\\+");
        String sumatoria = "";
        if (contenidoNetoParaSumar.length > 1) {
            String contenidoNetoSumado = "";
            for (String contenidoNetoASumar : contenidoNetoParaSumar) {
                contenidoNetoASumar = contenidoNetoASumar.trim();
                contenidoNetoSumado = sumarContenidosNetos(contenidoNetoSumado, contenidoNetoASumar);
            }
            sumatoria = contenidoNetoSumado;
        } else {
            sumatoria = contenidoNeto.trim();
        }
        return sumatoria;
    }

    public String sumarContenidosNetos(String sumado, String paraSumar) {
        String[] contenidoNetoPrincipal = sumado.split(" ");
        String[] contenidoNetoParaSumar = paraSumar.split(" ");
        if (contenidoNetoPrincipal.length > 1 && contenidoNetoParaSumar.length > 1) {
            BigDecimal contenidoPrincipal = NumbersValidators.obtenerNumero(contenidoNetoPrincipal[0]);
            String unidadDeMedidaPrincipal = contenidoNetoPrincipal[1];
            BigDecimal contenidoSecundario = NumbersValidators.obtenerNumero(contenidoNetoParaSumar[0]);
            String unidadDeMedidaSecundario = contenidoNetoParaSumar[1];
            if (unidadDeMedidaPrincipal.equals(unidadDeMedidaSecundario)) {
                contenidoPrincipal = contenidoPrincipal.add(contenidoSecundario);
                sumado = contenidoPrincipal.toString() + " " + unidadDeMedidaSecundario;
            }
        } else if (sumado.equals("")) {
            sumado = paraSumar;
        }
        return sumado;
    }

    public String multiplicarContenidoNeto(String multiplicado, String paraMultiplicar) {
        paraMultiplicar = paraMultiplicar.trim();
        String[] contenidoNetoParaSumar = paraMultiplicar.split("\\+");
        String contenidoNetoSuma = "";
        String contenidoNetoNoSuma = "";
        if (contenidoNetoParaSumar.length > 1) {
            contenidoNetoSuma = contenidoNetoParaSumar[0];
            contenidoNetoSuma = contenidoNetoSuma.trim();
            contenidoNetoNoSuma = contenidoNetoParaSumar[1];
            contenidoNetoNoSuma = " + " + contenidoNetoNoSuma.trim();
        }

        if (multiplicado.equals("")) {
            if (contenidoNetoSuma.equals("")) {
                multiplicado = paraMultiplicar;
            }
        } else {
            String[] contenidoNetoPrincipal = multiplicado.split(" ");
            String[] contenidoNetoParaMultiplicar = contenidoNetoSuma.split(" ");
            if (contenidoNetoPrincipal.length > 1 && contenidoNetoParaMultiplicar.length > 1) {
                BigDecimal contenidoPrincipal = NumbersValidators.obtenerNumero(contenidoNetoPrincipal[0]);
                String unidadDeMedidaPrincipal = contenidoNetoPrincipal[1];
                BigDecimal contenidoSecundario = NumbersValidators.obtenerNumero(contenidoNetoParaMultiplicar[0]);
                String unidadDeMedidaSecundario = contenidoNetoParaMultiplicar[1];
                if (unidadDeMedidaPrincipal.equals(unidadDeMedidaSecundario)) {
                    contenidoPrincipal = contenidoPrincipal.multiply(contenidoSecundario);
                    multiplicado = contenidoPrincipal.toString() + " " + unidadDeMedidaSecundario + contenidoNetoNoSuma;
                }
            } else if (contenidoNetoPrincipal.length == 1 && contenidoNetoParaMultiplicar.length > 1) {
                BigDecimal contenidoPrincipal = NumbersValidators.obtenerNumero(contenidoNetoPrincipal[0]);
                BigDecimal contenidoSecundario = NumbersValidators.obtenerNumero(contenidoNetoParaMultiplicar[0]);
                contenidoPrincipal = contenidoPrincipal.multiply(contenidoSecundario);
                String unidadDeMedidaSecundario = contenidoNetoParaMultiplicar[1];
                multiplicado = contenidoPrincipal.toString() + " " + unidadDeMedidaSecundario + contenidoNetoNoSuma;
            }
        }
        return multiplicado;
    }

    public String sumatoriaContenidoNeto(String[] contenidoNetoParaSumar) {
        String contenidoNetoSuma = "";
        for (String paraSumar : contenidoNetoParaSumar) {
            String[] contenidoNetoPrincipal = paraSumar.split(" ");
            if (contenidoNetoPrincipal.length > 1) {
                BigDecimal contenido = NumbersValidators.obtenerNumero(contenidoNetoPrincipal[0]);
                String unidadDeMedida = contenidoNetoPrincipal[1];
                if (!contenidoNetoSuma.equals("")) {
                    String[] contenidoNetoSumado = contenidoNetoSuma.split(" ");
                    BigDecimal contenidoSuma = NumbersValidators.obtenerNumero(contenidoNetoSumado[0]);
                    String unidadDeMedidaSuma = contenidoNetoSumado[1];
                    if (unidadDeMedida.equals(unidadDeMedidaSuma)) {
                        contenido = contenido.add(contenidoSuma);
                        contenidoNetoSuma = contenido.toString() + " " + unidadDeMedida;
                    }
                } else {
                    contenidoNetoSuma = contenido.toString() + unidadDeMedida;
                }
            }
        }
        return contenidoNetoSuma;
    }

    HashMap<Integer, String> obtenerNumerosEnteros(String stringToSearch) {
        Pattern integerPattern = Pattern.compile("\\d+\\.\\d+|\\d+\\,\\d+|\\d+\\/\\d+|\\d+");
        Matcher matcher = integerPattern.matcher(stringToSearch);
        HashMap<Integer, String> numeros = buscarCoincidencias(matcher, stringToSearch);
        return numeros;
    }

    HashMap<Integer, String> obtenerPalabras(String stringToSearch) {
        Pattern palabrasPattern = Pattern.compile("[A-Za-zÁÉÍÓÚÑÜáéíóúñü.]*[A-Za-zÁÉÍÓÚÑÜáéíóúñü]");
        Matcher matcher = palabrasPattern.matcher(stringToSearch);
        HashMap<Integer, String> palabras = buscarCoincidencias(matcher, stringToSearch);
        return palabras;
    }

    public HashMap<Integer, String> buscarCoincidencias(Matcher matcher, String stringToSearch) {
        HashMap<Integer, String> coincidencias = new HashMap<>();
        int indiceActual = 0;
        while (matcher.find()) {
            String numero = matcher.group();
            int index = stringToSearch.indexOf(numero);
            indiceActual += index;
            coincidencias.put(indiceActual, numero);
            stringToSearch = stringToSearch.substring(index + numero.length());
            indiceActual += numero.length();
        }
        return coincidencias;
    }

    public void agregarFechas(Producto productoCatalogo, common.rondanet.clasico.core.afiliados.models.Producto productoAfilidos) {
        productoCatalogo.setFechaCreacion(productoAfilidos.getFechaDeAlta() != null ? new DateTime(productoAfilidos.getFechaDeAlta()): null);
        productoCatalogo.setFechaDeAlta(productoAfilidos.getFechaDeAlta() != null ? new DateTime(productoAfilidos.getFechaDeAlta()): null);
        productoCatalogo.setFechaDeBaja(productoAfilidos.getFechaDeBaja() != null ? new DateTime(productoAfilidos.getFechaDeBaja()): null);
        productoCatalogo.setFechaDeVigenciaInicio(productoAfilidos.getFechaDeVigenciaInicio() != null ? new DateTime(productoAfilidos.getFechaDeVigenciaInicio()): null);
        productoCatalogo.setFechaDeVigenciaFin(productoAfilidos.getFechaDeVigenciaFin() != null ? new DateTime(productoAfilidos.getFechaDeVigenciaFin()): null);
    }
}
