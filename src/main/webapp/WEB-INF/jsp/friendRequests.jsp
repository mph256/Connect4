<section id="section-requests-friend">

	<c:set var="numberOfFriendRequests" value="${ fn:length(sessionScope.user.friendRequestsReceived) }"/>
	<c:set var="numberOfFriendRequestsToShowPerPage" value="3"/>
	<c:set var="currentFriendRequestsPage" value="1"/>

	<input type="hidden" id="requests-friend-number" name="requests-friend-number" value="${ numberOfFriendRequests }">
	<input type="hidden" id="requests-friend-page-items-number" name="requests-friend-page-items-number" value="${ numberOfFriendRequestsToShowPerPage }">
	<input type="hidden" id="requests-friend-page-current" name="requests-friend-page-current" value="${ currentFriendRequestsPage }">

	<div class="requests-friend-header">
		<h2>Demandes d'ami</h2>
		<div class="requests-friend-hide">
			<i class="fa-solid fa-xmark fa-lg"></i>
		</div>
	</div>

	<div class="requests-friend-body">

		<p class="${ (numberOfFriendRequests gt 0)?'search-request-friend':'search-request-friend hide' }">
			<input type="search" id="search-request-friend-login" name="search-request-friend-login">
		</p>

		<form method="post" action="friend-request" id="form-requests-friend">
			<c:choose>	
				<c:when test="${ numberOfFriendRequests gt 0 }">
					<c:forEach items="${ sessionScope.user.friendRequestsReceived }" var="friendRequest" varStatus="status" 
						begin="${ (currentFriendRequestsPage - 1) * numberOfFriendRequestsToShowPerPage }" 
						end="${ ((currentFriendRequestsPage - 1) * numberOfFriendRequestsToShowPerPage) + numberOfFriendRequestsToShowPerPage - 1 }">
						<p>
							<img src="${ pageContext.request.contextPath }/assets/img/users/${ friendRequest.sender.profilePicture.name }" alt="Photo de profil">
							<input type="text" id="request-friend-sender-${ status.count }" name="request-friend-sender" value="${ friendRequest.sender.login }">
							<button type="button" class="btn-accept-request-friend" name="action" value="accept-${ status.count }">Accepter</button>
							<button type="button" class="btn-decline btn-decline-request-friend" name="action" value="decline-${ status.count }">Décliner</button>
						</p>
					</c:forEach>
					<c:if test="${ numberOfFriendRequests gt numberOfFriendRequestsToShowPerPage }">
						<p class="center">
							<c:choose> 
								<c:when test="${ currentFriendRequestsPage ne 1 }">
									<span class="requests-friend-page-first">
										<i class="fa-solid fa-angles-left fa-sm"></i>&nbsp;
									</span>
								</c:when>
								<c:otherwise>
									<span class="requests-friend-page-disabled">
										<i class="fa-solid fa-angles-left fa-sm"></i>&nbsp;
									</span>
								</c:otherwise>
							</c:choose>
							<c:forEach var="i" 
								begin="1" 
								end="${ (numberOfFriendRequests mod numberOfFriendRequestsToShowPerPage eq 0)?(numberOfFriendRequests div numberOfFriendRequestsToShowPerPage):(numberOfFriendRequests div numberOfFriendRequestsToShowPerPage + 1) }">
								<c:choose>
									<c:when test="${ currentFriendRequestsPage eq i }">
										<span class="requests-friend-page-current">
											<c:out value="${ i }"/>&nbsp;
										</span>
									</c:when>
									<c:otherwise>
										<span class="requests-friend-page-target">
											<c:out value="${ i }"/>&nbsp;
										</span>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${ currentFriendRequestsPage ne ((numberOfFriendRequests mod numberOfFriendRequestsToShowPerPage eq 0)?(numberOfFriendRequests div numberOfFriendRequestsToShowPerPage):(numberOfFriendRequests div numberOfFriendRequestsToShowPerPage + 1)) }">
									<span class="requests-friend-page-last">
										<i class="fa-solid fa-angles-right fa-sm"></i>&nbsp;
									</span>
								</c:when>
								<c:otherwise>
									<span class="requests-friend-page-disabled">
										<i class="fa-solid fa-angles-right fa-sm"></i>&nbsp;
									</span>
								</c:otherwise>
							</c:choose>
						</p>
					</c:if>
				</c:when>
				<c:otherwise>
					<p>
						Aucune demande d'ami.
					</p>
				</c:otherwise>
			</c:choose>
		</form>
	</div>

</section>