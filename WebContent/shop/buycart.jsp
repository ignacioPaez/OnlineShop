<%@page import="modelo.Usuario"%>
<%@page import="dao.UsuarioDao"%>
<%@page import="dao.ProductoDao"%>
<%@page import="modelo.Producto"%>
<%@page import="java.util.Iterator"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Validar carrito</title>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.6.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/vanadium.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/validacion.css" media="screen, tv, projection" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/screen_yellow.css" media="screen, tv, projection" />
</head>

<body>
<!-- Contenedor principal-->
<div id="siteBox">

    <!--Cabecera-->
    <%@include file="/WEB-INF/include/header.jsp" %>


    <!-- Contenido de la pagina -->
    <div id="content">

  <!-- Menu Izquiero -->
  <%@include file="/WEB-INF/include/menu.jsp" %>

    <!-- Contenido de la columna derecha -->
    <div id="contentRight">
        <%@include file="/WEB-INF/include/resultados.jsp" %>

    <p>
        <div class="title">
		<span class="title_icon"> 
			<img src="${pageContext.request.contextPath}/images/template/bullet1.gif" alt title>
		</span> 
		Productos en la cesta
		</div>
        <% Carrito carro = (Carrito) session.getAttribute("carrito");
        if (carro == null){%>
            No se han a&ntilde;adido productos a la cesta de la compra
        <% } else if (carro.getArticulos().size() == 0){ %>
            No se han a&ntilde;adido productos a la cesta de la compra
        <% } else { %>
        <p>
        <div class="feat_prod_box_details">
        <table class="cart_table">
        	<tbody>
             <tr class="cart_title"><td>Producto</td> <td>Unidades</td><td>Precio Unidad</td><td>Precio Total</td></tr>
           <!--  <tr> <td>&nbsp;</td> <td>&nbsp;</td> <td>&nbsp;</td> <td>&nbsp;</td> </tr> -->
           <% Iterator <String> iteradorCarro = carro.getArticulos().keySet().iterator();
            while (iteradorCarro.hasNext()){
                String cod = iteradorCarro.next();
                int cant = carro.getArticulos().get(cod);
                Producto prod = ProductoDao.getInstance().getProduct(cod); %>
                <tr class="contentTable" >
                    <td><%= prod.getNombre() %></td>
                    <td><%= cant %></td>
                    <td><%= Tools.roundDouble(prod.getPrecio()) %> $</td>
                    <td><%= Tools.roundDouble(prod.getPrecio() * cant) %> $</td>
                </tr>
            <% } %>
            	<tr>
                	<td colspan="3" class="cart_total"><span class="red">PRECIO TOTAL:</span></td>
                	<td>  <%= Tools.roundDouble(carro.getPrecio()) %> $</td>                
                </tr>
            </tbody>
        </table>
        </div>
        <%-- </p>
        <p/>
        <br />
        <b>Precio Total:</b> <%= Tools.roundDouble(carro.getPrecio()) %> $
        <br />
        <p> --%>
        <div class="title">
		<span class="title_icon"> 
			<img src="${pageContext.request.contextPath}/images/template/bullet1.gif" alt title>
		</span> 
		Detalles del pago
		</div>
        <form name="buy" action="${pageContext.request.contextPath}/UpdateCartServlet" method="post" style="margin: 30px;" >
        <% Boolean autentificado = (Boolean)session.getAttribute("auth");
        String mailUsuario = (String)session.getAttribute("usuario");
        if (mailUsuario != null){
            Usuario user = UsuarioDao.getInstance().getUser(mailUsuario);
            if (user == null){
                request.setAttribute("errorSesion", "");
                request.setAttribute("resultados", "No se encontro el usuario de la sesion");
                Tools.anadirMensaje(request, "No se ha encontrado el usuario activo y se ha cerrado la sesion"); %>
                <jsp:forward page="./logout" />
            <% }else if (autentificado != null && autentificado == true){ %>
                <input type="hidden" name="name" value="<%= user.getNombre() %>" />
                <input type="hidden" name="dir" value="<%= user.getDir() %>" />
                <input type="hidden" name="email" value="<%= user.getMail() %>" />
                <b>Nombre: </b><%= user.getNombre() %><br />
                <b>Direcci&oacute;n: </b><%= user.getDir() %><br />
                <b>Email: </b><%= user.getMail() %><br />    
            <% }
        }else{ %>
            Nombre<br />
            <input type="text" name="name" size="50" maxlength="100" class=":alpha :required :only_on_blur"/><br /><br />

            Direcci&oacute;n<br />
            Ejemplo: Calle, 1 28002-Madrid<br />
            <input type="text" name="dir" size="30" maxlength="60" class=":required :only_on_blur"/><br /><br />

            Email<br />
            <input type="text" name="email" size="70" maxlength="200" class=":email :required :only_on_blur"/><br /><br />

            <% } %>
            <input type="hidden" name="buy" value="preBuy" />
            <input type="image" name="creditCard" src="${pageContext.request.contextPath}/images/icons/creditcard.png" title="Pago con tarjeta" style="margin: 20px 0px; height:80px; weight:80px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="image" name="reembolso" src="${pageContext.request.contextPath}/images/icons/contrareembolso.png" title="Pago contra reembolso" style="margin: 20px 0px; height:80px; weight:80px;" />&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="image" name="transfer" src="${pageContext.request.contextPath}/images/icons/banktransfer.png" title="Pago por transferencia bancaria" style="margin: 20px 0px; height:80px; weight:80px;" />

        </form>
        <!-- </p> -->

       <% } %>
    <!-- </p> -->

      <!-- Crea las esquinas redondeadas abajo -->
     <%--  <img src="${pageContext.request.contextPath}/images/template/corner_sub_bl.gif" alt="bottom corner" class="vBottom"/> --%>

    </div>
</div>

<!-- Pie de pagina -->
<%@include file="/WEB-INF/include/footer.jsp" %>

</div>

</body>
</html>

<%! String menuInicio = ""; %>
<%! String menuProductos = "class=\"active\""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = ""; %>
<%! String menuAbout = ""; %>