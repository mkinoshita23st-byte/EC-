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
		<c:choose>
			<c:when test="${orderInf.isLogin}">
				<a href="MainServlet?action=mypage_view">ログイン中：${orderInf.name}</a>
				<br>
				<a href="MainServlet?action=logout">ログアウト</a>
			</c:when>
			<c:otherwise>
				<a href="MainServlet?action=login">未ログイン：あなた</a>
			</c:otherwise>
		</c:choose>
		<h2 class="mb-4 text-center">マイページ</h2>
		<form method="POST" action="MypageServlet">
			<div class="card mb-4 shadow-sm">
				<div class="card-body">
					<div
						class="d-flex justify-content-between align-items-center section-header">
						<h4 class="card-title text-primary mb-0">基本会員情報</h4>
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
					<div class="mb-4">
						<c:forEach var="addr" items="${addressList}">
							<div class="row mb-2">
								<div class="col-sm-3 fw-bold">宛名</div>
								<div class="col-sm-9">${addr.getName()}</div>
							</div>
							<div class="row mb-2">
								<div class="col-sm-3 fw-bold">メールアドレス</div>
								<div class="col-sm-9">${addr.getEmail()}</div>
							</div>
							<div class="row mb-2">
								<div class="col-sm-3 fw-bold">住所</div>
								<div class="col-sm-9">
									〒${addr.getPostalCode().substring(0, 3)}-${addr.getPostalCode().substring(3)}<br>
									${addr.getAddress()}
								</div>
							</div>
							<hr>
						</c:forEach>
					</div>

					<div class="card p-3 bg-white border d-none" id="newAddressArea">
						<h5 class="fw-bold mb-3 text-secondary">新しいお届け先の入力</h5>
						<div class="row g-3">
							<div class="col-md-4">
								<label class="form-label small fw-bold">氏</label> <input
									type="text" name="newRecipientLastName" class="form-control"
									placeholder="例：山田">
							</div>
							<div class="col-md-4">
								<label class="form-label small fw-bold">名</label> <input
									type="text" name="newRecipientFirstName" class="form-control"
									placeholder="例：太郎">
							</div>
							<div class="col-12">
								<div class="col-md-3">
									<label class="form-label small fw-bold">ミドルネームの有無</label> <select
										id="middleNameSelect" name="middleNameOption"
										class="form-select">
										<option value="none">ミドルネーム無</option>
										<option value="exists">ミドルネームがある</option>
									</select>
								</div>
							</div>
							<div class="col-md-6 d-none" id="middleNameField">
								<label class="form-label small fw-bold">ミドルネーム</label> <input
									type="text" name="newRecipientMiddleName" class="form-control"
									placeholder="例：ディエゴ・ホセ・フランシスコ ">
								<div class="form-text small">ミドルネームが複数ある場合は間に「・」を入れてください</div>
							</div>
							<div class="col-12">
								<div class="col-md-3">
									<label class="form-label small fw-bold">郵便番号</label> <input
										type="text" name="newPostalCode" class="form-control"
										placeholder="例：1234567" maxlength="7" pattern="\d{7}"
										inputmode="numeric"
										oninput="this.value = this.value.replace(/[^0-9]/g, '')">
									<div class="form-text small">ハイフンなし半角数字7桁</div>
								</div>
							</div>
							<div class="col-12">
								<label class="form-label small fw-bold">住所</label> <input
									type="text" name="newAddress" class="form-control"
									placeholder="例：東京都千代田区...">
							</div>
							<div class="col-12">
								<label class="form-label small fw-bold">メールアドレス</label> <input
									type="text" name="newEmail" class="form-control"
									placeholder="例：example@domain.com">
							</div>
						</div>
						<div class="col-12 text-end mt-3">
							<button type="submit" id="addNewAddress" name="action"
								value="addNewAddress" class="btn btn-success px-4">このお届け先を登録する</button>
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
							<div class="col-md-8">
								<label class="form-label small fw-bold">カード番号</label>
								<div class="input-group">
									<input type="text" id="card1"
										class="form-control text-center card-no-input" maxlength="4"
										inputmode="numeric"> <span>-</span> <input type="text"
										id="card2" class="form-control text-center card-no-input"
										maxlength="4" inputmode="numeric"> <span
										class="input-group-text">-</span> <input type="text"
										id="card3" class="form-control text-center card-no-input"
										maxlength="4" inputmode="numeric"> <span
										class="input-group-text">-</span> <input type="text"
										id="card4" class="form-control text-center card-no-input"
										maxlength="4" inputmode="numeric">
								</div>
								<input type="hidden" name="newCardNumber" id="newCardNumber">
							</div>
							<div class="col-md-12">
								<div class="col-md-4">
									<label class="form-label small fw-bold">名義人（ローマ字）</label> <input
										type="text" name="newCardHolder" class="form-control"
										placeholder="例：TARO YAMADA">
								</div>
							</div>
							<div class="col-md-4">
								<label class="form-label small fw-bold">有効期限 (月 / 年)</label>
								<div class="input-group">
									<input type="text" name="newCardMonth" id="cardMonth"
										class="form-control text-center card-expiry-input"
										maxlength="2" inputmode="numeric"> <span
										class="input-group-text">/</span> <input type="text"
										name="newCardYear" id="cardYear"
										class="form-control text-center card-expiry-input"
										maxlength="2" inputmode="numeric">
								</div>
								<div class="form-text small" id="expiryExample">例：01/28</div>
							</div>
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
							<div class="col-12 text-end mt-3">
								<button type="submit" id="addNewCard" name="action"
									value="addNewCard" class="btn btn-success px-4">このカードを登録する</button>
							</div>
						</div>
					</div>
				</div>
			</div>
	</div>
	<div class="d-flex justify-content-between mt-5">
		<a href="MainServlet?action=book_list" class="btn btn-secondary">書籍一覧へ</a>
		<a href="MainServlet?action=mypage_edit" class="btn btn-secondary">会員情報を編集</a>
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

		const mypageForm = document.querySelector('form[action="MypageServlet"]');
		if (mypageForm) {
			mypageForm.addEventListener('submit',function(event) {
				const activeBtn = document.activeElement;
				if (activeBtn && activeBtn.value === 'addNewAddress') {
					const lastName = document
							.querySelector('input[name="newRecipientLastName"]').value.trim();
					const firstName = document
							.querySelector('input[name="newRecipientFirstName"]').value.trim();
					const postalCode = document
							.querySelector('input[name="newPostalCode"]').value.trim();
					const address = document
							.querySelector('input[name="newAddress"]').value.trim();
					const email = document
							.querySelector('input[name="newEmail"]').value.trim();
					if (!lastName || !firstName || !postalCode
							|| !address || !email) {
						alert('新しいお届け先の必須項目（氏、名、郵便番号、住所、メールアドレス）'
								+'をすべて入力してください。');
						event.preventDefault();
						return false;
					}
					const middleNameSelectElem = document
							.getElementById('middleNameSelect');
					if (middleNameSelectElem && middleNameSelectElem.value === 'exists') {
						const middleName = document
								.querySelector('input[name="newRecipientMiddleName"]').value.trim();
						if (!middleName) {
							alert('ミドルネームがある場合は入力してください。');
							event.preventDefault();
							return false;
						}
					}
				}
				if (activeBtn && activeBtn.value === 'addNewCard') {
					const cardNumber = document.querySelector('input[name="card1"]').value.trim()
							+document
							.querySelector('input[name="card2"]').value.trim()+document
							.querySelector('input[name="card3"]').value.trim()+document
							.querySelector('input[name="card4"]').value.trim();
					const cardHolder = document
							.querySelector('input[name="newCardHolder"]').value.trim();
					const cardExpiry年 = document
							.querySelector('input[name="]').value.trim();
					const cardExpiry月 = document
					const cardExpiry='20'+cardExpiry年+'-'+(cardExpiry月+1）+'-1';
					const now =new Date();年月日
					if (!cardNumber || !cardHolder|| !cardExpiry) {
						alert('新しいクレジットカードの必須項目（カード番号、名義人、有効期限、メインカードか）'
								+'をすべて入力してください。');
						event.preventDefault();
						return false;
					}eles{
						if(cardNumberが16桁でない場合){
							alert('番号足りんで。');
							event.preventDefault();
							return false;
						}
						if(cardExpiry<=now){
						alert('期限が切れています。有効期限内のカード情報を入力してください。');
						event.preventDefault();
						return false;
						}
					}
				}
			});
		}
		
		window.addEventListener('DOMContentLoaded', function() {
			const currentYearFull = new Date().getFullYear();
			const targetYearShort = (currentYearFull + 2 - 2000).toString().padStart(2, '0');
			const expiryExample = document.getElementById('expiryExample');
			expiryExample.textContent = '例：01/' + targetYearShort;
		});
	</script>
</body>
</html>