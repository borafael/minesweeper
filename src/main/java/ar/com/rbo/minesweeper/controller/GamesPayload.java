package ar.com.rbo.minesweeper.controller;

import java.util.List;

/**
 * Payload for return a {@link List} of {@link GamePayload}s
 */
public class GamesPayload {
	
	private List<GamePayload> games;
	
	/**
	 * Required by Jackson
	 */
	public GamesPayload() {}
	
	/**
	 * Initializes the payload
	 */
	public GamesPayload(List<GamePayload> games) {
		this.games = games;
	}
	
	/**
	 * Returns the {@link List} of {@link GamePayload}s
	 */
	public List<GamePayload> getGames() {
		return games;
	}

}
