<section id="section-game">

	<input type="hidden" id="gameId" name="gameId" value="${ game.id }">

	<div>

		<p>
			<img src="${ pageContext.request.contextPath }/assets/img/users/${ game.player1.profilePicture.name }" alt="Photo de profil">
			<span class="player-1" id="player-1">
				<c:out value="${ game.player1.login }"/>
			</span>
		</p>
		
		<p class="${ (game.player1.score.winStreak gt 0)?'streak-win-1':'streak-win-1 hide' }">
			Série de victoires: <span id="streak-win-1">${ game.player1.score.winStreak }</span>
		</p>

		<p>

			<input type="hidden" id="user-1" name="user-1" value="${ game.player1.login }">

			<c:if test="${ ('OVER' eq game.state) and (sessionScope.user eq game.player2) }">

				<c:set var="isFriend" value="${ false }"/>
				<c:set var="continueExecuting" value="${ true }"/>

				<c:forEach items="${ sessionScope.user.friends }" var="friend">
					<c:if test="${ continueExecuting and (game.player1 eq friend) }">
						<c:set var="isFriend" value="${ true }"/>
						<c:set var="continueExecuting" value="${ false }"/>
					</c:if>
				</c:forEach>

				<c:if test="${ not isFriend }">

					<c:set var="requestAlreadyReceived" value="${ false }"/>
					<c:set var="continueExecuting" value="${ true }"/>

					<c:forEach items="${ sessionScope.user.friendRequestsReceived }" var="friendRequest">
						<c:if test="${ continueExecuting and (game.player1 eq friendRequest.sender) }">
							<c:set var="requestAlreadyReceived" value="${ true }"/>
							<c:set var="continueExecuting" value="${ false }"/>
						</c:if>
					</c:forEach>

					<c:if test="${ not requestAlreadyReceived }">

						<c:set var="requestAlreadySent" value="${ false }"/>
						<c:set var="continueExecuting" value="${ true }"/>

						<c:forEach items="${ sessionScope.user.friendRequestsSent }" var="friendRequest">
							<c:if test="${ continueExecuting and (game.player1 eq friendRequest.receiver) }">
								<c:set var="requestAlreadySent" value="${ true }"/>
								<c:set var="continueExecuting" value="${ false }"/>
							</c:if>
						</c:forEach>

						<c:choose>
							<c:when test="${ requestAlreadySent }">
								<button type="button" class="btn-cancel btn-cancel-request-friend" name="action" value="cancel-1">
									Annuler
								</button>
							</c:when>
							<c:otherwise>
								<button type="button" class="btn-send-request-friend" name="action" value="send-1">
									Inviter
								</button>
							</c:otherwise>
						</c:choose>

					</c:if>

				</c:if>

			</c:if>

		</p>

	</div>

	<p class="state">
		Partie 
		<c:choose>
			<c:when test="${ 'RANKED' eq game.type }">
				classée 
			</c:when>
			<c:otherwise>
				amicale 
			</c:otherwise>
		</c:choose>
		<span id="state">
			<c:choose>
				<c:when test="${ 'PENDING' eq game.state}">
					en attente
				</c:when>
				<c:when test="${ 'CANCELED' eq game.state}">
					annulée
				</c:when>
				<c:when test="${ 'RUNNING' eq game.state}">
					en cours
				</c:when>
				<c:when test="${ 'OVER' eq game.state}">
					terminée
				</c:when>
			</c:choose>
		</span>
	</p>

	<p class="${ ('OVER' eq game.state)?'winner':'winner hide' }">

		<c:choose>

			<c:when test="${ ('OVER' eq game.state) and (not empty game.winner) }">
				Vainqueur: 
				<span class="${ (game.winner eq game.player1)?'player-1':'player-2' }" id="winner">
					<c:out value="${ game.winner.login }"/>
				</span>
			</c:when>

			<c:when test="${ ('OVER' eq game.state) and (empty game.winner) }">
				<span id="winner">Match nul</span>
			</c:when>

			<c:otherwise>
				<span id="winner"></span>
			</c:otherwise>

		</c:choose>

	</p>

	<p class="turn">
		Tour: <span class="${ (0 ne game.turn mod 2)?'player-1':'player-2' }" id="turn">${ game.turn }</span>
	</p>

	<p class="timer">
		Fin du tour dans: <span id="timer">${ game.timer }</span>
	</p>

	<table id="table-game">
		<thead>
			<tr>
				<th class="th-rotate-right"></th>
				<c:forEach items="${ game.board.squares[0] }" varStatus="column">
					<th>
						<button type="button" class="btn-drop-token" name="action" value="drop-${ column.count - 1}">
							<i class="fa-solid fa-arrow-down"></i>
						</button>
					</th>
				</c:forEach>
				<th class="th-rotate-left"></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${ game.board.squares }" varStatus="status">
				<tr>
					<c:if test="${ 1 eq status.count }">
						<td rowspan="${ game.board.rows }" class="td-rotate-right">
							<button type="button" class="btn-rotate-right" name="action" value="rotate-right">
								<i class="fa-solid fa-arrow-rotate-right"></i>
							</button>
						</td>
					</c:if>
					<c:forEach items="${ game.board.squares[status.count - 1] }" var="square">
						<c:choose>
							<c:when test="${ not empty square.token }">
								<td><span class="${ ('RED' eq square.token.color)?'token-red':'token-yellow' }"></span></td>
							</c:when>
							<c:otherwise>
								<td><span class="token-none"></span></td>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${ 1 eq status.count }">
						<td rowspan="${ game.board.rows }" class="td-rotate-left">
							<button type="button" class="btn-rotate-left" name="action" value="rotate-left">
								<i class="fa-solid fa-arrow-rotate-left"></i>
							</button>
						</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div>
	
		<p>
			<span class="player-2" id="player-2">
				<c:if test="${ not empty game.player2 }">
					<c:out value="${ game.player2.login }"/>
				</c:if>
			</span>
			<c:if test="${ not empty game.player2 }">
				<img src="${ pageContext.request.contextPath }/assets/img/users/${ game.player2.profilePicture.name }" alt="Photo de profil">
			</c:if>
		</p>
		
		<p class="${ ((not empty game.player2) and (game.player2.score.winStreak gt 0))?'streak-win-2':'streak-win-2 hide' }">
			Série de victoires: <span id="streak-win-2">${ game.player2.score.winStreak }</span>
		</p>

		<p>

			<input type="hidden" id="user-2" name="user-2" value="${ game.player2.login }">

			<c:if test="${ ('OVER' eq game.state) and (sessionScope.user eq game.player1) }">

				<c:set var="isFriend" value="${ false }"/>
				<c:set var="continueExecuting" value="${ true }"/>

				<c:forEach items="${ sessionScope.user.friends }" var="friend">
					<c:if test="${ continueExecuting and (game.player2 eq friend) }">
						<c:set var="isFriend" value="${ true }"/>
						<c:set var="continueExecuting" value="${ false }"/>
					</c:if>
				</c:forEach>

				<c:if test="${ not isFriend }">

					<c:set var="requestAlreadyReceived" value="${ false }"/>
					<c:set var="continueExecuting" value="${ true }"/>

					<c:forEach items="${ sessionScope.user.friendRequestsReceived }" var="friendRequest">
						<c:if test="${ continueExecuting and (game.player2 eq friendRequest.sender) }">
							<c:set var="requestAlreadyReceived" value="${ true }"/>
							<c:set var="continueExecuting" value="${ false }"/>
						</c:if>
					</c:forEach>

					<c:if test="${ not requestAlreadyReceived }">

						<c:set var="requestAlreadySent" value="${ false }"/>
						<c:set var="continueExecuting" value="${ true }"/>

						<c:forEach items="${ sessionScope.user.friendRequestsSent }" var="friendRequest">
							<c:if test="${ continueExecuting and (game.player2 eq friendRequest.receiver) }">
								<c:set var="requestAlreadySent" value="${ true }"/>
								<c:set var="continueExecuting" value="${ false }"/>
							</c:if>
						</c:forEach>

						<c:choose>
							<c:when test="${ requestAlreadySent }">
								<button type="button" class="btn-cancel btn-cancel-request-friend" name="action" value="cancel-2">
									Annuler
								</button>
							</c:when>
							<c:otherwise>
								<button type="button" class="btn-send-request-friend" name="action" value="send-2">
									Inviter
								</button>
							</c:otherwise>
						</c:choose>

					</c:if>

				</c:if>

			</c:if>

		</p>

	</div>

</section>