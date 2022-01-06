<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ tag import="org.springframework.util.StringUtils"%>
<%@tag pageEncoding="UTF-8"%>

<%
String categoryId = request.getParameter("categoryId");
%>
<%
String page = request.getParameter("page");
%>

<slide style="width: 250px; height: 137vh; background-color: #0d6efd">
	<a href="admin/user.htm" style="background-color: blue; color: white; text-decoration: none" class="p-3 d-block border-bottom">
		Quản lí người dùng
	</a>
	<a href="admin/category.htm" style="background-color: blue; color: white; text-decoration: none" class="p-3 d-block border-bottom">
		Quản lí nhóm sản phẩm
	</a>
	<a href="admin/product.htm" style="background-color: blue; color: white; text-decoration: none" class="p-3 d-block border-bottom">
		Quản lí sản phẩm
	</a>
	<a href="admin/order.htm" style="background-color: blue; color: white; text-decoration: none" class="p-3 d-block border-bottom">
		Quản lí đơn hàng
	</a>
</slide>