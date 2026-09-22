<!--注文内容入力-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文内容入力</title>
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
	<h2 class="mb-4">ご注文内容の入力</h2>
	<form method="POST" action="BookServlet">
		<h5 class="section-title fw-bold">購入商品情報</h5>
		<table class="table table-custom">
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

		<h5 class="section-title fw-bold">お届け先の選択</h5>
		<div class="card p-3 mb-4 bg-light">
			<div class="form-check mb-2">
				<input class="form-check-input" type="radio" name="addressType"
					id="myAddress" value="self"
					<c:if test="${empty addressType || addressType == 'self'}">checked</c:if>>
				<label class="form-check-label fw-bold" for="myAddress">
					ご自宅（会員登録住所へ送る） </label>
				<div class="text-muted small ms-3" id="selfAddressArea">
					〒${userInf.postalCode} ${userInf.address}（${userInf.accountName} 様）<br>
					メールアドレス: ${userInf.email}
				</div>
			</div>
			<div class="form-check mb-2">
				<input class="form-check-input" type="radio" name="addressType"
					id="addrRegistered" value="registered"
					<c:if test="${addressType == 'registered'}">checked</c:if>>
				<label class="form-check-label fw-bold" for="addrRegistered">
					登録済みのお届け先から選択 </label>
			</div>
			<div class="ms-4 mb-3 d-none" id="registeredAddressArea">
				<c:choose>
					<c:when test="${not empty addressList}">
						<select name="selectedAddressId" id="addressSelect" class="form-select">
							<c:forEach var="addr" items="${addressList}">
								<option value="${addr.addressId}"
									<c:if test="${addr.addressId == selectedAddressId}">selected</c:if>>
									〒${addr.postalCode} ${addr.address} (${addr.name} 様) [${addr.email}]
								</option>
							</c:forEach>
						</select>
					</c:when>
					<c:otherwise>
						<p class="text-danger small mb-0">登録されている別のお届け先がありません。</p>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="form-check mb-2">
				<input class="form-check-input" type="radio" name="addressType"
					id="addrNew" value="new"
					<c:if test="${addressType == 'new'}">checked</c:if>>
				<label class="form-check-label fw-bold" for="addrNew">
					新しいお届け先を指定する </label>
			</div>
			<div class="ms-4 mb-2 d-none card p-3 bg-white" id="newAddressArea">
				<div class="row g-3">
					<div class="col-md-6">
						<label class="form-label small fw-bold">受取人氏名</label>
						<input type="text" name="newRecipientName" class="form-control"
							placeholder="例：山田 太郎" value="${newRecipientName}">
					</div>
					<div class="col-md-6">
						<label class="form-label small fw-bold">郵便番号</label>
						<input type="text" name="newPostalCode" class="form-control"
							placeholder="例：000-0000" value="${newPostalCode}">
					</div>
					<div class="col-12">
						<label class="form-label small fw-bold">住所</label>
						<input type="text" name="newAddress" class="form-control"
							placeholder="例：東京都千代田区..." value="${newAddress}">
					</div>
					<div class="col-12">
						<label class="form-label small fw-bold">メールアドレス</label>
						<input type="text" name="newEmail" class="form-control"
							placeholder="例：example@domain.com" value="${newEmail}">
					</div>
				</div>
			</div>
		</div>
		<h5 class="section-title fw-bold">お支払い方法の選択</h5>
		<div class="card p-3 mb-4 bg-light">
			<div class="form-check mb-2">
				<input class="form-check-input" type="radio" name="paymentType"
					id="payCredit" value="credit"
					<c:if test="${empty paymentType || paymentType == 'credit'}">checked</c:if>>
				<label class="form-check-label fw-bold" for="payCredit"> クレジットカード </label>
			</div>
			<div class="ms-4 mb-3" id="creditCardArea">
				<c:choose>
					<c:when test="${not empty cardList}">
						<select name="selectedCardId" class="form-select">
							<c:forEach var="card" items="${cardList}">
								<option value="${card.cardId}"
									<c:if test="${card.cardId == selectedCardId}">selected</c:if>>
									カード番号: **** **** **** ${card.number.substring(card.number.length() - 4)}
									(有効期限: ${card.expiry})
								</option>
							</c:forEach>
						</select>
					</c:when>
					<c:otherwise>
						<p class="text-danger small mb-0">登録されているクレジットカードがありません。</p>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="form-check mb-2">
				<input class="form-check-input" type="radio" name="paymentType"
					id="payCod" value="cod"
					<c:if test="${paymentType == 'cod'}">checked</c:if>>
				<label class="form-check-label fw-bold" for="payCod"> 代金引換（代引き） </label>
			</div>
			<div class="form-check">
				<input class="form-check-input" type="radio" name="paymentType"
					id="payConvenience" value="convenience"
					<c:if test="${paymentType == 'convenience'}">checked</c:if>>
				<label class="form-check-label fw-bold" for="payConvenience"> コンビニ決済 </label>
			</div>
		</div>

		<div class="d-flex justify-content-between mt-4">
			<a href="BookServlet?action=book_list" class="btn btn-secondary btn-lg">書籍一覧へ戻る</a>
			<button type="submit" name="action" value="orderConfirm" class="btn btn-primary btn-lg px-5">次へ</button>
		</div>
	</form>
	<c:if test="${not empty sessionScope.loginSuccessMessage}">
		<script>
			alert("${sessionScope.loginSuccessMessage}");
		</script>
		<c:remove var="loginMessage" scope="session" />
	</c:if>

	<script>
		// 住所ラジオボタンの切り替え制御
		const myAddress = document.getElementById('myAddress');
		const addrRegistered = document.getElementById('addrRegistered');
		const addrNew = document.getElementById('addrNew');

		const selfAddressArea = document.getElementById('selfAddressArea');
		const registeredAddressArea = document.getElementById('registeredAddressArea');
		const newAddressArea = document.getElementById('newAddressArea');

		function toggleAddressArea() {
			selfAddressArea.classList.add('d-none');
			registeredAddressArea.classList.add('d-none');
			newAddressArea.classList.add('d-none');
			if (myAddress.checked) {
				selfAddressArea.classList.remove('d-none');
			} else if (addrRegistered.checked) {
				registeredAddressArea.classList.remove('d-none');
			} else if (addrNew.checked) {
				newAddressArea.classList.remove('d-none');
			}
		}

		myAddress.addEventListener('change', toggleAddressArea);
		addrRegistered.addEventListener('change', toggleAddressArea);
		addrNew.addEventListener('change', toggleAddressArea);

		toggleAddressArea();

		// 支払方法ラジオボタンの表示切替制御
		const payCredit = document.getElementById('payCredit');
		const payCod = document.getElementById('payCod');
		const payConvenience = document.getElementById('payConvenience');
		const creditCardArea = document.getElementById('creditCardArea');

		function toggleCreditArea() {
			if (payCredit.checked) {
				creditCardArea.classList.remove('d-none');
			} else {
				creditCardArea.classList.add('d-none');
			}
		}

		payCredit.addEventListener('change', toggleCreditArea);
		payCod.addEventListener('change', toggleCreditArea);
		payConvenience.addEventListener('change', toggleCreditArea);

		toggleCreditArea();
	</script>
</body>
</html>