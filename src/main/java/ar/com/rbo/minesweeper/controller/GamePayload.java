package ar.com.rbo.minesweeper.controller;

import java.util.UUID;

import ar.com.rbo.minesweeper.domain.Game;
import ar.com.rbo.minesweeper.domain.Game.CellState;
import ar.com.rbo.minesweeper.domain.Game.GameState;

/**
 * Payload that represents the current state of a {@link Game}
 */
public class GamePayload {
	
	private UUID id;

	private int rowCount;
	private int colCount;
	private int mineCount;

	private Game.GameState state;
	
	private Game.CellState[][] board;
	
	/**
	 * Needed by Jackson
	 */
	public GamePayload() {}

	/**
	 * Initializes the game payload
	 */
	public GamePayload(UUID id, int rowCount, int colCount, int mineCount, GameState state, CellState[][] board) {
		this.id = id;
		this.rowCount = rowCount;
		this.colCount = colCount;
		this.mineCount = mineCount;
		this.state = state;
		this.board = board;
	}
	
	/**
	 * Returns the game's unique id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Returns the game's row count
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Returns the game's column count
	 */
	public int getColCount() {
		return colCount;
	}

	/**
	 * Returns the game's mine count
	 * @return
	 */
	public int getMineCount() {
		return mineCount;
	}

	/**
	 * Returns the game's current state
	 */
	public Game.GameState getState() {
		return state;
	}

	/**
	 * Returns the current state of the board
	 */
	public Game.CellState[][] getBoard() {
		return board;
	}
}