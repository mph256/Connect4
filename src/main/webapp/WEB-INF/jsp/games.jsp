<section id="section-games">

	<c:set var="numberOfItems" value="${ fn:length(games) }"/>
	<c:set var="numberOfItemsToShowPerPage" value="5"/>
	<c:set var="currentPage" value="1"/>

	<input type="hidden" id="items-number" name="items-number" value="${ numberOfItems }">
	<input type="hidden" id="page-items-number" name="page-items-number" value="${ numberOfItemsToShowPerPage }">
	<input type="hidden" id="page-current" name="page-current" value="${ currentPage }">

	<table>

		<caption>Parties</caption>

		<thead>
			<tr>
				<th>Partie</th>
				<th>Type</th>
				<th>Joueur 1</th>
				<th>Joueur 2</th>
				<th>Début</th>
				<th></th>
			</tr>
			<tr class="${ (numberOfItems gt 0)?'tr-search-games':'tr-search-games hide' }">
				<th><input type="search" id="search-game-id" name="search-game-id"></th>
				<th><input type="search" id="search-game-type" name="search-game-type"></th>
				<th><input type="search" id="search-game-player1" name="search-game-player1"></th>
				<th><input type="search" id="search-game-player2" name="search-game-player2"></th>
				<th><input type="search" id="search-game-start" name="search-game-start"></th>
				<th></th>
			</tr>
		</thead>

		<c:choose>

			<c:when test="${ numberOfItems gt 0 }">

				<tbody class="table-body-games">
					<c:forEach items="${ games }" var="game" varStatus="status" 
						begin="${ (currentPage - 1) * numberOfItemsToShowPerPage }"
						end="${ ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1 }">
						<tr>
							<td>${ game.id }</td>
							<td>${ ("RANKED" eq game.type)?"Classée":"Amicale" }</td>
							<td>
								<img src="${ pageContext.request.contextPath }/assets/img/users/${ game.player1.profilePicture.name }" alt="Photo de profil"><span><c:out value="${ game.player1.login }"/></span>
							</td>
							<td>
								<img src="${ pageContext.request.contextPath }/assets/img/users/${ game.player2.profilePicture.name }" alt="Photo de profil"><span><c:out value="${ game.player2.login }"/></span>
							</td>
							<td>
								<fmt:parseDate value="${ game.start }" type="both" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"/>
								<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${ parsedDateTime }"/>
							</td>
							<td><a href="<c:url value="/game"><c:param name="id" value="${ game.id }"/></c:url>">Observer</a></td>
						</tr>
					</c:forEach>
				</tbody>

				<tfoot class="${ (numberOfItems gt numberOfItemsToShowPerPage)?'table-foot-games':'table-foot-games hide' }">
					<tr>
						<td colspan="6">
							<c:choose> 
								<c:when test="${ currentPage ne 1 }">
									<span class="page-first">
										<i class="fa-solid fa-angles-left fa-sm"></i>
									</span>
								</c:when>
								<c:otherwise>
									<span class="page-disabled">
										<i class="fa-solid fa-angles-left fa-sm"></i>
									</span>
								</c:otherwise>
							</c:choose>
							<c:forEach var="i" 
								begin="1" 
								end="${ (numberOfItems mod numberOfItemsToShowPerPage eq 0)?(numberOfItems div numberOfItemsToShowPerPage):(numberOfItems div numberOfItemsToShowPerPage + 1) }">
								<c:choose>
									<c:when test="${ currentPage eq i }">
										<span class="page-current">
											<c:out value="${ i }"/>
										</span>
									</c:when>
									<c:otherwise>
										<span class="page-target">
											<c:out value="${ i }"/>
										</span>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${ currentPage ne ((numberOfItems mod numberOfItemsToShowPerPage eq 0)?(numberOfItems div numberOfItemsToShowPerPage):(numberOfItems div numberOfItemsToShowPerPage + 1)) }">
									<span class="page-last">
										<i class="fa-solid fa-angles-right fa-sm"></i>
									</span>
								</c:when>
								<c:otherwise>
									<span class="page-disabled">
										<i class="fa-solid fa-angles-right fa-sm"></i>
									</span>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tfoot>

			</c:when>

			<c:otherwise>

				<tbody class="table-body-games">
					<tr>
						<td colspan="5">Aucune partie en cours.</td>
					</tr>
				</tbody>

				<tfoot class="table-foot-games hide">
				</tfoot>

			</c:otherwise>

		</c:choose>

	</table>

</section>