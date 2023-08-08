package com.mph.beans;

import java.util.Set;
import java.util.HashSet;

import java.util.Objects;

import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.atomic.AtomicLong;

import java.io.Serializable;

import java.time.LocalDateTime;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final AtomicLong count = new AtomicLong();

	private ScheduledExecutorService executor;

	private long id;

	private User player1;

	private User player2;

	private GameType type;

	private GameState state;

	private LocalDateTime start;

	private Board board;

	private int turn;

	private int timer;

	private Set<User> spectators;

	private User winner;

	public Game() {
	}

	public Game(User player1) {

		this.player1 = player1;

		id = count.incrementAndGet();

		type = GameType.RANKED;

		state = GameState.PENDING;

		board = new Board();

		turn = 1;
		timer = 15;

		spectators = new HashSet<User>();

	}

	public Game(User player1, User player2) {

		this.player1 = player1;
		this.player2 = player2;

		id = count.incrementAndGet();

		type = GameType.CASUAL;

		state = GameState.PENDING;

		board = new Board();

		turn = 1;
		timer = 15;

		spectators = new HashSet<User>();

	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;

		if(obj == null)
			return false;

		if(getClass() != obj.getClass())
			return false;

		Game other = (Game) obj;

		return id == other.id;

	}

	public ScheduledExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ScheduledExecutorService executor) {
		this.executor = executor;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getPlayer1() {
		return player1;
	}

	public void setPlayer1(User player1) {
		this.player1 = player1;
	}

	public User getPlayer2() {
		return player2;
	}

	public void setPlayer2(User player2) {
		this.player2 = player2;
	}

	public GameType getType() {
		return type;
	}

	public void setType(GameType type) {
		this.type = type;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public Set<User> getSpectators() {
		return spectators;
	}

	public void setSpectators(Set<User> spectators) {
		this.spectators = spectators;
	}

	public void addSpectator(User spectator) {
		spectators.add(spectator);
	}

	public void removeSpectator(User spectator) {
		spectators.remove(spectator);
	}

	public User getWinner() {
		return winner;
	}

	public void setWinner(User winner) {
		this.winner = winner;
	}

}