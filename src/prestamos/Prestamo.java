package prestamos;

import prestamos.usuarioException.PrestamoInvalidoException;

import java.time.*;

public class Prestamo {
    private String codigoLibro;
    private String tituloLibro;
    private Usuario socio;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;

    public Prestamo(String codigoLibro, String tituloLibro, Usuario socio, LocalDate fechaPrestamo) throws PrestamoInvalidoException {
        if (!codigoLibro.matches("^LIB\\d{4}$")) throw new PrestamoInvalidoException("El formato del código de libro (LIB1234)");
        if (tituloLibro.isEmpty()) throw new PrestamoInvalidoException("El título del libro no es válido");
        if (fechaPrestamo == null || fechaPrestamo.isAfter(LocalDate.now())) throw new PrestamoInvalidoException("La fecha del préstamo es inválido");

        this.codigoLibro = codigoLibro;
        this.tituloLibro = tituloLibro;
        this.socio = socio;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaPrestamo.plusDays(14);
        this.fechaDevolucionReal = null;
    }

    public void registrarDevolucion(LocalDate fecha){
        if (fecha == null || fecha.isBefore(this.fechaPrestamo)) return;
    }
}
