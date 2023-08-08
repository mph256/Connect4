<section id="section-friends-online">

	<c:set var="numberOfOnlineFriends" value="${ fn:length(sessionScope.user.onlineFriends) }"/>
	<c:set var="numberOfOnlineFriendsToShowPerPage" value="3"/>
	<c:set var="currentOnlineFriendsPage" value="1"/>

	<input type="hidden" id="friends-online-number" name="friends-online-number" value="${ numberOfOnlineFriends }">
	<input type="hidden" id="friends-online-page-items-number" name="friends-online-page-items-number" value="${ numberOfOnlineFriendsToShowPerPage }">
	<input type="hidden" id="friends-online-page-current" name="friends-online-page-current" value="${ currentOnlineFriendsPage }">

	<div class="friends-online-header">
		<div class="icon-friends">
			<i class="fa-solid fa-user-group"></i>
		</div>
		<h2 class="friends-online-show">Amis en ligne</h2>
		<div>
			<span class="counter-friends-online">(${ numberOfOnlineFriends })</span>
		</div>
		<div class="friends-online-hide">
			<i class="fa-solid fa-xmark fa-lg"></i>
		</div>
	</div>

	<div class="friends-online-body">

		<p class="${ (numberOfOnlineFriends gt 0)?'search-friend-online':'search-friend-online hide' }">
			<input type="search" id="search-friend-online-login" name="search-friend-online-login">
		</p>

		<form method="post" action="game-request" id="form-friends-online">

			<c:choose>

				<c:when test="${ numberOfOnlineFriends gt 0 }">

					<c:forEach items="${ sessionScope.user.onlineFriends }" var="onlineFriend" varStatus="status" 
						begin="${ (currentOnlineFriendsPage - 1) * numberOfOnlineFriendsToShowPerPage }" 
						end="${ ((currentOnlineFriendsPage - 1) * numberOfOnlineFriendsToShowPerPage) + numberOfOnlineFriendsToShowPerPage - 1 }">

						<c:set var="isRequested" value="${ false }"/>
						<c:set var="continueExecuting" value="${ true }"/>

						<c:forEach items="${ sessionScope.user.gameRequestsSent }" var="gameRequest">
							<c:if test="${ continueExecuting and onlineFriend eq gameRequest.receiver }">
								<c:set var="isRequested" value="${ true }"/>
								<c:set var="continueExecuting" value="${ false }"/>
							</c:if>
						</c:forEach>

						<p>

							<img src="${ pageContext.request.contextPath }/assets/img/users/${ onlineFriend.profilePicture.name }" alt="Photo de profil">
							<input type="text" id="friend-online-${ status.count }" name="friend-online" value="${ onlineFriend.login }" disabled>

							<c:choose>

								<c:when test="${ onlineFriend.inGame }">
									En partie
								</c:when>
			
								<c:when test="${ not sessionScope.user.inGame }">
		
									<c:choose>

										<c:when test="${ isRequested }">
											<button type="button" class="btn-cancel btn-cancel-request-game" name="action" value="cancel-${ status.count }">Annuler</button>
										</c:when>

										<c:otherwise>
											<button type="button" class="btn-send-request-game" name="action" value="send-${ status.count }">Inviter</button>
										</c:otherwise>

									</c:choose>

								</c:when>

							</c:choose>

						</p>

					</c:forEach>
					
					<c:if test="${ numberOfOnlineFriends gt numberOfOnlineFriendsToShowPerPage }">
	
						<p class="center">
							<c:choose> 
								<c:when test="${ currentOnlineFriendsPage ne 1 }">
									<span class="friends-online-page-first">
										<i class="fa-solid fa-angles-left fa-sm"></i>&nbsp;
									</span>
								</c:when>
								<c:otherwise>
									<span class="friends-online-page-disabled">
										<i class="fa-solid fa-angles-left fa-sm"></i>&nbsp;
									</span>
								</c:otherwise>
							</c:choose>
							<c:forEach var="i" 
								begin="1" 
								end="${ (numberOfOnlineFriends mod numberOfOnlineFriendsToShowPerPage eq 0)?(numberOfOnlineFriends div numberOfOnlineFriendsToShowPerPage):(numberOfOnlineFriends div numberOfOnlineFriendsToShowPerPage + 1) }">
								<c:choose>
									<c:when test="${ currentOnlineFriendsPage eq i }">
										<span class="friends-online-page-current">
											<c:out value="${ i }"/>&nbsp;
										</span>
									</c:when>
									<c:otherwise>
										<span class="friends-online-page-target">
											<c:out value="${ i }"/>&nbsp;
										</span>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${ currentOnlineFriendsPage ne ((numberOfOnlineFriends mod numberOfOnlineFriendsToShowPerPage eq 0)?(numberOfOnlineFriends div numberOfOnlineFriendsToShowPerPage):(numberOfOnlineFriends div numberOfOnlineFriendsToShowPerPage + 1)) }">
									<span class="friends-online-page-last">
										<i class="fa-solid fa-angles-right fa-sm"></i>&nbsp;
									</span>
								</c:when>
								<c:otherwise>
									<span class="friends-online-page-disabled">
										<i class="fa-solid fa-angles-right fa-sm"></i>&nbsp;
									</span>
								</c:otherwise>
							</c:choose>
						</p>
	
					</c:if>

				</c:when>

				<c:otherwise>

					<p>
						Aucun ami en ligne.
					</p>

				</c:otherwise>

			</c:choose>
		</form>

	</div>

</section>