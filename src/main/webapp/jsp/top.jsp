<!--トップページ-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TOP</title>
<style>
	/* 画面全体を中央寄せにする設定 */
	body {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		min-height: 80vh; 
		margin: 0;
		background-color: #f8f9fa; 
	}

	.title {
		font-size: 56px; 
		font-weight: bold;
		margin-bottom: 48px; /* ボタンとの間隔 */
		color: #212529;
	}

	/*ボタンを中央に配置し、横並びにするコンテナ */
	.button-container {
		display: flex;
		justify-content: center; /* ボタンの塊を真ん中に寄せる */
		gap: 24px; /* ボタン同士の間隔 */
		width: 100%;
	}
	
	/*ボタン個別の大きさを指定するカスタムクラス */
	.btn-custom {
		width: 200px;   
		padding: 14px 0;    
		font-size: 18px;    
		font-weight: bold;
		border-radius: 8px; /* 角の丸みを少し滑らかに */
		transition: all 0.2s; /* マウスを乗せたときのアニメーションを滑らかに */
	}
</style>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<form method="GET" action="BookServlet">
	<!-- タイトルをクラスで指定 -->
	<div class="title">福岡書店</div>
	<div class="button-container">
	<a href="BookServlet?action=book_list" class="btn btn-secondary">書籍一覧を見る</a>
	<a href="BookServlet?action=cart_view" class="btn btn-secondary">カート確認</a>
	</div>
</form>
</body>
</html>