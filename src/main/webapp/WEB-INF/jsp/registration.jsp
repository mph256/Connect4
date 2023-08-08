<section id="section-registration">

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
			<input type="password" class="input-password" id="password" name="password">
			<button type="button" class="btn-show-password"><i class="fa-regular fa-eye"></i></button>
		</p>
		<p>
			<label for="password-confirmation" class="label-registration">Confirmation mot de passe: </label>
			<input type="password" id="password-confirmation" name="password-confirmation">
		</p>
		<p>
			<button>Valider</button>
		</p>
	</form>

	<c:if test="${ not empty error }">
		<p class="error">
			<c:out value="${ error }"/>
		</p>
	</c:if>

</section>