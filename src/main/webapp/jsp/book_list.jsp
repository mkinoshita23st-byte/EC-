<!--書籍一覧-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>書籍一覧</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
	body{
		padding: 20px 40px;
	}
	/* 太字にして赤色をより引き立たせる */
	.count-input.text-danger {
		font-weight: bold;
	}
	/* 偶数行: 色付き*/
	.table-custom tbody tr:nth-child(even) td {
		background-color: rgb(192, 192, 192) !important;
	}
</style>
</head>
<body>
<form method="POST" action="BookServlet">
	<c:choose>
		<c:when test="${orderList.isLogin}">
			<a href="BookServlet?action=mypage_view">ログイン中：${orderList.name}</a>
		</c:when>
		<c:otherwise>
		<a href="BookServlet?action=login">未ログイン：あなた</a>
		</c:otherwise>
	</c:choose>
<table class="table table-custom">
	<thead>
		<tr>
			<th>書籍名</th>
			<th>価格</th>
			<th>出版社</th>
			<th>刊行年月日</th>
			<th>在庫数</th>
			<th>購入数</th>
			<th>カート</th>
		</tr>
	</thead>
<tbody>
	<c:forEach var="book" items="${bookList}" varStatus="status">
		<tr>
			<td>${book.title}</td>
			<td>${book.price}</td>
			<td>${book.publish}</td>
			<td>${book.published}</td>
			<td>${book.stock}</td>
			<td>
			<td>
				<c:choose>
				    <c:when test="${book.stock == 0 || (book.stock - book.cartCnt) <= 0}">
				        <span class="text-muted small">在庫なし</span>
				        <input type="hidden" name="${book.isbn}" value="0">
				    </c:when>
				    <c:otherwise>
				        <input type="number" name="${book.isbn}" value="0"
				            min="0" max="${book.stock - book.cartCnt}" class="count-input" required>
				    </c:otherwise>
				</c:choose>
			</td>
			<th>${book.cartCnt}</th>
		</tr>
	</c:forEach>
</tbody>
</table>
<!--GETとPOSTで分けている-->
<div class="d-flex gap-2 mt-3">
	<a href="BookServlet?action=top" class="btn btn-secondary">TOPへ</a>
	<a href="BookServlet?action=cart_view" class="btn btn-secondary">カートを確認</a>
	<button type="submit" name="action" value="inCart" class="btn btn-secondary">カートに入れる</button>
</div>
</form>

<script>
	// 画面内のすべての数量入力欄取得
	const inputs = document.querySelectorAll('.count-input');
	// 「カートに入れる」ボタン取得
	const inCartButton = document.querySelector('button[value="inCart"]');

	//「カートに入れる」ボタンを有効・無効
	function updateButtonStatus() {
		let totalInputCount = 0;
		inputs.forEach(input => {
			totalInputCount += parseInt(input.value) || 0;
		});
		inCartButton.disabled = (totalInputCount === 0);
	}

	inputs.forEach(input => {
		// 入力値が変わった瞬間に動くイベントリスナー
		input.addEventListener('input', function() {
			const value = parseInt(this.value) || 0;
			//赤文字にする・しない
			if (value > 0) {
				this.classList.add('text-danger');
			} else {
				this.classList.remove('text-danger');
			}

			// 「カートに入れる」ボタンチェック
			updateButtonStatus();
		});
	});
	// 画面が開いた初期のチェック
	updateButtonStatus();
</script>
</body>
</html>