package ar.com.rbo.minesweeper.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.google.common.annotations.VisibleForTesting;

/**
 * Service class for {@link Game} entity
 */
@Service
public class GameService {
	
	private Map<UUID, Game> games;
	
	/**
	 * Initializes the service with no games being played
	 */
	public GameService() {
		this(new HashMap<>());
	}
	
	@VisibleForTesting
	public GameService(Map<UUID, Game> games) {
		this.games = games;
	}
	
	/**
	 * Returns all stored games
	 */
	public Collection<Game> findGames() {
		return games.values();
	}
	
	/**
	 * Returns the game that matches the received id (if found) 
	 */
	public Optional<Game> findGame(UUID id) {
		return Optional.ofNullable(games.get(id));
	}
	
	/**
	 * Creates a game with the given parameters and returns the created game
	 */
	public Game createGame(int rowCount, int colCount, int mineCount) {
		Game game = new Game(rowCount, colCount, mineCount);
		return games.put(game.getId(), game);
	}
	
	/**
	 * Updates a game by making a move and returns the updated game
	 */
	public Game updateGame(UUID id, Move move) throws IllegalAccessException {
		Game game = games.get(id);
		move.apply(game);
		return game;		
	}
}
