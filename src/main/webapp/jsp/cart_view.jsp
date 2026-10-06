<!--カートの内容-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カートの内容</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
	.table-custom tbody tr:nth-child(odd) td {
		background-color: rgb(221, 221, 221) !important;
	}
		.table-custom tbody tr:nth-child(even) td {
		background-color: rgb(243, 243, 243) !important;
	}
</style>
</head>
<body>
	<form method="POST" action="BookServlet">
		<table class="table table-custom">
			<c:choose>
				<c:when test="${totalQuantity==0}">
		現在、カートの中には商品はありません。<br>
					<a href="BookServlet?action=top" class="btn btn-secondary">TOPへ</a>
					<a href="BookServlet?action=book_list" class="btn btn-secondary">書籍一覧へ</a>
				</c:when>
				<c:otherwise>
						<table class="table align-middle table-custom">
							<thead>
								<tr>
									<th>書籍名</th>
									<th>価格</th>
									<th>出版社</th>
									<th>刊行年月日</th>
									<c:if test="${updateCart == true}">
										<th class="text-center">前の購入数</th>
									</c:if>
									<th>購入数</th>
								</tr>
							</thead>
							<tbody >
							<c:forEach var="book" items="${bookCartList}">
									<c:if test="${book.cartCnt > 0 || (updateCart == true && book.beforeCartCnt > 0)}">
										<tr>
											<td>${book.title}</td>
											<td>${book.price}</td>
											<td>${book.publish}</td>
											<td>${book.published}</td>
											<c:if test="${updateCart == true}">
											
												<td style="text-decoration: line-through;" class="text-muted text-center">
													<c:if test="${book.beforeCartCnt > book.cartCnt && book.beforeCartCnt > 0}">
                										(${book.beforeCartCnt})
              										</c:if>
												</td>
											</c:if>
											<td><input type="number" name="${book.isbn}"
												value="${book.cartCnt}" min="0" max="${book.cartCnt}"
												class="count-input <c:if test='${updateCart == true && book.beforeCartCnt != book.cartCnt}'>text-danger fw-bold </c:if>"
												required></td>
										</tr>
									</c:if>
								</c:forEach>
							</tbody>
						</table>
					<button type="submit" name="action" value="cartTobookCartList">書籍一覧へ</button>
					<button type="submit" name="action" value="updateCart">カート更新</button>
					<button type="submit" name="action" value="toOrder">購入画面へ</button>
				</c:otherwise>
			</c:choose>
		</table>
	</form>
	<script>
	const inputs = document.querySelectorAll('.count-input');
	const updateCartButton = document.querySelector('button[value="updateCart"]');
	const toOrderButton = document.querySelector('button[value="toOrder"]');
	// ボタンの有効・無効関数
	function updateButtonStatus() {
		let isChanged = false;
		inputs.forEach(input => {
			const initialValue = parseInt(input.defaultValue) || 0;
			const currentValue = parseInt(input.value) || 0;
			if (initialValue !== currentValue) {
				isChanged = true;
			}
		});
		updateCartButton.disabled = !isChanged;
		toOrderButton.disabled = isChanged;
	}
	inputs.forEach(input => {
		input.addEventListener('input', function() {
			const initialValue = parseInt(this.defaultValue) || 0;
			const currentValue = parseInt(this.value) || 0;
			if (currentValue < initialValue) {
				this.classList.add('text-danger', 'fw-bold');
			} else {
				this.classList.remove('text-danger', 'fw-bold');
			}
			updateButtonStatus();
		});
	});
	updateButtonStatus();
</script>
</body>
</html>