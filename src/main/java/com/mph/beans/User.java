package com.mph.beans;

import java.util.Set;
import java.util.HashSet;

import java.util.Objects;

import java.io.Serializable;

import java.io.File;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.mph.util.Util;

public class User implements Comparable<User>, Serializable {

	private static final long serialVersionUID = 1L;

	private String login;

	private String email;

	private String password;

	private File profilePicture;

	private LocalDate registrationDate;

	private LocalDateTime lastConnection;

	private boolean isOnline;

	private boolean isInGame;

	private Set<User> friends;

	private Set<User> onlineFriends;

	private Set<FriendRequest> friendRequestsSent;

	private Set<FriendRequest> friendRequestsReceived;

	private Set<GameRequest> gameRequestsSent;

	private Set<GameRequest> gameRequestsReceived;

	private Score score;

	public User() {
	}

	public User(String login) {

		this.login = login;

		profilePicture = Util.getProfilePictureByLogin(login);

	}

	public User(String login, LocalDateTime lastConnection, boolean isOnline, boolean isInGame) {

		this.login = login;

		this.lastConnection = lastConnection;

		this.isOnline = isOnline;
		this.isInGame = isInGame;

		profilePicture = Util.getProfilePictureByLogin(login);

	}

	public User(String login, String email, String password) {

		this.login = login;
		this.email = email;
		this.password = password;

		profilePicture = Util.getProfilePictureByLogin(login);

		registrationDate = LocalDate.now();
		lastConnection = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);;

		isOnline = true;
		isInGame = false;

		friends = new HashSet<User>();
		onlineFriends = new HashSet<User>();

		friendRequestsSent = new HashSet<FriendRequest>();
		friendRequestsReceived = new HashSet<FriendRequest>();

		gameRequestsSent = new HashSet<GameRequest>();
		gameRequestsReceived = new HashSet<GameRequest>();

	}

	public User(String login, String email, String password,
		LocalDate registrationDate, LocalDateTime lastConnection,
		boolean isOnline, boolean isInGame) {

		this.login = login;
		this.email = email;
		this.password = password;

		this.registrationDate = registrationDate;
		this.lastConnection = lastConnection;

		this.isOnline = isOnline;
		this.isInGame = isInGame;

		profilePicture = Util.getProfilePictureByLogin(login);

	}

	@Override
	public int compareTo(User user) {
		return login.compareTo(user.getLogin());
	}

	@Override
	public int hashCode() {
		return Objects.hash(login);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;

		if(obj == null)
			return false;

		if(getClass() != obj.getClass())
			return false;

		User other = (User) obj;

		return Objects.equals(login, other.login);

	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public File getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(File profilePicture) {
		this.profilePicture = profilePicture;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public LocalDateTime getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(LocalDateTime lastConnection) {
		this.lastConnection = lastConnection;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isInGame() {
		return isInGame;
	}

	public void setInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}

	public Set<User> getFriends() {
		return friends;
	}

	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}

	public void addFriend(User friend) {
		friends.add(friend);
	}

	public void removeFriend(User friend) {
		friends.remove(friend);
	}

	public Set<User> getOnlineFriends() {
		return onlineFriends;
	}

	public void setOnlineFriends(Set<User> onlineFriends) {
		this.onlineFriends = onlineFriends;
	}

	public void addOnlineFriend(User friend) {
		onlineFriends.add(friend);
	}

	public void removeOnlineFriend(User friend) {
		onlineFriends.remove(friend);
	}

	public Set<FriendRequest> getFriendRequestsSent() {
		return friendRequestsSent;
	}

	public void setFriendRequestsSent(Set<FriendRequest> friendRequestsSent) {
		this.friendRequestsSent = friendRequestsSent;
	}

	public void addFriendRequestSent(FriendRequest friendRequest) {
		friendRequestsSent.add(friendRequest);
	}

	public void removeFriendRequestSent(FriendRequest friendRequest) {
		friendRequestsSent.remove(friendRequest);
	}

	public Set<FriendRequest> getFriendRequestsReceived() {
		return friendRequestsReceived;
	}

	public void setFriendRequestsReceived(Set<FriendRequest> friendRequestsReceived) {
		this.friendRequestsReceived = friendRequestsReceived;
	}

	public void addFriendRequestReceived(FriendRequest friendRequest) {
		friendRequestsReceived.add(friendRequest);
	}

	public void removeFriendRequestReceived(FriendRequest friendRequest) {
		friendRequestsReceived.remove(friendRequest);
	}

	public Set<GameRequest> getGameRequestsSent() {
		return gameRequestsSent;
	}

	public void setGameRequestsSent(Set<GameRequest> gameRequestsSent) {
		this.gameRequestsSent = gameRequestsSent;
	}

	public void addGameRequestSent(GameRequest gameRequest) {
		gameRequestsSent.add(gameRequest);
	}

	public void removeGameRequestSent(GameRequest gameRequest) {
		gameRequestsSent.remove(gameRequest);
	}

	public Set<GameRequest> getGameRequestsReceived() {
		return gameRequestsReceived;
	}

	public void setGameRequestsReceived(Set<GameRequest> gameRequestsReceived) {
		this.gameRequestsReceived = gameRequestsReceived;
	}

	public void addGameRequestReceived(GameRequest gameRequest) {
		gameRequestsReceived.add(gameRequest);
	}

	public void removeGameRequestReceived(GameRequest gameRequest) {
		gameRequestsReceived.remove(gameRequest);
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

}