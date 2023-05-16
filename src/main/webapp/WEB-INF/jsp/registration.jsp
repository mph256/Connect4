<main>
	<section id="form-registration">
		<form method="post" action="registration">
			<p>
				<label for="login" class="label-registration">Login: </label>
				<input type="text" id="login" name="login" autofocus>
			</p>
			<p>
				<label for="email" class="label-registration">Email: </label>
				<input type="email" id="email" name="email">
			</p>
			<p>
				<label for="password" class="label-registration">Mot de passe: </label>
				<input type="password" id="password" name="password">
			</p>
			<p>
				<label for="passwordConfirmation" class="label-registration">Confirmation mot de passe: </label>
				<input type="password" id="passwordConfirmation" name="passwordConfirmation">
			</p>
			<p>
				<input type="submit">
			</p>
		</form>
		<c:if test="${ !empty error }">
			<p class="error">
				<c:out value="${ error }"/>
			</p>
		</c:if>
	</section>
</main>