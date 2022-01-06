<%@tag pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<base href="${pageContext.servletContext.contextPath}/">
<header class="shadow-sm sticky-top bg-white">
	<style>
.navbar-brand:hover {
	opacity: 0.7;
	color: white;
}

.navbar-brand {
	text-decoration: none;
	color: white;
}

.link:hover {
	opacity: 0.7;
	color: white;
	text-decoration: none;
}

.link{
	color: white;
	text-decoration: none;
	font-size: 1.1rem;
}

.bg-white {
	background-color: #e77b6e !important;
}
</style>
	<div class="container-xl">
		<nav class="navbar navbar-expand mb-4">
			<div class="container-fluid">
				<a class="navbar-brand" style=""  href="home.htm">Home</a>

				<div class="collapse navbar-collapse justify-content-end"
					id="navbarSupportedContent">
					<a class="link" href="user/profile.htm">Thông tin</a>
					<a class="link" href="user/changePass.htm">Đổi mật khẩu</a>
					<a class="link" href="cart.htm">Giỏ hàng</a>
					<a class="link" href="user/order.htm">Đơn hàng</a>
					<a class="link" href="logout.htm">Đăng xuất</a>
				</div>
			</div>
		</nav>
	</div>
</header>