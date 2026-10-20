<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>マイページ</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
.section-header {
	border-bottom: 2px solid #0d6efd;
	padding-bottom: 12px;
	margin-bottom: 20px;
	margin-left: -1rem;
	margin-right: -1rem;
	padding-left: 1rem;
	padding-right: 1rem;
}

.badge-main {
	background-color: #0d6efd;
}
</style>
</head>
<body class="bg-light">
	<div class="container my-5" style="max-width: 900px;">
		<h2 class="mb-4 text-center">マイページ</h2>
		<form method="POST" action="MypageServlet">

			<div class="card mb-4 shadow-sm">
				<div class="card-body">
					<div
						class="d-flex justify-content-between align-items-center section-header">
						<h4 class="card-title text-primary mb-0">基本会員情報</h4>
						<a href="MainServlet?action=mypage_edit"
							class="btn btn-sm btn-outline-primary">+ 基本会員情報を編集</a>
					</div>
					<div class="row mb-2">
						<div class="col-sm-3 fw-bold">ユーザーID</div>
						<div class="col-sm-9">${userInf.getUserId()}</div>
					</div>
					<div class="row mb-2">
						<div class="col-sm-3 fw-bold">お名前</div>
						<div class="col-sm-9">${userInf.getName()}</div>
					</div>
					<div class="row mb-2">
						<div class="col-sm-3 fw-bold">メールアドレス</div>
						<div class="col-sm-9">${userInf.getEmail()}</div>
					</div>
					<div class="row mb-2">
						<div class="col-sm-3 fw-bold">ご自宅住所</div>
						<div class="col-sm-9">
							〒${userInf.getPostalCode().substring(0, 3)}-${userInf.getPostalCode().substring(3)}<br>
							${userInf.getAddress()}
						</div>
					</div>
				</div>
			</div>

			<div class="card mb-4 shadow-sm">
				<div class="card-body">
					<div
						class="d-flex justify-content-between align-items-center section-header">
						<h4 class="card-title text-primary mb-0">お届け先住所一覧</h4>
						<button type="button" id="toggleNewAddressBtn"
							class="btn btn-sm btn-outline-primary">+ 新しいお届け先を追加</button>
					</div>
					<div class="table-responsive mb-4">
						<table class="table table-hover align-middle">
							<thead class="table-light">
								<tr>
									<th>宛名</th>
									<th>郵便番号</th>
									<th>住所</th>
									<th>メールアドレス</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="address" items="${addressList}">
									<tr>
										<td>${address.getName()}</td>
										<td>〒${address.getPostalCode().substring(0, 3)}-${address.getPostalCode().substring(3)}</td>
										<td>${address.getAddress()}</td>
										<td>${address.getEmail()}</td>
										<td><c:if test="${address.getIsMain()}">
												<span class="badge badge-main">メイン住所</span>
											</c:if></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

					<div class="card p-3 bg-white border d-none" id="newAddressArea">
						<h5 class="fw-bold mb-3 text-secondary">新しいお届け先の入力</h5>
						<div class="row g-3">
							<div class="col-md-6">
								<label class="form-label small fw-bold">受取人氏名</label> <input
									type="text" name="newRecipientName" class="form-control"
									placeholder="例：山田 太郎" value="${newAddressInf.name}">
							</div>
							<div class="col-md-6">
								<label class="form-label small fw-bold">郵便番号</label> <input
									type="text" name="newPostalCode" class="form-control"
									placeholder="例：000-0000" value="〒${newAddressInf.getPostalCode().substring(0, 3)}-${newAddressInf.getPostalCode().substring(3)}">
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
							<div class="col-12">
								<label class="form-label small fw-bold d-block">メインの住所に設定しますか？</label>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio"
										name="isMainAddress" id="addressMainYes" value="true">
									<label class="form-check-label" for="addressMainYes">はい</label>
								</div>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio"
										name="isMainAddress" id="addressMainNo" value="false" checked>
									<label class="form-check-label" for="addressMainNo">いいえ</label>
								</div>
							</div>
							<div class="col-12 text-end mt-3">
								<button type="submit" name="action" value="addNewAddress"
									class="btn btn-success px-4">このお届け先を登録する</button>
							</div>
						</div>
					</div>

				</div>
			</div>

			<div class="card mb-4 shadow-sm">
				<div class="card-body">
					<div
						class="d-flex justify-content-between align-items-center section-header">
						<h4 class="card-title text-primary mb-0">登録クレジットカード情報</h4>
						<button type="button" id="toggleNewCardBtn"
							class="btn btn-sm btn-outline-primary">+ 新しいカードを追加</button>
					</div>

					<div class="table-responsive mb-4">
						<table class="table table-hover align-middle">
							<thead class="table-light">
								<tr>
									<th>カード番号</th>
									<th>名義人</th>
									<th>有効期限 (月/年)</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="card" items="${cardList}">
									<tr>
										<td>**** **** ****
											${card.number.substring(card.number.length() - 4)}</td>
										<td>${card.holder}</td>
										<td>${card.expiry}</td>
										<td><c:if test="${card.isMain}">
												<span class="badge badge-main">メイン使用</span>
											</c:if></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

					<div class="card p-3 bg-white border d-none" id="newCardArea">
						<h5 class="fw-bold mb-3 text-secondary">新しいクレジットカードの入力</h5>
						<div class="row g-3">
							<div class="col-md-6">
								<label class="form-label small fw-bold">カード番号</label> <input
									type="text" name="newCardNumber" class="form-control"
									placeholder="例：4111222233334444" value="${newCardInf.number}">
							</div>
							<div class="col-md-6">
								<label class="form-label small fw-bold">名義人（ローマ字）</label> <input
									type="text" name="newCardHolder" class="form-control"
									placeholder="例：TARO YAMADA" value="${newCardInf.holder}">
							</div>
							<div class="col-md-6">
								<label class="form-label small fw-bold">有効期限 (月/年)</label> <input
									type="text" name="newCardExpiry" class="form-control"
									placeholder="例：12/26" value="${newCardInf.expiry}">
							</div>
							<div class="col-12">
								<label class="form-label small fw-bold d-block">メインのカードに設定しますか？</label>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="isMainCard"
										id="cardMainYes" value="true"> <label
										class="form-check-label" for="cardMainYes">はい</label>
								</div>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="isMainCard"
										id="cardMainNo" value="false" checked> <label
										class="form-check-label" for="cardMainNo">いいえ</label>
								</div>
							</div>
							<div class="col-12 text-end mt-3">
								<button type="submit" name="action" value="addNewCard"
									class="btn btn-success px-4">このカードを登録する</button>
							</div>
						</div>
					</div>

				</div>
			</div>
		</form>
	</div>
	<script>
		const toggleAddressBtn = document.getElementById('toggleNewAddressBtn');
		const newAddressArea = document.getElementById('newAddressArea');
		toggleAddressBtn.addEventListener('click', function() {
			if (newAddressArea.classList.contains('d-none')) {
				newAddressArea.classList.remove('d-none');
				toggleAddressBtn.textContent = '- 住所フォームを閉じる';
			} else {
				newAddressArea.classList.add('d-none');
				toggleAddressBtn.textContent = '+ 新しいお届け先を追加';
			}
		});

		const toggleCardBtn = document.getElementById('toggleNewCardBtn');
		const newCardArea = document.getElementById('newCardArea');
		toggleCardBtn.addEventListener('click', function() {
			if (newCardArea.classList.contains('d-none')) {
				newCardArea.classList.remove('d-none');
				toggleCardBtn.textContent = '- カードフォームを閉じる';
			} else {
				newCardArea.classList.add('d-none');
				toggleCardBtn.textContent = '+ 新しいカードを追加';
			}
		});
	</script>
</body>
</html>