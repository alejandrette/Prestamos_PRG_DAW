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

            int diasRetraso = (prestamoActivo != null) ? prestamoActivo.calcularDiasRetraso() : 0;
            if (diasRetraso > 0){
                System.out.println("Devolución registrada con " + diasRetraso + " días de retraso");
                System.out.println("Usuario sancionado por " + diasRetraso + " días (hasta el " + Utils.formatoFecha(prestamoActivo.getSocio().getFechaFinSancion()) + ")");
            } else {
                System.out.println("Devolución realizada");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void mostrarPrestamosActivos(GestorBiblioteca gestor){
        Prestamo[] prestamos = gestor.getPrestamos();

        try {
            System.out.println("=== Prestamos Activos ===");
            if (prestamos[0] == null) {
                System.out.println("No hay prestamos activos");
                return;
            }

            for (Prestamo prestamo : prestamos) {
                if (prestamo != null && prestamo.estaDevuelto()) {
                    System.out.println(prestamo);
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void mostrarUsuariosSancionados(GestorBiblioteca gestor){
        Usuario[] usuarios = gestor.getUsuarios();
        Usuario[] usuariosSancionados = new Usuario[usuarios.length];

        try {
            for (int i = 0, j = 0; i < usuarios.length; i++) {
                if (usuarios[i] != null && usuarios[i].estaSancionado()){
                    usuariosSancionados[j] = usuarios[i];
                    j++;
                }
            }

            System.out.println("=== Usuarios Sancionados ===");
            if (usuariosSancionados[0] == null) {
                System.out.println("No hay usuarios sancionados");
                return;
            }
            for (Usuario user: usuariosSancionados) {
                if (user != null) System.out.println(user);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }

    public static void mostrarEstadoUsuario(Scanner in, GestorBiblioteca gestor){
        String numSocio;

        try {
            System.out.println("Escribe el número de sócio que quieres consultar (SOC00001): ");
            numSocio = in.nextLine();

            if (gestor.buscarUsuario(numSocio) != null){
                System.out.println(gestor.buscarUsuario(numSocio));
            } else {
                System.out.println("No existe ese número de sócio");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }

    public static void main(String[] args) throws UsuarioInvalidoException, UsuarioRepetidoException, PrestamoInvalidoException, UsuarioSancionadoException, LibroNoDisponibleException {
        Scanner in = new Scanner(System.in);
        GestorBiblioteca gestor = new GestorBiblioteca();
        int opcion = 0;

//        Usuario yo = new Usuario("Alejandro", "alejandro@gmail.com", "SOC00001", LocalDate.of(2021, 1, 1));
//        Usuario el = new Usuario("Jhon", "jhon@gmail.com", "SOC00002", LocalDate.of(2022, 10, 10));
//        Usuario otro = new Usuario("Ben", "ben@gmail.com", "SOC00003", LocalDate.of(2026, 12, 12));
//        gestor.registrarUsuario(yo);
//        gestor.registrarUsuario(el);
//        gestor.registrarUsuario(otro);
//        System.out.println(gestor.realizarPrestamo("LIB0001", "Celestina", yo, LocalDate.now()));
//        System.out.println(gestor.realizarPrestamo("LIB0002", "Don Quijote", yo, LocalDate.now()));
//        System.out.println(gestor.realizarPrestamo("LIB0003", "El Principito", el, LocalDate.now()));
//        System.out.println(gestor.realizarPrestamo("LIB0004", "Padre rico y padre pobre", otro, LocalDate.now()));
//        System.out.println(gestor.devolverLibro("LIB0003", LocalDate.of(2026, 10, 10)));
//        System.out.println(gestor.devolverLibro("LIB0004", LocalDate.now()));
//
//        System.out.println(Arrays.toString(gestor.getUsuarios()));

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
                        mostrarEstadoUsuario(in, gestor);
                        break;
                    case 5:
                        mostrarPrestamosActivos(gestor); // Solo ver prestamos que no se han devuelto
                        break;
                    case 6:
                        mostrarUsuariosSancionados(gestor);
                        break;
                    case 7:
                        gestor.levantarSanciones();
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
