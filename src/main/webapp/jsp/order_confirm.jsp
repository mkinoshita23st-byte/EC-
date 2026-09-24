<!--購入内容確認-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文内容の確認</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
/* ★ヘッダー（thead）の色付けの設定 */
.table-custom thead tr th {
	background-color: rgb(173, 181, 245) !important;
	color: rgb(0, 0, 0) !important;
	vertical-align: middle;
}

/* 偶数行: 色付き（下から1行目以内のtrは除く） */
.table-custom tbody tr:nth-child(even):not(:last-child) td {
	background-color: rgb(192, 192, 192) !important;
}

.section-title {
	border-left: 4px solid #0d6efd;
	padding-left: 10px;
	margin-top: 25px;
	margin-bottom: 15px;
}
</style>
</head>
<body class="container py-4">
	<h2 class="mb-4">ご注文内容の確認</h2>

	<div class="alert alert-warning text-center fw-bold py-3 mb-4"
		role="alert">
		★まだご注文は完了していません★<br> 内容をご確認の上、「注文を確定する」ボタンを押してください。
	</div>

	<form method="POST" action="BookServlet">
		<!-- 1. 購入商品情報 -->
		<h5 class="section-title fw-bold">購入商品情報</h5>
		<table class="table table-custom mb-4">
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
					<c:if test="${book.cartCnt > 0}">
						<tr>
							<td>${book.title}</td>
							<td>${book.price}円</td>
							<td>${book.publish}</td>
							<td>${book.published}</td>
							<td>${book.cartCnt}</td>
						</tr>
					</c:if>
				</c:forEach>
				<tr>
					<td colspan="5" class="text-end fw-bold fs-5">合計金額：${totalPrice}円（総数量：${totalQuantity}冊）</td>
				</tr>
			</tbody>
		</table>
		<h5 class="section-title fw-bold">お届け先情報</h5>
		<div class="card p-3 mb-4 bg-light">
			<c:choose>
				<c:when test="${!isNewAddress}">
					<c:choose>
						<c:when test="${!userInf.isRecipient}">
							<p class="mb-0 text-muted">
								〒${orderInf.postalCode}<br> ${orderInf.address}<br>
								受取人:${orderInf.accountName} 様<br> メールアドレス:
								${orderInf.email}
							</p>
						</c:when>
						<c:otherwise>
							<p class="mb-0 text-muted">
								〒${recipientInf.postalCode}<br> ${recipientInf.address}<br>
								受取人:${userInf.accountName} 様<br> メールアドレス: ${userInf.email}
							</p>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<p class="mb-1 fw-bold">新住所</p>
					<p class="mb-0 text-muted">
						〒${newAddressInf.postalCode}<br> ${newAddressInf.address}<br>
						受取人:${newAddressInf.accountName} 様<br> メールアドレス:
						${newAddressInf.email}
					</p>
				</c:otherwise>
			</c:choose>
		</div>
		<h5 class="section-title fw-bold">お支払い方法</h5>
		<div class="card p-3 mb-4 bg-light">
			<p class="mb-1 fw-bold">${orderInf.selectedPayment}</p>
			<c:choose>
				<c:when test="${orderInf.selectedPayment=='クレジットカード'}">
					<c:forEach var="card" items="${cardList}">
						<c:if test="${card.cardId == selectedCardId}">
							<p class="mb-0 text-muted">カード番号: **** **** ****
								${card.number.substring(card.number.length() - 4)} (有効期限:
								${card.expiry})</p>
						</c:if>
					</c:forEach>
				</c:when>
				<c:otherwise>

					<p class="mb-1 fw-bold"></p>
				</c:otherwise>
			</c:choose>
		</div>

		<!-- 4. サーバー受渡用のhiddenパラメータ (戻る・確定用) -->
		<input type="hidden" name="addressType" value="${addressType}">
		<input type="hidden" name="selectedAddressId"
			value="${selectedAddressId}"> <input type="hidden"
			name="newRecipientName" value="${newRecipientName}"> <input
			type="hidden" name="newPostalCode" value="${newPostalCode}">
		<input type="hidden" name="newAddress" value="${newAddress}">
		<input type="hidden" name="newEmail" value="${newEmail}"> <input
			type="hidden" name="paymentType" value="${paymentType}"> <input
			type="hidden" name="selectedCardId" value="${selectedCardId}">

		<!-- 5. 操作ボタン -->
		<div class="d-flex justify-content-between mt-4">
			<button type="submit" name="action" value="orderInputBack"
				class="btn btn-secondary btn-lg">修正する</button>
			<button type="submit" name="action" value="orderComplete"
				class="btn btn-danger btn-lg px-5 fw-bold">注文を確定する</button>
		</div>
	</form>
</body>
</html>