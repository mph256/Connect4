package com.mph.services;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import java.util.Comparator;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.TimerTask;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.mph.services.interfaces.GameService;
import com.mph.services.interfaces.ScoreService;

import com.mph.beans.Game;
import com.mph.beans.GameType;
import com.mph.beans.GameState;
import com.mph.beans.Board;
import com.mph.beans.Square;
import com.mph.beans.Token;
import com.mph.beans.Color;
import com.mph.beans.User;
import com.mph.beans.Score;

public class GameServiceImpl implements GameService {

	private static Queue<Game> pendingGames;

	private static Map<String, Game> runningGames;

	private static Map<String, Game> gamesOver;

	private ScoreService scoreService;

	static {

		pendingGames = new ConcurrentLinkedQueue<Game>();
		runningGames = new ConcurrentHashMap<String, Game>();
		gamesOver = new ConcurrentHashMap<String, Game>();

	}

	public GameServiceImpl() {
		scoreService = new ScoreServiceImpl();
	}

	@Override 
	public Game joinGame(User user) {

		Game game = null;

		if(!user.isInGame()) {

			if(pendingGames.isEmpty()) {

				game = new Game(user);

				pendingGames.add(game);

			} else {

				game = pendingGames.element();

				game.setPlayer2(user);

				startGame(game);

			}

			user.setInGame(true);

		} else
			game = getGameByUser(user);

		return game;

	}

	@Override 
	public Game joinGame(User user1, User user2) {

		Game game = new Game(user1, user2);

		pendingGames.add(game);

		startGame(game);

		user1.setInGame(true);
		user2.setInGame(true);

		return game;

	}

	@Override
	public Game observeGame(long gameId, User user) {

		Game game = runningGames.get(Long.toString(gameId));

		game.addSpectator(user);

		user.setInGame(true);

		return game;

	}
	
	@Override
	public void leaveGame(long gameId, User user) {

		Game game = getGameById(gameId);

		GameState state = game.getState();

		if(GameState.PENDING.equals(state)) {

			game.setState(GameState.CANCELED);

			removePendingGame(game);
			gamesOver.put(Long.toString(gameId), game);

		} else {

			if(GameState.RUNNING.equals(state)) {

				boolean isPlayer1 = user.equals(game.getPlayer1());
				boolean isPlayer2 = user.equals(game.getPlayer2());

				if(isPlayer1 || isPlayer2) {

					game.setState(GameState.OVER);

					runningGames.remove(Long.toString(game.getId()));
					gamesOver.put(Long.toString(gameId), game);

					if(isPlayer1) {

						User player2 = game.getPlayer2();

						game.setWinner(player2);
	
						if(GameType.RANKED.equals(game.getType())) {

							Set<String> fields = new HashSet<String>();

							fields.add("wins");
							updateScore(player2, fields);

							fields.remove("wins");
							fields.add("defeats");
							updateScore(user, fields);

						}

						if(player2.isOnline())
							player2.setInGame(false);

					} else {

						User player1 = game.getPlayer1();

						game.setWinner(player1);
			
						if(GameType.RANKED.equals(game.getType())) {

							Set<String> fields = new HashSet<String>();

							fields.add("wins");
							updateScore(player1, fields);

							fields.remove("wins");
							fields.add("defeats");
							updateScore(user, fields);

						}

						if(player1.isOnline())
							player1.setInGame(false);

					}

					game.getSpectators().forEach(x -> x.setInGame(false));

				} else
					game.removeSpectator(user);

			}

		}

		user.setInGame(false);

	}
	
	@Override
	public void leaveGame(long gameId) {

		Game game = getGameById(gameId);

		User player1 = game.getPlayer1();
		User player2 = game.getPlayer2();

		if(!player1.isOnline())
			leaveGame(gameId, player1);

		if(!player2.isOnline())
			leaveGame(gameId, player2);

		for(User spectator: game.getSpectators()) {

			if(!spectator.isOnline())
				leaveGame(gameId, spectator);

		}

	}

	@Override
	public Game dropToken(long gameId, int column, User user) {

		Game game = getGameById(gameId);

		if(GameState.RUNNING.equals(game.getState())) {

			boolean isPlayer1 = user.equals(game.getPlayer1());
			boolean isPlayer2 = user.equals(game.getPlayer2());

			if(isPlayer1 || isPlayer2) {
	
				int turn = game.getTurn();
	
				if((isPlayer1 && (turn % 2 != 0))
					|| (isPlayer2 && (turn % 2 == 0))) {

					Board board = game.getBoard();
					Square[][] squares = board.getSquares();

					if(squares[0][column].getToken() != null)
						return game;

					Color color = isPlayer1?Color.RED:Color.YELLOW;
					Token token = new Token(color);
	
					squares[0][column].setToken(token);

					applyGravity(squares, column);

					if(!isGameOver(game)) {

						game.setTurn(turn+1);

						restartTimer(game);

					}

				}

			}

		}

		return game;

	}

	@Override
	public Game rotateLeft(long gameId, User user) {

		Game game = getGameById(gameId);

		if(GameState.RUNNING.equals(game.getState())) {

			boolean isPlayer1 = user.equals(game.getPlayer1());
			boolean isPlayer2 = user.equals(game.getPlayer2());

			if(isPlayer1 || isPlayer2) {

				int turn = game.getTurn();

				if((isPlayer1 && (turn % 2 != 0))
					|| (isPlayer2 && (turn % 2 == 0))) {

					Board board = game.getBoard();
					Square[][] squares = board.getSquares();

					int rows = board.getRows();
					int columns = board.getColumns();

					Square[][] tmp = new Square[columns][rows];

					for(int i = 0; i < tmp.length; i++) {
						for(int j = 0; j < tmp[i].length; j++) {

							tmp[i][j] = squares[j][columns-1-i];

						}
					}

					board.setRows(columns);
					board.setColumns(rows);

					applyGravity(tmp);

					board.setSquares(tmp);

					if(!isGameOver(game)) {

						game.setTurn(turn+1);

						restartTimer(game);

					}

				}

			}

		}

		return game;

	}

	@Override
	public Game rotateRight(long gameId, User user) {

		Game game = getGameById(gameId);

		if(GameState.RUNNING.equals(game.getState())) {
	
			boolean isPlayer1 = user.equals(game.getPlayer1());
			boolean isPlayer2 = user.equals(game.getPlayer2());

			if(isPlayer1 || isPlayer2) {
	
				int turn = game.getTurn();
	
				if((isPlayer1 && (turn % 2) != 0)
					|| (isPlayer2 && (turn % 2) == 0)) {

					Board board = game.getBoard();
					Square[][] squares = board.getSquares();
	
					int rows = board.getRows();
					int columns = board.getColumns();

					Square[][] tmp = new Square[columns][rows];
	
					for(int i = 0; i < tmp.length; i++) {
						for(int j = 0; j < tmp[i].length; j++) {

							tmp[i][j] = squares[rows-1-j][i];

						}
					}

					board.setRows(columns);
					board.setColumns(rows);

					applyGravity(tmp);

					board.setSquares(tmp);

					if(!isGameOver(game)) {

						game.setTurn(turn+1);

						restartTimer(game);

					}

				}
	
			}

		}

		return game;

	}
	
	@Override
	public Game getGameById(long gameId) {

		Game game = getPendingGameById(gameId);

		if(game == null)
			game = runningGames.get(Long.toString(gameId));

		if(game == null)
			game = gamesOver.get(Long.toString(gameId));

		return game;

	}
	
	@Override
	public Game getGameByUser(User user) {
	
		Game game = null;

		game = getPendingGameByUser(user);

		if(game == null)
			game = runningGames.values()
				.stream()
				.filter(x-> user.equals(x.getPlayer1()) || user.equals(x.getPlayer2()) || x.getSpectators().contains(user))
				.findFirst()
				.orElse(null);

		if(game == null)
			game = gamesOver.values()
				.stream()
				.filter(x-> user.equals(x.getPlayer1()) || user.equals(x.getPlayer2()) || x.getSpectators().contains(user))
				.findFirst()
				.orElse(null);

		return game;

	}

	@Override
	public Set<Game> getGames() {

		return new LinkedHashSet<Game>(
			runningGames.values()
			.stream()
			.sorted(Comparator.comparing(Game::getStart).reversed())
			.collect(Collectors.toSet())
		);

	}

	private void startGame(Game game) {
		
		pendingGames.remove();
		runningGames.put(Long.toString(game.getId()), game);

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		TimerTask task = new TimerTask() {

	        @Override
	        public void run() {

	    		game.setState(GameState.RUNNING);
				game.setStart(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

				startTimer(game);    

	        }

	    };

	    executor.schedule(task, 5, TimeUnit.SECONDS);

	}

	private void startTimer(Game game) {
		
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		game.setExecutor(executor);

		TimerTask task = new TimerTask() {

	        @Override
	        public void run() {

	        	if(GameState.OVER.equals(game.getState()))
	        		executor.shutdownNow();

	        	int timer = game.getTimer();

	        	if(timer == 1) {

	        		game.setTurn(game.getTurn() + 1);
	        		game.setTimer(15);

	        	} else
	        		game.setTimer(timer - 1);    

	        }

	    };

	    executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);

	}

	private void restartTimer(Game game) {

		ScheduledExecutorService executor = game.getExecutor();

		executor.shutdownNow();

		game.setTimer(15);

		startTimer(game);

	}

	private boolean checkColumn(Square[][] squares, int i, int j) {

		Color color = squares[i][j].getToken().getColor();

		if((i - 1 >= 0) && (squares[i-1][j].getToken() != null) && (color.equals(squares[i-1][j].getToken().getColor()))) {

			if((i - 2 >= 0) && (squares[i-2][j].getToken() != null) && (color.equals(squares[i-2][j].getToken().getColor()))) {

				if((i - 3 >= 0) && (squares[i-3][j].getToken() != null) && (color.equals(squares[i-3][j].getToken().getColor()))
					|| ((i + 1 < squares.length) && (squares[i+1][j].getToken() != null) && (color.equals(squares[i+1][j].getToken().getColor())))) {

					return true;

				}

			} else if(((i + 1 < squares.length) && (squares[i+1][j].getToken() != null) && (color.equals(squares[i+1][j].getToken().getColor())))
				&& ((i + 2 < squares.length) && (squares[i+2][j].getToken() != null) && (color.equals(squares[i+2][j].getToken().getColor())))) {

				return true;

			}

		} else if(((i + 1 < squares.length) && (squares[i+1][j].getToken() != null) && (color.equals(squares[i+1][j].getToken().getColor())))
			&& ((i + 2 < squares.length) && (squares[i+2][j].getToken() != null) && (color.equals(squares[i+2][j].getToken().getColor())))
			&& ((i + 3 < squares.length) && (squares[i+3][j].getToken() != null) && (color.equals(squares[i+3][j].getToken().getColor())))) {

			return true;

		}

		return false;

	}

	private boolean checkRow(Square[][] squares, int i, int j) {

		Color color = squares[i][j].getToken().getColor();

		if((j - 1 >= 0) && (squares[i][j-1].getToken() != null) && (color.equals(squares[i][j-1].getToken().getColor()))) {

			if((j - 2 >= 0) && (squares[i][j-2].getToken() != null) && (color.equals(squares[i][j-2].getToken().getColor()))) {

				if(((j - 3 >= 0) && (squares[i][j-3].getToken() != null) && (color.equals(squares[i][j-3].getToken().getColor())))
					|| ((j + 1 < squares[i].length) && (squares[i][j+1].getToken() != null) && (color.equals(squares[i][j+1].getToken().getColor())))) {

					return true;
	
				}

			} else if(((j + 1 < squares[i].length) && (squares[i][j+1].getToken() != null) && (color.equals(squares[i][j+1].getToken().getColor())))
				&& ((j + 2 < squares[i].length) && (squares[i][j+2].getToken() != null) && (color.equals(squares[i][j+2].getToken().getColor())))) {

				return true;
	
			}

		} else if(((j + 1 < squares[i].length) && (squares[i][j+1].getToken() != null) && (color.equals(squares[i][j+1].getToken().getColor())))
			&& ((j + 2 < squares[i].length) && (squares[i][j+2].getToken() != null) && (color.equals(squares[i][j+2].getToken().getColor())))
			&& ((j + 3 < squares[i].length) && (squares[i][j+3].getToken() != null) && (color.equals(squares[i][j+3].getToken().getColor())))) {

			return true;

		}
		
		return false;
		
	}

	private boolean checkLeftDiagonal(Square[][] squares, int i, int j) {

		Color color = squares[i][j].getToken().getColor();

		if((i - 1 >= 0) && (j - 1 >= 0) && (squares[i-1][j-1].getToken() != null) && (color.equals(squares[i-1][j-1].getToken().getColor()))) {

			if((i - 2 >= 0) && (j - 2 >= 0) && (squares[i-2][j-2].getToken() != null) && (color.equals(squares[i-2][j-2].getToken().getColor()))) {

				if(((i - 3 >= 0) && (j - 3 >= 0) && (squares[i-3][j-3].getToken() != null) && (color.equals(squares[i-3][j-3].getToken().getColor())))
					|| ((i + 1 < squares.length) && (j + 1 < squares[i].length) && (squares[i+1][j+1].getToken() != null) && (color.equals(squares[i+1][j+1].getToken().getColor())))) {

					return true;

				}

			} else if(((i + 1 < squares.length) && (j + 1 < squares[i].length) && (squares[i+1][j+1].getToken() != null) && (color.equals(squares[i+1][j+1].getToken().getColor())))
				&& ((i + 2 < squares.length) && (j + 2 < squares[i].length) && (squares[i+2][j+2].getToken() != null) && (color.equals(squares[i+2][j+2].getToken().getColor())))) {

					return true;

			}

		} else if(((i + 1 < squares.length) && (j + 1 < squares[i].length) && (squares[i+1][j+1].getToken() != null) && (color.equals(squares[i+1][j+1].getToken().getColor())))
			&& ((i + 2 < squares.length) && (j + 2 < squares[i].length) && (squares[i+2][j+2].getToken() != null) && (color.equals(squares[i+2][j+2].getToken().getColor())))
			&& ((i + 3 < squares.length) && (j + 3 < squares[i].length) && (squares[i+3][j+3].getToken() != null) && (color.equals(squares[i+3][j+3].getToken().getColor())))) {

			return true;

		}

		return false;

	}

	private boolean checkRightDiagonal(Square[][] squares, int i, int j) {

		Color color = squares[i][j].getToken().getColor();

		if((i - 1 >= 0) && (j + 1 < squares[i].length) && (squares[i-1][j+1].getToken() != null) && (color.equals(squares[i-1][j+1].getToken().getColor()))) {

			if((i - 2 >= 0) && (j + 2 < squares[i].length) && (squares[i-2][j+2].getToken() != null) && (color.equals(squares[i-2][j+2].getToken().getColor()))) {

				if(((i - 3 >= 0) && (j + 3 < squares[i].length) && (squares[i-3][j+3].getToken() != null) && (color.equals(squares[i-3][j+3].getToken().getColor())))
					|| ((i + 1 < squares.length) && (j - 1 >= 0) && (squares[i+1][j-1].getToken() != null) && (color.equals(squares[i+1][j-1].getToken().getColor())))) {

					return true;

				}

			} else if(((i + 1 < squares.length) && (j - 1 >= 0) && (squares[i+1][j-1].getToken() != null) && (color.equals(squares[i+1][j-1].getToken().getColor())))
				&& ((i + 2 < squares.length) && (j - 2 >= 0) && (squares[i+2][j-2].getToken() != null) && (color.equals(squares[i+2][j-2].getToken().getColor())))) {

					return true;

			}

		} else if(((i + 1 < squares.length) && (j - 1 >= 0) && (squares[i+1][j-1].getToken() != null) && (color.equals(squares[i+1][j-1].getToken().getColor()))
			&& ((i + 2 < squares.length) && (j - 2 >= 0) && (squares[i+2][j-2].getToken() != null) && (color.equals(squares[i+2][j-2].getToken().getColor())))
			&& ((i + 3 < squares.length) && (j - 3 >= 0) && (squares[i+3][j-3].getToken() != null) && (color.equals(squares[i+3][j-3].getToken().getColor()))))) {

			return true;

		}

		return false;

	}

	private void applyGravity(Square[][] squares) {

		for(int i = squares.length - 1; i >= 0; i--) {
			for(int j = 0; j < squares[i].length; j++) {

				if(squares[i][j].getToken() != null) {

					for(int k = i; k < squares.length - 1; k++) {

						if(squares[k+1][j].getToken() == null) {

							Token token = squares[k][j].getToken();
							squares[k+1][j].setToken(token);
							squares[k][j].setToken(null);

						} else
							break;

					}

				}

			}
		}

	}

	private void applyGravity(Square[][] squares, int column) {

		Token token = squares[0][column].getToken();

		for(int i = 0; i < squares.length-1; i++) {

			if(squares[i+1][column].getToken() == null) {

				 squares[i][column].setToken(null);
				 squares[i+1][column].setToken(token);

			} else
				break;	

		}

	}
	
	private boolean isGameOver(Game game) {

		long gameId = game.getId();

		Board board = game.getBoard();
		Square[][] squares = board.getSquares();

		User player1 = game.getPlayer1();
		User player2 = game.getPlayer2();

		User winner = null;

		boolean isDrawGame = false;
		boolean isGameOver = false;

		for(int j = 0; j < squares[0].length; j++) {

			if(squares[0][j].getToken() != null) 
				isDrawGame = true;
			else {

				isDrawGame = false;
				break;

			}

		}

		if(isDrawGame)
			isGameOver = true;
		else {

			for(int i = 0; i < squares.length; i++) {
				for(int j = 0; j < squares[i].length; j++) {

					Token token = squares[i][j].getToken();

					if(token != null) {

						if(checkColumn(squares, i, j)
							|| checkRow(squares, i, j)
							|| checkLeftDiagonal(squares, i, j)
							|| checkRightDiagonal(squares, i, j)) {

							if(winner == null)
								winner = (Color.RED.equals(token.getColor()))?player1:player2;
							else {

								if(((player1.equals(winner)) && (Color.YELLOW.equals(token.getColor())))
									|| ((player2.equals(winner)) && (Color.RED.equals(token.getColor()))))
									isDrawGame = true;

							}

							isGameOver = true;

						}

					}

				}
			}

		}

		if(isGameOver) {

			game.setState(GameState.OVER);

			runningGames.remove(Long.toString(gameId));	
			gamesOver.put(Long.toString(gameId), game);

			player1.setInGame(false);
			player2.setInGame(false);

			game.getSpectators().forEach(x -> x.setInGame(false));

			if(isDrawGame) {

				if(GameType.RANKED.equals(game.getType())) {

					Set<String> fields = new HashSet<String>();

					fields.add("draws");

					updateScore(player1, fields);
					updateScore(player2, fields);

				}

				return true;

			}

			if(player1.equals(winner)) {

				game.setWinner(player1);
				
				if(GameType.RANKED.equals(game.getType())) {

					Set<String> fields = new HashSet<String>();

					fields.add("wins");
					updateScore(player1, fields);

					fields.remove("wins");
					fields.add("defeats");
					updateScore(player2, fields);
	
				}

				return true;

			}

			if(player2.equals(winner)) {

				game.setWinner(player2);
		
				if(GameType.RANKED.equals(game.getType())) {

					Set<String> fields = new HashSet<String>();

					fields.add("wins");
					updateScore(player2, fields);

					fields.remove("wins");
					fields.add("defeats");
					updateScore(player1, fields);
				
				}

				return true;

			}

		}

		return false;

	}
	
	private void updateScore(User user, Set<String> fields) {

		Score score = user.getScore();

		for(String field: fields) {

			switch(field) {

				case "wins":

					score.setWins(score.getWins() + 1);
					fields.add("winStreak");

					int winStreak = score.getWinStreak() + 1;
					score.setWinStreak(winStreak);

					if(winStreak > score.getBestWinStreak()) {

						fields.add("bestWinStreak");
						score.setBestWinStreak(winStreak);

					}

					break;

				case "defeats":

					score.setDefeats(score.getDefeats() + 1);
					score.setWinStreak(0);

					break;

				case "draws":

					score.setDraws(score.getDraws() + 1);

					break;

			}

		}

		scoreService.updateScore(score, fields);

		user.setScore(score);

	}

	private void removePendingGame(Game game) {

		Queue<Game> tmp = new LinkedList<Game>();

		int size = pendingGames.size();
		int counter = 0;

		while(!pendingGames.isEmpty() && !game.equals(pendingGames.element())) {

	        tmp.add(pendingGames.element());
	        pendingGames.remove();

	        counter++;

	    }

	    if(pendingGames.isEmpty()) {

	    	while(!tmp.isEmpty()) {

	    		pendingGames.add(tmp.element());
    			tmp.remove();

    		}

	    } else {

	    	pendingGames.remove();

	        while(!tmp.isEmpty()) {

	        	pendingGames.add(tmp.element());
	        	tmp.remove();

	        }

	        int i = size - counter - 1;

	        while(i-- > 0) {

	            Game game2 = pendingGames.element();
	            pendingGames.remove();
	            pendingGames.add(game2);

	        }

	    }

	}

	private Game getPendingGameById(long gameId) {
	
		Queue<Game> tmp = new LinkedList<Game>();

		Game game = null;

		while(!pendingGames.isEmpty() && gameId != Long.valueOf(pendingGames.element().getId())) {

	        tmp.add(pendingGames.element());
	        pendingGames.remove();

	    }

	    if(pendingGames.isEmpty()) {

	    	while(!tmp.isEmpty()) {

	    		pendingGames.add(tmp.element());
    			tmp.remove();

    		}

	    } else {

	    	game = pendingGames.element();

	        while(!tmp.isEmpty()) {

	        	pendingGames.add(tmp.element());
	        	tmp.remove();

	        }

	    }

	    return game;

	}
	
	private Game getPendingGameByUser(User user) {
		
		Queue<Game> tmp = new LinkedList<Game>();

		Game game = null;

		while(!pendingGames.isEmpty()
			&& !user.equals(pendingGames.element().getPlayer1())
			&& !user.equals(pendingGames.element().getPlayer2())
			&& !pendingGames.element().getSpectators().contains(user)) {

	        tmp.add(pendingGames.element());
	        pendingGames.remove();

	    }

	    if(pendingGames.isEmpty()) {

	    	while(!tmp.isEmpty()) {

	    		pendingGames.add(tmp.element());
    			tmp.remove();

    		}

	    } else {

	    	game = pendingGames.element();

	        while(!tmp.isEmpty()) {

	        	pendingGames.add(tmp.element());
	        	tmp.remove();

	        }

	    }

	    return game;

	}

}