<section id="section-requests-game">

	<c:set var="numberOfGameRequests" value="${ fn:length(sessionScope.user.gameRequestsReceived) }"/>
	<c:set var="numberOfGameRequestsToShowPerPage" value="3"/>
	<c:set var="currentGameRequestsPage" value="1"/>

	<input type="hidden" id="requests-game-number" name="requests-game-number" value="${ numberOfGameRequests }">
	<input type="hidden" id="requests-game-page-items-number" name="requests-game-page-items-number" value="${ numberOfGameRequestsToShowPerPage }">
	<input type="hidden" id="requests-game-page-current" name="requests-game-page-current" value="${ currentGameRequestsPage }">

	<div class="requests-game-header">
		<h2>Demandes de jeu</h2>
		<div class="requests-game-hide">
			<i class="fa-solid fa-xmark fa-lg"></i>
		</div>
	</div>

	<div class="requests-game-body">

		<p class="${ (numberOfGameRequests gt 0)?'search-request-game':'search-request-game hide' }">
			<input type="search" id="search-request-game-login" name="search-request-game-login">
		</p>

		<form method="post" action="game-request" id="form-requests-game">
			<c:choose>
				<c:when test="${ numberOfGameRequests gt 0 }">
					<c:forEach items="${ sessionScope.user.gameRequestsReceived }" var="gameRequest" varStatus="status" 
						begin="${ (currentGameRequestsPage - 1) * numberOfGameRequestsToShowPerPage }" 
						end="${ ((currentGameRequestsPage - 1) * numberOfGameRequestsToShowPerPage) + numberOfGameRequestsToShowPerPage - 1 }">
						<p>
							<img src="${ pageContext.request.contextPath }/assets/img/users/${ gameRequest.sender.profilePicture.name }" alt="Photo de profil">
							<input type="text" id="request-game-sender-${ status.count }" name="request-game-sender" value="${ gameRequest.sender.login }">
							<button class="btn-accept-request-game" name="action" value="accept-${ status.count }">Accepter</button>
							<button type="button" class="btn-decline btn-decline-request-game" name="action" value="decline-${ status.count }">Décliner</button>
						</p>
					</c:forEach>
					<c:if test="${ numberOfGameRequests gt numberOfGameRequestsToShowPerPage }">
						<p class="center">
							<c:choose> 
								<c:when test="${ currentGameRequestsPage ne 1 }">
									<span class="requests-game-page-first">
										<i class="fa-solid fa-angles-left fa-sm"></i>&nbsp;
									</span>
								</c:when>
								<c:otherwise>
									<span class="requests-game-page-disabled">
										<i class="fa-solid fa-angles-left fa-sm"></i>&nbsp;
									</span>
								</c:otherwise>
							</c:choose>
							<c:forEach var="i" 
								begin="1" 
								end="${ (numberOfGameRequests mod numberOfGameRequestsToShowPerPage eq 0)?(numberOfGameRequests div numberOfGameRequestsToShowPerPage):(numberOfGameRequests div numberOfGameRequestsToShowPerPage + 1) }">
								<c:choose>
									<c:when test="${ currentGameRequestsPage eq i }">
										<span class="requests-game-page-current">
											<c:out value="${ i }"/>&nbsp;
										</span>
									</c:when>
									<c:otherwise>
										<span class="requests-game-page-target">
											<c:out value="${ i }"/>&nbsp;
										</span>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${ currentGameRequestsPage ne ((numberOfGameRequests mod numberOfGameRequestsToShowPerPage eq 0)?(numberOfGameRequests div numberOfGameRequestsToShowPerPage):(numberOfGameRequests div numberOfGameRequestsToShowPerPage + 1)) }">
									<span class="requests-game-page-last">
										<i class="fa-solid fa-angles-right fa-sm"></i>&nbsp;
									</span>
								</c:when>
								<c:otherwise>
									<span class="requests-game-page-disabled">
										<i class="fa-solid fa-angles-right fa-sm"></i>&nbsp;
									</span>
								</c:otherwise>
							</c:choose>
						</p>
					</c:if>
				</c:when>
				<c:otherwise>
					<p>
						Aucune demande de jeu.
					</p>
				</c:otherwise>
			</c:choose>
		</form>
	</div>

</section>