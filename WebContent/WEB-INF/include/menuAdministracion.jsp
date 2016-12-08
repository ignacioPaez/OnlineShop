<%@page import="dao.UsuarioDao"%>
<%@page import="modelo.Usuario"%>

<div id="contentLeft">
	<div class="title">
		<span class="title_icon"> 
			<img src="${pageContext.request.contextPath}/images/template/bullet1.gif" alt title>
		</span> 
		Panel de usuario
	</div>

  <% Usuario actualUser = UsuarioDao.getInstance().getUser((String)session.getAttribute("usuario")); %>
  <ul class="list">
      <div class="crumb_nav">Opciones</div>
        <li><a href="${pageContext.request.contextPath}/admin/preferences.jsp" title="Preferencias" class="menuItem">Preferencias</a></li>
        <% if (actualUser.getPermisos() != 'a'){ %>
        <li><a href="${pageContext.request.contextPath}/admin/salesrecord.jsp" title="Historial de compras" class="menuItem">Hist&oacute;rico de compras</a></li>
        <% } %>
        <li><a href="${pageContext.request.contextPath}/LogoutServlet" title="Cerrar sesi&uacute;n" class="menuItem">Cerrar sesi&oacute;n</a></li>
  </ul>
  <% if ( actualUser.getPermisos() == 'a' ){ %>
  <ul class="list">
      <div class="crumb_nav">Administraci&oacute;n</div>
      	<li><a href="${pageContext.request.contextPath}/admin/administration/user_administration.jsp" title="Administraci&oacute;n de usuarios" class="menuItem">Administraci&oacute;n de usuarios</a></li>
      	<li><a href="${pageContext.request.contextPath}/admin/administration/products_administration.jsp" title="Administraci&oacute;n de productos" class="menuItem">Administraci&oacute;n de productos</a></li>
      	<li><a href="${pageContext.request.contextPath}/admin/salesrecord.jsp" title="Registro de ventas" class="menuItem">Hist&oacute;rico de ventas</a></li>
      	<li><a href="${pageContext.request.contextPath}/admin/administration/stats.jsp" title="Estad&iacute;sticas de ventas" class="menuItem">Estad&iacute;sticas de ventas</a></li>
  </ul>
  <% } %>

      <!-- Esquina redondeada en la parte de abajo del menu -->
      <%-- <div class="bottomCorner">
        <img src="${pageContext.request.contextPath}/images/template/corner_sub_br.gif" alt="bottom corner" class="vBottom"/>
      </div> --%>

    </div>
  