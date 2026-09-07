package tse2026.ej1.datos;

import jakarta.ejb.Remote;

@Remote
public interface TrabajadorSaludSingletonRemote {

    void agregar(String nombre, String apellido,
                 String especialidad, String matricula,
                 String fechaIngreso, Boolean activo);

    String listar();

    String buscarRemotoPorMatricula(String matricula);

    String eliminarTodosRemoto();
}
