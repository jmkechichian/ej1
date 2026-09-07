package tse2026.ej1.negocio;

import java.time.LocalDate;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import tse2026.ej1.datos.TrabajadorSaludSingletonLocal;
import tse2026.ej1.entidad.TrabajadorSalud;

@Stateless
public class TrabajadorSaludService
        implements TrabajadorSaludServiceLocal, TrabajadorSaludServiceRemote {

    @EJB
    private TrabajadorSaludSingletonLocal dao;

    /** Constructor sin argumentos requerido por el contenedor EJB. */
    public TrabajadorSaludService() {
    }

    /**
     * Constructor para pruebas unitarias fuera del contenedor: permite
     * inyectar un DAO manualmente sin levantar WildFly.
     */
    TrabajadorSaludService(TrabajadorSaludSingletonLocal dao) {
        this.dao = dao;
    }

    // =========================
    // INTERFAZ LOCAL
    // =========================

    @Override
    public void agregar(TrabajadorSalud trabajador) {

        TrabajadorSalud existente =
                dao.buscarPorMatricula(trabajador.getMatricula());

        if (existente != null) {
            throw new ReglaNegocioException(
                    "Ya existe un trabajador con la matrícula "
                    + trabajador.getMatricula()
            );
        }

        dao.agregar(trabajador);
    }

    @Override
    public List<TrabajadorSalud> obtenerTodos() {
        return dao.obtenerTodos();
    }

    @Override
    public TrabajadorSalud buscarPorMatricula(String matricula) {
        return dao.buscarPorMatricula(matricula);
    }

    @Override
    public void eliminarTodos() {
        dao.eliminarTodos();
    }

    // =========================
    // INTERFAZ REMOTA
    // =========================

    @Override
    public String agregar(String nombre, String apellido,
            String especialidad, String matricula,
            String fechaIngreso, Boolean activo) {

        TrabajadorSalud trabajador = new TrabajadorSalud(
                nombre,
                apellido,
                especialidad,
                matricula,
                LocalDate.parse(fechaIngreso),
                activo
        );

        try {

            agregar(trabajador);

            return "Trabajador agregado correctamente.";

        } catch (ReglaNegocioException e) {

            return e.getMessage();
        }
    }

    @Override
    public String listar() {

        String resultado = "";

        for (TrabajadorSalud trabajador : dao.obtenerTodos()) {
            resultado += trabajador.toString() + "\n";
        }

        return resultado;
    }

    @Override
    public String buscarRemotoPorMatricula(String matricula) {

        TrabajadorSalud trabajador =
                dao.buscarPorMatricula(matricula);

        if (trabajador == null) {
            return "No se encontró un trabajador con matrícula: "
                    + matricula;
        }

        return trabajador.toString();
    }

    @Override
    public String eliminarTodosRemoto() {

        int cantidad = dao.obtenerTodos().size();
        dao.eliminarTodos();

        return "Se eliminaron " + cantidad + " trabajador(es).";
    }
}