package prestamos;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class Main extends Utils {
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

            Usuario usuario = new Usuario(nombre, email, numSocio, obtenerFechas(fechaRegistro));
            gestor.registrarUsuario(usuario);
            return true;
        } catch (Exception e){
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
            }

            Prestamo nuevoPrestamo = gestor.realizarPrestamo(codigoLibro, titulo, usuario, obtenerFechas(fechaPrestamo));
            System.out.println("Devolución prevista: " + formatoFecha(nuevoPrestamo.getFechaDevolucionPrevista()));
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
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
                        String rnu = registrarNuevoUsuario(in, gestor) ? "Usuario correctamente registrado." : "No se ha podido registrar el usuario.";
                        System.out.println(rnu);
                        break;
                    case 2:
                        String rpl = realizarPrestamoLibro(in, gestor) ? "Préstamo realizado." : "No se ha podido realizar el préstamo.";
                        System.out.println(rpl);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
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
