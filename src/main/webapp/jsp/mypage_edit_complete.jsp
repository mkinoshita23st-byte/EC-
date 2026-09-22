<!--マイページ編集完了-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 変動タイトル -->
<c:choose>
    <c:when test="${totalQuantity == 'true'}"><c:set var="pageTitle" value="マイページ更新" /></c:when>
    <c:when test="${totalQuantity == 'false'}"><c:set var="pageTitle" value="マイページ更新エラー" /></c:when>
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
			<c:when test="${totalQuantity=="true"}">
				<div>
					<h1>変更後の内容</h1>
					<h2>以下の内容で登録しました</h2>
					ユーザーID　　　　　：${login.user_id}<br>
					氏名（アカウント名）：${login.account_name}<br>
					住所　　　　　　　　：${login.address}
					メールアドレス　　　：${login.email}<br>
					パスワード　　　　　：${login.password}<br><!-- *****にする -->
				</div>
				<button type="submit" name="action" value="login">ログインページへ</button>
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