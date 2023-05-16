<nav>
	<div>
		<c:choose>
			<c:when test="${ empty sessionScope.login }"> 
				<a href="<c:url value="/connection"/>"><i class="fa-solid fa-arrow-right-to-bracket"></i></a>
			</c:when>
			<c:otherwise>
				<a href="<c:url value="/disconnection"/>"><i class="fa-solid fa-arrow-right-from-bracket"></i></a>
			</c:otherwise>
		</c:choose>
	</div>
</nav>