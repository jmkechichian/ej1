package tse2026.ej1.negocio;

import jakarta.ejb.ApplicationException;

/**
 * Excepcion de negocio que se lanza cuando NO se cumple una regla para dar de
 * alta una entidad (por ejemplo, matricula repetida).
 *
 * {@code @ApplicationException(rollback = true)} le indica al contenedor EJB que
 * NO la envuelva en una {@code EJBException}: viaja tal cual hasta la capa de
 * presentacion, que puede distinguirla y mostrar un mensaje claro. Ademas marca
 * la transaccion para rollback.
 */
@ApplicationException(rollback = true)
public class ReglaNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
