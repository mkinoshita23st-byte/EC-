<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン失敗</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
	body {
		display: flex;
		flex-direction: column;
		min-height: 100vh; 
		margin: 0;
		padding-top: 30px;
	}
</style>
</head>
<body>
	<form method="GET" action="MainServlet">
		<div class="text-center fs-4 fw-bold text-danger mb-3">
			ログインに失敗しました<br>
		</div>
		<div class="d-flex justify-content-center gap-3">
			<button type="submit" name="action" value="top" class="btn btn-secondary">TOPへ</button>
			<button type="submit" name="action" value="login" class="btn btn-secondary">もう一度</button>
		</div>

	</form>
</body>
</html>