package ar.com.rbo.minesweeper.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Representation of a game of minesweeper 
 */
public class Game {
	
	/**
	 * Possible states of the game
	 */
	public enum GameState {
		IN_PROGRESS,
		LOST,
		WON
	}
	
	/**
	 * Possible states of a cell on the board
	 */
	public enum CellState {
		UNKNOWN,
		EMPTY,
		MINED,
		FLAGGED,
		MARKED
	}
	
	/**
	 * State of the game
	 */
	private GameState state;
	
	/**
	 * Parameters that define the game
	 */
	private int rowCount;
	private int colCount;
	private int mineCount;
	
	/**
	 * State of the board that the player can see
	 */
	private CellState[][] board;
	
	/**
	 * Distribution of mines within the board
	 */
	private boolean[][] mines;
	
	/**
	 * Initializes a game of minesweeper
	 */
	public Game(int rowCount, int colCount, int mineCount) {
		this.rowCount = rowCount;
		this.colCount = colCount;
		this.mineCount = mineCount;
		
		this.state = GameState.IN_PROGRESS;

		initBoard();
	}
	
	/**
	 * Initializes the board with all unknown cells and a random distribution of mines
	 */
	private void initBoard() {
		this.board = new CellState[rowCount][colCount];
		this.mines = new boolean[rowCount][colCount];
		
		List<Boolean> cells = new ArrayList<>(rowCount * colCount);
		
		IntStream.range(0, rowCount * colCount - mineCount).forEach(cellIndex -> cells.add(Boolean.FALSE));
		IntStream.range(0, mineCount).forEach(cellIndex -> cells.add(Boolean.TRUE));
		
		Collections.shuffle(cells);
		
		IntStream.range(0, rowCount * colCount)
			.forEach(cellIndex -> {
				int row = cellIndex / colCount;
				int col = cellIndex - row * colCount;
				board[row][col] = CellState.UNKNOWN;
				mines[row][col] = cells.get(cellIndex);
			});
	}
	
	/**
	 * Reveals a cell of the board. If the revealed cell turns out to be mined then the game is lost, if not,
	 * adjacent cells are revealed (unless they were already revealed, flagged or marked)
	 * 
	 * @throws IllegalAccessException if game is no longer in progress (player either lost or won)
	 */
	public void reveal(int row, int col) throws IllegalAccessException {
		validateMove(row, col);
		
		if (mines[row][col]) {
			board[row][col] = CellState.MINED;
			state = GameState.LOST;
		} else {
			revealRecursive(row, col);
		}
	}
	
	/**
	 * Marks a cell as flagged
	 * 
	 * @throws IllegalAccessException if game is no longer in progress (player either lost or won)
	 */
	public void flag(int row, int col) throws IllegalAccessException {
		validateMove(row, col);
		board[row][col] = CellState.FLAGGED;
	}
	
	/**
	 * Marks a cell with a question mark
	 * 
	 * @throws IllegalAccessException if game is no longer in progress (player either lost or won) 
	 */
	public void mark(int row, int col) throws IllegalAccessException {
		validateMove(row, col);
		board[row][col] = CellState.MARKED;
	}
	
	/**
	 * Removes any previous marking and renders the cell as in UNKNOWN state
	 * 
	 * @throws IllegalAccessException if game is no longer in progress (player either lost or won)
	 */
	public void clear(int row, int col) throws IllegalAccessException {
		validateMove(row, col);
		board[row][col] = CellState.UNKNOWN;
	}
	
	/**
	 * Returns the state the game is in
	 */
	public GameState getState() {
		return state;
	}
	
	/**
	 * Returns the amount of rows of the board
	 */
	public int getRowCount() {
		return rowCount;
	}
	
	/**
	 * Returns the amount of columns of the board
	 */
	public int getColCount() {
		return colCount;
	}

	/**
	 * Returns the amount of mines on the board
	 */
	public int getMineCount() {
		return mineCount;
	}
	
	/**
	 * Returns the state of a cell in the board
	 */
	public CellState getCellState(int row, int col) {
		return board[row][col];
	}
	
	/**
	 * Recursively reveals all adjacent cells that need to be revealed
	 */
	private void revealRecursive(int row, int col) {
		if (shouldBeRevealed(row, col)) {
			board[row][col] = CellState.EMPTY;
			revealRecursive(row + 1, col);
			revealRecursive(row - 1, col);
			revealRecursive(row, col + 1);
			revealRecursive(row, col - 1);
		}
	}

	/**
	 * Returns whether or not a cell should be revealed by recursion
	 */
	private boolean shouldBeRevealed(int row, int col) {
		return 
				row >= 0 && row < rowCount &&
				col >= 0 && col < colCount &&
				GameState.IN_PROGRESS == state &&
				CellState.UNKNOWN == board[row][col];
	}

	/**
	 * Validates a given move
	 *
	 * @throws IllegalAccessException if the move is illegal
	 */
	private void validateMove(int row, int col) throws IllegalAccessException {
		
		if (GameState.IN_PROGRESS != state) {
			throw new IllegalAccessException("Game is no longer in progress");
		}
		
		if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
			throw new IllegalAccessException("Cell coordinates (" + row + ", " + col + ") outside existing board");
		}
	}
}
