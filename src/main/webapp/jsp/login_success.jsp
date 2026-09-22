<!-- ログイン成功 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン成功</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
	body {
		display: flex;
		flex-direction: column;
		align-items: center;
		min-height: 100vh; 
		margin: 0;
		padding-top: 60px;
	}
</style>
</head>
<body>
	<form method="GET" action="BookServlet">
			<div>
					<h4>ログインに成功しました</h4>
					<h5>ようこそ
					${account_name}
					さん</h5>
			</div>
		<a href="BookServlet?action=cart_view" class="btn btn-secondary">カートページへ</a>
	</form>
</body>
</html>