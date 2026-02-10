package prestamos;

import prestamos.usuarioException.PrestamoInvalidoException;

import java.time.*;
import java.time.temporal.*;

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

    public void registrarDevolucion(LocalDate fecha) throws PrestamoInvalidoException {
        if (fecha == null || fecha.isBefore(this.fechaPrestamo)) throw new PrestamoInvalidoException("La fecha indicada es nula o anterior a la fecha del préstamo");
        this.fechaDevolucionReal = fecha;
    }

    public int calcularDiasRestraso(){
        if (this.fechaDevolucionReal.isAfter(LocalDate.now())){
            return (int) ChronoUnit.DAYS.between(this.fechaPrestamo, LocalDate.now());
        } else {
            return (int) ChronoUnit.DAYS.between(this.fechaDevolucionPrevista, this.fechaDevolucionReal);
        }
    }

    public boolean estaRetrasado(){
        return this.fechaDevolucionPrevista.isAfter(LocalDate.now());
    }

    @Override
    public String toString(){
        return "Código del libro: " + this.codigoLibro + ", título del libro: " + this.tituloLibro + ", fecha del préstamo: " + this.fechaPrestamo + ", fecha devolución prevista: " + this.fechaDevolucionPrevista + "\n\t" + this.socio;
    }
}
