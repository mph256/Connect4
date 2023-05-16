<main>
	<section id="form-connection">
		<form method="post" action="">
			<p>
				<label for="login" class="label-connection">Login: </label>
				<input type="text" name="login" id="login" autofocus>
			</p>
			<p>
				<label for="password" class="label-connection">Mot de passe: </label>
				<input type="password" name="password" id="password">
			</p>
			<p>
				<input type="submit">
			</p>
		</form>
		<p>
			Vous n'avez pas encore de compte ? <a href="<c:url value="/registration"/>">Créez-en un!</a>
		</p>
		<c:if test="${ !empty error }">
			<p class="error">
				<c:out value="${ error }"/>
			</p>
		</c:if>
	</section>
</main>