<%@page import="dao.ProductoDao"%>
<%@page import="java.util.Iterator"%>
<%@page import="modelo.Producto"%>
<%@page import="modelo.Carrito"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Carrito</title>

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
			<span class="title_icon"><img src="${pageContext.request.contextPath}/images/template/bullet3.gif"
			alt="" title=""></span> Carrito de la compra
		</div>
        
        <% Carrito carro = (Carrito) session.getAttribute("carrito");
        if (carro == null){%>
            No se han a&ntilde;adido productos a la cesta de la compra
        <% } else if (carro.getArticulos().size() == 0){ %>
            No se han a&ntilde;adido productos a la cesta de la compra
        <% } else { %>
        <div class="feat_prod_box_details">
        	<table class="cart_table">
        	<tbody>
             <tr class="cart_title"><td>Nombre</td><td>Precio Unidad</td><td>Precio Total</td><td>Unidades</td><td></td></tr>
           <!--  <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr> -->
            <% Iterator <String> iteradorProductos = carro.getArticulos().keySet().iterator();
            //PersistenceInterface persistencia = (PersistenceInterface) application.getAttribute("persistence");
            while (iteradorProductos.hasNext()){
                String cod = iteradorProductos.next();
                Producto prod = ProductoDao.getInstance().getProduct(cod); %>
                <tr class="contentTable">
                    <td><%= prod.getNombre() %></td>
                    <td><%= Tools.roundDouble(prod.getPrecio()) %> $</td>
                    <td><%= Tools.roundDouble(carro.getArticulos().get(cod) * prod.getPrecio()) %> $</td>
                    <td>
                        <form name="cantidad" action="${pageContext.request.contextPath}/EditAmountServlet" method="post">
                            <input type="text" name="cant" maxlength="6" size="3" class=":digits :required" value="<%= carro.getArticulos().get(cod) %>" />
                            <input type="hidden" name="prod" value="<%= cod %>" />
                            <input type="submit" name="setCant" value="Actualizar" />
                        </form>
                    </td>
                            <td><a href="${pageContext.request.contextPath}/EditAmountServlet?prod=<%= cod %>&cant=0" ><img src="${pageContext.request.contextPath}/images/icons/removeprodcart.png" alt="Eliminar de la cesta" title="Eliminar del carrito" /></a></td>
                </tr>
            <% } %>
            	<tr>
               			<td colspan="4" class="cart_total"><span class="red">PRECIO TOTAL:</span></td>
                		<td> <%= Tools.roundDouble(carro.getPrecio()) %> $</td>                
            	</tr>
            </tbody>
        </table>
        </div>
        <%-- <p>
            Precio total:<%= Tools.roundDouble(carro.getPrecio()) %> $
        </p> --%>
        <p>
            <span class="subHeader"></span>
            <table border="0" align="left" width="60%" ><tr class="contentTable" >
                    <td><a href="${pageContext.request.contextPath}/DeleteCartServlet" ><img src="${pageContext.request.contextPath}/images/icons/delcart.png" alt="Vaciar carrito" title="Vaciar carrito" style="height: 100px; weidth:100px;" /></a></td>
                    <td><a href="${pageContext.request.contextPath}/UpdateCartServlet" ><img src="${pageContext.request.contextPath}/images/icons/validatecart.png" alt="Validar carrito" title="Validar carrito" style="height: 100px; weidth:100px;" /></a></td>
                    <td><a href="${pageContext.request.contextPath}/shop/products.jsp"><img src="${pageContext.request.contextPath}/images/icons/continueshopping.png" alt="Continuar comprando" title="Continuar comprando" style="height: 100px; weidth:100px;" /></a></td>
            </tr></table>
        </p>
        <% } %>
    </p>

      <!-- Crea las esquinas redondeadas abajo -->
      <%-- <img src="${pageContext.request.contextPath}/images/template/corner_sub_bl.gif" alt="bottom corner" class="vBottom"/> --%>

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
