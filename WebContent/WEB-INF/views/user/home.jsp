<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags"%>
<base href="${pageContext.servletContext.contextPath }/">

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Trang chủ</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
	integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ=="
	crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<style>
.link {
	padding: 12px;
	text-decoration: none;
	transition: .15s;
}

.link:hover {
	text-decoration: underline;
}

.category {
	text-decoration: none;
	padding: 10px;
	background: #198754;
	color: #fff;
	border-radius: 6px;
	margin: 10px;
}

.product-list {
	grid-template-columns: repeat(4, 1fr);
	gap: 20px;
}

.btn-adjust {
	width: 38px;
	height: 38px;
}

.quantity {
	flex: 1;
	text-align: center;
	font-size: 18px;
	font-weight: 600;
}

.disabled {
  pointer-events: none;
  cursor: default;
}
.d-inline-flex{
                  margin-left: 250px;
}
</style>
</head>

<body>
	<tag:header />
	<section>
		<div class="container-xl">
		<form class="d-inline-flex" action="home.htm" method="post" >
						<input name="searchInput" id="searchInput" class="form-control me-2" type="text"
							placeholder="Search" aria-label="Search" required>
						<button name="btnsearch" id="btnseach"
							class="btn btn-outline-success" type="submit" >Search</button>
					</form>
			<c:if test="${not empty message}">
				<div class="alert alert-primary mb-3">${message }</div>
			</c:if>

			<div class="d-flex flex-wrap mb-3	">
				<c:forEach var="category" items="${categories}">
					<a type="button"
						class="btn ${category.id == param.categoryId ? 'btn-primary' : 'btn-success' } me-2 mb-2"
						style="border-radius: 100px; min-width: 60px"
						href="home.htm?categoryId=${category.id}">${category.name}</a>
				</c:forEach>
			</div>

			<div class="d-grid product-list mb-4">
				<c:forEach var="product" items="${pageListHolder.pageList}">
					<div class="card">
						<div class="position-relative" style="padding-top: 75%">
							<div class="position-absolute top-0 bottom-0 start-0 end-0">
								<img src="resources/images/${product.image}" class="card-img-top h-100"
									style="object-fit: cover">
							</div>
						</div>
						<div class="card-body d-flex flex-column justify-content-between">
							<div>
								<div style="font-size: 18px; font-weight: 500">${product.name }</div>

								<p class="card-text mb-0"
									style="color: #b70000; font-weight: 500;">
									<f:formatNumber
										value="${product.price * (100 - product.discount) / 100 }"
										type="currency" currencySymbol="đ" maxFractionDigits="0" />

									<c:if test="${product.discount > 0}">
										<span class="text-muted text-decoration-line-through"><f:formatNumber
												value="${product.price }" type="currency" currencySymbol="đ"
												maxFractionDigits="0" /></span>
										<span class="badge bg-danger">${product.discount}%</span>
									</c:if>
								</p>
								<p style="font-size: 14px" class="mb-0">ĐVT: ${product.unit}</p>
								<p style="font-size: 14px">Số lượng tồn: ${product.soLuongTon}</p>
							</div>

							<a class="btn btn-primary w-100 btn-buy ${product.soLuongTon <= 0 ? 'disabled' : ''}"
								href="cart/add.htm?productId=${product.id}">Chọn mua</a>
						</div>
					</div>
				</c:forEach>
			</div>

			<tag:paging />
		</div>
	</section>
	<script>
		
	</script>
</body>
</html>

