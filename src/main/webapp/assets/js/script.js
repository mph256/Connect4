document.addEventListener("DOMContentLoaded", async () => {

	function loadShowPasswordListener() {

		const buttonShowPassword = document.querySelector(".btn-show-password");

		if(buttonShowPassword !== null) {

			buttonShowPassword.addEventListener("click", () => {

				const inputPassword = document.querySelector("#password");
				const inputPasswordConfirmation = document.querySelector("#password-confirmation");

				inputPassword.type = "text";

				if(inputPasswordConfirmation !== null)
					inputPasswordConfirmation.type = "text"; 

				setTimeout(() => {

					inputPassword.type = "password";

					if(inputPasswordConfirmation !== null)
						inputPasswordConfirmation.type = "password";

				}, 3000);

			});

		}

	}

	async function getUser() {

		const response = await fetch("/Connect4/user", {
			headers: { "Content-Type": "application/json" }
		});

		user = await response.json();

	}

	function loadShowFriendRequestsListener() {

		const buttonShowFriendRequests = document.querySelector(".requests-friend-show");

		if(buttonShowFriendRequests !== null) {

			buttonShowFriendRequests.addEventListener("click", () => {

				showFriendRequests();
				hideGameRequests();
				hideOnlineFriends();

			});

		}

	}

	function showFriendRequests() {
		document.querySelector("#section-requests-friend").style.display = "block";
	}

	function loadHideFriendRequestsListener() {

		const buttonHideGameRequests = document.querySelector(".requests-friend-hide");

		if(buttonHideGameRequests !== null) {

			buttonHideGameRequests.addEventListener("click", () => {
				hideFriendRequests();
			});

		}

	}

	function hideFriendRequests() {
		document.querySelector("#section-requests-friend").style.display = "none";
	}

	async function getFriendRequests(targetPage) {

		const sectionFriendRequests = document.querySelector("#section-requests-friend");

		if(sectionFriendRequests !== null) {

			const response = await fetch("/Connect4/friend-requests?state=pending&page=" 
				+ ((page.indexOf("game?id=") !== -1)?page.substring(0, page.indexOf("?")):page), {
				headers: { "Content-Type": "application/json" }
			});

			const sectionGame = document.querySelector("#section-game");

			if(sectionGame !== null) {

				const object = await response.json();

				if(object.player !== undefined) {

					friendRequests = object.friendRequests;

					updatePlayer(object.player);

				} else
					friendRequests = object;

			} else
				friendRequests = await response.json();

			if("" !== inputSearchFriendRequestByLogin)
				searchFriendRequest();
			else
				updateFriendRequests(friendRequests, targetPage);

		}

	}

	function updateFriendRequests(results, targetPage) {

		if(targetPage === null)
			targetPage = document.querySelector("#requests-friend-page-current").value;
		else
			document.querySelector("#requests-friend-page-current").value = targetPage;

		const numberOfItemsToShowPerPage = document.querySelector("#requests-friend-page-items-number").value;
		const currentPage = targetPage;

		const formFriendRequests = document.querySelector("#form-requests-friend");

		let form = "<form method='post' action='friend-request' id='form-requests-friend'>";

		if(results.length > 0) {

			let i = 1;

			document.querySelector(".search-request-friend").classList.remove("hide");

			for(let friendRequest of results) {
	
				if(((i - 1) >= (currentPage - 1) * numberOfItemsToShowPerPage) && ((i - 1) <= ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1)) {

					form += "<p>"
						+ "<img src='/Connect4/assets/img/users/" + friendRequest.sender.profilePicture + "' alt='Photo de profil'>"
						+ "<input type='text' id='request-friend-sender-" + i + "' name='request-friend-sender' value='" + friendRequest.sender.login + "'>"
						+ "<button type='button' class='btn-accept-request-friend' name='action' value='accept-" + i + "'>Accepter</button>"
						+ "<button type='button' class='btn-decline btn-decline-request-friend' name='action' value='decline-" + i + "'>D&eacute;cliner</button>"
						+ "</p>";
	
				}

				i++;

			}

			if(results.length > numberOfItemsToShowPerPage) {

				form += "<p class='center'>";

				if(currentPage != 1)
					form += "<span class='requests-friend-page-first'>";
				else
					form += "<span class='requests-friend-page-disabled'>";

				form += "<i class='fa-solid fa-angles-left fa-sm'></i>"
					+ "</span>"
					+ "&nbsp";

				for(i = 1; i <= Math.ceil(results.length/numberOfItemsToShowPerPage); i++) {

					if(currentPage == i)
					 	form += "<span class='requests-friend-page-current'>";
					else
						form += "<span class='requests-friend-page-target'>";

					form += i
						+ "</span>"
						+ "&nbsp;";

				}

				if(currentPage != Math.ceil(results.length/numberOfItemsToShowPerPage))
				 	form += "<span class='requests-friend-page-last'>";
				else
					form += "<span class='requests-friend-page-disabled'>";

				form += "<i class='fa-solid fa-angles-right fa-sm'></i>"
					+ "</span>";
		
				form += "</p>"; 

			}

			document.querySelector(".counter-requests-friend").style.display = "block";

			if(results.length >= 99)
				document.querySelector(".counter-requests-friend").innerHTML = "<span class='counter-requests'>99+</span>";
			else
				document.querySelector(".counter-requests-friend").innerHTML = "<span class='counter-requests'>" + friendRequests.length + "</span>";

		} else {

			form += "<p class='center'>";

			if("" !== inputSearchFriendRequestByLogin) {

				document.querySelector(".search-request-friend").classList.remove("hide");

				form += "Aucun r&eacute;sultat.";

			} else {

				document.querySelector(".search-request-friend").classList.add("hide");

				form += "Aucune demande d'ami.";

			}

			form += "</p>";

			document.querySelector(".counter-requests-friend").style.display = "none";
			document.querySelector(".counter-requests-friend").innerHTML = "";

		}

		form += "</form>";

		formFriendRequests.innerHTML = form;

		loadFriendRequestsPaginationListeners();
		loadAcceptFriendRequestListeners();
		loadDeclineFriendRequestListeners();

	}

	function loadSearchFriendRequestListeners() {

		getFriendRequests(1);

		const searchFriendRequestByLogin = document.querySelector("#search-request-friend-login");

		searchFriendRequestByLogin.addEventListener("keyup", () => {

			inputSearchFriendRequestByLogin = searchFriendRequestByLogin.value;

			searchFriendRequest();

		});

		searchFriendRequestByLogin.addEventListener("search", () => {

			inputSearchFriendRequestByLogin = searchFriendRequestByLogin.value;

			searchFriendRequest();

		});

	}

	function searchFriendRequest() {

		resultsSearchFriendRequest = friendRequests;

		if("" !== inputSearchFriendRequestByLogin)
			resultsSearchFriendRequest = resultsSearchFriendRequest.filter(x => (x.sender.login).startsWith(inputSearchFriendRequestByLogin));

		updateFriendRequests(resultsSearchFriendRequest, 1);

	}

	async function loadSendFriendRequestListeners() {

		const buttonsSendFriendRequest = document.querySelectorAll(".btn-send-request-friend");

		if(buttonsSendFriendRequest !== null) {

			buttonsSendFriendRequest.forEach(buttonSendFriendRequest => {

				buttonSendFriendRequest.addEventListener("click", async (event) => {

					const action = "send";
					const user = document.querySelector("#user-" + event.target.value.split("-")[1]).value;

					let data = {
						action: action,
						receiver: user,
						page: ((page.indexOf("game?id=") !== -1)?page.substring(0, page.indexOf("?")):page)
					};

					const response = await fetch("/Connect4/friend-request", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					const sectionGame = document.querySelector("#section-game");
					const sectionUsers = document.querySelector("#section-users");

					if(sectionGame !== null) {

						const player = await response.json();

						if(player !== null)
							updatePlayer(player);

					}

					if(sectionUsers !== null) {

						users = await response.json();

						if("" !== inputSearchUserByLogin)
							searchUser();
						else
							updateUsers(users, null);

					}

				});

			});

		}

	}

	async function loadCancelFriendRequestListeners() {

		const buttonsCancelFriendRequest = document.querySelectorAll(".btn-cancel-request-friend");

		if(buttonsCancelFriendRequest !== null) {

			buttonsCancelFriendRequest.forEach(buttonCancelFriendRequest => {

				buttonCancelFriendRequest.addEventListener("click", async (event) => {

					const action = "cancel";
					const user = document.querySelector("#user-" + event.target.value.split("-")[1]).value;

					let data = {
						action: action,
						receiver: user,
						page: ((page.indexOf("game?id=") !== -1)?page.substring(0, page.indexOf("?")):page)
					};

					const response = await fetch("/Connect4/friend-request", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					const sectionGame = document.querySelector("#section-game");
					const sectionUsers = document.querySelector("#section-users");

					if(sectionGame !== null) {

						const player = await response.json();

						if(player !== null)
							updatePlayer(player);

					}

					if(sectionUsers !== null) {

						users = await response.json();

						if("" !== inputSearchUserByLogin)
							searchUser();
						else
							updateUsers(users, null);

					}

				});

			});

		}

	}

	async function loadAcceptFriendRequestListeners() {

		const buttonsAcceptFriendRequest = document.querySelectorAll(".btn-accept-request-friend");

		if(buttonsAcceptFriendRequest !== null) {

			buttonsAcceptFriendRequest.forEach(buttonAcceptFriendRequest => {

				buttonAcceptFriendRequest.addEventListener("click", async (event) => {

					const action = "accept";
					const sender = document.querySelector("#request-friend-sender-" + event.target.value.split("-")[1]).value;

					let data = {
						action: action,
						sender: sender,
						page: ((page.indexOf("game?id=") !== -1)?page.substring(0, page.indexOf("?")):page)

					};

					const response = await fetch("/Connect4/friend-request", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					const object = await response.json();

					friendRequests = object.friendRequests;

					friends = object.friends;
					onlineFriends = friends.filter(x => x.isOnline);

					if("" !== inputSearchFriendRequestByLogin)
						searchFriendRequest();
					else
						updateFriendRequests(friendRequests, null);

					if("" !== inputSearchOnlineFriendByLogin)
						searchOnlineFriend();
					else
						updateOnlineFriends(onlineFriends, null);

					const sectionGame = document.querySelector("#section-game");
					const sectionProfile = document.querySelector("#section-profile");

					if(sectionGame !== null) {

						if(object.player !== undefined)
							updatePlayer(object.player);

					}

					if(sectionProfile !== null) {

						if(("" !== inputSearchFriendByLogin)
							|| ("" !== inputSearchFriendByLastConnection))
							searchFriend();
						else
							updateFriends(friends, null);

					}

				});

			});

		}

	}

	async function loadDeclineFriendRequestListeners() {

		const buttonsDeclineFriendRequest = document.querySelectorAll(".btn-decline-request-friend");

		if(buttonsDeclineFriendRequest !== null) {

			buttonsDeclineFriendRequest.forEach(buttonDeclineFriendRequest => {

				buttonDeclineFriendRequest.addEventListener("click", async (event) => {

					const action = "decline";
					const sender = document.querySelector("#request-friend-sender-" + event.target.value.split("-")[1]).value;

					let data = {
						action: action,
						sender: sender,
						page: ((page.indexOf("game?id=") !== -1)?page.substring(0, page.indexOf("?")):page)

					};

					const response = await fetch("/Connect4/friend-request", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					const sectionGame = document.querySelector("#section-game");

					if(sectionGame !== null) {

						const object = await response.json();

						if(object.player !== undefined) {

							friendRequests = object.friendRequests;

							updatePlayer(object.player);

						} else
							friendRequests = object;

					} else
						friendRequests = await response.json();

					if("" !== inputSearchFriendRequestByLogin)
						searchFriendRequest();
					else
						updateFriendRequests(friendRequests, null);

				});

			});

		}

	}

	function loadShowGameRequestsListener() {

		const buttonShowGameRequests = document.querySelector(".requests-game-show");

		if(buttonShowGameRequests !== null) {

			buttonShowGameRequests.addEventListener("click", () => {

				showGameRequests();
				hideFriendRequests();
				hideOnlineFriends();

			});

		}

	}

	function showGameRequests() {
		document.querySelector("#section-requests-game").style.display = "block";
	}

	function loadHideGameRequestsListener() {

		const buttonHideGameRequests = document.querySelector(".requests-game-hide");

		if(buttonHideGameRequests !== null) {

			buttonHideGameRequests.addEventListener("click", () => {
				hideGameRequests();
			});

		}

	}

	function hideGameRequests() {
		document.querySelector("#section-requests-game").style.display = "none";
	}

	async function getGameRequests(targetPage) {

		const sectionGameRequests = document.querySelector("#section-requests-game");
		const sectionGame = document.querySelector("#section-game");

		if(sectionGameRequests !== null) {

			const response = await fetch("/Connect4/game-requests?state=pending", {
				headers: { "Content-Type": "application/json" }
			});

			gameRequests = await response.json();

			if("" !== inputSearchGameRequestByLogin)
				searchGameRequest();
			else
				updateGameRequests(gameRequests, targetPage);

		}

		if(sectionGame === null) {

			const response = await fetch("/Connect4/game-request?state=accepted", {
				headers: { "Content-Type": "application/json" }
			});

			const object = await response.json();

			const gameId = object.gameId;

			if(gameId !== undefined) {

				const buttonCloseAlert = document.querySelector(".btn-close-alert");

				buttonCloseAlert.addEventListener("click", () => {

					clearTimeout(redirectToGame);

					document.querySelector("#section-alert").style.display = "none";

					window.location.href = "/Connect4/game?id=" + gameId;

				});

				document.querySelector("#message-alert").innerHTML = "L'utilisateur " + object.receiver + " a accept&eacute; votre demande de jeu.";

				document.querySelector("#section-alert").style.display = "block";

				const redirectToGame = setTimeout(() => {

					document.querySelector("#section-alert").style.display = "none";

					window.location.href = "/Connect4/game?id=" + gameId;

				}, 3000);

			}

		}

	}

	function updateGameRequests(results, targetPage) {

		if(targetPage === null)
			targetPage = document.querySelector("#requests-game-page-current").value;
		else
			document.querySelector("#requests-game-page-current").value = targetPage;

		const numberOfItemsToShowPerPage = document.querySelector("#requests-game-page-items-number").value;
		const currentPage = targetPage;

		const formGameRequests = document.querySelector("#form-requests-game");

		let form = "<form method='post' action='game-request' id='form-requests-game'>";

		if(results.length > 0) {

			let i = 1;

			document.querySelector(".search-request-game").classList.remove("hide");

			for(let gameRequest of results) {

				if(((i - 1) >= (currentPage - 1) * numberOfItemsToShowPerPage) && ((i - 1) <= ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1)) {

					form += "<p>"
						+ "<img src='/Connect4/assets/img/users/" + gameRequest.sender.profilePicture + "' alt='Photo de profil'>"
						+ "<input type='text' id='request-game-sender-" + i + "' name='request-game-sender' value='" + gameRequest.sender.login + "'>"
						+ "<button class='btn-accept-request-game' name='action' value='accept-" + i + "'>Accepter</button>"
						+ "<button type='button' class='btn-decline btn-decline-request-game' name='action' value='decline-" + i + "'>D&eacute;cliner</button>"
						+ "</p>";

				}

				i++;

			}

			if(results.length > numberOfItemsToShowPerPage) {

				form += "<p class='center'>";

				if(currentPage != 1)
					form += "<span class='requests-game-page-first'>";
				else
					form += "<span class='requests-game-page-disabled'>";

				form += "<i class='fa-solid fa-angles-left fa-sm'></i>"
					+ "</span>"
					+ "&nbsp";

				for(i = 1; i <= Math.ceil(results.length/numberOfItemsToShowPerPage); i++) {

					if(currentPage == i)
					 	form += "<span class='requests-game-page-current'>";
					else
						form += "<span class='requests-game-page-target'>";

					form += i
						+ "</span>"
						+ "&nbsp;";

				}

				if(currentPage != Math.ceil(results.length/numberOfItemsToShowPerPage))
				 	form += "<span class='requests-game-page-last'>";
				else
					form += "<span class='requests-game-page-disabled'>";

				form += "<i class='fa-solid fa-angles-right fa-sm'></i>"
					+ "</span>";

				form += "</p>"; 

			}

			document.querySelector(".counter-requests-game").style.display = "block";

			if(results.length >= 99)
				document.querySelector(".counter-requests-game").innerHTML = "<span class='counter-requests'>99+</span>";
			else
				document.querySelector(".counter-requests-game").innerHTML = "<span class='counter-requests'>" + gameRequests.length + "</span>";

		} else {

			form += "<p class='center'>";

			if("" !== inputSearchGameRequestByLogin) {

				document.querySelector(".search-request-game").classList.remove("hide");

				form += "Aucun r&eacute;sultat.";

		 	} else {

				document.querySelector(".search-request-game").classList.add("hide");

				form += "Aucune demande de jeu.";

			}

			form += "</p>";

			document.querySelector(".counter-requests-game").style.display = "none";
			document.querySelector(".counter-requests-game").innerHTML = "";

		}

		form += "</form>";

		formGameRequests.innerHTML = form;

		loadGameRequestsPaginationListeners();
		loadDeclineGameRequestListeners();

	}

	function loadSearchGameRequestListeners() {

		getGameRequests(1);

		const searchGameRequestByLogin = document.querySelector("#search-request-game-login");

		searchGameRequestByLogin.addEventListener("keyup", () => {

			inputSearchGameRequestByLogin = searchGameRequestByLogin.value;

			searchGameRequest();

		});

		searchGameRequestByLogin.addEventListener("search", () => {

			inputSearchGameRequestByLogin = searchGameRequestByLogin.value;

			searchGameRequest();

		});

	}

	function searchGameRequest() {

		resultsSearchGameRequest = gameRequests;

		if("" !== inputSearchGameRequestByLogin)
			resultsSearchGameRequest = resultsSearchGameRequest.filter(x => (x.sender.login).startsWith(inputSearchGameRequestByLogin));

		updateGameRequests(resultsSearchGameRequest, 1);

	}

	async function loadSendGameRequestListeners() {

		const buttonsSendGameRequest = document.querySelectorAll(".btn-send-request-game");

		if(buttonsSendGameRequest !== null) {

			buttonsSendGameRequest.forEach(buttonSendGameRequest => {

				buttonSendGameRequest.addEventListener("click", async (event) => {

					const action = "send";
					const friend = document.querySelector("#friend-online-" + event.target.value.split("-")[1]).value;

					let data = {
						action: action,
						receiver: friend
					};

					const response = await fetch("/Connect4/game-request", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					onlineFriends = await response.json();

					if("" !== inputSearchOnlineFriendByLogin)
						searchOnlineFriend();
					else
						updateOnlineFriends(onlineFriends, null);

				});

			});

		}

	}

	async function loadCancelGameRequestListeners() {

		const buttonsCancelGameRequest = document.querySelectorAll(".btn-cancel-request-game");

		if(buttonsCancelGameRequest !== null) {

			buttonsCancelGameRequest.forEach(buttonCancelGameRequest => {

				buttonCancelGameRequest.addEventListener("click", async (event) => {

					const action = "cancel";
					const friend = document.querySelector("#friend-online-" + event.target.value.split("-")[1]).value;

					let data = {
						action: action,
						receiver: friend
					};

					const response = await fetch("/Connect4/game-request", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					onlineFriends = await response.json();

					if("" !== inputSearchOnlineFriendByLogin)
						searchOnlineFriend();
					else
						updateOnlineFriends(onlineFriends, null);

				});

			});

		}

	}

	async function loadDeclineGameRequestListeners() {

		const buttonsDeclineGameRequest = document.querySelectorAll(".btn-decline-request-game");

		if(buttonsDeclineGameRequest !== null) {

			buttonsDeclineGameRequest.forEach(buttonDeclineGameRequest => {

				buttonDeclineGameRequest.addEventListener("click", async (event) => {

					const action = "decline";
					const sender = document.querySelector("#request-game-sender-" + event.target.value.split("-")[1]).value;

					let data = {
						action: action,
						sender: sender
					};

					const response = await fetch("/Connect4/game-request", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					gameRequests = await response.json();

					if("" !== inputSearchGameRequestByLogin)
						searchGameRequest();
					else
						updateGameRequests(gameRequests, null);

				});

			});

		}

	}

	function loadShowOnlineFriendsListener() {

		const btnShowOnlineFriends = document.querySelector(".friends-online-show");

		if(btnShowOnlineFriends !== null) {

			btnShowOnlineFriends.addEventListener("click", () => {

				showOnlineFriends();
				hideFriendRequests();
				hideGameRequests();

			});

		}

	}

	function showOnlineFriends() {

		document.querySelector(".friends-online-hide").style.display = "block";
		document.querySelector(".friends-online-body").style.display = "block";

	}

	function loadHideOnlineFriendsListener() {

		const btnHideOnlineFriends = document.querySelector(".friends-online-hide");

		if(btnHideOnlineFriends !== null) {

			btnHideOnlineFriends.addEventListener("click", () => {

				hideOnlineFriends();

			});

		}

	}

	function hideOnlineFriends() {

		document.querySelector(".friends-online-hide").style.display = "none";
		document.querySelector(".friends-online-body").style.display = "none";

	}

	function updateOnlineFriends(results, targetPage) {

		if(targetPage === null)
			targetPage = document.querySelector("#friends-online-page-current").value;
		else
			document.querySelector("#friends-online-page-current").value = targetPage;

		const numberOfItemsToShowPerPage = document.querySelector("#friends-online-page-items-number").value;
		const currentPage = targetPage;

		const formOnlineFriends = document.querySelector("#form-friends-online");

		let form = "<form method='post' action='game-request' id='form-friends-online'>";

		if(results.length > 0) {

			let i = 1;

			document.querySelector(".search-friend-online").classList.remove("hide");

			for(let onlineFriend of results) {

				if(((i - 1) >= (currentPage - 1) * numberOfItemsToShowPerPage) && ((i - 1) <= ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1)) {

					form += "<p>";

					form += "<img src='/Connect4/assets/img/users/" + onlineFriend.profilePicture + "' alt='Photo de profil'>"
						+ "<input type='text' id='friend-online-" + i + "' name='friend-online' value='" + onlineFriend.login + "' disabled>";

					if(onlineFriend.isInGame)
						form += "En partie";
					else if(!user.isInGame) {

						if(onlineFriend.isRequested)
							form += "<button type='button' class='btn-cancel btn-cancel-request-game' name='action' value='cancel-" + i + "'>Annuler</button>";
						else
							form += "<button type='button' class='btn-send-request-game' name='action' value='send-" + i + "'>Inviter</button>";

					}

					form += "</p>";

				}

				i++;

			}

			if(results.length > numberOfItemsToShowPerPage) {

				form += "<p class='center'>";

				if(currentPage != 1)
					form += "<span class='friends-online-page-first'>";
				else
					form += "<span class='friends-online-page-disabled'>";

				form += "<i class='fa-solid fa-angles-left fa-sm'></i>"
					+ "</span>"
					+ "&nbsp";

				for(i = 1; i <= Math.ceil(results.length/numberOfItemsToShowPerPage); i++) {

					if(currentPage == i)
					 	form += "<span class='friends-online-page-current'>";
					else
						form += "<span class='friends-online-page-target'>";

					form += i
						+ "</span>"
						+ "&nbsp;";

				}

				if(currentPage != Math.ceil(results.length/numberOfItemsToShowPerPage))
				 	form += "<span class='friends-online-page-last'>";
				else
					form += "<span class='friends-online-page-disabled'>";

				form += "<i class='fa-solid fa-angles-right fa-sm'></i>"
					+ "</span>";

				form += "</p>"; 

			}

		} else {

			form += "<p class='center'>";

			if("" !== inputSearchOnlineFriendByLogin) {

				document.querySelector(".search-friend-online").classList.remove("hide");

				form += "Aucun r&eacute;sultat.";

			} else {

				document.querySelector(".search-friend-online").classList.add("hide");

				form += "Aucun ami en ligne.";

			}

			form += "</p>";

		}

		form += "</form>";

		formOnlineFriends.innerHTML = form;

		document.querySelector(".counter-friends-online").innerHTML = "(" + onlineFriends.length + ")";

		loadOnlineFriendsPaginationListeners();
		loadSendGameRequestListeners();
		loadCancelGameRequestListeners();

	}

	function loadSearchOnlineFriendListeners() {

		getFriends(1, true, false);

		const searchOnlineFriendByLogin = document.querySelector("#search-friend-online-login");

		searchOnlineFriendByLogin.addEventListener("keyup", () => {

			inputSearchOnlineFriendByLogin = searchOnlineFriendByLogin.value;

			searchOnlineFriend();

		});

		searchOnlineFriendByLogin.addEventListener("search", () => {

			inputSearchOnlineFriendByLogin = searchOnlineFriendByLogin.value;

			searchOnlineFriend();

		});

	}

	function searchOnlineFriend() {

		resultsSearchOnlineFriend = onlineFriends;

		if("" !== inputSearchOnlineFriendByLogin)
			resultsSearchOnlineFriend = resultsSearchOnlineFriend.filter(x => (x.login).startsWith(inputSearchOnlineFriendByLogin));

		updateOnlineFriends(resultsSearchOnlineFriend, 1);

	}

	async function getGame() {

		const sectionGame = document.querySelector("#section-game");

		if(sectionGame !== null) {

			const gameId = document.querySelector("#gameId").value;

			const response = await fetch("/Connect4/game?id=" + gameId, {
				headers: { "Content-Type": "application/json" }
			});

			const game = await response.json();

			updateGame(game);

		}

	}

	function updateGame(game) {

		const isPlayer1 = (user.login === game.player1.login);
		const isPlayer2 = (user.login === game.player2.login);	

		if(("" === document.querySelector("#player-2").innerText) && ("" !== game.player2.login))
			updatePlayer2(game.player2);

		updateState(game.state);

		updateBoard(game.squares);

		updateTurn(game.turn);
		updateTimer(game.timer);

		if("RUNNING" === game.state)
			loadPlayGameListeners();

		if(("CANCELED" === game.state) || ("OVER" === game.state)) {

			clearInterval(refreshGame);

			if("OVER" === game.state) {

				updateWinner(game.winner);

				if("" !== winner) {

					updateWinStreak(game.player1);
					updateWinStreak(game.player2);

				}

				if(isPlayer1 || isPlayer2) {

					if(isPlayer1) {

						if((!game.player2.isFriend) && (friendRequests.filter(x => game.player2.login === x.sender.login).length === 0)) {

							const parent = document.querySelector("#player-2").parentElement.parentElement;

							if("BUTTON" !== parent.lastElementChild.lastElementChild.nodeName) {

								let button;

								if(game.player2.isRequested)
									button = "<button type='button' class='btn-cancel btn-cancel-request-friend' name='action' value='cancel-2'>Annuler</button>";
								else
									button = "<button type='button' class='btn-send-request-friend' name='action' value='send-2'>Inviter</button>";

								parent.lastElementChild.insertAdjacentHTML('beforeend', button);

								if(game.player2.isRequested)
									loadCancelFriendRequestListeners();
								else
									loadSendFriendRequestListeners();

							}

						}

						setInterval(() => {
							getPlayer(game.player2.login);
						}, 3000);

					}

					if(isPlayer2) {

						if((!game.player1.isFriend) && (friendRequests.filter(x => game.player1.login === x.sender.login).length === 0)) {

							const parent = document.querySelector("#player-1").parentElement.parentElement;

							if("BUTTON" !== parent.lastElementChild.lastElementChild.nodeName) {

								let button;

								if(game.player1.isRequested)
									button = "<button type='button' class='btn-cancel btn-cancel-request-friend' name='action' value='cancel-1'>Annuler</button>";
								else
									button = "<button type='button' class='btn-send-request-friend' name='action' value='send-1'>Inviter</button>";

								parent.lastElementChild.insertAdjacentHTML('beforeend', button);

								if(game.player1.isRequested)
									loadCancelFriendRequestListeners();
								else
									loadSendFriendRequestListeners();

							}

						}

						setInterval(() => {
							getPlayer(game.player1.login);
						}, 3000);

					}

				}

			}

		}

	}

	function updateState(state) {

		switch(state) {
			case "CANCELED":
				document.querySelector("#state").innerHTML = "annul&eacute;e";
				break;
			case "RUNNING":
				document.querySelector("#state").innerHTML = "en cours";
				break;
			case "OVER":
				document.querySelector("#state").innerHTML = "termin&eacute;e";
				break;	
		}

	}

	function updatePlayer2(player2) {

		if("" !== player2.login) {

			const login = document.querySelector("#player-2");
			const profilePicture = "<img src='/Connect4/assets/img/users/" + player2.profilePicture + "' alt='Photo de profil'>";

			login.insertAdjacentHTML('afterend', profilePicture);
			login.innerHTML = player2.login;

			updateWinStreak(player2);

			document.querySelector("#user-2").value = player2.login;

		}

	}

	function updateWinStreak(player) {

		const player1 = document.querySelector("#player-1").innerText;

		let winStreak;

		if(player1 === player.login) {

			winStreak = document.querySelector(".streak-win-1");

			document.querySelector("#streak-win-1").innerHTML = player.winStreak;

		} else {

			winStreak = document.querySelector(".streak-win-2");

			document.querySelector("#streak-win-2").innerHTML = player.winStreak;

		}

		if(player.winStreak > 0)
			winStreak.classList.remove("hide");
		else
			winStreak.classList.add("hide");

	}

	function updateBoard(squares) {

		const tableGame = document.querySelector("#table-game");
		
		let tab = "<table id='table-game'>";

		tab += "<thead>"
			+ "<tr>"
			+ "<th class='th-rotate-right'></th>";

		for(let i in squares[0]) {

			tab += "<th>"
				+ "<button type='button' class='btn-drop-token' name='action' value='drop-" + i + "'>"
				+ "<i class='fa-solid fa-arrow-down'></i>"
				+ "</button>"
				+ "</th>";

		}

		tab += "<th class='th-rotate-left'></th>"
			+ "</tr>"
			+ "</thead>";
	
		tab += "<tbody>";

		for(let i in squares) {

			tab += "<tr>";

			if(i == 0) {

				tab += "<td rowspan='" + squares.length + "' class='td-rotate-right'>"
					+ "<button type='button' class='btn-rotate-right' name='action' value='rotate-right'>"
					+ "<i class='fa-solid fa-arrow-rotate-right'></i>"
					+ "</button>"
					+ "</td>";

			}

			for(let square of squares[i]) {

				if("" !== square) {
					if("RED" === square)
						tab += "<td><span class='token-red'></span></td>";
					else
						tab += "<td><span class='token-yellow'></span></td>";
				} else
					tab += "<td><span class='token-none'></span></td>";

			}

			if(i == 0) {

				tab += "<td rowspan='" + squares.length + "' class='td-rotate-left'>"
					+ "<button type='button' class='btn-rotate-left' name='action' value='rotate-left'>"
					+ "<i class='fa-solid fa-arrow-rotate-left'></i>"
					+ "</button>"
					+ "</td>";

			}

			tab += "</tr>";

		}

		tab += "</tbody>";

		tab += "</table>";

		tableGame.innerHTML = tab;

	}

	function updateTurn(turn) {

		document.querySelector("#turn").innerHTML = turn;

		(turn % 2 == 0)?replaceClass("turn", "player-1", "player-2"):replaceClass("turn", "player-2", "player-1");

	}

	function updateTimer(timer) {
		document.querySelector("#timer").innerHTML = timer;
	}

	function updateWinner(player) {

		const winner = document.querySelector("#winner");

		if("" === winner.innerText) {

			if("" !== player) {

				const player1 = document.querySelector("#player-1").innerText;

				if(winner.parentElement.innerText !== ("Vainqueur: " + player))
					winner.insertAdjacentHTML('beforebegin', "Vainqueur: ");

				winner.innerHTML = player;
				winner.className = (player === player1)?"player-1":"player-2";

			} else
				winner.innerHTML = "Match nul";

			document.querySelector(".winner").classList.remove("hide");

		}

	}

	async function getPlayer(login) {

		const sectionGame = document.querySelector("#section-game");

		if(sectionGame !== null) {

			const response = await fetch("/Connect4/player?login=" + login, {
				headers: { "Content-Type": "application/json" }
			});

			const player = await response.json();

			updatePlayer(player);

		}

	}

	function updatePlayer(player) {

		const player1 = document.querySelector("#player-1").innerText;
		const player2 = document.querySelector("#player-2").innerText;

		const isPlayer1 = (player1 === player.login);

		let parent;

		let requestAlreadyReceived;

		if(isPlayer1) {

			parent = document.querySelector("#player-1").parentElement.parentElement;

			requestAlreadyReceived = (friendRequests.filter(x => player1 === x.sender.login).length != 0);

		} else {

			parent = document.querySelector("#player-2").parentElement.parentElement;

			requestAlreadyReceived = (friendRequests.filter(x => player2 === x.sender.login).length != 0);

		}

		const lastChild = parent.lastElementChild;

		if("BUTTON" === lastChild.lastElementChild.nodeName)
			lastChild.lastElementChild.remove();

		if(!player.isFriend && !requestAlreadyReceived) {

			let button;

			if(isPlayer1) {

				if(player.isRequested)
					button = "<button type='button' class='btn-cancel btn-cancel-request-friend' name='action' value='cancel-1'>Annuler</button>";
				else
					button = "<button type='button' class='btn-send-request-friend' name='action' value='send-1'>Inviter</button>";

			} else {

				if(player.isRequested)
					button = "<button type='button' class='btn-cancel btn-cancel-request-friend' name='action' value='cancel-2'>Annuler</button>";
				else
					button = "<button type='button' class='btn-send-request-friend' name='action' value='send-2'>Inviter</button>";

			}

			lastChild.insertAdjacentHTML('beforeend', button);

			if(player.isRequested)
				loadCancelFriendRequestListeners();
			else
				loadSendFriendRequestListeners();

		}

	}

	async function loadPlayGameListeners() {

		const buttonsDropToken = document.querySelectorAll(".btn-drop-token");

		if(buttonsDropToken !== null) {

			buttonsDropToken.forEach(buttonDropToken => {

				buttonDropToken.addEventListener("click", async (event) => {

					const action = "dropToken";
					const gameId = document.querySelector("#gameId").value;
					const column = event.currentTarget.value.split("-")[1];

					let data = {
						action: action,
						gameId: gameId,
						column: column
					};

					const response = await fetch("/Connect4/game", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					const game = await response.json();

					if(game !== null)
						updateGame(game);

				});

			});

		}

		const buttonRotateLeft = document.querySelector(".btn-rotate-left");

		if(buttonRotateLeft !== null) {

			buttonRotateLeft.addEventListener("click", async () => {

				const action = "rotateLeft";
				const gameId = document.querySelector("#gameId").value;

				let data = {
					action: action,
					gameId: gameId
				};

				const response = await fetch("/Connect4/game", {
					method: "post",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify(data)
				});

				const game = await response.json();

				if(game !== null)
					updateGame(game);

			});

		}

		const buttonRotateRight = document.querySelector(".btn-rotate-right");

		if(buttonRotateRight !== null) {

			buttonRotateRight.addEventListener("click", async () => {

				const action = "rotateRight";
				const gameId = document.querySelector("#gameId").value;

				let data = {
					action: action,
					gameId: gameId
				};

				const response = await fetch("/Connect4/game", {
					method: "post",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify(data)
				});
		
				const game = await response.json();

				if(game !== null)
					updateGame(game);

			});

		}

	}

	function loadLeaveGameListeners() {

		if(page.indexOf("game?id=") === -1) {

			addEventListener("load", () => {

				const gameId = sessionStorage.getItem("gameId");

				if(gameId !== null) {

					let data = {
						action: "leaveGame",
						gameId: gameId
					};

					fetch("/Connect4/game", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					sessionStorage.removeItem("gameId");

				}

			});

		}

		if(page.indexOf("game?id=") !== -1) {

			const gameId = document.querySelector("#gameId").value;

			addEventListener("beforeunload", () => {

				let data = {
					action: "leaveGame",
					gameId: gameId
				};

				fetch("/Connect4/game", {
					method: "post",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify(data)
				});

			});

			addEventListener("unload", () => {
				sessionStorage.setItem("gameId", gameId);
			});

		}

	}

	async function getGames(targetPage) {

		const sectionGames = document.querySelector("#section-games");

		if(sectionGames !== null) {

			const response = await fetch("/Connect4/games?state=running", {
				headers: { "Content-Type": "application/json" }
			});

			games = await response.json();

			if(("" !== inputSearchGameById)
				|| ("" !== inputSearchGameByType)
				|| ("" !== inputSearchGameByPlayer1)
				|| ("" !== inputSearchGameByPlayer2)
				|| ("" !== inputSearchGameByStart)) 
				searchGame();
			else
				updateGames(games, targetPage);

		}

	}

	function updateGames(results, targetPage) {

		if(targetPage === null)
			targetPage = document.querySelector("#page-current").value;
		else
			document.querySelector("#page-current").value = targetPage;

		const numberOfItemsToShowPerPage = document.querySelector("#page-items-number").value;
		const currentPage = targetPage;

		const tableBodyGames = document.querySelector(".table-body-games");
		const tableFootGames = document.querySelector(".table-foot-games");

		let tableBody = "<tbody class='table-body-games'>";
		let tableFoot;

		if(results.length > 0) {

			let i = 1;

			document.querySelector(".tr-search-games").classList.remove("hide");

			for(let game of results) {

				if(((i - 1) >= (currentPage - 1) * numberOfItemsToShowPerPage) && ((i - 1) <= ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1)) {

					tableBody += "<tr>"
						+ "<td>" + game.id + "</td>"
						+ "<td>" + game.type + "</td>";

					tableBody += "<td>"
						+ "<img src='/Connect4/assets/img/users/" + game.player1.profilePicture + "' alt='Photo de profil'>"
						+ "<span>" + game.player1.login + "</span>"
						+ "</td>";

					tableBody += "<td>"
						+ "<img src='/Connect4/assets/img/users/" + game.player2.profilePicture + "' alt='Photo de profil'>"
						+ "<span>" + game.player2.login + "</span>"
						+ "</td>";

					tableBody += "<td>" + game.start + "</td>"
						+ "<td><a href='/Connect4/game?id=" + game.id + "'>Observer</a></td>"
						+ "</tr>";	

				}

				i++;

			}

			if(results.length > numberOfItemsToShowPerPage) {

				tableFoot = "<tfoot class='table-foot-games'>";

				tableFoot += "<tr>"
					+ "<td colspan='6'>";

				if(currentPage != 1) 
					tableFoot += "<span class='page-first'>"
				else
					tableFoot += "<span class='page-disabled'>"

				tableFoot += "<i class='fa-solid fa-angles-left fa-sm'></i>"
					+ "</span>"
					+ "&nbsp";

				for(i = 1; i <= Math.ceil(results.length/numberOfItemsToShowPerPage); i++) {

					if(currentPage == i) 
						tableFoot += "<span class='page-current'>";
					else
						tableFoot += "<span class='page-target'>";

					tableFoot += i
						+ "</span>"
						+ "&nbsp;";

				}

				if(currentPage != Math.ceil(results.length/numberOfItemsToShowPerPage)) 
					tableFoot += "<span class='page-last'>"
				else 
					tableFoot += "<span class='page-disabled'>";

				tableFoot += "<i class='fa-solid fa-angles-right fa-sm'></i>"
					+ "</span>";

				tableFoot += "</td>"
					+ "</tr>";

			} else
				tableFoot = "<tfoot class='table-foot-games hide'>";

		} else {

			if(("" !== inputSearchGameById)
				|| ("" !== inputSearchGameByType)
				|| ("" !== inputSearchGameByPlayer1)
				|| ("" !== inputSearchGameByPlayer2)
				|| ("" !== inputSearchGameByStart)) {

				document.querySelector(".tr-search-games").classList.remove("hide");

				tableBody += "<tr><td colspan='5'>Aucun r&eacute;sultat.</td></tr>";

			} else {

				document.querySelector(".tr-search-games").classList.add("hide");

				tableBody += "<tr><td colspan='5'>Aucune partie en cours.</td></tr>";

			}

			tableFoot = "<tfoot class='table-foot-games hide'>";

		}

		tableBody += "</tbody>";	
		tableFoot += "</tfoot>";

		tableBodyGames.innerHTML = tableBody;
		tableFootGames.innerHTML = tableFoot;

		loadPaginationListeners();

	}

	function loadSearchGameListeners() {

		getGames(1);

		const searchGameById = document.querySelector("#search-game-id");

		searchGameById.addEventListener("keyup", () => {

			inputSearchGameById = searchGameById.value;

			searchGame();

		});

		searchGameById.addEventListener("search", () => {

			inputSearchGameById = searchGameById.value;

			searchGame();

		});

		const searchGameByType = document.querySelector("#search-game-type");

		searchGameByType.addEventListener("keyup", () => {

			inputSearchGameByType = searchGameByType.value;

			searchGame();

		});

		searchGameByType.addEventListener("search", () => {

			inputSearchGameByType = searchGameByType.value;

			searchGame();

		});

		const searchGameByPlayer1 = document.querySelector("#search-game-player1");

		searchGameByPlayer1.addEventListener("keyup", () => {

			inputSearchGameByPlayer1 = searchGameByPlayer1.value;

			searchGame();

		});

		searchGameByPlayer1.addEventListener("search", () => {

			inputSearchGameByPlayer1 = searchGameByPlayer1.value;

			searchGame();

		});

		const searchGameByPlayer2 = document.querySelector("#search-game-player2");

		searchGameByPlayer2.addEventListener("keyup", () => {

			inputSearchGameByPlayer2 = searchGameByPlayer2.value;

			searchGame();

		});

		searchGameByPlayer2.addEventListener("search", () => {

			inputSearchGameByPlayer2 = searchGameByPlayer2.value;

			searchGame();

		});

		const searchGameByStart = document.querySelector("#search-game-start");

		searchGameByStart.addEventListener("keyup", () => {

			inputSearchGameByStart = searchGameByStart.value;

			searchGame();

		});

		searchGameByStart.addEventListener("search", () => {

			inputSearchGameByStart = searchGameByStart.value;

			searchGame();

		});

	}

	function searchGame() {

		resultsSearchGame = games;

		if("" !== inputSearchGameById)
			resultsSearchGame = resultsSearchGame.filter(x => ((x.id).toString()).startsWith(inputSearchGameById));

		if("" !== inputSearchGameByType)
			resultsSearchGame = resultsSearchGame.filter(x => (x.type).startsWith(inputSearchGameByType));

		if("" !== inputSearchGameByPlayer1)
			resultsSearchGame = resultsSearchGame.filter(x => (x.player1.login).startsWith(inputSearchGameByPlayer1));

		if("" !== inputSearchGameByPlayer2)
			resultsSearchGame = resultsSearchGame.filter(x => (x.player2.login).startsWith(inputSearchGameByPlayer2));

		if("" !== inputSearchGameByStart)
			resultsSearchGame = resultsSearchGame.filter(x => (x.start).startsWith(inputSearchGameByStart));

		updateGames(resultsSearchGame, 1);

	}

	async function getScores(targetPage) {

		const sectionScores = document.querySelector("#section-scores");

		if(sectionScores !== null) {

			const response = await fetch("/Connect4/scores", {
				headers: { "Content-Type": "application/json" }
			});

			scores = await response.json();

			if(("" !== inputSearchScoreByRank)
				|| ("" !== inputSearchScoreByLogin)
				|| ("" !== inputSearchScoreByRatio)
				|| ("" !== inputSearchScoreByWins)
				|| ("" !== inputSearchScoreByDefeats)
				|| ("" !== inputSearchScoreByDraws)
				|| ("" !== inputSearchScoreByBestWinStreak)) 
				searchScore();
			else {

				if(friendsOnly)
					updateScores(getFriendsScores(), targetPage);
				else
					updateScores(scores, targetPage);

			}

		}

	}

	function getFriendsScores() {

		const tmp = [];

		friends.forEach(x => tmp.push(x.login));
		tmp.push(user.login);

		return scores.filter(x => tmp.includes(x.user.login));

	}

	function updateScores(results, targetPage) {

		if(targetPage === null)
			targetPage = document.querySelector("#page-current").value;
		else
			document.querySelector("#page-current").value = targetPage;

		const numberOfItemsToShowPerPage = document.querySelector("#page-items-number").value;
		const currentPage = targetPage;

		const tableBodyScores = document.querySelector(".table-body-scores");
		const tableFootScores = document.querySelector(".table-foot-scores");

		let tableBody = "<tbody class='table-body-scores'>";
		let tableFoot;

		if(results.length > 0) {

			let i = 1;

			document.querySelector(".tr-search-scores").classList.remove("hide");

			for(let score of results) {

				if(((i - 1) >= (currentPage - 1) * numberOfItemsToShowPerPage) && ((i - 1) <= ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1)) {

					if(user.login === score.user.login)
						tableBody += "<tr class='tr-user'>";
					else
						tableBody += "<tr>";

					tableBody += "<td>" + score.rank + "</td>";

					tableBody += "<td>"
						+ "<img src='/Connect4/assets/img/users/" + score.user.profilePicture + "' alt='Photo de profil'>"
						+ "<span>" + score.user.login + "</span>";
						+ "</td>";

					tableBody += "<td>" + (Math.trunc(100*(score.ratio))/100) + "</td>"
						+ "<td>" + score.wins + "</td>"
						+ "<td>" + score.defeats + "</td>"
						+ "<td>" + score.draws + "</td>"
						+ "<td>" + score.bestWinStreak + "</td>";

					tableBody += "</tr>";	

				}

				i++;

			}

			if(results.length > numberOfItemsToShowPerPage) {

				tableFoot = "<tfoot class='table-foot-scores'>";

				tableFoot += "<tr>"
					+ "<td colspan='7'>";

				if(currentPage != 1) 
					tableFoot += "<span class='page-first'>"
				else
					tableFoot += "<span class='page-disabled'>"

				tableFoot += "<i class='fa-solid fa-angles-left fa-sm'></i>"
					+ "</span>"
					+ "&nbsp";

				for(i = 1; i <= Math.ceil(results.length/numberOfItemsToShowPerPage); i++) {

					if(currentPage == i) 
						tableFoot += "<span class='page-current'>";
					else
						tableFoot += "<span class='page-target'>";

					tableFoot += i
						+ "</span>"
						+ "&nbsp;";

				}

				if(currentPage != Math.ceil(results.length/numberOfItemsToShowPerPage)) 
					tableFoot += "<span class='page-last'>"
				else 
					tableFoot += "<span class='page-disabled'>";

				tableFoot += "<i class='fa-solid fa-angles-right fa-sm'></i>"
					+ "</span>";

				tableFoot += "</td>"
					+ "</tr>";
	
			} else
				tableFoot = "<tfoot class='table-foot-scores hide'>";

		} else {

			if(("" !== inputSearchScoreByRank)
				|| ("" !== inputSearchScoreByLogin)
				|| ("" !== inputSearchScoreByRatio)
				|| ("" !== inputSearchScoreByWins)
				|| ("" !== inputSearchScoreByDefeats)
				|| ("" !== inputSearchScoreByDraws)
				|| ("" !== inputSearchScoreByBestWinStreak)) {

				document.querySelector(".tr-search-scores").classList.remove("hide");

				tableBody += "<tr><td colspan='7'>Aucun r&eacute;sultat.</td></tr>";

			} else {

				document.querySelector(".tr-search-scores").classList.add("hide");

				tableBody += "<tr><td colspan='7'>Aucun score.</td></tr>";

			}

			tableFoot = "<tfoot class='table-foot-scores hide'>";

		}

		tableBody += "</tbody>";	
		tableFoot += "</tfoot>";

		tableBodyScores.innerHTML = tableBody;
		tableFootScores.innerHTML = tableFoot;

		loadPaginationListeners();

	}

	function loadSearchScoreListeners() {

		getScores(1);

		const searchScoreByRank = document.querySelector("#search-score-rank");

		searchScoreByRank.addEventListener("keyup", () => {

			inputSearchScoreByRank = searchScoreByRank.value;

			searchScore();

		});

		searchScoreByRank.addEventListener("search", () => {

			inputSearchScoreByRank = searchScoreByRank.value;

			searchScore();

		});

		const searchScoreByLogin = document.querySelector("#search-score-login");

		searchScoreByLogin.addEventListener("keyup", () => {

			inputSearchScoreByLogin = searchScoreByLogin.value;

			searchScore();

		});

		searchScoreByLogin.addEventListener("search", () => {

			inputSearchScoreByLogin = searchScoreByLogin.value;

			searchScore();

		});

		const searchScoreByRatio = document.querySelector("#search-score-ratio");

		searchScoreByRatio.addEventListener("keyup", () => {

			inputSearchScoreByRatio = searchScoreByRatio.value;

			searchScore();

		});

		searchScoreByRatio.addEventListener("search", () => {

			inputSearchScoreByRatio = searchScoreByRatio.value;

			searchScore();

		});

		const searchScoreByWins = document.querySelector("#search-score-wins");

		searchScoreByWins.addEventListener("keyup", () => {

			inputSearchScoreByWins = searchScoreByWins.value;

			searchScore();

		});

		searchScoreByWins.addEventListener("search", () => {

			inputSearchScoreByWins = searchScoreByWins.value;

			searchScore();

		});

		const searchScoreByDefeats = document.querySelector("#search-score-defeats");

		searchScoreByDefeats.addEventListener("keyup", () => {
	
			inputSearchScoreByDefeats = searchScoreByDefeats.value;

			searchScore();

		});

		searchScoreByDefeats.addEventListener("search", () => {

			inputSearchScoreByDefeats = searchScoreByDefeats.value;

			searchScore();

		});

		const searchScoreByDraws = document.querySelector("#search-score-draws");

		searchScoreByDraws.addEventListener("keyup", () => {

			inputSearchScoreByDraws = searchScoreByDraws.value;

			searchScore();

		});

		searchScoreByDraws.addEventListener("search", () => {

			inputSearchScoreByDraws = searchScoreByDraws.value;

			searchScore();

		});

		const searchScoreByBestWinStreak = document.querySelector("#search-score-streak-win-best");

		searchScoreByBestWinStreak.addEventListener("keyup", () => {

			inputSearchScoreByBestWinStreak = searchScoreByBestWinStreak.value;

			searchScore();

		});

		searchScoreByBestWinStreak.addEventListener("search", () => {

			inputSearchScoreByBestWinStreak = searchScoreByBestWinStreak.value;

			searchScore();

		});

	}

	function searchScore() {

		if(friendsOnly)
			resultsSearchScore = getFriendsScores();
		else
			resultsSearchScore = scores;

		if("" !== inputSearchScoreByRank)
			resultsSearchScore = resultsSearchScore.filter(x => ((x.rank).toString()).startsWith(inputSearchScoreByRank));

		if("" !== inputSearchScoreByLogin)
			resultsSearchScore = resultsSearchScore.filter(x => (x.user.login).startsWith(inputSearchScoreByLogin));

		if("" !== inputSearchScoreByRatio)
			resultsSearchScore = resultsSearchScore.filter(x => ((x.ratio).toString()).startsWith(inputSearchScoreByRatio));

		if("" !== inputSearchScoreByWins)
			resultsSearchScore = resultsSearchScore.filter(x => ((x.wins).toString()).startsWith(inputSearchScoreByWins));

		if("" !== inputSearchScoreByDefeats)
			resultsSearchScore = resultsSearchScore.filter(x => ((x.defeats).toString()).startsWith(inputSearchScoreByDefeats));

		if("" !== inputSearchScoreByDraws)
			resultsSearchScore = resultsSearchScore.filter(x => ((x.draws).toString()).startsWith(inputSearchScoreByDraws));

		if("" !== inputSearchScoreByBestWinStreak)
			resultsSearchScore = resultsSearchScore.filter(x => ((x.bestWinStreak).toString()).startsWith(inputSearchScoreByBestWinStreak));

		updateScores(resultsSearchScore, 1);

	}
	
	function loadFilterScoresByFriendsListener() {

		const buttonFilterScoresByFriends = document.querySelector(".btn-filter-scores-friends");

		if(buttonFilterScoresByFriends !== null) {

			buttonFilterScoresByFriends.addEventListener("click", () => {

				friendsOnly = !friendsOnly;

				if(friendsOnly)
					button = "<button type='button' class='btn-cancel btn-filter-scores-friends'>Annuler</button>";
				else
					button = "<button type='button' class='btn-filter-scores-friends'>Amis seulement</button>";

				document.querySelector(".filter-scores").innerHTML = button;

				if(("" !== inputSearchScoreByRank)
					|| ("" !== inputSearchScoreByLogin)
					|| ("" !== inputSearchScoreByRatio)
					|| ("" !== inputSearchScoreByWins)
					|| ("" !== inputSearchScoreByDefeats)
					|| ("" !== inputSearchScoreByDraws)
					|| ("" !== inputSearchScoreByBestWinStreak))
					searchScore();
				else {

					if(friendsOnly)
						updateScores(getFriendsScores(), null);
					else
						updateScores(scores, null);

				}

				loadFilterScoresByFriendsListener();

			});

		}

	}

	function loadUpdateUserListener() {

		const buttonUpdateAccount = document.querySelector(".btn-update-account");

		if(buttonUpdateAccount !== null) {

			buttonUpdateAccount.addEventListener("click", () => {

				const login = document.querySelector("#login").value;
				const email = document.querySelector("#email").value;
				const password = document.querySelector("#password").value;

				const formProfile = document.querySelector("#form-profile");

				let form = "<p>"
					+ "<label for='login' class='label-registration'>Login: </label>"
					+ "<input type='text' id='login' name='login' value='" + login + "' disabled>"
					+ "</p>";

				form += "<p>"
					+ "<label for='email' class='label-registration'>Email: </label>"
					+ "<input type='email' id='email' name='email' value='" + email + "' autofocus>"
					+ "</p>";

				form += "<p>"
					+ "<label for='password' class='label-registration'>Mot de passe: </label>"
					+ "<input type='password' class='input-password' id='password' name='password' value='" + password + "'>"
					+ "<button type='button' class='btn-show-password'><i class='fa-regular fa-eye'></i></button>"
					+ "</p>";

				form += "<p>"
					+ "<label for='password-confirmation' class='label-registration'>Confirmation mot de passe: </label>"
					+ "<input type='password' id='password-confirmation' name='password-confirmation' value='" + password + "'>"
					+ "</p>";

				form += "<p>"
					+ "<label for='profilePicture' class='label-registration'>Photo de profil: </label>"
					+ "<input type='file' id='profile-picture' name='profile-picture' accept='image/*'>"
					+ "</p>";

				form += "<p>"
					+ "<input type='hidden' id='action' name='action' value='update'>"
					+ "<button class='btn-update-account-confirmation'>Valider</button>"
					+ "<a href='/Connect4/profile'><button type='button' class='btn-previous'>Annuler</button></a>"
					+ "</p>";

				formProfile.innerHTML = form;

				loadShowPasswordListener();

			});

		}

	}

	function loadDeleteUserListener() {

		const formProfile = document.querySelector("#form-profile");

		if(formProfile !== null) {

			formProfile.addEventListener("submit", (event) => {

				if("delete" === document.querySelector("#action").value) {

					event.preventDefault();

					const buttonConfirmAction = document.querySelector(".btn-confirm-action");

					buttonConfirmAction.addEventListener("click", () => {

						document.querySelector("#section-confirm").style.display = "none";

						event.target.submit();

					});

					const buttonCancelAction = document.querySelector(".btn-cancel-action");

					buttonCancelAction.addEventListener("click", () => {

						document.querySelector("#section-confirm").style.display = "none";

					});

					document.querySelector("#message-confirm").innerHTML = "&Ecirc;tes-vous s&ucirc;r de vouloir supprimer votre compte?";

					document.querySelector("#section-confirm").style.display = "block";

				}

			});

		}

	}

	async function getFriends(targetPage, onlineOnly, profilePageOnly) {

		const sectionOnlineFriends = document.querySelector("#section-friends-online");

		if(sectionOnlineFriends !== null) {

			const response = await fetch("/Connect4/friends", {
				headers: { "Content-Type": "application/json" }
			});

			friends = await response.json();
			onlineFriends = friends.filter(x => x.isOnline);

			if("" !== inputSearchOnlineFriendByLogin)
				searchOnlineFriend();
			else
				updateOnlineFriends(onlineFriends, profilePageOnly?null:targetPage);

			if(!onlineOnly) {

				const sectionProfile = document.querySelector("#section-profile");

				if(sectionProfile !== null) {

					if(("" !== inputSearchFriendByLogin)
						|| ("" !== inputSearchFriendByLastConnection))
						searchFriend();
					else
						updateFriends(friends, targetPage);

				}

			}

		}

	}

	function updateFriends(results, targetPage) {

		if(targetPage === null)
			targetPage = document.querySelector("#page-current").value;
		else
			document.querySelector("#page-current").value = targetPage;

		const numberOfItemsToShowPerPage = document.querySelector("#page-items-number").value;
		const currentPage = targetPage;

		const tableHeadFriends = document.querySelector(".table-head-friends");
		const tableBodyFriends = document.querySelector(".table-body-friends");
		const tableFootFriends = document.querySelector(".table-foot-friends");

		let tableHead;
		let tableBody = "<tbody class='table-body-friends'>";
		let tableFoot;

		if(results.length > 0) {

			let i = 1;

			tableHead = "<tr><th>Ami</th><th>Derni&egrave;re connexion</th><th rowspan='2'><a href='/Connect4/users'/>Ajouter un ami</a></th></tr>"

			document.querySelector(".tr-search-friends").classList.remove("hide");

			for(let friend of results) {

				if(((i - 1) >= (currentPage - 1) * numberOfItemsToShowPerPage) && ((i - 1) <= ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1)) {

					tableBody += "<tr>";

					tableBody += "<td>"
						+ "<img src='/Connect4/assets/img/users/" + friend.profilePicture + "' alt='Photo de profil'>"
						+ "<input type='text' id='friend-" + i + "' name='friend-" + i + "' value='" + friend.login + "' disabled>"
						+ "</td>";
		
					tableBody += "<td><input type='text' id='lastConnection' name='lastConnection' value='" + friend.lastConnection + "' disabled></td>"
						+ "<td><button type='button' class='btn-remove btn-remove-friend' name='action' value='remove-" + i + "'>Supprimer</button></td>";
		
					tableBody += "</tr>";

				}

				i++;

			}

			if(results.length > numberOfItemsToShowPerPage) {

				tableFoot = "<tfoot class='table-foot-friends'>";

				tableFoot += "<tr>"
					+ "<td colspan='3'>";

				if(currentPage != 1) 
					tableFoot += "<span class='page-first'>"
				else
					tableFoot += "<span class='page-disabled'>"

				tableFoot += "<i class='fa-solid fa-angles-left fa-sm'></i>"
					+ "</span>"
					+ "&nbsp";

				for(i = 1; i <= Math.ceil(results.length/numberOfItemsToShowPerPage); i++) {

					if(currentPage == i) 
						tableFoot += "<span class='page-current'>";
					else
						tableFoot += "<span class='page-target'>";

					tableFoot += i
						+ "</span>"
						+ "&nbsp;";

				}

				if(currentPage != Math.ceil(results.length/numberOfItemsToShowPerPage)) 
					tableFoot += "<span class='page-last'>"
				else 
					tableFoot += "<span class='page-disabled'>";

				tableFoot += "<i class='fa-solid fa-angles-right fa-sm'></i>"
					+ "</span>";

				tableFoot += "</td>"
					+ "</tr>";

			} else
				tableFoot = "<tfoot class='table-foot-friends hide'>";

		} else {

			tableHead = "<tr><th>Login</th><th>Derni&egrave;re connexion</th><th><a href='/Connect4/users'/>Ajouter un ami</a></th></tr>"

			if(("" !== inputSearchFriendByLogin)
				|| ("" !== inputSearchFriendByLastConnection)) {

				document.querySelector(".tr-search-friends").classList.remove("hide");

				tableBody += "<tr><td colspan='2'>Aucun r&eacute;sultat.</td></tr>";

			} else {

				document.querySelector(".tr-search-friends").classList.add("hide");

				tableBody += "<tr><td colspan='2'>Aucun ami.</td></tr>";

			}

			tableFoot = "<tfoot class='table-foot-friends hide'>";

		}

		tableBody += "</tbody>";	
		tableFoot += "</tfoot>";

		tableHeadFriends.innerHTML = tableHead;
		tableBodyFriends.innerHTML = tableBody;
		tableFootFriends.innerHTML = tableFoot;

		document.querySelector(".counter-friends").innerHTML = "(" + friends.length + ")";

		loadPaginationListeners();
		loadRemoveFriendListeners();

	}

	function loadSearchFriendListeners() {

		getFriends(1, false, true);

		const searchFriendByLogin = document.querySelector("#search-friend-login");

		searchFriendByLogin.addEventListener("keyup", () => {

			inputSearchFriendByLogin = searchFriendByLogin.value;

			searchFriend();

		});

		searchFriendByLogin.addEventListener("search", () => {

			inputSearchFriendByLogin = searchFriendByLogin.value;

			searchFriend();

		});

		const searchFriendByLastConnection = document.querySelector("#search-friend-connection-last");

		searchFriendByLastConnection.addEventListener("keyup", () => {

			inputSearchFriendByLastConnection = searchFriendByLastConnection.value;

			searchFriend();

		});

		searchFriendByLastConnection.addEventListener("search", () => {

			inputSearchFriendByLastConnection = searchFriendByLastConnection.value;

			searchFriend();

		});

	}

	function searchFriend() {

		resultsSearchFriend = friends;

		if("" !== inputSearchFriendByLogin)
			resultsSearchFriend = resultsSearchFriend.filter(x => (x.login).startsWith(inputSearchFriendByLogin));

		if("" !== inputSearchFriendByLastConnection)
			resultsSearchFriend = resultsSearchFriend.filter(x => (x.lastConnection).startsWith(inputSearchFriendByLastConnection));

		updateFriends(resultsSearchFriend, 1);

	}

	async function loadRemoveFriendListeners() {

		const buttonsRemoveFriend = document.querySelectorAll(".btn-remove-friend");

		if(buttonsRemoveFriend !== null) {

			buttonsRemoveFriend.forEach(buttonRemoveFriend => {

				buttonRemoveFriend.addEventListener("click", async (event) => {

					const action = "remove";
					const friend = document.querySelector("[name=friend-" + event.target.value.split("-")[1] + "]").value;

					let data = {
						action: action,
						friend: friend
					};

					const response = await fetch("/Connect4/friend", {
						method: "post",
						headers: { "Content-Type": "application/json" },
						body: JSON.stringify(data)
					});

					const object = await response.json();

					friends = object.friends;
					onlineFriends = friends.filter(x => x.isOnline);

					gameRequests = object.gameRequests;

					if(("" !== inputSearchFriendByLogin)
						|| ("" !== inputSearchFriendByLastConnection)) 
						searchFriend();
					else
						updateFriends(friends, null);

					if("" !== inputSearchOnlineFriendByLogin)
						searchOnlineFriend();
					else
						updateOnlineFriends(onlineFriends, null);

					if("" !== inputSearchGameRequestByLogin)
						searchGameRequest();
					else
						updateGameRequests(gameRequests, null);

				});

			});

		}

	}

	async function getUsers(targetPage) {

		const sectionUsers = document.querySelector("#section-users");

		if(sectionUsers !== null) {

			const response = await fetch("/Connect4/users", {
				headers: { "Content-Type": "application/json" }
			});

			users = await response.json();

			if("" !== inputSearchUserByLogin)
				searchUser();
			else
				updateUsers(users, targetPage);

		}

	}

	function updateUsers(results, targetPage) {

		if(targetPage === null)
			targetPage = document.querySelector("#page-current").value;
		else
			document.querySelector("#page-current").value = targetPage;

		const numberOfItemsToShowPerPage = document.querySelector("#page-items-number").value;
		const currentPage = targetPage;

		const tableBodyUsers = document.querySelector(".table-body-users");
		const tableFootUsers = document.querySelector(".table-foot-users");

		let tableBody = "<tbody class='table-body-users'>";
		let tableFoot;

		if(results.length > 0) {

			let i = 1;

			document.querySelector(".tr-search-users").classList.remove("hide");

			for(let user of results) {

				if(((i - 1) >= (currentPage - 1) * numberOfItemsToShowPerPage) && ((i - 1) <= ((currentPage - 1) * numberOfItemsToShowPerPage) + numberOfItemsToShowPerPage - 1)) {

					tableBody += "<tr>";

					tableBody += "<td>"
						+ "<img src='/Connect4/assets/img/users/" + user.profilePicture + "' alt='Photo de profil'>"
						+ "<input type='text' id='user-" + i + "' name='user' value='" + user.login + "' disabled>"
						+ "</td>";

					tableBody += "<td>";

					if(user.isRequested)
						tableBody += "<button type='button' class='btn-cancel btn-cancel-request-friend' name='action' value='cancel-" + i + "'>Annuler</button>";
					else
						tableBody += "<button type='button' class='btn-send-request-friend' name='action' value='send-" + i + "'>Inviter</button>";

					tableBody += "</td>";

					tableBody += "</tr>";

				}

				i++;

			}

			if(results.length > numberOfItemsToShowPerPage) {

				tableFoot = "<tfoot class='table-foot-users'>";

				tableFoot += "<tr>"
					+ "<td colspan='2'>";

				if(currentPage != 1) 
					tableFoot += "<span class='page-first'>"
				else
					tableFoot += "<span class='page-disabled'>"

				tableFoot += "<i class='fa-solid fa-angles-left fa-sm'></i>"
					+ "</span>"
					+ "&nbsp";

				for(i = 1; i <= Math.ceil(results.length/numberOfItemsToShowPerPage); i++) {

					if(currentPage == i) 
						tableFoot += "<span class='page-current'>";
					else
						tableFoot += "<span class='page-target'>";

					tableFoot += i
						+ "</span>"
						+ "&nbsp;";

				}

				if(currentPage != Math.ceil(results.length/numberOfItemsToShowPerPage)) 
					tableFoot += "<span class='page-last'>"
				else 
					tableFoot += "<span class='page-disabled'>";

				tableFoot += "<i class='fa-solid fa-angles-right fa-sm'></i>"
					+ "</span>";

				tableFoot += "</td>"
					+ "</tr>";

			} else
				tableFoot = "<tfoot class='table-foot-users hide'>";

		} else {

			if("" !== inputSearchUserByLogin) {

				document.querySelector(".tr-search-users").classList.remove("hide");

				tableBody += "<tr><td colspan='1'>Aucun r&eacute;sultat.</td></tr>";

			} else {

				document.querySelector(".tr-search-users").classList.add("hide");

				tableBody += "<tr><td colspan='1'>Aucun utilisateur &agrave; ajouter.</td></tr>";

			}

			tableFoot = "<tfoot class='table-foot-users hide'>";

		}

		tableBody += "</tbody>";	
		tableFoot += "</tfoot>";

		tableBodyUsers.innerHTML = tableBody;
		tableFootUsers.innerHTML = tableFoot;

		loadPaginationListeners();
		loadSendFriendRequestListeners();
		loadCancelFriendRequestListeners();

	}

	function loadSearchUserListeners() {

		getUsers(1);

		const searchUserByLogin = document.querySelector("#search-user-login");

		searchUserByLogin.addEventListener("keyup", () => {

			inputSearchUserByLogin = searchUserByLogin.value;

			searchUser();

		});

		searchUserByLogin.addEventListener("search", () => {

			inputSearchUserByLogin = searchUserByLogin.value;

			searchUser();

		});

	}

	function searchUser() {

		resultsSearchUser = users;

		if("" !== inputSearchUserByLogin)
			resultsSearchUser = resultsSearchUser.filter(x => (x.login).startsWith(inputSearchUserByLogin));

		updateUsers(resultsSearchUser, 1);

	}

	function loadPaginationListeners() {

		const buttonFirstPage = document.querySelector(".page-first");

		if(buttonFirstPage !== null) {

			buttonFirstPage.addEventListener("click", () => {

				if(document.querySelector("#section-games") !== null);
					getGames(1);

				if(document.querySelector("#section-scores") !== null);
					getScores(1);

				if(document.querySelector("#section-friends") !== null);
					getFriends(1, false, true);

				if(document.querySelector("#section-users") !== null);
					getUsers(1);

			});

		}

		const buttonsChangePage = document.querySelectorAll(".page-target");

		if(buttonsChangePage !== null) {

			buttonsChangePage.forEach(buttonChangePage => {

				buttonChangePage.addEventListener("click", event => {

					const targetPage = event.target.innerText;

					if(document.querySelector("#section-games") !== null);
						getGames(targetPage);

					if(document.querySelector("#section-scores") !== null);
						getScores(targetPage);

					if(document.querySelector("#section-friends") !== null);
						getFriends(targetPage, false, true);

					if(document.querySelector("#section-users") !== null);
						getUsers(targetPage);

				});

			});

		}

		const buttonLastPage = document.querySelector(".page-last");

		if(buttonLastPage !== null) {

			buttonLastPage.addEventListener("click", () => {

				const numberOfItems = document.querySelector("#items-number").value;
				const numberOfItemsToShowPerPage = document.querySelector("#page-items-number").value;
				const lastPage = Math.ceil(numberOfItems/numberOfItemsToShowPerPage);

				if(document.querySelector("#section-games") !== null);
					getGames(lastPage);

				if(document.querySelector("#section-scores") !== null)
					getScores(lastPage);

				if(document.querySelector("#section-friends") !== null);
					getFriends(lastPage, false, true);

				if(document.querySelector("#section-users") !== null);
					getUsers(lastPage);

			});

		}

	}

	function loadFriendRequestsPaginationListeners() {

		const buttonFirstPage = document.querySelector(".requests-friend-page-first");

		if(buttonFirstPage !== null) {

			buttonFirstPage.addEventListener("click", () => {

				getFriendRequests(1);

			});

		}

		const buttonsChangePage = document.querySelectorAll(".requests-friend-page-target");

		if(buttonsChangePage !== null) {

			buttonsChangePage.forEach(buttonChangePage => {

				buttonChangePage.addEventListener("click", event => {

					const targetPage = event.target.innerText;

					getFriendRequests(targetPage);

				});

			});

		}

		const buttonLastPage = document.querySelector(".requests-friend-page-last");

		if(buttonLastPage !== null) {

			buttonLastPage.addEventListener("click", () => {

				const numberOfItems = document.querySelector("#requests-friend-number").value;
				const numberOfItemsToShowPerPage = document.querySelector("#requests-friend-page-items-number").value;
				const lastPage = Math.ceil(numberOfItems/numberOfItemsToShowPerPage);

				getFriendRequests(lastPage);

			});

		}

	}

	function loadGameRequestsPaginationListeners() {

		const buttonFirstPage = document.querySelector(".requests-game-page-first");

		if(buttonFirstPage !== null) {

			buttonFirstPage.addEventListener("click", () => {

				getGameRequests(1);

			});

		}

		const buttonsChangePage = document.querySelectorAll(".requests-game-page-target");

		if(buttonsChangePage !== null) {

			buttonsChangePage.forEach(buttonChangePage => {

				buttonChangePage.addEventListener("click", event => {

					const targetPage = event.target.innerText;

					getGameRequests(targetPage);

				});

			});

		}

		const buttonLastPage = document.querySelector(".requests-game-page-last");

		if(buttonLastPage !== null) {

			buttonLastPage.addEventListener("click", () => {

				const numberOfItems = document.querySelector("#requests-game-number").value;
				const numberOfItemsToShowPerPage = document.querySelector("#requests-game-page-items-number").value;
				const lastPage = Math.ceil(numberOfItems/numberOfItemsToShowPerPage);

				getGameRequests(lastPage);

			});

		}

	}

	function loadOnlineFriendsPaginationListeners() {

		const buttonFirstPage = document.querySelector(".friends-online-page-first");

		if(buttonFirstPage !== null) {

			buttonFirstPage.addEventListener("click", () => {

				getFriends(1, true, false);

			});

		}

		const buttonsChangePage = document.querySelectorAll(".friends-online-page-target");

		if(buttonsChangePage !== null) {

			buttonsChangePage.forEach(buttonChangePage => {

				buttonChangePage.addEventListener("click", event => {

					const targetPage = event.target.innerText;

					getFriends(targetPage, true, false);

				});

			});

		}

		const buttonLastPage = document.querySelector(".friends-online-page-last");

		if(buttonLastPage !== null) {

			buttonLastPage.addEventListener("click", () => {

				const numberOfItems = document.querySelector("#friends-online-number").value;
				const numberOfItemsToShowPerPage = document.querySelector("#friends-online-page-items-number").value;
				const lastPage = Math.ceil(numberOfItems/numberOfItemsToShowPerPage);

				getFriends(lastPage, true, false);

			});

		}

	}

	function replaceClass(id, oldClass, newClass) {

		let element = document.querySelector("#" + id);

		element.classList.remove(oldClass);
    	element.classList.add(newClass);

	}

	var page = location.href.substring(location.href.lastIndexOf("/") + 1);

	loadLeaveGameListeners();

	if(("registration" === page) 
		|| ("connection" === page))
		loadShowPasswordListener();

	if(("registration" !== page)
		&& ("connection" !== page)) {

		var user;

		var friendRequests = [];

		var resultsSearchFriendRequest = [];

		inputSearchFriendRequestByLogin = "";

		var gameRequests = [];

		var resultsSearchGameRequest = [];

		inputSearchGameRequestByLogin = "";

		var friends = [];
		var onlineFriends = [];

		var resultsSearchOnlineFriend = [];

		inputSearchOnlineFriendByLogin = "";

		loadShowFriendRequestsListener();
		loadHideFriendRequestsListener();

		loadSearchFriendRequestListeners();
		loadFriendRequestsPaginationListeners();

		loadAcceptFriendRequestListeners();
		loadDeclineFriendRequestListeners();

		loadShowGameRequestsListener();
		loadHideGameRequestsListener();

		loadSearchGameRequestListeners()
		loadGameRequestsPaginationListeners()

		loadCancelGameRequestListeners();	
		loadDeclineGameRequestListeners();

		loadShowOnlineFriendsListener();
		loadHideOnlineFriendsListener();

		loadSearchOnlineFriendListeners();
		loadOnlineFriendsPaginationListeners()

		loadSendGameRequestListeners();

		getUser();

		setInterval(() => {
			getUser();
		}, 3000);

		setInterval(() => {
			getFriendRequests(null);
		}, 3000);

		setInterval(() => {
			getGameRequests(null);
		}, 3000);

		setInterval(() => {
			getFriends(null, false, false)
		}, 3000);

	}

	if(page.indexOf("game?id=") !== -1) {

		loadPlayGameListeners();

		var refreshGame = setInterval(() => {
			getGame();
		}, 1000);

	}

	if("games" === page) {

		var games = [];

		var resultsSearchGame = [];

		var inputSearchGameById = "";
		var inputSearchGameByType = "";
		var inputSearchGameByPlayer1 = "";
		var inputSearchGameByPlayer2 = "";
		var inputSearchGameByStart = "";

		loadSearchGameListeners()
		loadPaginationListeners();

		loadSendFriendRequestListeners();
		loadCancelFriendRequestListeners();

		setInterval(() => {
			getGames(null);
		}, 3000);

	}

	if("scores" === page) {

		var scores = [];

		var resultsSearchScore = [];

		var inputSearchScoreByRank = "";
		var inputSearchScoreByLogin = "";
		var inputSearchScoreByRatio = "";
		var inputSearchScoreByWins = "";
		var inputSearchScoreByDefeats = "";
		var inputSearchScoreByDraws = "";
		var inputSearchScoreByBestWinStreak = "";

		var friendsOnly = false;

		loadSearchScoreListeners();
		loadPaginationListeners();

		loadFilterScoresByFriendsListener();

		setInterval(() => {
			getScores(null);
		}, 3000);

	}

	if("profile" === page) {

		var resultsSearchFriend = [];

		var inputSearchFriendByLogin = "";
		var inputSearchFriendByLastConnection = "";

		loadShowPasswordListener();

		loadUpdateUserListener();
		loadDeleteUserListener();

		loadSearchFriendListeners();
		loadPaginationListeners();

		loadRemoveFriendListeners();

	}

	if("users" === page) {

		var users = [];

		var resultsSearchUser = [];

		var inputSearchUserByLogin = "";

		loadSearchUserListeners();
		loadPaginationListeners();

		loadSendFriendRequestListeners();
		loadCancelFriendRequestListeners();

		setInterval(() => {
			getUsers(null);
		}, 3000);

	}

});