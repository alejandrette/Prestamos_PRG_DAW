package prestamos;

import prestamos.usuarioException.*;

import java.time.*;
import java.time.temporal.*;

public class GestorBiblioteca extends Utils{
    private static final int MAX_USUARIOS = 50;
    private static final int MAX_PRESTAMOS = 200;

    private Usuario[] usuarios;
    private Prestamo[] prestamos;
    private int numeroUsuarios;
    private int numeroPrestamos;

    public GestorBiblioteca(){
        this.usuarios = new Usuario[MAX_USUARIOS];
        this.prestamos = new Prestamo[MAX_PRESTAMOS];
        this.numeroUsuarios = 0;
        this.numeroPrestamos = 0;
    }

    public void registrarUsuario(Usuario user) throws UsuarioRepetidoException {
        if (user == null) throw new UsuarioRepetidoException("El usuario es nulo");

        for (int i = 0; i < numeroUsuarios; i++) {
            if (usuarios[i] == user) {
                throw new UsuarioRepetidoException("El usuario ya pertenece a la lista");
            }
        }

        if (numeroUsuarios >= MAX_USUARIOS) throw new UsuarioRepetidoException("No se pueden registrar mas usuarios");

        usuarios[numeroUsuarios] = user;
        numeroUsuarios++;
    }

    public Prestamo realizarPrestamo(String codigoLibro, String tituloLibro, Usuario usuario, LocalDate fechaPrestamo) throws PrestamoInvalidoException, UsuarioSancionadoException, LibroNoDisponibleException{
        if (usuario == null) throw new PrestamoInvalidoException("Usuario nulo");
        if (usuario.estaSancionado()) throw new UsuarioSancionadoException("El usuario está sancionado hasta " + formatoFecha(usuario.getFechaFinSancion()));

        for (int i = 0; i < numeroPrestamos; i++) {
            Prestamo p = prestamos[i];
            if (p.getCodigoLibro().equalsIgnoreCase(codigoLibro)) {
                throw new LibroNoDisponibleException("El libro está actualmente prestado");
            }
        }

        if (numeroPrestamos >= MAX_PRESTAMOS) throw new PrestamoInvalidoException("No se pueden registrar mas prestamos");

        Prestamo nuevoPrestamo = new Prestamo(codigoLibro, tituloLibro, usuario, fechaPrestamo);
        prestamos[numeroPrestamos] = nuevoPrestamo;
        numeroPrestamos++;
        return nuevoPrestamo;
    }

    public boolean devolverLibro(String codigoLibro, LocalDate fechaDevolucion) throws PrestamoInvalidoException{
        if (fechaDevolucion.isBefore(LocalDate.now())) throw new PrestamoInvalidoException("La fecha de devolución es anterior al prestamo");

        for (int i = 0; i < numeroPrestamos; i++) {
            if (codigoLibro.equalsIgnoreCase(prestamos[i].getCodigoLibro())){
                prestamos[i].registrarDevolucion(fechaDevolucion);
                return true;
            }
            // Comprobar funcionamiento
            if (fechaDevolucion.isAfter(prestamos[i].getFechaDevolucionPrevista())){
                int diasSancionar = (int) ChronoUnit.DAYS.between(fechaDevolucion, prestamos[i].getFechaDevolucionPrevista());
                prestamos[i].getSocio().sancionar(diasSancionar);
            }
        }

        return false;
    }

    public Usuario buscarUsuario(String numeroSocio){
        for (int i = 0; i < numeroUsuarios; i++) {
            if (usuarios[i].getNumeroSocio().equalsIgnoreCase(numeroSocio)) return usuarios[i];
        }
        return null;
    }

    public Prestamo[] getPrestamos(){
        Prestamo[] copia = new Prestamo[numeroPrestamos];
        for (int i = 0; i < this.numeroPrestamos; i++) {
            copia[i] = prestamos[numeroPrestamos];
        }

        return copia;
    }

    public Usuario[] getUsuarios(){
        Usuario[] copia = new Usuario[numeroUsuarios];
        for (int i = 0; i < this.numeroUsuarios; i++) {
            copia[i] = this.usuarios[i];
        }

        return copia;
    }

    public String toString(){
        String stringUsuario = "=== USUARIOS === \n";
        for (int i = 0; i < this.numeroUsuarios; i++) {
            stringUsuario += " - " + usuarios[i] + "\n";
        }

        String stringPrestamo = "=== Prestamos === \n";
        for (int i = 0; i < this.numeroPrestamos; i++) {
            stringPrestamo += " - " + prestamos[i] + "\n";
        }
        return stringUsuario + stringPrestamo;
    }
}
