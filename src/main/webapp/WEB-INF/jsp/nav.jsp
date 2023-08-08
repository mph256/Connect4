<nav>
	<c:choose>

		<c:when test="${ empty sessionScope.user }">
			<div>
				<a href="<c:url value="/connection"/>"><i class="fa-solid fa-arrow-right-to-bracket"></i></a>
			</div>
		</c:when>

		<c:otherwise>

			<div class="icon-home">
				<a href="<c:url value="/home"/>"><i class="fa-solid fa-house"></i></a>
			</div>

			<div class="requests-friend-show">
				<i class="fa-solid fa-user-plus"></i>
				<div class="counter-requests-friend">
					<c:if test="${ not empty sessionScope.user.friendRequestsReceived }">
						<c:set var="numberOfFriendRequests" value="${ fn:length(sessionScope.user.friendRequestsReceived) }"/>
						<span class="counter-requests">
							<c:choose>
								<c:when test="${ numberOfFriendRequests ge 99 }">
									99+
								</c:when>
								<c:otherwise>
									${ numberOfFriendRequests }
								</c:otherwise>
							</c:choose>
						</span>
					</c:if>
				</div>
			</div>

			<div class="requests-game-show">
				<i class="fa-regular fa-envelope fa-lg"></i>
				<div class="counter-requests-game">
					<c:if test="${ not empty sessionScope.user.gameRequestsReceived }">
						<c:set var="numberOfGameRequests" value="${ fn:length(sessionScope.user.gameRequestsReceived) }"/>
						<span class="counter-requests">
							<c:choose>
								<c:when test="${ numberOfGameRequests ge 99 }">
									99+
								</c:when>
								<c:otherwise>
									${ numberOfGameRequests }
								</c:otherwise>
							</c:choose>
						</span>
					</c:if>
				</div>
			</div>

			<div>
				<a href="<c:url value="/profile"/>">
					<img src="${ pageContext.request.contextPath }/assets/img/users/${ sessionScope.user.profilePicture.name }" alt="Photo de profil">
				</a>
			</div>

			<div>
				<a href="<c:url value="/disconnection"/>"><i class="fa-solid fa-arrow-right-from-bracket"></i></a>
			</div>

		</c:otherwise>

	</c:choose>
</nav>