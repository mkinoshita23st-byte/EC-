<!--ログアウト確認-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン状況</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
/* 画面全体を中央寄せにする設定 */
body {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: flex-start;
	padding-top:100px;
	min-height: 100vh;
	margin: 0;
}
</style>
</head>
<body>
	<form method="POST" action="LoginServlet">
		現在、以下のアカウントでログイン中です。<br>
		アカウント名：${userInf.userId}さん<br>
		<hr class="my-4 text-muted">
		ログアウトしますか？<br>
		<button type="submit" name="action" value="logout">はい</button>
		<a href="MainServlet?action=book_list" class="btn btn-secondary">いいえ</a>
	</form>
</body>
</html>