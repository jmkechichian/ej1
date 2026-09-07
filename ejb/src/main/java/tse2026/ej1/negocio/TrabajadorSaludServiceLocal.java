package tse2026.ej1.negocio;

import java.util.List;

import jakarta.ejb.Local;

import tse2026.ej1.entidad.TrabajadorSalud;

@Local
public interface TrabajadorSaludServiceLocal {

    void agregar(TrabajadorSalud trabajador);

    List<TrabajadorSalud> obtenerTodos();

    TrabajadorSalud buscarPorMatricula(String matricula);

    void eliminarTodos();
}