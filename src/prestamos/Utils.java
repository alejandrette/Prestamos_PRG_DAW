package prestamos;

import java.time.*;
import java.time.format.*;

public class Utils {
    public String formatoFecha(LocalDate fecha){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formato);
    }

    public String[]
}
