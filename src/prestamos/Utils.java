package prestamos;

import java.time.*;
import java.time.format.*;

public class Utils {
    public static String formatoFecha(LocalDate fecha){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formato);
    }

    public static LocalDate obtenerFechas(String fecha){
        int[] nuevaFecha = new int[3];

        nuevaFecha[0] = Integer.parseInt(fecha.substring(0, 2));
        nuevaFecha[1] = Integer.parseInt(fecha.substring(3, 5));
        nuevaFecha[2] = Integer.parseInt(fecha.substring(6));

        return LocalDate.of(nuevaFecha[2], nuevaFecha[1], nuevaFecha[0]);
    }
}
