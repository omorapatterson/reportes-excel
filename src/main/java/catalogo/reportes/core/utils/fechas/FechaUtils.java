package catalogo.reportes.core.utils.fechas;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class FechaUtils {

    /**
     * Formatea fecha date time.
     *
     * @param formatoFecha    the formato fecha
     * @param fechaAFormatear the fecha a formatear
     * @return the date time
     */
    public static DateTime formateaFecha(String formatoFecha, String fechaAFormatear) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(formatoFecha);
        return formatter.parseDateTime(fechaAFormatear);
    }

    public static Date obtenerFechaDate(String fecha) {
        Date fechaDate = null;
        try {
            if (fecha != null && !fecha.equals("")) {
                DateTime fechaDateTime = formateaFecha("dd-MM-yyyy HH:mm:ss", fecha);
                fechaDate = fechaDateTime.toDate();
            }
        } catch (Exception exception) {
            // no hacer nada
        }
        return fechaDate;
    }

}
