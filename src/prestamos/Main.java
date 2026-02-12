package prestamos;

import java.util.Scanner;

public class Main {
    public static void registrarNuevoUsuario(Scanner in){
        String nombre, email, numSocio, fechaRegistro;
        System.out.println("Nombre: ");
        nombre = in.nextLine();
        System.out.println("Email: ");
        email = in.nextLine();
        System.out.println("Fecha registro (dd/mm/aaaa): ");
        email = in.nextLine();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("=== SISTEMA GESTIÓN BIBLIOTECA ===" +
                    "1. Registrar nuevo usuario \n" +
                    "2. Realizar préstamo de libro \n" +
                    "3. Devolver libro \n" +
                    "4. Consultar estado de usuario \n" +
                    "5. Mostrar préstamos activos \n" +
                    "6. Mostrar usuarios sancionados \n" +
                    "7. Actualizar sanciones \n" +
                    "8. Salir");

            System.out.println("Escribe tu opción: ");

            switch (opcion){
                case 1:
                    registrarNuevoUsuario(in);
                    break;
                case 2:
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
            }

            System.out.println("....");
            in.nextLine();
        } while (opcion != 8);
    }
}
