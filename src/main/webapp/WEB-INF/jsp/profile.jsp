<section id="section-profile">

	<div>
		<img src="${ pageContext.request.contextPath }/assets/img/users/${ sessionScope.user.profilePicture.name }" alt="Photo de profil">
	</div>

	<form method="post" action="profile" enctype='multipart/form-data' id="form-profile">
		<p>
			<label for="login" class="label-profile">Login: </label>
			<input type="text" class="input-profile" id="login" name="login" value="${ sessionScope.user.login }" disabled>
		</p>
		<p>
			<label for="email" class="label-profile">Email: </label>
			<input type="email" class="input-profile" id="email" name="email" value="${ sessionScope.user.email }" disabled>
		</p>
		<p>
			<label for="password" class="label-profile">Mot de passe: </label>
			<input type="password" class="input-profile input-password" id="password" name="password" value="${ sessionScope.user.password }" disabled>
			<button type="button" class="btn-show-password"><i class="fa-regular fa-eye"></i></button>
		</p>
		<p>
			<label for="date-registration" class="label-profile">Date d'inscription: </label>
			<input type="date" class="input-profile" id="date-registration" name="date-registration" value="${ sessionScope.user.registrationDate }" disabled>
		</p>
		<p>
			<label for="date-connection-last" class="label-profile">Dernière connexion: </label>
			<input type="datetime-local" class="input-profile" id="date-connection-last" name="date-connection-last" value="${ sessionScope.user.lastConnection }" disabled>
		</p>
		<p>
			<input type="hidden" id="action" name="action" value="delete">
			<button type="button" class="btn-update-account">Modifier</button>
			<button class="btn-delete btn-delete-account" >Supprimer</button>
		</p>
	</form>

	<c:if test="${ !empty error }">
		<p class="error">
			<c:out value="${ error }"/>
		</p>
	</c:if>

	<%@ include file="friends.jsp"%>

</section>