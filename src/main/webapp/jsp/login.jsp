<!--ログイン-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
<style>
	/* 画面全体を中央寄せにする設定 */
	body {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		min-height: 100vh; 
		margin: 0;
	}

	/* ログイン全体の枠 */
	.login-box {
		width: 100%;
		max-width: 450px;
		padding: 30px;
		background: rgb(227, 227, 227);
		border-radius: 12px;
		box-shadow: 0 4px 12px rgba(0,0,0,0.05);
	}
	/* ★入力欄フォーカス時の水色の枠線を外す */
	.form-control:focus {
		border-color: #ced4da;
		box-shadow: none !important;
	}

	/* アプリ名 */
	.title {
		font-size: 48px; 
		font-weight: bold;
		margin-bottom: 32px;
		color: #212529;
		text-align: center;
	}

	/* リンク */
	.link-container {
		margin-top: 24px;
		text-align: center;
		font-size: 14px;
	}
	
	.link-container a {
		text-decoration: none;
	}
	.link-container a:hover {
		text-decoration: underline;
	}
</style>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap Icons（目のアイコン） -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>
<div class="login-box">
	<div class="title">福岡書店</div>
	<form method="POST" action="BookServlet">
		<div class="mb-3">
			<label for="uid" class="form-label font-weight-bold">ログインID</label>
			<input type="text" id="uid" name="input_id" class="form-control form-control-lg" placeholder="ユーザーIDを入力" required>
		</div>
		<div class="mb-4">
			<label for="password" class="form-label font-weight-bold">パスワード</label>
			<!-- パスワード入力欄とトグルボタンのグループ化 -->
			<div class="input-group">
				<input type="password" id="password" name="input_password" class="form-control form-control-lg" placeholder="パスワードを入力" required>
				<button class="btn btn-outline-secondary" type="button" id="togglePassword" aria-label="パスワードの表示切替">
					<i class="bi bi-eye-slash" id="toggleIcon"></i>
				</button>
			</div>
		</div>
		<button type="submit" name="action" value="loginCheck" class="btn btn-primary btn-lg w-100 fw-bold" style="border-radius: 8px;">ログイン</button>
		<hr class="my-4">
		<div class="link-container d-flex flex-column gap-2">
			<div>
				<a href="bookList.jsp" class="text-secondary">ログインせずに閲覧する</a>
			</div>
			<div class="d-flex justify-content-between mt-2">
				<a href="registerInput.jsp" class="link-primary fw-bold">新規ユーザー登録</a>
				<a href="forgotPassword.jsp" class="text-muted">パスワードをお忘れの方</a>
			</div>
		</div>
	</form>
</div>
<c:if test="${not empty sessionScope.loginMessage}">
    <script>
        alert("${sessionScope.loginMessage}");
    </script>
	<c:remove var="loginMessage" scope="session" />
</c:if>

<!-- パスワード表示・非表示切替のJavaScript -->
<script>
document.getElementById('togglePassword').addEventListener('click', function () {
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.getElementById('toggleIcon');
    // type属性の切り替え
    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordInput.setAttribute('type', type);
    // アイコンの切り替え
    toggleIcon.classList.toggle('bi-eye');
    toggleIcon.classList.toggle('bi-eye-slash');
});
</script>
</body>
</html>