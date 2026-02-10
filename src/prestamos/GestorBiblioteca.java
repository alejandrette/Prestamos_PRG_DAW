package prestamos;

import prestamos.usuarioException.*;

public class GestorBiblioteca {
    private static final int MAX_USUARIOS = 50;
    private static final int MAX_PRESTAMOS = 200;

    private Usuario[] usuarios;
    private Prestamo[] prestamos;
    private int numeroUsuarios;
    private int numeroPrestamos;

    public GestorBiblioteca(){
        this.usuarios = new Usuario[MAX_USUARIOS];
        this.prestamos = new Prestamo[MAX_PRESTAMOS];
        this.numeroUsuarios = usuarios.length;
        this.numeroPrestamos = prestamos.length;
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

    public void realizarPrestamo() throws PrestamoInvalidoException{

    }
}
