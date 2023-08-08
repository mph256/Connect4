package com.mph.beans;

import java.util.Objects;

import java.io.Serializable;

public class GameRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private User sender;

	private User receiver;

	private RequestState state;

	private Game game;

	public GameRequest() {
	}

	public GameRequest(User sender, User receiver) {

		this.sender = sender;
		this.receiver = receiver;

		state = RequestState.PENDING;

	}

	@Override
	public int hashCode() {
		return Objects.hash(sender, receiver);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		GameRequest other = (GameRequest) obj;

		return Objects.equals(sender, other.sender) && Objects.equals(receiver, other.receiver);

	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public RequestState getState() {
		return state;
	}

	public void setState(RequestState state) {
		this.state = state;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}