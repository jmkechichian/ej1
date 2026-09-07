package tse2026.ej1.consola;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;
import java.util.Scanner;
import tse2026.ej1.negocio.TrabajadorSaludServiceRemote;

public class ConsolaApp {

    public static void main(String[] args) throws Exception {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.wildfly.naming.client.WildFlyInitialContextFactory");
        env.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
        // Usuario de aplicación dado de alta en WildFly (add-user).
        env.put(Context.SECURITY_PRINCIPAL, "prueba");
        env.put(Context.SECURITY_CREDENTIALS, "prueba");
        Context ctx = new InitialContext(env);

        // ejb:<app>/<módulo>/<bean>!<interfaz remota>
        // app = ej1 (por el ej1.ear), módulo = ej1-ejb (finalName del jar EJB).
        String jndiName = "ejb:ej1/ej1-ejb/TrabajadorSaludService!" +
                TrabajadorSaludServiceRemote.class.getName();
        TrabajadorSaludServiceRemote service =
                (TrabajadorSaludServiceRemote) ctx.lookup(jndiName);

        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        while (!salir) {
            System.out.println("\n1) Agregar  2) Listar  3) Buscar por matrícula  4) Borrar todos  5) Salir");
            System.out.print("Opción: ");
            switch (sc.nextLine().trim()) {
                case "1":
                    // El Id no se pide: lo asigna la capa de datos (autoincremental).
                    System.out.print("Nombre: "); String nombre = sc.nextLine();
                    System.out.print("Apellido: "); String apellido = sc.nextLine();
                    System.out.print("Especialidad: "); String especialidad = sc.nextLine();
                    System.out.print("Matrícula: "); String matricula = sc.nextLine();
                    System.out.print("Fecha ingreso (YYYY-MM-DD): "); String fecha = sc.nextLine();
                    System.out.print("Activo (s/n): ");
                    Boolean activo = sc.nextLine().trim().equalsIgnoreCase("s");

                    String resultado = service.agregar(nombre, apellido, especialidad,
                            matricula, fecha, activo);
                    System.out.println(resultado);
                    break;
                case "2":
                    System.out.println(service.listar());
                    break;
                case "3":
                    System.out.print("Matrícula a buscar: ");
                    System.out.println(service.buscarRemotoPorMatricula(sc.nextLine()));
                    break;
                case "4":
                    System.out.print("¿Borrar TODOS los trabajadores? (s/n): ");
                    if (sc.nextLine().trim().equalsIgnoreCase("s")) {
                        System.out.println(service.eliminarTodosRemoto());
                    } else {
                        System.out.println("Cancelado.");
                    }
                    break;
                case "5":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        sc.close();
    }
}