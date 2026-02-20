package prestamos;

import prestamos.usuarioException.*;

import java.time.*;
import java.time.temporal.*;

public class GestorBiblioteca{
    private static final int MAX_USUARIOS = 50;
    private static final int MAX_PRESTAMOS = 200;

    private final Usuario[] usuarios;
    private final Prestamo[] prestamos;
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
            if (usuarios[i].getNumeroSocio().equals(user.getNumeroSocio())) {
                throw new UsuarioRepetidoException("El usuario ya pertenece a la lista");
            }
        }

        if (numeroUsuarios >= MAX_USUARIOS) throw new UsuarioRepetidoException("No se pueden registrar mas usuarios");

        usuarios[numeroUsuarios] = user;
        numeroUsuarios++;
    }

    public Prestamo realizarPrestamo(String codigoLibro, String tituloLibro, Usuario usuario, LocalDate fechaPrestamo) throws PrestamoInvalidoException, UsuarioSancionadoException, LibroNoDisponibleException{
        if (usuario == null) throw new PrestamoInvalidoException("Usuario nulo");
        if (usuario.estaSancionado()) throw new UsuarioSancionadoException("El usuario está sancionado hasta " + Utils.formatoFecha(usuario.getFechaFinSancion()));

        for (int i = 0; i < numeroPrestamos; i++) {
            if (prestamos[i] != null && prestamos[i].getCodigoLibro().equalsIgnoreCase(codigoLibro) && !prestamos[i].estaDevuelto()) {
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
        if (codigoLibro == null) throw new PrestamoInvalidoException("Código de libro vacío");
        if (fechaDevolucion == null) throw new PrestamoInvalidoException("La fecha de devolución es nula");

        for (int i = 0; i < numeroPrestamos; i++) {
            if (codigoLibro.equalsIgnoreCase(prestamos[i].getCodigoLibro())){
                if (prestamos[i].estaDevuelto()) throw new PrestamoInvalidoException("Préstamo ya devuelto");
                prestamos[i].registrarDevolucion(fechaDevolucion);

                int retraso = prestamos[i].calcularDiasRetraso();
                if (retraso > 0) prestamos[i].getSocio().sancionar(retraso);
                return true;
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
            copia[i] = this.prestamos[i];
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

    public void levantarSanciones(){
        Usuario[] usuariosALevantar = this.getUsuarios();

        for (Usuario usuario : usuariosALevantar) {
            if (usuario != null && usuario.getFechaFinSancion().isBefore(LocalDate.now()) && usuario.getFechaFinSancion() != null) {
                usuario.levantarSancion();
            }
        }
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
