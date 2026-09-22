<!--ログアウト確認-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 変動タイトル -->
<c:choose>
    <c:when test="${totalQuantity == 'is_logout'}"><c:set var="pageTitle" value="ログイン状況" /></c:when>
    <c:when test="${totalQuantity == 'logout'}"><c:set var="pageTitle" value="ログアウト" /></c:when>
    <c:otherwise><c:set var="pageTitle" value="システムエラー" /></c:otherwise>
</c:choose>
<title>${pageTitle}</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
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
</style>
</head>
<body>
	<form method="GET" action="BookServlet">
		<c:choose>
			<c:when test="${isLogout==true}">
				<div>
					現在、以下のアカウントでログイン中です。<br>
					アカウント名：${account_name}(${real_name})
					<hr class="my-4 text-muted">
					ログアウトしますか？
				</div>
				<button type="submit" name="action" value="logout">はい</button>
				<button type="submit" name="action" value="bookList">いいえ</button>
			</c:when>
			<c:when test="${isLogout==true}">
				<div>
					ログアウトしました<br>
				</div>
				<button type="submit" name="action" value="top">TOPへ</button>
			</c:when>
			<c:otherwise>
				<h4 class="text-warning fw-bold mb-3">システムエラー</h4>
				<div>
					不正なアクセス、または処理中に問題が発生しました。
				</div>
				<button type="submit" name="action" value="top">TOP画面へ戻る</button>
			</c:otherwise>
		</c:choose>
	</form>
</body>
</html>