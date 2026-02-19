package prestamos;

import prestamos.usuarioException.*;
import prestamos.usuarioException.PrestamoInvalidoException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class Main{
    public static void mostrarMenu(){
        System.out.println("""
                === SISTEMA GESTIÓN BIBLIOTECA ===\s
                1. Registrar nuevo usuario\s
                2. Realizar préstamo de libro\s
                3. Devolver libro\s
                4. Consultar estado de usuario\s
                5. Mostrar préstamos activos\s
                6. Mostrar usuarios sancionados\s
                7. Actualizar sanciones\s
                8. Salir""");
    }

    public static boolean registrarNuevoUsuario(Scanner in, GestorBiblioteca gestor){
        String nombre, email, numSocio, fechaRegistro;

        try{
            System.out.println("Nombre: ");
            nombre = in.nextLine();
            System.out.println("Email: ");
            email = in.nextLine();
            System.out.println("Número de socio (SOC00011): ");
            numSocio = in.nextLine();
            System.out.println("Fecha registro (dd/mm/aaaa): ");
            fechaRegistro = in.nextLine();

            Usuario usuario = new Usuario(nombre, email, numSocio, Utils.obtenerFechas(fechaRegistro));
            gestor.registrarUsuario(usuario);
            return true;
        } catch (UsuarioInvalidoException | UsuarioRepetidoException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean realizarPrestamoLibro(Scanner in, GestorBiblioteca gestor){
        String codigoLibro, titulo, numSocio, fechaPrestamo;

        try{
            System.out.println("Código libro (LIB0001): ");
            codigoLibro = in.nextLine();
            System.out.println("Título: ");
            titulo = in.nextLine();
            System.out.println("Número de socio (SOC00011): ");
            numSocio = in.nextLine();
            System.out.println("Fecha de préstamo (dd/mm/aaaa): ");
            fechaPrestamo = in.nextLine();

            Usuario usuario = gestor.buscarUsuario(numSocio);
            if (usuario == null) {
                System.out.println("No se ha encontrado al usuario con número de socio " + numSocio);
                return false;
            }

            Prestamo nuevoPrestamo = gestor.realizarPrestamo(codigoLibro, titulo, usuario, Utils.obtenerFechas(fechaPrestamo));
            System.out.println("Devolución prevista: " + Utils.formatoFecha(nuevoPrestamo.getFechaDevolucionPrevista()));
            return true;
        } catch (UsuarioSancionadoException | LibroNoDisponibleException | PrestamoInvalidoException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void devolverLibro(Scanner in, GestorBiblioteca gestor){
        String codigoLibro, fechaDevolucion;

        try {
            System.out.println("Código libro (LIB0001): ");
            codigoLibro = in.nextLine();
            System.out.println("Fecha devolución (dd/mm/aaaa): ");
            fechaDevolucion = in.nextLine();

            Prestamo[] prestamos = gestor.getPrestamos();
            Prestamo prestamoActivo = null;
            for (Prestamo prestamo: prestamos){
                if (prestamo != null && prestamo.getCodigoLibro().equalsIgnoreCase(codigoLibro)){
                    prestamoActivo = prestamo;
                }
            }

            boolean devolver = gestor.devolverLibro(codigoLibro, Utils.obtenerFechas(fechaDevolucion));
            if (!devolver){
                System.out.println("Préstamo ya devuelto o no existe el código de libro");
                return;
            }

            int diasRestraso = (prestamoActivo != null) ? prestamoActivo.calcularDiasRetraso() : 0;
            if (diasRestraso > 0){
                System.out.println("Devolución registrada con " + diasRestraso + " días de retraso");
                System.out.println("Usuario sancionado por " + diasRestraso + " días (hasta el " + Utils.formatoFecha(prestamoActivo.getSocio().getFechaFinSancion()) + ")");
            } else {
                System.out.println("Devolución realizada");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void mostrarPrestamosActivos(GestorBiblioteca gestor){
        System.out.println(Arrays.toString(gestor.getPrestamos()));
    }

    public static void mostrarUsuariosSancionados(GestorBiblioteca gestor){
        Usuario[] usuarios = gestor.getUsuarios();
        Usuario[] usuariosSancionados = null;

        for (int i = 0; i < usuarios.length; i++) {
            if (usuarios[i] != null && usuarios[i].estaSancionado()){
                usuariosSancionados[i] = usuarios[i];
            }
        }

        if (usuariosSancionados == null){
            System.out.println("No hay usuarios sancionados");
        } else {
            System.out.println("=== Usuarios Sancionados ===");
            System.out.println(Arrays.toString(usuariosSancionados));
        }
    }

    public static void main(String[] args) throws UsuarioInvalidoException, UsuarioRepetidoException, PrestamoInvalidoException, UsuarioSancionadoException, LibroNoDisponibleException {
        Scanner in = new Scanner(System.in);
        GestorBiblioteca gestor = new GestorBiblioteca();
        int opcion = 0;

        Usuario yo = new Usuario("Alejandro", "alejandro@gmail.com", "SOC00001", LocalDate.of(2021, 1, 1));
        gestor.registrarUsuario(yo);
        System.out.println(gestor.realizarPrestamo("LIB0001", "Celestina", yo, LocalDate.now()));
        System.out.println(gestor.realizarPrestamo("LIB0002", "Don Quijote", yo, LocalDate.now()));

        while (opcion != 8) {
            try{
                mostrarMenu();
                System.out.println("Escribe tu opción: ");
                opcion = Integer.parseInt(in.nextLine());
                switch (opcion){
                    case 1:
                        String rnu = registrarNuevoUsuario(in, gestor) ? "Usuario correctamente registrado." : "No se ha podido registrar el usuario.";
                        System.out.println(rnu);
                        break;
                    case 2:
                        String rpl = realizarPrestamoLibro(in, gestor) ? "Préstamo realizado." : "No se ha podido realizar el préstamo.";
                        System.out.println(rpl);
                        break;
                    case 3:
                        devolverLibro(in, gestor);
                        break;
                    case 4:
                        break;
                    case 5:
                        mostrarPrestamosActivos(gestor);
                        break;
                    case 6:
                        mostrarUsuariosSancionados(gestor);
                        break;
                    case 7:

                        break;
                    case 8:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opciones válidas 1 - 8");
                }
                System.out.println("....");
                in.nextLine();
            } catch (Exception e){
                System.out.println("Opciones válidas 1 - 8");
            }
        }
        in.close();
    }
}
