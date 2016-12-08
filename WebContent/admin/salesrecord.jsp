<%@page import="java.util.ArrayList"%>
<%@page import="control.Tools"%>
<%@page import="modelo.Carrito"%>
<%@page import="dao.UsuarioDao"%>
<%@page import="dao.CarritoDao"%>
<%@page import="java.util.Map"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Hist&oacute;rico de ventas</title>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/screen_yellow.css" media="screen, tv, projection" />
</head>

<body>
<!-- Contenedor principal-->
<div id="siteBox">

	<!--Cabecera-->
    <%@include file="/WEB-INF/include/header.jsp" %>
	
  
  <!-- Contenido de la pagina -->
  <div id="content">
  
  <!-- Menu Izquiero : side bar links/news/search/etc. -->
  <%@include file="/WEB-INF/include/menuAdministracion.jsp" %>
    
    <!-- Contenido de la columna derecha -->
    <div id="contentRight">

    <!--<p>
        <span class="header">Registro de ventas</span><br /><br />
        <form action="" method="post" name="searchForm">
            Buscador &nbsp;&nbsp;
            <input type="text" name="term" maxlength="200" />
            &nbsp;&nbsp;
            <select name="searchField">
                <option value="name">Nombre</option>
                <option value="dir">Direcci&oacute;n</option>
                <option value="mail">Email</option>
            </select>

            <input type="submit" name="start" value="Buscar" />
        </form>
    </p>
    <br />-->

            <% //PersistenceInterface persistencia = (PersistenceInterface) application.getAttribute("persistence");
            Usuario user = UsuarioDao.getInstance().getUser( (String)session.getAttribute("usuario"));
            ArrayList <Carrito> historial = new ArrayList<Carrito>();
            if (user.getPermisos() == 'a'){
                historial = CarritoDao.getInstance().requestSalesRecord("1","1");
            }else{
                historial = CarritoDao.getInstance().requestSalesRecord("Email", user.getMail());
            }

            if (historial != null){ %>

            <div class="title">
					<span class="title_icon"><img
						src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Registro de ventas
			</div>
            	<div class="feat_prod_box_details">
                <table class="cart_table">
                	<tbody>
                    <tr class="cart_title"><td>&nbsp;</td><td>Fecha</td><td>Hora</td><td>Email</td><td>Precio</td><td>Forma de pago</td></tr>
                    <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                    <%for (int i = 0; i < historial.size(); i++){ %>
                    <tr class="contentTable">
                        <td><a href="${pageContext.request.contextPath}/admin/cartdetails.jsp?cod=<%= historial.get(i).getCodigo() %>" ><img src ="${pageContext.request.contextPath}/images/icons/viewReport.png" alt="informe" title="Ver detalles"/></a></td>
                        <td><%= Tools.printDate(historial.get(i).getFecha()) %></td>
                        <td><%= historial.get(i).getHora() %></td>
                        <td><%= historial.get(i).getUser() %></td>
                        <td><%= Tools.roundDouble(historial.get(i).getPrecio()) %></td>
                        <td><%= historial.get(i).getFormaPago() %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
                </div>
                <% } else { %><p>
                <span class="header" >No hay registros</span>
                No se han encontrado registros de ventas</p>
                <% } %>
      <!-- Crea las esquinas redondeadas abajo -->
    <%--   <img src="${pageContext.request.contextPath}/images/template/corner_sub_bl.gif" alt="bottom corner" class="vBottom"/> --%>
    
    </div>
</div>

<!-- Pie de pagina -->
<%@include file="/WEB-INF/include/footer.jsp" %>

</div>

</body>
</html>

<%! String menuInicio = ""; %>
<%! String menuProductos = ""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = "class=\"active\""; %>
<%! String menuAbout = ""; %>