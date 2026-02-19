package prestamos;

import java.time.*;
import java.time.format.*;

public class Utils {
    public static String formatoFecha(LocalDate fecha){
        if (fecha == null ) return "N/A";
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formato);
    }

    public static LocalDate obtenerFechas(String fecha){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(fecha, formato);
    }
}
