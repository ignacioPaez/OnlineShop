<%@page import="control.Tools"%>
<%@page import="modelo.Carrito"%>
<%-- Contenido menu izquierdo --%>
<div id="contentLeft">
	<div class="title">
		<span class="title_icon"> 
			<img src="${pageContext.request.contextPath}/images/template/bullet1.gif" alt title>
		</span> 
		Men&uacute;
	</div>

	<ul class="list">
		<li><a href="${pageContext.request.contextPath}/index.jsp"
			class="menuItem">Inicio</a></li>
			
		<li><a
			href="${pageContext.request.contextPath}/shop/products.jsp"
			class="menuItem">Tienda</a></li>
		<%
			if (session.getAttribute("auth") != null
					&& (Boolean) session.getAttribute("auth") == true) {
		%>
		<li><a href="${pageContext.request.contextPath}/admin/index.jsp"
			class="menuItem">Preferencias</a></li> 
			
		<li>	<a
			href="${pageContext.request.contextPath}/LogoutServlet"
			class="menuItem">Cerrar sesi&oacute;n</a></li>
		<%
			} else {
		%>
		<li><a href="${pageContext.request.contextPath}/login.jsp"
			class="menuItem">Iniciar sesi&oacute;n</a></li>
		<%
			}
		%>
	</ul>


	<%
		if (session.getAttribute("carrito") != null) {
			Carrito carro = (Carrito) session.getAttribute("carrito");
	%>
	<div class="title">
		<span class="title_icon"> 
			<img src="${pageContext.request.contextPath}/images/template/bullet1.gif" alt title>
		</span> 
		Carrito
	</div>
	<%
		if (carro.getArticulos().size() == 0) {
	%>
	<center>
		<a href="${pageContext.request.contextPath}/shop/cart.jsp"><img
			src="${pageContext.request.contextPath}/images/icons/cartEmpty.png"
			alt="Carrito" title="Ir al carrito (carrito vac&iacute;o)" /></a>
	</center>
	<%
		} else {
	%>
	<p>
		<a href="${pageContext.request.contextPath}/shop/cart.jsp"><img
			src="${pageContext.request.contextPath}/images/icons/cartFull.png"
			alt="Carrito" title="Ir al carrito" style="height: 100px; weight: 100px;" /></a>
	</p>
	<%
		}
	%>
	<p>
		<b>N&uacute;mero de productos: </b><%=carro.getLenght()%><br /> <b>Precio:
		</b>
		<%=Tools.roundDouble(carro.getPrecio())%>
		$<br />
	</p>
	<p>
		<a href="${pageContext.request.contextPath}/UpdateCartServlet"><img
			src="${pageContext.request.contextPath}/images/icons/validatecartlittle.png"
			alt="Validar carro" title="Validar carrito" /></a>
	</p>

	<%
		}
	%>


<%-- 	<!-- Esquina redondeada en la parte de abajo del menu -->
	<div class="bottomCorner">
		<img
			src="${pageContext.request.contextPath}/images/template/corner_sub_br.gif"
			alt="bottom corner" class="vBottom" />
	</div>
 --%>
</div>
<%-- Fin menu izquierdo --%>