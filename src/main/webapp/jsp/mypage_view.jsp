<!--マイページ-->
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
.section-title {
	border-bottom: 2px solid #0d6efd;
	padding-bottom: 8px;
	margin-bottom: 20px;
}

.badge-main {
	background-color: #0d6efd;
}
</style>
</head>
<body class="bg-light">

	<div class="container my-5" style="max-width: 900px;">
		<h2 class="mb-4 text-center">マイページ</h2>

		<!-- 1. 基本ユーザー情報 (usr_mypage) -->
		<div class="card mb-4 shadow-sm">
			<div class="card-body">
				<h4 class="card-title section-title text-primary">基本会員情報</h4>
				<div class="row mb-2">
					<div class="col-sm-3 fw-bold">ユーザーID</div>
					<div class="col-sm-9">${user.getUserId()}</div>
				</div>
				<div class="row mb-2">
					<div class="col-sm-3 fw-bold">お名前</div>
					<div class="col-sm-9">田中 太郎</div>
				</div>
				<div class="row mb-2">
					<div class="col-sm-3 fw-bold">メールアドレス</div>
					<div class="col-sm-9">tanaka@example.com</div>
				</div>
				<div class="row mb-2">
					<div class="col-sm-3 fw-bold">ご自宅住所</div>
					<div class="col-sm-9">
						〒100-0001<br> 東京都千代田区千代田1-1
					</div>
				</div>
			</div>
		</div>

		<!-- 2. お届け先住所一覧 (usr_addresses) -->
		<div class="card mb-4 shadow-sm">
			<div class="card-body">
				<div class="d-flex justify-content-between align-items-center mb-3">
					<h4 class="card-title section-title text-primary mb-0">お届け先住所一覧</h4>
					<a href="#" class="btn btn-sm btn-outline-primary">+ 新しいお届け先を追加</a>
				</div>

				<div class="table-responsive">
					<table class="table table-hover align-middle">
						<thead class="table-light">
							<tr>
								<th>宛名</th>
								<th>郵便番号</th>
								<th>住所</th>
								<th>メールアドレス</th>
								<th>区分</th>
							</tr>
						</thead>
						<tbody>
							<!-- 本来は <c:forEach var="addr" items="${addressList}"> などでループ処理を行います -->
							<tr>
								<td>田中 太郎</td>
								<td>100-0001</td>
								<td>東京都千代田区千代田1-1</td>
								<td>taro.tanaka@example.com</td>
								<td><span class="badge badge-main">メイン住所</span> <span
									class="badge bg-secondary">本人</span></td>
							</tr>
							<tr>
								<td>田中 一郎</td>
								<td>160-0022</td>
								<td>東京都新宿区新宿3-1-1</td>
								<td>ichiro.tanaka@example.com</td>
								<td>-</td>
							</tr>
							<tr>
								<td>田中 次郎</td>
								<td>180-0004</td>
								<td>東京都武蔵野市吉祥寺本町1-2-3</td>
								<td>jiro.tanaka@example.com</td>
								<td>-</td>
							</tr>
							<tr>
								<td>阿部 寛</td>
								<td>231-0001</td>
								<td>神奈川県横浜市中区新港1-1</td>
								<td>kan.abe@example.com</td>
								<td>-</td>
							</tr>
							<tr>
								<td>田中 太郎</td>
								<td>108-0075</td>
								<td>東京都港区港南2-1-1</td>
								<td>tanaka.sub@example.com</td>
								<td><span class="badge bg-secondary">本人</span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="card mb-4 shadow-sm">
			<div class="card-body">
				<div class="d-flex justify-content-between align-items-center mb-3">
					<h4 class="card-title section-title text-primary mb-0">登録クレジットカード情報</h4>
					<a href="#" class="btn btn-sm btn-outline-primary">+ 新しいカードを追加</a>
				</div>

				<div class="table-responsive">
					<table class="table table-hover align-middle">
						<thead class="table-light">
							<tr>
								<th>カード番号</th>
								<th>名義人</th>
								<th>有効期限 (月/年)</th>
								<th>設定</th>
							</tr>
						</thead>
						<tbody>
							<!-- 本来は <c:forEach var="card" items="${cardList}"> などでループ処理を行います -->
							<tr>
								<td>**** **** **** 1111</td>
								<td>TARO TANAKA</td>
								<td>12/28</td>
								<td><span class="badge badge-main">メイン使用</span></td>
							</tr>
							<tr>
								<td>**** **** **** 0000</td>
								<td>TARO TANAKA</td>
								<td>08/26</td>
								<td>-</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>

	</div>

	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>