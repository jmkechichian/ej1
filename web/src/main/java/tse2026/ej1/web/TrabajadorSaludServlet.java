package tse2026.ej1.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import tse2026.ej1.entidad.TrabajadorSalud;
import tse2026.ej1.negocio.ReglaNegocioException;
import tse2026.ej1.negocio.TrabajadorSaludServiceLocal;

@WebServlet("/trabajadores")
public class TrabajadorSaludServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private TrabajadorSaludServiceLocal service;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null || accion.equals("listar")) {

            listar(request, response);

        } else if (accion.equals("buscar")) {

            buscar(request, response);

        } else {

            listar(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("agregar".equals(accion)) {

            agregar(request, response);

        } else {

            listar(request, response);
        }
    }

    private void agregar(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        try {

            // El id NO se pide: lo asigna la capa de datos (autoincremental).

            String nombre =
                    request.getParameter("nombre");

            String apellido =
                    request.getParameter("apellido");

            String especialidad =
                    request.getParameter("especialidad");

            String matricula =
                    request.getParameter("matricula");

            LocalDate fechaIngreso =
                    LocalDate.parse(
                            request.getParameter("fechaIngreso"));

            Boolean activo =
                    Boolean.parseBoolean(
                            request.getParameter("activo"));

            TrabajadorSalud trabajador =
                    new TrabajadorSalud(
                            nombre,
                            apellido,
                            especialidad,
                            matricula,
                            fechaIngreso,
                            activo);

            service.agregar(trabajador);

            response.sendRedirect(
                    request.getContextPath()
                    + "/trabajadores?accion=listar");

        } catch (ReglaNegocioException | IllegalArgumentException e) {

            // ReglaNegocioException: regla de negocio violada (matrícula repetida).
            // IllegalArgumentException: dato de formulario inválido (id no numérico).
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage());
        }
    }

    private void listar(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "trabajadores",
                service.obtenerTodos());

        request.getRequestDispatcher(
                "/WEB-INF/trabajadores.jsp")
                .forward(request, response);
    }

    private void buscar(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String matricula =
                request.getParameter("matricula");

        TrabajadorSalud trabajador =
                service.buscarPorMatricula(matricula);

        if (trabajador != null) {
            request.setAttribute(
                    "trabajadores",
                    Collections.singletonList(trabajador));
        } else {
            request.setAttribute(
                    "trabajadores",
                    Collections.emptyList());
        }

        request.setAttribute("busqueda", matricula);

        request.getRequestDispatcher(
                "/WEB-INF/trabajadores.jsp")
                .forward(request, response);
    }
}