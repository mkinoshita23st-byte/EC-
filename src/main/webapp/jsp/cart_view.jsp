<!--カートの内容-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カートの内容</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
		/* 偶数行: 色付き*/
	.table-custom tbody tr:nth-child(even) td {
		background-color: rgb(192, 192, 192) !important;
	}
</style>
</head>
<body>
<form method="POST" action="BookServlet">
<table class="table table-custom">
<c:choose>
	<%--①カートが空の場合 --%>
	<c:when test="${totalQuantity==0}">
		現在、カートの中には商品はありません。<br>
		<a href="BookServlet?action=top" class="btn btn-secondary">TOPへ</a>
		<a href="BookServlet?action=book_list" class="btn btn-secondary">書籍一覧へ</a>
	</c:when>
	<%-- ①カートに商品が入っていた場合 --%>
	<c:otherwise>
		<c:choose>
			<%-- ②カート更新後の場合 --%>
			<c:when test="${updateCart==true}">
				<table class="table" >
				<thead>
				<tr>
					<th>書籍名</th>
					<th>価格</th>
					<th>出版社</th>
					<th>刊行年月日</th>
					<th class="text-center">前の購入数</th>
					<th>購入数</th>
				</tr>
				</thead>
				<tbody>
					<c:forEach var="book" items="${bookList}">
						<c:if test="${book.beforeCartCnt>0}">
							<tr>
								<td>${book.title}</td>
								<td>${book.price}</td>
								<td>${book.publish}</td>
								<td>${book.published}</td>
								<td style="text-decoration: line-through;" class="text-muted text-center">
									(${book.beforeCartCnt})
								</td>
								<td>
								<input type="number" name="${book.isbn}" value="${book.cartCnt}"
									min="0" max="${book.cartCnt}"
									class="form-control count-input
									<c:if test='${book.beforeCartCnt != book.cartCnt}'>text-danger fw-bold</c:if>"
									required>
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
				</table>
			</c:when>
			<%-- ②それ以外の画面遷移 --%>
			<c:otherwise>
				<table class="table" >
				<thead>
				<tr>
					<th>書籍名</th>
					<th>価格</th>
					<th>出版社</th>
					<th>刊行年月日</th>
					<th>購入数</th>
				</tr>
				</thead>
				<tbody>
					<c:forEach var="book" items="${bookList}">
						<c:if test="${ book.cartCnt>0}">
							<tr>
								<td>${book.title}</td>
								<td>${book.price}</td>
								<td>${book.publish}</td>
								<td>${book.published}</td>
								<td>
								<input type="number" name="${book.isbn}" value="${book.cartCnt}"
									min="0" max="${book.cartCnt}" class="count-input" required>
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
				</table>
			</c:otherwise>
		</c:choose>
		<button type="submit" name="action" value="cartToBookList">書籍一覧へ</button>
		<button type="submit" name="action" value="updateCart">カート更新</button>
		<button type="submit" name="action" value="toOrder">購入画面へ</button>
	</c:otherwise>
</c:choose>
</table>
</form>
<script>
	// 画面内のすべての数量入力欄取得
	const inputs = document.querySelectorAll('.count-input');
	// 「カート更新」ボタン取得
	const updateCartButton = document.querySelector('button[value="updateCart"]');
 
	// ボタンの有効・無効関数
	function updateButtonStatus() {
		let isChanged = false;
 
		inputs.forEach(input => {
			// input.defaultValue＝画面が表示された初期の value の値
			const initialValue = parseInt(input.defaultValue) || 0;
			const currentValue = parseInt(input.value) || 0;
			if (initialValue !== currentValue) {
				isChanged = true;
			}
		});
 
		// 変更がある時：更新ボタンを有効(false)、購入ボタンを無効(true)
		// 変更がない時：更新ボタンを無効(true)、購入ボタンを有効(false)
		updateCartButton.disabled = !isChanged;
		toCheckoutButton.disabled = isChanged;
	}
 
	inputs.forEach(input => {
		// 入力値が変わった瞬間に動くイベントリスナー
		input.addEventListener('input', function() {
			const initialValue = parseInt(this.defaultValue) || 0;
			const currentValue = parseInt(this.value) || 0;
			
			// 赤く太字にする
			if (currentValue < initialValue) {
				this.classList.add('text-danger', 'fw-bold');
			} else {
				this.classList.remove('text-danger', 'fw-bold');
			}
 
			// 入力があるたびにボタンの状態をチェック
			updateButtonStatus();
		});
	});
	// 画面が開いた初期状態チェック
	updateButtonStatus();
</script>
</body>
</html>