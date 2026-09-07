package tse2026.ej1.negocio;

import jakarta.ejb.Remote;

/**
 * Interfaz remota de la capa de negocio.
 *
 * Vive en el modulo {@code ej1-api} para que un cliente Java (por ejemplo la
 * aplicacion de consola {@code ej1-console}) pueda invocar el Session Bean sin
 * necesidad de tener en el classpath todo el jar de EJBs.
 *
 * Trabaja con tipos simples (String / Boolean) y devuelve texto ya formateado,
 * de modo que el cliente remoto no necesita la clase entidad.
 */
@Remote
public interface TrabajadorSaludServiceRemote {

    /**
     * Alta de un trabajador de la salud. NO recibe id: lo asigna la capa de
     * datos de forma autoincremental.
     *
     * @return mensaje de resultado; si se viola una regla de negocio devuelve
     *         el motivo en lugar de lanzar la excepcion al cliente remoto.
     */
    String agregar(String nombre, String apellido,
                   String especialidad, String matricula,
                   String fechaIngreso, Boolean activo);

    /** Devuelve todos los trabajadores registrados, uno por linea. */
    String listar();

    /** Busca un trabajador por su matricula (atributo de tipo texto). */
    String buscarRemotoPorMatricula(String matricula);

    /** Elimina todos los trabajadores. Devuelve cuantos se borraron. */
    String eliminarTodosRemoto();
}
