<%@page import="modelo.Producto"%>
<%@page import="dao.ProductoDao"%>
<%@page import="control.Tools"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%
	if (validateEntry(request) == false) {
		response.sendError(404);
		return;
	}
%>
<%
	//PersistenceInterface persistencia = (PersistenceInterface) application.getAttribute("persistence");
	Producto prod = ProductoDao.getInstance().getProduct(
			request.getParameter("prod"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%
	if (prod == null) {
%>
<title>Producto no encontrado</title>
<%
	} else {
%>
<title><%=prod.getNombre()%></title>
<%
	}
%>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/lightbox.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-1.6.1.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/vanadium.js"></script>
	
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/validacion.css"
	media="screen, tv, projection" />

<script type="text/javascript" src="/scripts/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript" src="/scripts/scripts.js"></script>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/screen_yellow.css"
	media="screen, tv, projection" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/lightbox.css"
	media="screen" />
<link href="//cdn.rawgit.com/noelboss/featherlight/1.6.1/release/featherlight.min.css" type="text/css" rel="stylesheet" />
</head>

<body onload="loadEditor();">
	<!-- Contenedor principal-->
	<div id="siteBox">

		<!--Cabecera-->
		<%@include file="/WEB-INF/include/header.jsp"%>

		<!-- Contenido de la pagina -->
		<div id="content">

			<!-- Menu Izquiero -->
			<%@include file="/WEB-INF/include/menu.jsp"%>

			<!-- Contenido de la columna derecha -->
			<div id="contentRight">

				<%-- Resultados de la operaci&oacute;n --%>
				<%@include file="/WEB-INF/include/resultados.jsp"%>

				<%
					if (prod == null) {
				%>
				<p>
					<span class="header">Producto no encontrado</span>
				</p>
				<p>
					El producto seleccionado no se ha encontrado. Posiblemente esta
					petici&oacute;n fue alterada <br /> Puede volver al <a
						href="./shop/products.jsp">listado de productos</a>
				</p>
				<%
					} else {
				%>
				<p>
					<%--  <span class="header" >Hoja de producto: <span class="headerComplement" ><%= prod.getNombre()%></span></span> --%>
					<div class="title">
						<span class="title_icon"><img
							src="${pageContext.request.contextPath}/images/template/bullet3.gif"
							alt="" title=""></span>
						<%=prod.getNombre()%>
					</div>

					<%-- <br />
                        <% if (Tools.existeElFichero(application.getRealPath("/images/" + prod.getCodigo())) == true){ %>
                            <img src ="/OnlineShop/images/<%= prod.getCodigo() %>" alt="imagen producto" height="400" width="450" align="right"/>
                        <% } %>
                            
                        <ul>
                            <li> <b>Precio: </b><%= Tools.roundDouble(prod.getPrecio())%> $ </li>
                            <li> <b>Disponibilidad: </b> <%= prod.getDisponibilidad()%> </li>
                            <li> <b>Autor: </b> <%= prod.getDesc()%> </li>

                        </ul>
                    </p>
                            <p>
                                <a href="${pageContext.request.contextPath}/AddCarritoServlet?prod=<%= prod.getCodigo() %>&cant=1"><img src="${pageContext.request.contextPath}/images/icons/addCarro.png" alt="a&ntilde;adir a la cesta" title="A&ntilde;adir producto a la cesta"/></a>
                            </p>
                    <p>
                        <span class="subHeader">Detalles del producto</span><br />
                        <%= prod.getDetalles()%>
                    </p> --%>

					<div class="feat_prod_box_details">

						<div class="prod_img">
							<a href=" ">
								<%
									if (Tools.existeElFichero(application.getRealPath("/images/"
												+ prod.getCodigo())) == true) {
								%>
								<img src="/OnlineShop/images/<%=prod.getCodigo()%>"
								alt="imagen producto" style="height: 250px; weidth:150px;"title="" border="0" /> <%
 							}
 							%>
							</a> <br>
							<br>
							<%-- <a href="images/big_pic.jpg" rel="lightbox"><img src="${pageContext.request.contextPath}/images/template/zoom.gif" alt="" title="" border="0"></a> --%>
							<a href="/OnlineShop/images/<%=prod.getCodigo()%>" data-featherlight="image"><img
								src="${pageContext.request.contextPath}/images/template/zoom.gif"
								alt="" title="" border="0"></a>
						</div>

						<div class="prod_det_box">
							<div class="box_top"></div>
							<div class="box_center">
								<div class="prod_title">Detalles</div>
								<p class="details"><%=prod.getDetalles()%></p>
								<div class="price">
									<strong>PRECIO:</strong> <span class="red"><%=Tools.roundDouble(prod.getPrecio())%>
										$ </span>
								</div>
								<div class="price">
									<strong>DISPONIBILIDAD:</strong> <span class="red"><%=prod.getDisponibilidad()%></span>
								</div>
								<div class="price">
									<strong>AUTOR:</strong> <span class="red"><%=prod.getDesc()%>
									</span>
								</div>
								<a
									href="${pageContext.request.contextPath}/AddCarritoServlet?prod=<%= prod.getCodigo() %>&cant=1"
									class="more"><img
									src="${pageContext.request.contextPath}/images/template/order_now.gif"
									alt="" title="" border="0"></a>
								<div class="clear"></div>
							</div>
							<div class="box_bottom"></div>

						</div>
						<div class="clear"></div>

					</div>
					<!-- <div class="clear"></div> -->

					<%@include file="/WEB-INF/include/comentarios.jsp"%>

					<%
						}
					%>
				
			</div>



			<!-- Crea las esquinas redondeadas abajo -->
			<%-- <img src="${pageContext.request.contextPath}/images/template/corner_sub_bl.gif" alt="bottom corner" class="vBottom"/> --%>

		</div>

		<!-- Pie de pagina -->
		<%@include file="/WEB-INF/include/footer.jsp"%>

	</div>
	
	<script src="//code.jquery.com/jquery-latest.js"></script>
	<script src="//cdn.rawgit.com/noelboss/featherlight/1.6.1/release/featherlight.min.js" type="text/javascript" charset="utf-8"></script>
	
</body>
</html>

<%!private boolean validateEntry(HttpServletRequest request) {
		if (request.getParameterMap().size() >= 1
				&& request.getParameter("prod") != null) {
			return Tools.validateUUID(request.getParameter("prod"));
		} else {
			return false;
		}
	}%>

<%!String menuInicio = "";%>
<%!String menuProductos = "class=\"active\"";%>
<%!String menuLogin = "";%>
<%!String menuPreferencias = "";%>
<%!String menuAbout = "";%>
