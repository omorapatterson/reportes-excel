package catalogo.reportes.core.utils.validador;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumbersValidators {

    public static BigDecimal obtenerNumero(String value) {
        BigDecimal numero = null;
        try {
            value = eliminarSaltosDeLinea(value);
            if (value.contains("/")) {
                String[] contenidoNeto = value.split("/");
                BigDecimal dividendo = new BigDecimal(contenidoNeto[0]);
                BigDecimal divisor = new BigDecimal(contenidoNeto[1]);
                numero = dividendo.divide(divisor, 2, RoundingMode.HALF_UP);
            } else {
                numero = new BigDecimal(value);
            }
        } catch (Exception exception) {
            // no hacer nada
        }
        return numero;
    }

    public static String eliminarSaltosDeLinea(String value) {
        value = value != null ? value : "";
        value = value.replace("\\r", "");
        value = value.replace("\\n", "");
        value = value.replace("\\t", "");
        value = value.trim();
        return value;
    }
}
