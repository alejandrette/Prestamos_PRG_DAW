package prestamos;

import prestamos.usuarioException.*;

import java.time.*;

public class Usuario {
    private String nombre;
    private String email;
    private String numeroSocio;
    private LocalDate fechaRegistro;
    private boolean sancionado;
    private LocalDate fechaFinSancion;

    public Usuario(String nombre, String email, String numeroSocio, LocalDate fechaRegistro) throws UsuarioInvalidoException {
        if (nombre == null || nombre.isEmpty()) throw new UsuarioInvalidoException("Falta nombre de usuario");
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) throw new UsuarioInvalidoException("El formato de email es inválido (ejemplo@gmail.com)");
        if (!numeroSocio.matches("^SOC\\d{5}$")) throw new UsuarioInvalidoException("El formato de número de sócio es inválido (SOC12345)");
        if (fechaRegistro == null) throw new UsuarioInvalidoException("La fecha de registro está incompleta");

        this.nombre = nombre;
        this.email = email;
        this.numeroSocio = numeroSocio;
        this.fechaRegistro = fechaRegistro;
        this.sancionado = false;
        this.fechaFinSancion = null;
    }

    public void sancionar(int dias){
        this.fechaFinSancion = LocalDate.now().plusDays(dias);
        this.sancionado = true;
    }

    public void levantarSancion(){
        this.sancionado = false;
        this.fechaFinSancion = null;
    }

    public boolean estaSancionado(){
        if (!this.sancionado) return false;
        if (this.fechaFinSancion == null) return false;
        if(this.fechaFinSancion.isBefore(LocalDate.now())) {
            levantarSancion();
            return false;
        }
        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getNumeroSocio() {
        return numeroSocio;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public boolean isSancionado() {
        return sancionado;
    }

    public LocalDate getFechaFinSancion() {
        return fechaFinSancion;
    }

    @Override
    public String toString(){
        String msg = this.sancionado ? ", Sancionado hasta = " + Utils.formatoFecha(this.fechaFinSancion) : ", Sin sanción";
        return "Usuario: " + this.nombre + ", email: " + this.email + ", numero de sócio: " + this.numeroSocio + ", fecha de registro: " + Utils.formatoFecha(this.fechaRegistro) + msg;
    }
}
