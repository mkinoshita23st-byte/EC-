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

.table-custom tbody tr:nth-child(even):not(:last-child) td {
	background-color: rgb(221, 221, 221) !important;
}

.table-custom tbody tr:nth-child(odd):not(:last-child) td {
	background-color: rgb(243, 243, 243) !important;
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
	<form method="POST" action="OrderServlet">
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
				<c:forEach var="book" items="${bookCartList}">
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
			<div class="card p-3 mb-4 bg-light">
				<div class="form-check mb-2">
					<input class="form-check-input" type="radio" name="addressType"
						id="myAddress" value="self"
						<c:if test="${empty addressType || addressType == 'self'}">checked</c:if>>
					<label class="form-check-label fw-bold" for="myAddress">
						ご自宅（会員登録住所へ送る） </label>
					<div class="text-muted small ms-3" id="selfAddressArea">
						${userInf.name} 様<br> 〒${userInf.getPostalCode().substring(0, 3)}-${userInf.getPostalCode().substring(3)}<br>
						${userInf.address}<br> メールアドレス: ${userInf.email}
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
							<select name="selectedAddressId" id="addressSelect"
								class="form-select">
								<c:forEach var="addr" items="${addressList}">
									<option value="${addr.addressId}"
										<c:if test="${addr.addressId == selectedCardId || addr.addressId == selectedAddressId}">selected</c:if>>
										${addr.name} 様<br> 〒${addr.postalCode.substring(0, 3)}-${addr.postalCode.substring(3)}<br>
										${addr.address}<br> メールアドレス: ${addr.email}
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
						<c:if test="${addressType == 'new'}">checked</c:if>> <label
						class="form-check-label fw-bold" for="addrNew">
						新しいお届け先を指定する </label>
				</div>
				<div class="ms-4 mb-2 d-none card p-3 bg-white" id="newAddressArea">
					<div class="row g-3">
						<div class="col-md-4">
							<label class="form-label small fw-bold">氏</label> <input
								type="text" name="newRecipientLastName" class="form-control"
								placeholder="例：山田" value="${newAddressInf.lastName}">
						</div>
						<div class="col-md-4">
							<label class="form-label small fw-bold">名</label> <input
								type="text" name="newRecipientFirstName" class="form-control"
								placeholder="例：太郎" value="${newAddressInf.firstName}">
						</div>
						<div class="col-12">
							<div class="col-md-3">
								<label class="form-label small fw-bold">ミドルネームの有無</label> <select
									id="middleNameSelect" name="middleNameOption" class="form-select">
									<option value="none">ミドルネーム無</option>
									<option value="exists">ミドルネームがある</option>
								</select>
							</div>
						</div>
						<div class="col-md-6 d-none" id="middleNameField">
							<label class="form-label small fw-bold">ミドルネーム</label> <input
								type="text" name="newRecipientMiddleName" class="form-control"
								placeholder="例：ディエゴ・ホセ・フランシスコ "
								value="${newAddressInf.middleName}">
							<div class="form-text small">ミドルネームが複数ある場合は間に「・」を入れてください</div>
						</div>
						<div class="col-12">
							<div class="col-md-3">
								<label class="form-label small fw-bold">郵便番号</label> <input
									type="text" name="newPostalCode" class="form-control"
									placeholder="例：0000000" value="${newAddressInf.postalCode}"
									maxlength="7" pattern="\d{7}" inputmode="numeric"
									oninput="this.value = this.value.replace(/[^0-9]/g, '')">
								<div class="form-text small">ハイフンなし半角数字7桁</div>
							</div>
						</div>
						<div class="col-12">
							<label class="form-label small fw-bold">住所</label> <input
								type="text" name="newAddress" class="form-control"
								placeholder="例：東京都千代田区..." value="${newAddressInf.address}">
						</div>
						<div class="col-12">
							<label class="form-label small fw-bold">メールアドレス</label> <input
								type="text" name="newEmail" class="form-control"
								placeholder="例：example@domain.com"
								value="${newAddressInf.email}">
						</div>
					</div>
				</div>
			</div>
			<h5 class="section-title fw-bold">お支払い方法の選択</h5>
			<div class="card p-3 mb-4 bg-light">
				<div class="form-check mb-2">
					<input class="form-check-input" type="radio" name="paymentType"
						id="payCredit" value="credit"
						<c:if test="${empty cardList}">disabled</c:if>
						<c:if test="${not empty cardList && (empty isReOrder || !isReOrder || paymentType == 'credit')}">checked</c:if>>
					<label
						class="form-check-label fw-bold <c:if test='${empty cardList}'>text-muted</c:if>"
						for="payCredit"> クレジットカード <c:if test="${empty cardList}">（登録されているカードがありません）</c:if>
					</label>
				</div>
				<div class="ms-4 mb-3" id="creditCardArea">
					<c:if test="${not empty cardList}">
						<select name="selectedCardId" class="form-select">
							<c:forEach var="card" items="${cardList}" varStatus="status">
								<option value="${card.cardId}"
									<c:choose>
								<c:when test="${isReOrder}">
									<c:if test="${card.cardId == selectedCardId}">selected</c:if>
								</c:when>
								<c:otherwise>
									<c:if test="${status.first}">selected</c:if>
								</c:otherwise>
							</c:choose>>
									カード番号: **** **** ****
									${card.number.substring(card.number.length() - 4)} (有効期限:
									${card.expiry})</option>
							</c:forEach>
						</select>
					</c:if>
				</div>
				<div class="form-check mb-2">
					<input class="form-check-input" type="radio" name="paymentType"
						id="payCod" value="cod"
						<c:if test="${isReOrder && paymentType == 'cod'}">checked</c:if>>
					<label class="form-check-label fw-bold" for="payCod">
						代金引換（代引き） </label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="radio" name="paymentType"
						id="payConvenience" value="convenience"
						<c:if test="${isReOrder && paymentType == 'convenience'}">checked</c:if>>
					<label class="form-check-label fw-bold" for="payConvenience">
						コンビニ決済 </label>
				</div>
			</div>
			<div class="d-flex justify-content-between mt-4">
				<a href="MainServlet?action=book_list"
					class="btn btn-secondary btn-lg">書籍一覧へ戻る</a>
				<button type="submit" name="action" value="orderConfirm"
					class="btn btn-primary btn-lg px-5">次へ</button>
			</div>
	</form>
	<c:if test="${not empty sessionScope.loginSuccessMessage}">
		<script>
			alert("${sessionScope.loginSuccessMessage}");
		</script>
		<c:remove var="loginSuccessMessage" scope="session" />
	</c:if>

	<script>
		const middleNameSelect = document.getElementById('middleNameSelect');
		const middleNameField = document.getElementById('middleNameField');

		middleNameSelect.addEventListener('change', function() {
			if (this.value === 'exists') {
				middleNameField.classList.remove('d-none');
			} else {
				middleNameField.classList.add('d-none');
				middleNameField.querySelector('input').value = '';
			}
		});
		const myAddress = document.getElementById('myAddress');
		const addrRegistered = document.getElementById('addrRegistered');
		const addrNew = document.getElementById('addrNew');

		const selfAddressArea = document.getElementById('selfAddressArea');
		const registeredAddressArea = document
				.getElementById('registeredAddressArea');
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

		const orderForm = document.querySelector('form[action="OrderServlet"]');
		orderForm.addEventListener('submit', function(event) {
		    const nextButton = document.activeElement;
		    if (nextButton && nextButton.value === 'orderConfirm') {
		        const addrNewRadio = document.getElementById('addrNew');
		        if (addrNewRadio && addrNewRadio.checked) {
		            const lastName = document.querySelector('input[name="newRecipientLastName"]').value.trim();
		            const firstName = document.querySelector('input[name="newRecipientFirstName"]').value.trim();
		            const postalCode = document.querySelector('input[name="newPostalCode"]').value.trim();
		            const address = document.querySelector('input[name="newAddress"]').value.trim();
		            const email = document.querySelector('input[name="newEmail"]').value.trim();

		            if (!lastName || !firstName || !postalCode || !address || !email) {
		                alert('新しいお届け先の必須項目（氏、名、郵便番号、住所、メールアドレス）をすべて入力してください。');
		                event.preventDefault();
		                return false;
		            }
		            const middleNameSelect = document.getElementById('middleNameSelect');
		            if (middleNameSelect && middleNameSelect.value === 'exists') {
		                const middleName = document.querySelector('input[name="newRecipientMiddleName"]').value.trim();
		                if (!middleName) {
		                    alert('ミドルネームを入力してください。');
		                    event.preventDefault();
		                    return false;
		                }
		            }
		        }
		    }
		});
	</script>
</body>
</html>