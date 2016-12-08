<div id="header">


	<!--Esquina redondeada de arriba-->
	<img
		src="${pageContext.request.contextPath}/images/template/corner_tl.gif"
		alt="corner" style="float: left;" />

	<!-- Site title and subTitle -->
	<div class="titleheader"> 
		<a href="${pageContext.request.contextPath}/index.jsp">
			<img src="${pageContext.request.contextPath}/images/template/logo.gif" alt title border="0">
		</a>
	</div>

	<%
		Boolean auth = (Boolean) session.getAttribute("auth");
	%>
	<!-- El menu se define en orden inverso a como se muestra (caused by float: right) -->
	<div id="menuheader">
		<ul>
			<li><a href="${pageContext.request.contextPath}/index.jsp"
				<%= menuInicio %>>Inicio</a></li>

			<%
				if (auth != null && auth == true) {
			%>
			<li><a href="${pageContext.request.contextPath}/admin/index.jsp"
				<%= menuPreferencias %>>Preferencias</a></li>
			<%
				}
			%>

			<%
				if (auth == null || auth == false) {
			%>
			<li><a href="${pageContext.request.contextPath}/login.jsp"
				<%= menuLogin %>>Login / Registro</a></li>
			<%
				} else {
			%>
			<li><a href="${pageContext.request.contextPath}/LogoutServlet"
				<%= menuLogin %>>Cerrar sesi&oacute;n</a></li>
			<%
				}
			%>

			<li><a
				href="${pageContext.request.contextPath}/shop/products.jsp"
				<%= menuProductos %>>Productos</a></li>

			<li><a href="${pageContext.request.contextPath}/about.jsp"
				<%= menuAbout %> class="lastMenuItem">Acerca de</a></li>

		</ul>
	</div>

</div>