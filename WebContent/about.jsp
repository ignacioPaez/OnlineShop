<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Acerca de</title>

<link rel="stylesheet" type="text/css" href="./css/screen_yellow.css"
	media="screen, tv, projection" />
</head>

<body>
	<!-- Contenedor principal-->
	<div id="siteBox">

		<!--Cabecera-->
		<%@include file="/WEB-INF/include/header.jsp"%>

		<!-- Contenido de la pagina -->
		<div id="content">

			<!-- Menu Izquiero : side bar links/news/search/etc. -->
			<%@include file="/WEB-INF/include/menu.jsp"%>

			<!-- Contenido de la columna derecha -->
			<div id="contentRight">

				<p>
					<span class="header">Acerca de</span> Aplicaci&oacute;n web
					desarrollada por Ignacio Paez
				</p>


				<!-- Crea las esquinas redondeadas abajo -->
				<!--    <img src="./images/template/corner_sub_bl.gif" alt="bottom corner" class="vBottom"/> -->

			</div>
		</div>

		<!-- Pie de pagina -->
		<%@include file="/WEB-INF/include/footer.jsp"%>

	</div>

</body>
</html>

<%!String menuInicio = "";%>
<%!String menuProductos = "";%>
<%!String menuLogin = "";%>
<%!String menuPreferencias = "";%>
<%!String menuAbout = "class=\"active\"";%>