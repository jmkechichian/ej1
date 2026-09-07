package tse2026.ej1.datos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.ejb.Singleton;

import tse2026.ej1.entidad.TrabajadorSalud;

@Singleton
public class TrabajadorSaludSingleton
        implements TrabajadorSaludSingletonLocal, TrabajadorSaludSingletonRemote {

    private List<TrabajadorSalud> trabajadores = new ArrayList<>();

    /** Secuencia para el id autoincremental (simula una columna IDENTITY). */
    private final AtomicLong secuencia = new AtomicLong(0);

    @Override
    public void agregar(TrabajadorSalud trabajador) {
        trabajador.setId(secuencia.incrementAndGet());   // id asignado por la capa de datos
        trabajadores.add(trabajador);
    }

    @Override
    public List<TrabajadorSalud> obtenerTodos() {
        return trabajadores;
    }

    @Override
    public TrabajadorSalud buscarPorMatricula(String matricula) {

        for (TrabajadorSalud trabajador : trabajadores) {

            if (trabajador.getMatricula().equals(matricula)) {
                return trabajador;
            }
        }

        return null;
    }

    @Override
    public void eliminarTodos() {
        trabajadores.clear();
        secuencia.set(0);            // el id vuelve a arrancar en 1
    }

    // =========================
    // Métodos de la interfaz Remota
    // =========================

    @Override
    public void agregar(String nombre, String apellido,
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

        agregar(trabajador);   // asigna el id autoincremental
    }

    @Override
    public String listar() {

        String resultado = "";

        for (TrabajadorSalud trabajador : trabajadores) {
            resultado += trabajador.toString() + "\n";
        }

        return resultado;
    }

    @Override
    public String buscarRemotoPorMatricula(String matricula) {

        TrabajadorSalud trabajador = buscarPorMatricula(matricula);

        if (trabajador == null) {
            return "No se encontró un trabajador con matrícula: " + matricula;
        }

        return trabajador.toString();
    }

    @Override
    public String eliminarTodosRemoto() {

        int cantidad = trabajadores.size();
        eliminarTodos();

        return "Se eliminaron " + cantidad + " trabajador(es).";
    }
}
