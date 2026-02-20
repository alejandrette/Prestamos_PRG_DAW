package prestamos;

import prestamos.usuarioException.*;
import prestamos.usuarioException.PrestamoInvalidoException;

import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main{
    public static void mostrarMenu(){
        System.out.println("\u001B[34m === SISTEMA GESTIÓN BIBLIOTECA ===\u001B[0m");
        System.out.println(""" 
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
        } catch (DateTimeParseException dte) {
            System.out.println("\u001B[31m Formato de fecha inválido (20/02/2026)");
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
                System.out.println("\u001B[33m No se ha encontrado al usuario con número de socio " + numSocio);
                return false;
            }

            Prestamo nuevoPrestamo = gestor.realizarPrestamo(codigoLibro, titulo, usuario, Utils.obtenerFechas(fechaPrestamo));
            System.out.println("\u001B[32m Devolución prevista: " + Utils.formatoFecha(nuevoPrestamo.getFechaDevolucionPrevista()));
            return true;
        } catch (DateTimeParseException dte) {
            System.out.println("\u001B[33m Formato de fecha inválido (20/02/2026)");
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

            Prestamo prestamoActivo = null;
            for (Prestamo prestamo: gestor.getPrestamos()){
                if (prestamo != null && prestamo.getCodigoLibro().equalsIgnoreCase(codigoLibro) && !prestamo.estaDevuelto()){
                    prestamoActivo = prestamo;
                    break;
                }
            }

            boolean devolver = gestor.devolverLibro(codigoLibro, Utils.obtenerFechas(fechaDevolucion));
            if (!devolver){
                System.out.println("\u001B[33m Préstamo ya devuelto o no existe el código de libro");
                return;
            }

            int diasRetraso = (prestamoActivo != null) ? prestamoActivo.calcularDiasRetraso() : 0;
            if (diasRetraso > 0){
                System.out.println("\u001B[33mDevolución registrada con " + diasRetraso + " días de retraso");
                System.out.println("\u001B[33m Usuario sancionado por " + diasRetraso + " días (hasta el " + Utils.formatoFecha(prestamoActivo.getSocio().getFechaFinSancion()) + ")");
            } else {
                System.out.println("\u001B[32m Devolución realizada");
            }
        } catch (DateTimeParseException dte) {
            System.out.println("\u001B[33m Formato de fecha inválido (20/02/2026)");
        } catch (PrestamoInvalidoException pie) {
            System.out.println(pie.getMessage());
        }
    }

    public static void mostrarPrestamosActivos(GestorBiblioteca gestor){
        Prestamo[] prestamos = gestor.getPrestamos();

        try {
            System.out.println("\u001B[34m === Prestamos Activos ===\u001B[0m");
            if (prestamos.length == 0) {
                System.out.println("\u001B[33m No hay prestamos activos");
                return;
            }

            for (Prestamo prestamo : prestamos) {
                if (prestamo != null && !prestamo.estaDevuelto()) {
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

            System.out.println("\u001B[34m === Usuarios Sancionados ===\u001B[0m");
            if (usuariosSancionados[0] == null) {
                System.out.println("\u001B[33m No hay usuarios sancionados");
                return;
            }
            for (Usuario user: usuariosSancionados) {
                if (user != null) System.out.println(user);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void mostrarEstadoUsuario(Scanner in, GestorBiblioteca gestor){
        String numSocio;

        try {
            System.out.println("Escribe el número de sócio que quieres consultar (SOC00001): ");
            numSocio = in.nextLine();

            System.out.println("\u001B[34m === Estado del usuario ===\u001B[0m");
            if (gestor.buscarUsuario(numSocio) != null){
                System.out.println(gestor.buscarUsuario(numSocio));
            } else {
                System.out.println("\u001B[33m No existe ese número de sócio");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        GestorBiblioteca gestor = new GestorBiblioteca();
        int opcion = 0;

        while (opcion != 8) {
            try{
                mostrarMenu();
                System.out.println("Escribe tu opción: ");
                opcion = Integer.parseInt(in.nextLine());
                switch (opcion){
                    case 1:
                        String rnu = registrarNuevoUsuario(in, gestor) ? "\u001B[32m Usuario correctamente registrado." : "\u001B[33m No se ha podido registrar el usuario.";
                        System.out.println(rnu);
                        break;
                    case 2:
                        String rpl = realizarPrestamoLibro(in, gestor) ? "\u001B[32m Préstamo realizado." : "\u001B[33m No se ha podido realizar el préstamo.";
                        System.out.println(rpl);
                        break;
                    case 3:
                        devolverLibro(in, gestor);
                        break;
                    case 4:
                        mostrarEstadoUsuario(in, gestor);
                        break;
                    case 5:
                        mostrarPrestamosActivos(gestor);
                        break;
                    case 6:
                        mostrarUsuariosSancionados(gestor);
                        break;
                    case 7:
                        boolean levantadas = gestor.levantarSanciones();
                        System.out.println(levantadas ? "\u001B[32m Sanciones levantadas a usuarios" : "\u001B[33m No hay usuarios a poder levantar sanciones");
                        break;
                    case 8:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opciones válidas 1 - 8");
                }
                System.out.println("\u001B[0m....");
                in.nextLine();
            } catch (NumberFormatException nfe){
                System.out.println("Opciones válidas 1 - 8");
            }
        }
        in.close();
    }
}
