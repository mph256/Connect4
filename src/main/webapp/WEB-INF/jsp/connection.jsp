<section id="section-connection">

	<form method="post" action="connection">
		<p>
			<label for="login" class="label-connection">Login: </label>
			<input type="text" id="login" name="login" value="${ login }" autofocus>
		</p>
		<p>
			<label for="password" class="label-connection">Mot de passe: </label>
			<input type="password" class="input-password" id="password" name="password">
			<button type="button" class="btn-show-password"><i class="fa-regular fa-eye"></i></button>
		</p>
		<p>
			<button>Valider</button>
		</p>
	</form>

	<p>
		Vous n'avez pas encore de compte? <a href="<c:url value="/registration"/>">Créez-en un!</a>
	</p>

	<c:if test="${ not empty error }">
		<p class="error">
			<c:out value="${ error }"/>
		</p>
	</c:if>

</section>