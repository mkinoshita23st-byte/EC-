<!--ログアウト完了-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログアウト</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
body {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: flex-start;
	padding-top: 100px;
	min-height: 100vh;
	margin: 0;
}
</style>
</head>
<body>
	<form method="GET" action="MainServlet">
			ログアウトしました<br>
		<button type="submit" name="action" value="top"
			class="btn btn-secondary">TOPへ</button>
	</form>
</body>
</html>