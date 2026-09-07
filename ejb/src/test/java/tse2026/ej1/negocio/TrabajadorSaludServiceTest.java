package tse2026.ej1.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tse2026.ej1.datos.TrabajadorSaludSingleton;
import tse2026.ej1.entidad.TrabajadorSalud;

/**
 * Prueba unitaria de la capa de negocio SIN contenedor.
 *
 * El Stateless Session Bean se instancia con {@code new} y se le inyecta a mano
 * el Singleton de acceso a datos (que es un POJO sin dependencias). Sirve para
 * verificar hoy mismo la regla de negocio sin necesidad de desplegar en WildFly.
 */
class TrabajadorSaludServiceTest {

    private TrabajadorSaludService service;

    @BeforeEach
    void setUp() {
        service = new TrabajadorSaludService(new TrabajadorSaludSingleton());
    }

    private TrabajadorSalud nuevo(String matricula) {
        // sin id: lo asigna la capa de datos al agregar
        return new TrabajadorSalud("Ana", "Perez", "Enfermeria",
                matricula, LocalDate.of(2020, 1, 15), Boolean.TRUE);
    }

    @Test
    void agregarYListar() {
        service.agregar(nuevo("M-100"));

        assertEquals(1, service.obtenerTodos().size());
        assertNotNull(service.buscarPorMatricula("M-100"));
    }

    @Test
    void agregar_asignaIdAutoincremental() {
        service.agregar(nuevo("M-1"));
        service.agregar(nuevo("M-2"));

        assertEquals(1L, service.buscarPorMatricula("M-1").getId());
        assertEquals(2L, service.buscarPorMatricula("M-2").getId());

        // tras vaciar, la secuencia vuelve a arrancar en 1
        service.eliminarTodos();
        service.agregar(nuevo("M-3"));
        assertEquals(1L, service.buscarPorMatricula("M-3").getId());
    }

    @Test
    void reglaDeNegocio_noPermiteMatriculaDuplicada() {
        service.agregar(nuevo("M-200"));

        ReglaNegocioException ex = assertThrows(
                ReglaNegocioException.class,
                () -> service.agregar(nuevo("M-200")));

        assertTrue(ex.getMessage().contains("M-200"));
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    void buscar_matriculaInexistenteDevuelveNull() {
        assertEquals(null, service.buscarPorMatricula("NO-EXISTE"));
    }

    @Test
    void eliminarTodos_vaciaLaLista() {
        service.agregar(nuevo("M-1"));
        service.agregar(nuevo("M-2"));

        service.eliminarTodos();

        assertTrue(service.obtenerTodos().isEmpty());
    }
}
