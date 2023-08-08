<section id="section-users">

	<c:set var="numberOfItems" value="${ fn:length(users) }"/>
	<c:set var="numberOfItemsToShowPerPage" value="5"/>
	<c:set var="currentPage" value="1"/>

	<input type="hidden" id="items-number" name="items-number" value="${ numberOfItems }">
	<input type="hidden" id="page-items-number" name="page-items-number" value="${ numberOfItemsToShowPerPage }">
	<input type="hidden" id="page-current" name="page-current" value="${ currentPage }">

	<table>

		<caption>Utilisateurs</caption>

		<thead>
			<tr>
				<th>Utilisateur</th>
				<th></th>
			</tr>
			<tr class="${ (numberOfItems gt 0)?'tr-search-users':'tr-search-users hide' }">
				<th><input type="search" id="search-user-login" name="search-user-login"></th>
				<th></th>
			</tr>
		</thead>

		<c:choose>

			<c:when test="${ numberOfItems gt 0 }">

				<tbody class="table-body-users">
					<c:forEach items="${ users }" var="user" varStatus="status" 
						begin="${ (currentPage - 1) * numberOfItemsToShowPerPage }" 
						end="${ ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1 }">

						<c:set var="isRequested" value="${ false }"/>
						<c:set var="continueExecuting" value="${ true }"/>

						<c:forEach items="${ sessionScope.user.friendRequestsSent }" var="friendRequest">
							<c:if test="${ continueExecuting and user.login eq friendRequest.receiver.login }">
								<c:set var="isRequested" value="${ true }"/>
								<c:set var="continueExecuting" value="${ false }"/>
							</c:if>
						</c:forEach>

						<tr>
							<td>
								<img src="${ pageContext.request.contextPath }/assets/img/users/${ user.profilePicture.name }" alt="Photo de profil"><input type="text" id="user-${ status.count }" name="user" value="${ user.login }" disabled>
							</td>
							<c:choose>
								<c:when test="${ isRequested }">
									<td>
										<button type="button" class="btn-cancel btn-cancel-request-friend" name="action" value="cancel-${ status.count }">Annuler</button>
									</td>
								</c:when>
								<c:otherwise>
									<td>
										<button type="button" class="btn-send-request-friend" name="action" value="send-${ status.count }">Inviter</button>
									</td>
								</c:otherwise>
							</c:choose>
						</tr>

					</c:forEach>
				</tbody>

				<tfoot class="${ (numberOfItems gt numberOfItemsToShowPerPage)?'table-foot-users':'table-foot-users hide' }">
					<tr>
						<td colspan="2">
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

				<tbody class="table-body-users">
					<tr>
						<td colspan="1">Aucun utilisateur à ajouter.</td>
					</tr>
				</tbody>

				<tfoot class="table-foot-users hide">
				</tfoot>

			</c:otherwise>

		</c:choose>

	</table>

</section>