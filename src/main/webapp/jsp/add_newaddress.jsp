<!-- 購入時の新規住所入力時のみ遷移するページ -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新規住所追加</title>
</head>
<body>
	<form id="saveAddressForm" method="POST" action="OrderServlet">
		<input type="hidden" name="action" value="saveAddressForm">
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