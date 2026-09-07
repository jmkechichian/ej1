<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="tse2026.ej1.entidad.TrabajadorSalud" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Gestión de Trabajadores de Salud</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f4f4f4;
        }

        h1 {
            color: #333;
        }

        h2 {
            color: #444;
        }

        .contenedor {
            background-color: white;
            padding: 25px;
            margin-bottom: 25px;
            border-radius: 8px;
        }

        form {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-top: 10px;
            font-weight: bold;
        }

        input {
            padding: 8px;
            width: 300px;
        }

        button {
            margin-top: 15px;
            padding: 10px 20px;
            cursor: pointer;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #eeeeee;
        }

        .activo {
            color: green;
            font-weight: bold;
        }

        .inactivo {
            color: red;
            font-weight: bold;
        }

    </style>

</head>

<body>

    <h1>Gestión de Trabajadores de Salud</h1>


    <!-- ================================================= -->
    <!-- AGREGAR TRABAJADOR -->
    <!-- ================================================= -->

    <div class="contenedor">

        <h2>Agregar trabajador</h2>

        <form action="${pageContext.request.contextPath}/trabajadores"
              method="post">

            <input type="hidden"
                   name="accion"
                   value="agregar">

            <!-- El ID no se ingresa: lo genera la capa de datos (autoincremental). -->

            <label for="nombre">
                Nombre:
            </label>

            <input type="text"
                   id="nombre"
                   name="nombre"
                   required>


            <label for="apellido">
                Apellido:
            </label>

            <input type="text"
                   id="apellido"
                   name="apellido"
                   required>


            <label for="especialidad">
                Especialidad:
            </label>

            <input type="text"
                   id="especialidad"
                   name="especialidad"
                   required>


            <label for="matricula">
                Matrícula:
            </label>

            <input type="text"
                   id="matricula"
                   name="matricula"
                   required>


            <label for="fechaIngreso">
                Fecha de ingreso:
            </label>

            <input type="date"
                   id="fechaIngreso"
                   name="fechaIngreso"
                   required>


            <label for="activo">
                Activo:
            </label>

            <select id="activo"
                    name="activo">

                <option value="true">
                    Sí
                </option>

                <option value="false">
                    No
                </option>

            </select>


            <br>

            <button type="submit">
                Agregar trabajador
            </button>

        </form>

    </div>


    <!-- ================================================= -->
    <!-- BUSCAR TRABAJADOR -->
    <!-- ================================================= -->

    <div class="contenedor">

        <h2>Buscar trabajador por matrícula</h2>

        <form action="${pageContext.request.contextPath}/trabajadores"
              method="get">

            <input type="hidden"
                   name="accion"
                   value="buscar">


            <label for="matriculaBuscar">
                Matrícula:
            </label>

            <input type="text"
                   id="matriculaBuscar"
                   name="matricula"
                   required>


            <br>

            <button type="submit">
                Buscar
            </button>

        </form>

    </div>


    <!-- ================================================= -->
    <!-- LISTADO -->
    <!-- ================================================= -->

    <div class="contenedor">

        <h2>Trabajadores registrados</h2>

        <%
            List<TrabajadorSalud> trabajadores =
                (List<TrabajadorSalud>)
                request.getAttribute("trabajadores");

            if (trabajadores != null && !trabajadores.isEmpty()) {
        %>


        <table>

            <thead>

                <tr>

                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Apellido</th>
                    <th>Especialidad</th>
                    <th>Matrícula</th>
                    <th>Fecha de ingreso</th>
                    <th>Activo</th>

                </tr>

            </thead>


            <tbody>

                <%
                    for (TrabajadorSalud trabajador : trabajadores) {
                %>

                <tr>

                    <td>
                        <%= trabajador.getId() %>
                    </td>

                    <td>
                        <%= trabajador.getNombre() %>
                    </td>

                    <td>
                        <%= trabajador.getApellido() %>
                    </td>

                    <td>
                        <%= trabajador.getEspecialidad() %>
                    </td>

                    <td>
                        <%= trabajador.getMatricula() %>
                    </td>

                    <td>
                        <%= trabajador.getFechaIngreso() %>
                    </td>

                    <td>

                        <%
                            if (Boolean.TRUE.equals(trabajador.getActivo())) {
                        %>

                            <span class="activo">
                                Sí
                            </span>

                        <%
                            } else {
                        %>

                            <span class="inactivo">
                                No
                            </span>

                        <%
                            }
                        %>

                    </td>

                </tr>

                <%
                    }
                %>

            </tbody>

        </table>


        <%
            } else {
        %>

            <p>
                No hay trabajadores registrados.
            </p>

        <%
            }
        %>

    </div>


    <!-- ================================================= -->
    <!-- LISTAR TODOS -->
    <!-- ================================================= -->

    <div class="contenedor">

        <form action="${pageContext.request.contextPath}/trabajadores"
              method="get">

            <input type="hidden"
                   name="accion"
                   value="listar">

            <button type="submit">
                Mostrar todos los trabajadores
            </button>

        </form>

    </div>

</body>

</html>