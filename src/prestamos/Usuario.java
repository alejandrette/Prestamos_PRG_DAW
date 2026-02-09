package prestamos;

import prestamos.usuarioException.*;

import java.time.LocalDate;

public class Usuario {
    private String nombre;
    private String email;
    private String numeroSocio;
    private LocalDate fechaRegistro;
    private boolean sancionado;
    private LocalDate fechaFinSancion;

    public Usuario(String nombre, String email, String numeroSocio, LocalDate fechaRegistro) throws UsuarioInvalidoException {
        if (nombre.isEmpty()) throw new UsuarioInvalidoException("Falta nombre de usuario");
        if (!email.matches(".+@.\\..+")) throw new UsuarioInvalidoException("El formato de email es inválido (ejemplo@gmail.com)");
        if (!numeroSocio.matches("SOC\\d{5}")) throw new UsuarioInvalidoException("El formato de número de sócio es inválido (SOC12345)");
        //if (fechaRegistro.) throw new UsuarioInvalidoException("");

        this.nombre = nombre;
        this.email = email;
        this.numeroSocio = numeroSocio;
        this.fechaRegistro = fechaRegistro;
    }

    public void sancionar(int dias){
        this.fechaFinSancion = fechaFinSancion.plusDays(dias);
        this.sancionado = true;
    }

    public void levantarSancion(){
        this.sancionado = false;
    }

    public boolean estaSancionado(){
        return this.sancionado;
    }

    @Override
    public String toString(){
        return "Usuario: " + this.nombre + ", email: " + this.email + ", numero de sócio: " + this.numeroSocio + ", fecha de registro: ";
    }
}
