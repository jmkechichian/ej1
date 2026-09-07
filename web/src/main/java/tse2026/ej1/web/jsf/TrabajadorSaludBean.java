package tse2026.ej1.web.jsf;

import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import tse2026.ej1.entidad.TrabajadorSalud;
import tse2026.ej1.negocio.ReglaNegocioException;
import tse2026.ej1.negocio.TrabajadorSaludServiceLocal;

/*
 capa de presentacion JSF (Ejercicio 4).
 
 */
@Named
@RequestScoped
public class TrabajadorSaludBean {

    @EJB
    private TrabajadorSaludServiceLocal service;

    // ---- modelo del formulario de alta (sin id: lo asigna la capa de datos) ----
    private String nombre;
    private String apellido;
    private String especialidad;
    private String matricula;
    private LocalDate fechaIngreso;
    private boolean activo = true;

    // ---- busqueda ----
    private String matriculaBuscar;

    // ---- resultado mostrado en la tabla ----
    private List<TrabajadorSalud> trabajadores;

    @PostConstruct
    public void init() {
        trabajadores = service.obtenerTodos();
    }

    /** Alta con AJAX. La regla de negocio (matricula unica) la valida el EJB. */
    public void agregar() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        try {
            service.agregar(new TrabajadorSalud(nombre, apellido,
                    especialidad, matricula, fechaIngreso, activo));

            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Trabajador agregado", "Matricula " + matricula));
            limpiarFormulario();
        } catch (ReglaNegocioException e) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No se pudo agregar", e.getMessage()));
        }
        trabajadores = service.obtenerTodos();
    }

    /* Busca por matricula  */
    public void buscar() {
        if (matriculaBuscar == null || matriculaBuscar.isBlank()) {
            trabajadores = service.obtenerTodos();
            return;
        }
        TrabajadorSalud t = service.buscarPorMatricula(matriculaBuscar.trim());
        trabajadores = (t == null) ? List.of() : List.of(t);
    }

    /*Limpia el filtro y vuelve a mostrar todos. */
    public void listarTodos() {
        matriculaBuscar = null;
        trabajadores = service.obtenerTodos();
    }

    private void limpiarFormulario() {
        nombre = null;
        apellido = null;
        especialidad = null;
        matricula = null;
        fechaIngreso = null;
        activo = true;
    }

    //getters y setters 

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getMatriculaBuscar() { return matriculaBuscar; }
    public void setMatriculaBuscar(String matriculaBuscar) { this.matriculaBuscar = matriculaBuscar; }

    public List<TrabajadorSalud> getTrabajadores() { return trabajadores; }
}
