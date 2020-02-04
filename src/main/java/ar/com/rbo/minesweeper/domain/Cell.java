package ar.com.rbo.minesweeper.domain;

/**
 * Represents a cell from the board
 */
public class Cell {

	/**
	 * Possible states of a cell
	 */
	public enum State {
		UNKNOWN,
		EMPTY,
		MINED,
		FLAGGED,
		MARKED
	}
	
	private State state;
	private Integer adjacentMines;
	
	/**
	 * Required by Jackson
	 */
	public Cell() {}
	
	/**
	 * Initializes the cell with a state and an amount of adjacent mines
	 */
	public Cell(State state, Integer adjacentMines) {
		this.state = state;
		this.adjacentMines = adjacentMines;
	}

	/**
	 * Returns the state of the mine
	 */
	public State getState() {
		return state;
	}

	/**
	 * Returns the amount of adjacent mines
	 */
	public int getAdjacentMines() {
		return adjacentMines;
	}
	
	/**
	 * Returns a copy of the cell with the received state
	 */
	public Cell changeState(State state) {
		return new Cell(state, this.adjacentMines);
	}
}
