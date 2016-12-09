<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TiendaOnline</title>
<link rel="stylesheet" type="text/css" href="./css/screen_yellow.css" media="screen, tv, projection" />
    </head>

    <body>
        <!-- Contenedor principal-->
        <div id="siteBox">

            <!--Cabecera-->
            <%@include file="/WEB-INF/include/header.jsp" %>

            <!-- Contenido de la pagina -->
            <div id="content">

                <!-- Menu Izquiero : side bar links/news/search/etc. -->
                <%@include file="/WEB-INF/include/menu.jsp" %>

                <!-- Contenido de la columna derecha -->
                <div id="contentRight">

                    <div class="title">
							<span class="title_icon"><img
							src="${pageContext.request.contextPath}/images/template/bullet3.gif"
						alt="" title=""></span> Book Store
					</div> 
                    <p>
                        Bienvenidos a Bookstore, la aplicaci&oacute;n que le permite comprar sus libros preferidos. En caso de que tenga alg&uacute;n problema con el 
                        servicio o quiere darse de baja no dude en ponerse en contacto a consulta@bookstore.com y haremos todo lo que este en nuestra mano para solucionar el problema lo antes 
                        posible
                    </p>
                    
                    <p>
                        Puede <a href="./login.jsp">registrarse</a> en la tienda o puede consultar los libros disponibles.
                        Usando la opci&oacute;n del registro previo solo tendr&aacute; que introducir una vez los datos personales. El resto
                        de las veces lo &uacute;nico que tendr&aacute; que hacer es elegir la forma de pago con que dese abonar el precio de la compra.
                    </p>

                    <p>
                        Puede llenar su carrito de la compra y nosotros lo guardaremos por usted hasta que decida comprarlo, su carrito de la compra 
                        ser&aacute; conservado aunque cierre la sesi&oacute;n. Cuando usted cierre la sesi&oacute;n con un carrito lleno sin haberlo confirmado guardaremos 
                        su carrito y podr&aacute; verlo lleno la pr&oacute;xima vez que inicie sesi&oacute;n.
                    </p>

                    <!-- Crea las esquinas redondeadas abajo -->
                    <!-- <img src="./images/template/corner_sub_bl.gif" alt="bottom corner" class="vBottom"/> -->

                </div>
            </div>

            <!-- Pie de pagina -->
            <%@include file="/WEB-INF/include/footer.jsp" %>

        </div>

    </body>
</html>

<%! String menuInicio = "class=\"active\"";%> 
<%! String menuProductos = "";%>
<%! String menuLogin = "";%>
<%! String menuPreferencias = "";%>
<%! String menuAbout = "";%>