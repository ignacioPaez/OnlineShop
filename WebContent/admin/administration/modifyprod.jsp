<%@page import="dao.ProductoDao"%>
<%@page import="control.Tools"%>
<%@page import="modelo.Producto"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%if (validar(request, session) == false) {
        response.sendError(404);
        return;
    }%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Modificar producto</title>

        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.6.1.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/vanadium.js"></script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/validacion.css" media="screen, tv, projection" />

        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/tiny_mce/tiny_mce.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/scripts.js"></script>

        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/screen_yellow.css" media="screen, tv, projection" />
    </head>

    <body onload="loadEditor();">
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
                    <%
                        String nombre = "";
                        String precio = "";
                        String stock = "";
                        String descripcion = "";
                        String detalles = "";
                        String codigo = "";
                        boolean encontrado = true;
                        Producto prod = null;

                        //PersistenceInterface persistencia = (PersistenceInterface) application.getAttribute("persistence");
                        prod = ProductoDao.getInstance().getProduct(request.getParameter("prod"));
                        if (prod != null) {
                            encontrado = true;
                        } else {
                            encontrado = false;
                        }

                        if (prod != null) {
                            nombre = "value=\"" + prod.getNombre() + "\"";
                            precio = "value=\"" + prod.getPrecio() + "\"";
                            stock = "value=\"" + prod.getStock() + "\"";
                            descripcion = "value=\"" + prod.getDesc() + "\"";
                            detalles = prod.getDetalles();
                            codigo = prod.getCodigo();
                        }

                    %>

                    <% if (encontrado == true) {%>
                    
                    <div class="title">
					<span class="title_icon"><img
						src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Modificar producto
					</div>

                    <div class="feat_prod_box_details">
					<div class="contact_form">
					<div class="form_subtitle">Libro:<%= prod.getNombre()%></div>
                    <form name="modifyprod" method="post" action="${pageContext.request.contextPath}/EditProductServlet" enctype="multipart/form-data">
                        <input type="hidden" name="codigo" value="<%= codigo%>" />

                        <b>Nombre</b> <br />
                        <input type="text" name="name" maxlength="50" class=":alpha :required :only_on_blur" <%= nombre%> /><br /><br />
                        <b>Precio</b> <br />
                        <input type="text" name="price" maxlength="10" class=":number :required :only_on_blur" <%= precio%> /><br /><br />
                        <b>Unidades en stock</b> <br />
                        <input type="text" name="stock" maxlength="5" class=":digits :required :only_on_blur" <%= stock%> /><br /><br />
                        <b>Autor</b> <br />
                        <input type="text" name="desc" maxlength="100" class=":required :only_on_blur" <%= descripcion%> /><br /><br />
                        Imagen del producto (opcional)<br />
                        <% if (Tools.existeElFichero(request.getServletContext().getRealPath("/images/" + codigo)) == true) {%>
                        <input type="checkbox" name="conserv" value="conservarImagen" checked /> Conservar imagen actual<br />
                        <% }%>
                        <input type="file" name="foto" /><br /><br />
                        <b>Detalles</b> <br />
                        <textarea name="detail" cols="45" rows="15" class=":required :only_on_blur"><%= detalles.replace("<br />", "\n")%></textarea><br /><br />
                        <!-- <input type="submit" name="sendProd" value="Enviar datos" /> -->
                        <div class="form_row">
							<input name="sendProd" type="submit" value="Editar" class="register" />
						</div>
                    </form>
					</div>
					</div>
				
                    <% } else {%>
                    <p>
                        <span class="header">Producto no encontrado</span><br />
                        Ha ocurrido un error editando el producto. El producto no ha sido encontrado<br /><br />
                        <a href="${pageContext.request.contextPath}/admin/administration/products_administration.jsp">Administraci&oacute;n de productos</a>
                    </p>
                    <% }%>
                    <!-- Crea las esquinas redondeadas abajo -->
                   <%--  <img src="${pageContext.request.contextPath}/images/template/corner_sub_bl.gif" alt="bottom corner" class="vBottom" /> --%>

                </div>
            </div>

            <!-- Pie de pagina -->
            <%@include file="/WEB-INF/include/footer.jsp" %>

        </div>

    </body>
</html>

<%--Funcion para validar la entrada en esta jsp con los parametros necesarios--%>
<%! private boolean validar(HttpServletRequest request, HttpSession sesion) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("prod") != null) {
            return Tools.validateUUID(request.getParameter("prod")); 
        } else {
            return false;
        }
    }%>

<%! String menuInicio = "";%>
<%! String menuProductos = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "class=\"active\"";%>
<%! String menuAbout = "";%>