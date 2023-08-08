<c:set var="numberOfItems" value="${ fn:length(sessionScope.user.friends) }"/>
<c:set var="numberOfItemsToShowPerPage" value="5"/>
<c:set var="currentPage" value="1"/>

<input type="hidden" id="items-number" name="items-number" value="${ numberOfItems }">
<input type="hidden" id="page-items-number" name="page-items-number" value="${ numberOfItemsToShowPerPage }">
<input type="hidden" id="page-current" name="page-current" value="${ currentPage }">

<table>

	<caption>Amis <span class="counter-friends">(${ fn:length(sessionScope.user.friends) })</span></caption>
	
	<thead>
		<tr class="table-head-friends">
			<th>Ami</th>
			<th>Dernière connexion</th>
			<c:choose>
				<c:when test="${ (numberOfItems gt 0) }">
					<th rowspan="2">
				</c:when>
				<c:otherwise>
					<th>
				</c:otherwise>
			</c:choose>
				<a href="<c:url value="/users"/>">Ajouter un ami</a>
			</th>
		</tr>
		<tr class="${ (numberOfItems gt 0)?'tr-search-friends':'tr-search-friends hide' }">
			<th><input type="search" id="search-friend-login" name="search-friend-login"></th>
			<th><input type="search" id="search-friend-connection-last" name="search-friend-connection-last"></th>
		</tr>
	</thead>

	<c:choose>

		<c:when test="${ numberOfItems gt 0 }">

			<tbody class="table-body-friends">
				<c:forEach items="${ sessionScope.user.friends }" var="friend" varStatus="status" 
					begin="${ (currentPage - 1) * numberOfItemsToShowPerPage }" 
					end="${ ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1 }">
					<tr>
						<td>
							<img src="${ pageContext.request.contextPath }/assets/img/users/${ friend.profilePicture.name }" alt="Photo de profil"><input type="text" id="friend-${ status.count }" name="friend-${ status.count }" value="${ friend.login }" disabled>
						</td>
						<td>
							<fmt:parseDate value="${ friend.lastConnection }" type="both" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"/>
							<fmt:formatDate pattern="dd/MM/yyyy" value="${ parsedDateTime }"/>
						<td><button type="button" class="btn-remove btn-remove-friend" name="action" value="remove-${ status.count }">Supprimer</button></td>
					</tr>
				</c:forEach>
			</tbody>

			<tfoot class="${ (numberOfItems gt numberOfItemsToShowPerPage)?'table-foot-friends':'table-foot-friends hide' }">
				<tr>
					<td colspan="3">
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
						<c:forEach var="i" begin="1" end="${ (numberOfItems mod numberOfItemsToShowPerPage eq 0)?(numberOfItems div numberOfItemsToShowPerPage):(numberOfItems div numberOfItemsToShowPerPage + 1) }">
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

			<tbody class="table-body-friends">
				<tr>
					<td colspan="2">
						Aucun ami.
					</td>
				</tr>
			</tbody>

			<tfoot class="table-foot-friends hide">
			</tfoot>

		</c:otherwise>

	</c:choose>

</table>