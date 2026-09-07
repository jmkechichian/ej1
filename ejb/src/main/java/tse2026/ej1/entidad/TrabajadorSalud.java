package tse2026.ej1.entidad;

import java.io.Serializable;
import java.time.LocalDate;

public class TrabajadorSalud implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String matricula;
    private LocalDate fechaIngreso;
    private Boolean activo;

    public TrabajadorSalud() {
    }

    /**
     * Constructor para dar de alta: NO recibe id. El id lo asigna la capa de
     * datos de forma autoincremental al persistir el trabajador.
     */
    public TrabajadorSalud(String nombre, String apellido,
            String especialidad, String matricula,
            LocalDate fechaIngreso, Boolean activo) {

        this(null, nombre, apellido, especialidad, matricula, fechaIngreso, activo);
    }

    public TrabajadorSalud(Long id, String nombre, String apellido,
            String especialidad, String matricula,
            LocalDate fechaIngreso, Boolean activo) {

        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
        this.matricula = matricula;
        this.fechaIngreso = fechaIngreso;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "TrabajadorSalud{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", matricula='" + matricula + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", activo=" + activo +
                '}';
    }
}