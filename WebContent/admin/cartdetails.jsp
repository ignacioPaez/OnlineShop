<%@page import="java.util.ArrayList"%>
<%@page import="modelo.Producto"%>
<%@page import="modelo.Usuario"%>
<%@page import="control.Tools"%>
<%@page import="dao.UsuarioDao"%>
<%@page import="dao.CarritoDao"%>
<%@page import="dao.ProductoDao"%>
<%@page import="modelo.Carrito"%>
<%@page import="java.util.Map"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<% if (validate(request) == false) {
        response.sendError(404);
        return;
    }%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Detalles de compra</title>

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

                    <% //PersistenceInterface persistencia = (PersistenceInterface) application.getAttribute("persistence");
                        Usuario user = UsuarioDao.getInstance().getUser((String) session.getAttribute("usuario"));
                        ArrayList<Carrito> historial = CarritoDao.getInstance().requestSalesRecord("CodigoCarrito", request.getParameter("cod"));
                        if (historial != null) {
        if (historial.get(0).getUser().equals(user.getMail()) == false && user.getPermisos() != 'a') {%>
                    <span class="header" >No permitido </span>
                    <p>
                        No tienes permiso para ver los detalles de este carrito.
                    </p>
                    <% } else {
                        ArrayList<Producto> listado = CarritoDao.getInstance().getDetailsCartRecord(request.getParameter("cod"));%>
                    <p>
                        <span class="header" >Detalles de la venta: <span class="headerComplement"><%= request.getParameter("cod")%></span></span>
                    </p>
                    <div class="feat_prod_box_details">
                        <table class="cart_table">
                        	<tbody>
                             <tr class="cart_title"><td>Producto</td><td>Unidades</td><td>Precio Unidad</td><td>Precio</td></tr>
                            <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                                <%for (int i = 0; i < listado.size(); i++) {%>
                                <tr class="contentTable">
                                    <% if (ProductoDao.getInstance().getProduct(listado.get(i).getCodigo()) != null) {%>
                                    <td><a href="${pageContext.request.contextPath}/shop/viewprod.jsp?prod=<%= listado.get(i).getCodigo()%>" title="Ver producto actual" ><%= listado.get(i).getNombre()%></a></td>
                                    <% } else {%>
                                    <td><%= listado.get(i).getNombre()%></td>
                                    <% }%>
                                    <td><%= listado.get(i).getStock()%></td>
                                    <td><%= Tools.roundDouble(listado.get(i).getPrecio())%> $</td>
                                    <td><%= Tools.roundDouble(listado.get(i).getPrecio() * listado.get(i).getStock())%> $</td>
                                </tr>
                                <% }%>
                             	<tr>
                					<td colspan="3" class="cart_total"><span class="red">PRECIO TOTAL:</span></td>
                					<td> <%= Tools.roundDouble(historial.get(0).getPrecio())%> $</td>                
                				</tr>
                            </tbody>    
                        </table>
                     </div>
                       <%--  <p>
                            <b>Precio total: </b><%= Tools.roundDouble(historial.get(0).getPrecio())%> &euro;
                        </p> --%>
                        <% }%>

                    <% } else {%>
                    <span class="header" >Error en la solicitud </span>
                    <p>No se han encontrado registros de ventas para esta solicitud</p>
                    <% }%>
                    <!-- Crea las esquinas redondeadas abajo -->
                    <%-- <img src="${pageContext.request.contextPath}/images/template/corner_sub_bl.gif" alt="bottom corner" class="vBottom"/> --%>

                </div>
            </div>

            <!-- Pie de pagina -->
            <%@include file="/WEB-INF/include/footer.jsp" %>

        </div>

    </body>
</html>

<%! String menuInicio = "";%>
<%! String menuProductos = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "class=\"active\"";%>
<%! String menuAbout = "";%>

<%! private boolean validate(HttpServletRequest request) {
        if (request.getParameter("cod") != null && Tools.validateUUID(request.getParameter("cod")) == true) {
            return true;
        }
        return false;
    }
%>
