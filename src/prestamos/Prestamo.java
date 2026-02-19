package prestamos;

import prestamos.usuarioException.PrestamoInvalidoException;

import java.time.*;
import java.time.temporal.*;

public class Prestamo{
    private String codigoLibro;
    private String tituloLibro;
    private Usuario socio;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;

    public Prestamo(String codigoLibro, String tituloLibro, Usuario socio, LocalDate fechaPrestamo) throws PrestamoInvalidoException {
        if (!codigoLibro.matches("^LIB\\d{4}$")) throw new PrestamoInvalidoException("El formato del código de libro (LIB1234)");
        if (tituloLibro.isEmpty()) throw new PrestamoInvalidoException("El título del libro no es válido");
        if (fechaPrestamo == null) throw new PrestamoInvalidoException("La fecha del préstamo es inválido");
        if (fechaPrestamo.isAfter(LocalDate.now())) throw new PrestamoInvalidoException("La fecha del préstamo posterior a la fecha actual");
        if (socio == null) throw new PrestamoInvalidoException("Socio nulo");

        this.codigoLibro = codigoLibro;
        this.tituloLibro = tituloLibro;
        this.socio = socio;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaPrestamo.plusDays(14);
        this.fechaDevolucionReal = null;
    }

    public void registrarDevolucion(LocalDate fecha) throws PrestamoInvalidoException {
        if (fecha == null) throw new PrestamoInvalidoException("La fecha indicada es nula");
        if (fecha.isBefore(this.fechaPrestamo)) throw new PrestamoInvalidoException("La fecha indicada es anterior a la fecha del préstamo");
        this.fechaDevolucionReal = fecha;
    }

    public int calcularDiasRetraso(){
        LocalDate comparacion = (fechaDevolucionReal != null) ? fechaDevolucionReal : LocalDate.now();
        return (int) ChronoUnit.DAYS.between(this.fechaDevolucionPrevista, comparacion);
    }

    public boolean estaRetrasado(){
        return LocalDate.now().isAfter(this.fechaDevolucionPrevista);
    }

    public String getCodigoLibro() {
        return codigoLibro;
    }

    public Usuario getSocio() {
        return socio;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucionPrevista() {
        return this.fechaDevolucionPrevista;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public LocalDate getFechaDevolucionReal() {
        return this.fechaDevolucionReal;
    }

    public boolean estaDevuelto() {
        return this.fechaDevolucionReal == null;
    }

    @Override
    public String toString(){
        return "\nCódigo del libro: " + this.codigoLibro + ", título del libro: " + this.tituloLibro + ", fecha del préstamo: " + Utils.formatoFecha(this.fechaPrestamo) + ", fecha devolución prevista: " + Utils.formatoFecha(this.fechaDevolucionPrevista) + "\n\t" + this.socio;
    }
}
