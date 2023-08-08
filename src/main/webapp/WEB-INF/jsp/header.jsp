<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>

	<head>

		<meta charset="UTF-8">

		<meta name="viewport" content="width=device-width, initial-scale=1.0">

		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Cuprum&family=Titan+One&family=Truculenta:opsz,wght@12..72,500&display=swap" rel="stylesheet">

		<style><%@ include file="/assets/css/style.css"%></style>

		<script src="https://kit.fontawesome.com/9e1cf11dc1.js" crossorigin="anonymous"></script>

		<script><%@ include file="/assets/js/script.js"%></script>

		<title>Connect 4</title>

	</head>

	<body>

		<header>
			<h1>Connect 4</h1>
			<div>
				<img src="${ pageContext.request.contextPath }/assets/img/logo.png" alt="Logo">
			</div>
		</header>

		<%@ include file="nav.jsp"%>

		<main>
			<c:if test="${ not empty sessionScope.user }">

				<%@ include file="friendRequests.jsp"%>
				<%@ include file="gameRequests.jsp"%>
				<%@ include file="onlineFriends.jsp"%>

				<%@ include file="alert.jsp"%>
				<%@ include file="confirm.jsp"%>

			</c:if>