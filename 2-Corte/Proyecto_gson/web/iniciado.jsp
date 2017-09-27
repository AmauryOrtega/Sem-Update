<%@page import="modelo.Util"%>
<%@page import="modelo.Pc"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>



<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            Pc usuario = (Pc) request.getSession().getAttribute("pc");
            Integer id = usuario.getId();
            Integer php = usuario.getPuertoPHP();
            Integer sql = usuario.getPuertoSQL();
        %>
        <title>Servidor <%=id%></title>
    </head>
    <body>
        <h1>INICIADO SERVIDOR #<%=id%></h1>
        <%--Aqui se puede cambiar util.ip por request.get... para IP--%>
        <h4><a href="http://<%=Util.ip%>:<%=php%>/phpmyadmin">
        http://<%=Util.ip%>:<%=php%>/phpmyadmin   
        </a></h4>
        <h4>Puerto PHP <%=php%></h4>
        <h4>Puerto SQL <%=sql%></h4>

        <form action="servidordetener?id=<%=id%>" name="form" method="post">
            <button type="submit">detener</button><br>
        </form>
    </body>
</html>
