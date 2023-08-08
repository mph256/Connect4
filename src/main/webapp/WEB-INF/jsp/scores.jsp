<section id="section-scores">

	<c:set var="numberOfItems" value="${ fn:length(scores) }"/>
	<c:set var="numberOfItemsToShowPerPage" value="8"/>
	<c:set var="currentPage" value="1"/>

	<input type="hidden" id="items-number" name="items-number" value="${ numberOfItems }">
	<input type="hidden" id="page-items-number" name="page-items-number" value="${ numberOfItemsToShowPerPage }">
	<input type="hidden" id="page-current" name="page-current" value="${ currentPage }">

	<table>

		<caption>Classement</caption>

		<thead>
			<tr>
				<th>Rang</th>
				<th>Joueur</th>
				<th>Ratio</th>
				<th>Victoires</th>
				<th>Défaites</th>
				<th>Nuls</th>
				<th>Meilleur série de victoires</th>
			</tr>
			<tr class="${ (numberOfItems gt 0)?'tr-search-scores':'tr-search-scores hide' }">
				<th><input type="search" id="search-score-rank" name="search-score-rank"></th>
				<th><input type="search" id="search-score-login" name="search-score-login"></th>
				<th><input type="search" id="search-score-ratio" name="search-score-ratio"></th>
				<th><input type="search" id="search-score-wins" name="search-score-wins"></th>
				<th><input type="search" id="search-score-defeats" name="search-score-defeats"></th>
				<th><input type="search" id="search-score-draws" name="search-score-draws"></th>
				<th><input type="search" id="search-score-streak-win-best" name="search-score-streak-win-best"></th>
			</tr>
		</thead>

		<c:choose>

			<c:when test="${ numberOfItems gt 0 }">

				<tbody class="table-body-scores">
					<c:forEach items="${ scores }" var="score" varStatus="rank" 
						begin="${ (currentPage - 1) * numberOfItemsToShowPerPage }" 
						end="${ ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1 }">

						<c:choose>
							<c:when test="${ sessionScope.user eq score.user }">
								<tr class="tr-user">
							</c:when>
							<c:otherwise>
								<tr>
							</c:otherwise>
						</c:choose>

							<td>${ rank.count }</td>
							<td>
								<img src="${ pageContext.request.contextPath }/assets/img/users/${ score.user.profilePicture.name }" alt="Photo de profil"><span><c:out value="${ score.user.login }"/></span>
							</td>
							<td>
								${ ((score.defeats eq 0)?
								(score.wins):
								((fn:substringAfter((score.wins div score.defeats), ".") eq "0")?
								(fn:substring((score.wins div score.defeats), 0, fn:indexOf((score.wins div score.defeats), "."))):
								(fn:substring((score.wins div score.defeats), 0, fn:indexOf((score.wins div score.defeats), ".") + 3)))) }
							</td>
							<td>${ score.wins }</td>
							<td>${ score.defeats }</td>
							<td>${ score.draws }</td>
							<td>${ score.bestWinStreak }</td>

						</tr>

					</c:forEach>
				</tbody>

				<tfoot class="${ (numberOfItems gt numberOfItemsToShowPerPage)?'table-foot-scores':'table-foot-scores hide'}">
					<tr>
						<td colspan="7">
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

				<tbody class="table-body-scores">
					<tr>
						<td colspan="7">Aucun score.</td>
					</tr>
				</tbody>

				<tfoot class="table-foot-scores hide">
				</tfoot>

			</c:otherwise>

		</c:choose>

	</table>

	<p class="filter-scores">
		<button type="button" class="btn-filter-scores-friends">Amis seulement</button>
	</p>

</section>