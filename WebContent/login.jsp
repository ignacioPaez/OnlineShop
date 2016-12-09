<%@page import="modelo.Usuario"%>
<%@page import="dao.UsuarioDao"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%
	boolean iniciado = false;
	//PersistenceInterface persistencia = (PersistenceInterface) application.getAttribute("persistence");
	if (session.getAttribute("auth") != null
			&& (Boolean) session.getAttribute("auth") == true
			&& session.getAttribute("usuario") != null) {
		Usuario user = UsuarioDao.getInstance().getUser(
				(String) session.getAttribute("usuario"));
		if (user != null) {
			iniciado = true;
		} else {
			response.sendRedirect("./logout");
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Login</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.6.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/vanadium.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/screen_yellow.css"
	media="screen, tv, projection" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/validacion.css"
	media="screen, tv, projection" />
</head>

<body>
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
				<%@include file="/WEB-INF/include/resultados.jsp"%>
				<%
					if (iniciado == true) {
				%>
				<p>
					<div class="title">
					<span class="title_icon"><img
						src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Sesi&oacute;n ya iniciada
				</div> Ya
					tiene sesi&oacute;n iniciada con un usuario. Debe <a href="${pageContext.request.contextPath}/LogoutServlet">cerrar
						la sesi&oacute;n</a> con este usuario si desea iniciar sesi&oacute;n
					con otro usuario.
				</p>
				
			
				<%
					} else if (session.getAttribute("intentosLogin") != null
							&& (Integer) session.getAttribute("intentosLogin") >= 5) {
				%>
				<p>
					<span class="header">Inicio de Sesi&oacute;n bloqueado</span> <br />
				</p>
				<%
					} else {
				%>


				<div class="title">
					<span class="title_icon"><img
						src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Iniciar sesi&oacute;n
				</div>
				
				<div class="feat_prod_box_details">

				<div class="contact_form">
					<div class="form_subtitle">login into your account</div>

					<form action="LoginServlet" name="login" method="post">
						<div class="form_row">
							<label class="contact"><strong>Email:</strong></label> <input
								name="email" type="text" maxlength="60" class=":email :required :only_on_blur contact_input" />
						</div>
						<div class="form_row">
							<label class="contact"><strong>Password:</strong></label> <input
								name="pass" type="password" size="25" maxlength="20"
								class=":password :required :only_on_blur contact_input" />
						</div>
						<div class="form_row">
							<input name="login" type="submit" value="Login" class="register" />
						</div>
					</form>
				</div>
				
				</div>

				<a name="posregistro"></a>
				<div class="title">
					<span class="title_icon"><img
						src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Registro
				</div>

				<div class="feat_prod_box_details">
				<div class="contact_form">
					<div class="form_subtitle">create new account</div>
					<form action="RegisterServlet" method="post" name="register">
						<div class="form_row">
							<label class="contact"><strong>Nombre</strong></label> <input
								name="name" type="text" size="50" maxlength="100"
								class=":alpha :required :only_on_blur contact_input" />
						</div>
						<div class="form_row">
							<label class="contact"><strong>Direccion</strong></label> <input
								name="dir" type="text" size="70" maxlength="200"
								class=":required :only_on_blur contact_input" />
						</div>
						<div class="form_row">
							<label class="contact"><strong>Email</strong></label> <input
								id="email" name="email" type="text" size="30" maxlength="60"
								class=":email :required :ajax;/checkmail :only_on_blur contact_input" />
						</div>
						<div class="form_row">
							<label class="contact"><strong>Contrase&ntilde;a</strong></label>
							<input id="pass" name="pass" type="password" size="25"
								maxlength="20" class=":password :required :only_on_blur contact_input" /><br />
							<br />
						</div>
						<div class="form_row">
							<label class="contact"><strong>Reescriba la
									contrase&ntilde;a</strong></label> <input name="repeatPass" type="password"
								size="25" maxlength="20" class=":same_as;pass :required :only_on_blur contact_input" /><br />
							<br />
						</div>
						<div class="form_row">
							<input name="register" type="submit" value="register"
								class="register" />
							<!-- <input name="register" type="submit" value="register" id="submit" class="register"/> -->
						</div>
					</form>

				</div>
				</div>

				<%
					}
				%>




				<!-- Crea las esquinas redondeadas abajo -->
				<!-- <img src="./images/template/corner_sub_bl.gif" alt="bottom corner"
					class="vBottom" /> -->

			</div>
		</div>

		<!-- Pie de pagina -->
		<%@include file="/WEB-INF/include/footer.jsp"%>

	</div>

</body>
</html>

<%!String menuInicio = "";%>
<%!String menuProductos = "";%>
<%!String menuLogin = "class=\"active\"";%>
<%!String menuPreferencias = "";%>
<%!String menuAbout = "";%>