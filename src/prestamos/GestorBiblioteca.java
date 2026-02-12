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
        for (int i = 0; i < numeroUsuarios; i++) {
            if (usuarios[i] == user) {
                throw new UsuarioRepetidoException("El usuario ya pertenece a la lista");
            } else {
                usuarios[i] = user;
            }
        }
    }

    public Prestamo realizarPrestamo(String codigoLibro, String tituloLibro, Usuario usuario, LocalDate fechaPrestamo) throws PrestamoInvalidoException, UsuarioSancionadoException, LibroNoDisponibleException{
        if (usuario.estaSancionado()) throw new UsuarioSancionadoException("El usuario está sancionado hasta " + formatoFecha(usuario.getFechaFinSancion()));
        if (usuario == null) throw new PrestamoInvalidoException("Usuario nulo");

        for (int i = 0; i < numeroPrestamos; i++) {
            Prestamo p = prestamos[i];
            if (p.getCodigoLibro().equalsIgnoreCase(codigoLibro)) {
                throw new LibroNoDisponibleException("El libro está actualmente prestado");
            }
        }

        return new Prestamo(codigoLibro, tituloLibro, usuario, fechaPrestamo);
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

    public String getPrestamos(){
        String prestamos = "";
        for (int i = 0; i < numeroPrestamos; i++) {
            prestamos += (i+1) + ". " + prestamos;
        }

        return prestamos;
    }

    public String getUsuarios(){
        String usuarios = "";
        for (int i = 0; i < numeroUsuarios; i++) {
            usuarios += (i+1) + ". " + usuarios;
        }

        return usuarios;
    }

    public String toString(){
        return this.getPrestamos() + this.getUsuarios();
    }
}
