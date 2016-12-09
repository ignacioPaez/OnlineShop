<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Preferencias de usuario</title>

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
  
  <!-- Menu Izquiero : side bar links/news/search/etc. -->
  <%@include file="/WEB-INF/include/menuAdministracion.jsp" %>
    
    <!-- Contenido de la columna derecha -->
    <div id="contentRight">
        <%@include file="/WEB-INF/include/resultados.jsp" %>

    
    <div class="title">
					<span class="title_icon"><img
						src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Cambiar contraseña
	</div>
	 <p>
        Puede usar este formulario para cambiar su contrase&ntilde;a
    </p>
    <div class="feat_prod_box_details">
    	<div class="contact_form">
      	<form name="changePass" action="${pageContext.request.contextPath}/ChangePassServlet" method="post">
      		<div class="form_row">
      		<label class="contact"><strong>Contrase&ntilde;a anterior</strong></label>
      		
        	<input name="prevPass" type="password" size="25" maxlength="20" class=":password :required :only_on_blur contact_input"/>
       	 	</div>
       	 	<div class="form_row">
       	 	
        	<label class="contact"><strong>Nueva contrase&ntilde;a</strong></label>
        	<input id="pass" name="newPass" type="password" size="25" maxlength="20" class=":password :required :only_on_blur contact_input"/>
        	</div>
        	<div class="form_row">
        	<label class="contact"><strong>Repita la nueva contrase&ntilde;a</strong></label>       	
        	<input name="repeatPass" type="password" size="25" maxlength="20" class=":same_as;pass :required :only_on_blur contact_input"/>
        	</div>
        	<div class="form_row">
							<input name="changePass" type="submit" value="cambiar" class="register" />
						</div>
        </form>
        </div>
     </div>
     
     <div class="title">
					<span class="title_icon"><img
						src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Cambiar los datos de registro
	</div> 
      <p>
        Puede usar este formulario para cambiar sus datos personales
      </p>
      <div class="feat_prod_box_details">
      	<div class="contact_form">
      	<form name="changeData" action="${pageContext.request.contextPath}/EditUserServlet" method="post">
      		<div class="form_row">
      		<label class="contact"><strong>Nombre</strong></label>
        	<input name="name" type="text" size="50" maxlength="100" class=":alpha :required :only_on_blur contact_input" value="<%= actualUser.getNombre() %>"/>
        	</div>
        	<div class="form_row">
      		<label class="contact"><strong> Direcci&oacute;n</strong></label>
        	<input name="dir" type="text" size="70" maxlength="200" class=":required :only_on_blur contact_input" value="<%= actualUser.getDir() %>" />
        	</div>
        	<div class="form_row">
							<input name="changeData" type="submit" value="Modificar" class="register" />
						</div> 
      	</form>
      	</div>
      </div>
      
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