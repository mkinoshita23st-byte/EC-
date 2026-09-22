<!--購入完了-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>購入完了</title>
<style>
/* 1. 画面全体で中央寄せにする設定 */
body {
	display: flex;
	justify-content: center; /* 左右の中央寄せ */
	margin: 0;
	font-family: sans-serif;
}

/* 2. 購入完了メッセージ全体の幅と行間を指定 */
.message-container {
	width: 400px; /* 横幅を決める */
	line-height: 1.8; /* 文章の行間を少し広げて読みやすく */
	margin-top: 15vh; /* 画面の上から 15% ほど下がった位置 */
}

/* 3. 「TOPへ」のリンクだけを右寄りにする設定 */
.link-right {
	text-align: right; /* 文字を右寄りに配置 */
	margin-top: 24px; /* 上の文章との間隔をあける */
}
</style>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<form method="POST" action="BookServlet">
		<div class="message-container">
			ご購入ありがとうございます！<br>
			※後ほど、ご注文内容を記載したメールを<br>
			送信しますのでご確認ください。<br>

			<div class="link-right">
				<button type="submit" name="action" value="logout" class="btn btn-outline-secondary btn-sm me-2">ログアウト</button>
				<button type="submit" name="action" value="toTop" class="btn btn-primary btn-sm">TOPへ</button>
			</div>
		</div>
	</form>
	<!-- 新住所登録用フォーム -->
	<form id="saveAddressForm" method="POST" action="BookServlet">
		<input type="hidden" name="action" value="registerNewAddress">
	</form>

	<c:if test="${not empty sessionScope.newAddressMessage}">
		<script>
			window.addEventListener('DOMContentLoaded', () => {
				const isConfirm = confirm("${sessionScope.newAddressMessage}");
				if (isConfirm) {
					document.getElementById('saveAddressForm').submit();
				}
			});
		</script>
	</c:if>
	<c:remove var="newAddressMessage" scope="session" />
</body>
</html>