<!--マイページ編集-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>会員情報編集</title>
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
		<h2 class="mb-4 text-center">会員情報編集</h2>
		<form method="POST" action="MypageServlet">
			<div class="card mb-4 shadow-sm">
				<div class="card-body">
					<div
						class="d-flex justify-content-between align-items-center section-header">
						<h4 class="card-title text-primary mb-0">基本会員情報 編集</h4>
					</div>
					<div class="row g-3">
						<div class="col-md-6">
							<label class="form-label fw-bold">ユーザーID</label> <input
								type="text" name="userId" class="form-control"
								value="${userInf.getUserId()}" readonly>
						</div>
						<div class="col-md-6">
							<label class="form-label fw-bold">お名前</label> <input type="text"
								name="accountName" class="form-control"
								value="${userInf.getName()}">
						</div>
						<div class="col-12">
							<label class="form-label fw-bold">メールアドレス</label> <input
								type="email" name="email" class="form-control"
								value="${userInf.getEmail()}">
						</div>
						<div class="col-md-6">
							<label class="form-label fw-bold">郵便番号</label> <input type="text"
								name="postalCode" class="form-control"
								value="${userInf.getPostalCode()}">
						</div>
						<div class="col-12">
							<label class="form-label fw-bold">ご自宅住所</label> <input
								type="text" name="address" class="form-control"
								value="${userInf.getAddress()}">
						</div>
					</div>
				</div>
			</div>

			<div class="card mb-4 shadow-sm">
				<div class="card-body">
					<div class="section-header">
						<h4 class="card-title text-primary mb-0">お届け先住所の編集</h4>
					</div>
					<c:forEach var="address" items="${addressList}" varStatus="status">
						<div class="border rounded p-3 mb-3 bg-white">
							<input type="hidden" name="addressId_${status.index}"
								value="${address.getId()}">
							<div class="row g-2 mb-2">
								<div class="col-md-6">
									<label class="form-label small text-muted mb-1 fw-bold">宛名</label>
									<div class="row g-2">
										<div class="col-6">
											<input type="text" name="addressLastName_${status.index}"
												class="form-control form-control-sm" placeholder="氏"
												value="${address.getLastName()}">
										</div>
										<div class="col-6">
											<input type="text" name="addressFirstName_${status.index}"
												class="form-control form-control-sm" placeholder="名"
												value="${address.getFirstName()}">
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<label class="form-label small text-muted mb-1 fw-bold">メールアドレス</label>
									<input type="email" name="addressEmail_${status.index}"
										class="form-control form-control-sm"
										value="${address.getEmail()}">
								</div>
							</div>
							<div class="row g-2 mb-2 align-items-center">
								<div class="col-md-6">
									<label class="form-label small text-muted mb-1 fw-bold">郵便番号</label>
									<input type="text" name="addressPostalCode_${status.index}"
										class="form-control form-control-sm" maxlength="7"
										pattern="\d{7}" placeholder="1000001"
										value="${address.getPostalCode()}">
									<div class="form-text text-muted" style="font-size: 0.75rem;">ハイフンなしで7桁の数値で入力してください</div>
								</div>
							</div>
							<div class="row g-2">
								<div class="col-12">
									<label class="form-label small text-muted mb-1 fw-bold">住所</label>
									<input type="text" name="addressDetail_${status.index}"
										class="form-control form-control-sm"
										value="${address.getAddress()}">
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>

			<div class="card mb-4 shadow-sm">
				<div class="card-body">
					<div class="section-header">
						<h4 class="card-title text-primary mb-0">クレジットカード情報の編集</h4>
					</div>
					<div class="table-responsive mb-3">
						<table class="table table-hover align-middle">
							<thead class="table-light">
								<tr>
									<th>カード番号</th>
									<th>名義人</th>
									<th>有効期限 (月/年)</th>
									<th>このカードをメインにする</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="card" items="${cardList}" varStatus="status">
									<tr>
										<td><input type="hidden" name="cardId_${status.index}"
											value="${card.getId()}"> <input type="text"
											name="cardNumber_${status.index}"
											class="form-control form-control-sm"
											value="${card.getNumber()}"></td>
										<td><input type="text" name="cardHolder_${status.index}"
											class="form-control form-control-sm"
											value="${card.getHolder()}"></td>
										<td><input type="text" name="cardExpiry_${status.index}"
											class="form-control form-control-sm"
											value="${card.getExpiry()}"></td>
										<td>
											<div class="form-check">
												<input class="form-check-input" type="radio"
													name="mainCardIndex" value="${status.index}"
													<c:if test="${card.isMain}">checked</c:if>>
											</div>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>

			<div class="d-flex justify-content-between align-items-center mb-5">
				<button type="submit" id="returnMypage" name="action" value=""
					class="btn btn-primary px-5">マイページに戻る</button>
				<button type="submit" name="action" value="confirmMypage"
					class="btn btn-primary px-5">変更を保存する</button>
			</div>

		</form>
	</div>
	<script>
		const backBtn = document.getElementById('backBtn');
		if (backBtn) {
			backBtn
					.addEventListener(
							'click',
							function() {
								if (confirm('変更内容を破棄してマイページに戻りますか？')) {
									const form = this.closest('form');
									form.action = 'MainServlet';
									let actionInput = form
											.querySelector('input[name="action"][value="mypage_view"]');
									if (!actionInput) {
										actionInput = document
												.createElement('input');
										actionInput.type = 'hidden';
										actionInput.name = 'action';
										actionInput.value = 'mypage_view';
										form.appendChild(actionInput);
									}
									form.submit();
								}
							});
		}
	</script>
</body>
</html>