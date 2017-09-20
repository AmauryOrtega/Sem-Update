<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>FORMULARIO PARA AGREGAR REGISTRO</title>
    </head>
    <%
        Date fecha = new Date();
    %>
    <body>
        <h5>La hora es <%= fecha%></h5>

    <center>
        <h1>AGREGAR REGISTRO</h1>

        <form name="f1" action="cregistro" method="POST">
            <fieldset style="width: 40%">
                <legend>Datos de registro</legend>
                <table border="1">
                    <tbody>
                        <tr>
                            <th>ID:</th>
                            <td><input type="number" name="id" value="" /></td>
                        </tr>
                        <tr>
                            <th>PuertoSQL:</th>
                            <td><input type="number" name="puertosql" value="" /></td>
                        </tr>
                        <tr>
                            <th>PuertoPHP:</th>
                            <td><input type="number" name="puertophp" value="" /></td>
                        </tr>
                        <tr colspan="2">
                            <input type="submit" value="Guardar" name="accion" />&nbsp;&nbsp;&nbsp;
                            <input type="reset" value="Limpiar" />
                        </tr>
                    </tbody>
                </table>

            </fieldset>
        </form>
    </center>

</body>
</html>


<%!
    public void unMetodo() {

    }
%>