<%@page import="control.Tools"%>
<%@page import="dao.ProductoDao"%>
<%@page import="modelo.Producto"%>
<%@page import="java.util.Map"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Tienda Online</title>

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

    <div class="title">
					<span class="title_icon"><img
						src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Productos disponibles
	</div>
		<div class="feat_prod_box_details">
        <form name="busquedaProductos" method="post" action="${pageContext.request.contextPath}/SearchProductServlet">
            <input type="hidden" name="redirect" value="/shop/products.jsp" />
            <input name="term" type="text" class=":only_on_blur"/>
            <select name="campo">
                <option value="name">Nombre</option>
                <option value="desc">Autor</option>
                <option value="detail">Detalles</option>
            </select>
            <input name="search" value="Buscar" type="submit" class="register2" />
        </form><br />
        </div>

    <% Map <String, Producto> productos = null;
    if (request.getAttribute("resultadosBusqueda") == null){
        productos = ProductoDao.getInstance().getProducts();
    }else{
        productos = (Map <String, Producto>)request.getAttribute("resultadosBusqueda");
    }
    if (productos != null && productos.size() != 0){%>
    
    <div class="feat_prod_box_details">
    <table class="cart_table">
    	<tbody>
        <tr class="cart_title"><td></td><td>Nombre</td><td>Precio</td><td>Autor</td><td>Disponibilidad</td><td></td></tr>
        <!-- <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr> -->
        <% for (Producto prod : productos.values()){ %>
        <tr class="contentTable">
            <td><%
					if (Tools.existeElFichero(application.getRealPath("/images/"
							+ prod.getCodigo())) == true) {
								%>
				<img src="/OnlineShop/images/<%=prod.getCodigo()%>"
					alt="imagen producto" style="height: 80px; weidth:80px;"title="" border="0" /> <%
 							}
 							%></td>
            <td><a href="${pageContext.request.contextPath}/shop/viewprod.jsp?prod=<%= prod.getCodigo() %>"><%= prod.getNombre() %></a></td>
            <td><%= Tools.roundDouble(prod.getPrecio()) %> $</td>
            <td><%= prod.getDesc() %></td>
            <td><%= prod.getDisponibilidad() %></td>           
            <% if (session.getAttribute("auth") != null){ %>
            <td>
            <% if (prod.getStock() > 0){ %>
            	<a href="${pageContext.request.contextPath}/AddCarritoServlet?prod=<%= prod.getCodigo() %>&cant=1"><img src="${pageContext.request.contextPath}/images/icons/addCarro.png" alt="A&ntilde;adir al carrito" title="A&ntilde;adir producto a la cesta" /></a>
            <% }else{ %>
            	<img src="${pageContext.request.contextPath}/images/icons/emptybox.png" alt="Producto agotado" title="Producto agotado" />
            <% }%>
            </td>
            <% } %>
        </tr>
        <% } %>
        </tbody>
    </table>
    </div>
        <% } else{ %>
        <p>No se han encontrado productos</p>
        <% } %>

     <%--  <!-- Crea las esquinas redondeadas abajo -->
      <img src="${pageContext.request.contextPath}/images/template/corner_sub_bl.gif" alt="bottom corner" class="vBottom"/> --%>

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