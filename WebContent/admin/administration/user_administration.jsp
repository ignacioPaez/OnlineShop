<%@page import="java.util.Map"%>
<%@page import="dao.UsuarioDao"%>
<%@page import="dao.AdminDao"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Gesti&oacute;n de usuarios</title>

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
        <%@include file="/WEB-INF/include/resultados.jsp" %>

            <% Map <String, Usuario> usuarios = null;
            if (request.getAttribute("resultadosBusqueda") == null){
                usuarios = UsuarioDao.getInstance().getUsers();
            }else{
                usuarios = (Map <String, Usuario>)request.getAttribute("resultadosBusqueda");
            }

            if (usuarios != null && usuarios.size() != 0){ %>
            <div class="title">
					<span class="title_icon"><img
						src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Administraci&oacute;n de usuarios
			</div>
				<div class="feat_prod_box_details">
                <table class="cart_table">
                	<tbody>
                    <tr class="cart_title"><td>&nbsp;</td><td>Nombre</td><td>Email</td><td>Permisos</td><td>&nbsp;</td></tr>
                    <!-- <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr> -->
                    <% int numberAdmin = AdminDao.getInstance().anyAdmin();
                    for (Usuario user : usuarios.values()){ %>
                    <tr class="contentTable">
                        <td><a href="${pageContext.request.contextPath}/admin/administration/userdetails.jsp?email=<%= user.getMail() %>" ><img src="${pageContext.request.contextPath}/images/icons/viewUser.png" alt="Ver usuario" title="Ver usuario" /></a></td>
                        <td><%= user.getNombre() %></td>
                        <td><%= user.getMail() %></td>
                        <td><%= user.getPrintablePermissions() %></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/administration/edituser.jsp?user=<%= user.getMail() %>"><img title="Editar usuario" alt="Editar usuario" src="${pageContext.request.contextPath}/images/icons/editProd.png"/></a>&nbsp;&nbsp;
                            <% if (numberAdmin == 1 && user.getPermisos() == 'a'){ %>
                            
                            <% }else{ %>
                            <a href="${pageContext.request.contextPath}/DeleteUserServlet?user=<%= user.getMail() %>"><img title="Borrar usuario" alt="Borrar usuario" src="${pageContext.request.contextPath}/images/icons/deleteProd.png"/></a>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
                </div>
                <% } else { %>
                <p>No se han encontrado usuarios</p>
                <% } %>
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
<%! String menuProductos = ""; %>
<%! String menuLogin = ""; %>
<%! String menuPreferencias = "class=\"active\""; %>
<%! String menuAbout = ""; %>