package com.mph.beans;

import java.io.Serializable;

import java.io.File;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Objects;

import com.mph.util.Util;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String login;

	private String password;

	private String email;

	private File profilPicture;

	private LocalDate registrationDate;

	private LocalDateTime lastConnection;

	private boolean isOnline;

	private boolean isInGame;

	public User() {
	}

	public User(String login) {
		this.login = login;
	}

	public User(String login, String password, String email) {

		this.login = login;
		this.password = password;
		this.email = email;

		loadProperties();

		registrationDate = LocalDate.now();
		lastConnection = LocalDateTime.now();
		isOnline = true;
		isInGame = false;

	}

	private void loadProperties() {
		profilPicture = Util.getResourceFile(Util.getProperties(new String[]{ "img.profil.default" }).get(0));
	}

	@Override
	public int hashCode() {
		return Objects.hash(login);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public File getProfilPicture() {
		return profilPicture;
	}

	public void setProfilPicture(File profilPicture) {
		this.profilPicture = profilPicture;
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

}