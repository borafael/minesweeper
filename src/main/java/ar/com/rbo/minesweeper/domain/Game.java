package ar.com.rbo.minesweeper.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import ar.com.rbo.minesweeper.domain.Cell.State;

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
	 * Unique identifier of a game
	 */
	private UUID id;
	
	/**
	 * Date of creation
	 */
	private Date creationDate;
	
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
	 * Counter for empty cells revealed
	 */
	private int emptyCellsRevealed;
	
	/**
	 * State of the board that the player can see
	 */
	private Cell[][] board;
	
	/**
	 * Distribution of mines within the board
	 */
	private boolean[][] mines;
	
	/**
	 * Initializes a game of minesweeper
	 */
	public Game(int rowCount, int colCount, int mineCount) {
		this.id = UUID.randomUUID();
		
		this.creationDate = new Date();
		
		this.rowCount = rowCount;
		this.colCount = colCount;
		this.mineCount = mineCount;
		
		this.emptyCellsRevealed = 0;
		
		this.state = GameState.IN_PROGRESS;

		initBoard();
	}
	
	/**
	 * Initializes the board with all unknown cells and a random distribution of mines
	 */
	private void initBoard() {
		this.board = new Cell[rowCount][colCount];
		this.mines = new boolean[rowCount][colCount];
		
		List<Boolean> cells = new ArrayList<>(rowCount * colCount);
		
		IntStream.range(0, rowCount * colCount - mineCount).forEach(cellIndex -> cells.add(Boolean.FALSE));
		IntStream.range(0, mineCount).forEach(cellIndex -> cells.add(Boolean.TRUE));
		
		Collections.shuffle(cells);
		
		IntStream.range(0, rowCount * colCount)
			.forEach(cellIndex -> {
				int row = cellIndex / colCount;
				int col = cellIndex - row * colCount;
				board[row][col] = new Cell(Cell.State.UNKNOWN, countAdjacentMines(cells, row, col));
				mines[row][col] = cells.get(cellIndex);
			});
	}
	
	/**
	 * Returns the number of adjacent mines of the cell 
	 */
	private int countAdjacentMines(List<Boolean> cells, int row, int col) {
		int adjacentMines = 0;
		
		if (row > 0 && col > 0 && cells.get(toCellIndex(row - 1, col - 1))) {
			adjacentMines++;
		}

		if (row > 0 && cells.get(toCellIndex(row - 1, col))) {
			adjacentMines++;
		}

		if (row > 0 && col < colCount - 1 && cells.get(toCellIndex(row - 1, col + 1))) {
			adjacentMines++;
		}
		
		if (col < colCount - 1 && cells.get(toCellIndex(row, col + 1))) {
			adjacentMines++;
		}
		
		if (col > 0 && cells.get(toCellIndex(row, col - 1))) {
			adjacentMines++;
		}
		
		if (row < rowCount - 1 && col > 0 && cells.get(toCellIndex(row + 1, col - 1))) {
			adjacentMines++;
		}

		if (row < rowCount - 1 && cells.get(toCellIndex(row + 1, col))) {
			adjacentMines++;
		}

		if (row < rowCount - 1 && col < colCount - 1 && cells.get(toCellIndex(row + 1, col + 1))) {
			adjacentMines++;
		}

		return adjacentMines;
	}
	
	/**
	 * Maps the bidimensional coordinates in the board to a monodimensional coordinate of the cell array
	 */
	private int toCellIndex(int row, int col) {
		return col + row * colCount;
	}
	
	/**
	 * Returns the unique identifier of the game
	 */
	public UUID getId() {
		return id;
	}
	
	/**
	 * Returns the date the game was started
	 */
	public Date getCreationDate() {
		return creationDate;
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
			board[row][col] = board[row][col].changeState(State.MINED);
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
		board[row][col] = board[row][col].changeState(State.FLAGGED);
	}
	
	/**
	 * Marks a cell with a question mark
	 * 
	 * @throws IllegalAccessException if game is no longer in progress (player either lost or won) 
	 */
	public void mark(int row, int col) throws IllegalAccessException {
		validateMove(row, col);
		board[row][col] = board[row][col].changeState(State.MARKED);
	}
	
	/**
	 * Removes any previous marking and renders the cell as in UNKNOWN state
	 * 
	 * @throws IllegalAccessException if game is no longer in progress (player either lost or won)
	 */
	public void clear(int row, int col) throws IllegalAccessException {
		validateMove(row, col);
		board[row][col] = board[row][col].changeState(State.UNKNOWN);
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
	 * Returns a cell from the board
	 */
	public Cell getCell(int row, int col) {
		return board[row][col];
	}
	
	/**
	 * Returns a snapshot of the board's state
	 */
	public Cell[][] getBoard() {
		Cell[][] board = new Cell[rowCount][colCount];
		
		IntStream.range(0, rowCount)
			.forEach(row -> {
				IntStream.range(0, colCount)
					.forEach(col -> {
						Cell cell = getCell(row, col);
						board[row][col] = new Cell(cell.getState(), Cell.State.EMPTY == cell.getState() ? cell.getAdjacentMines() : -1);
					});
			});
		
		return board;
	}
	
	/**
	 * Recursively reveals all adjacent cells that need to be revealed
	 */
	private void revealRecursive(int row, int col) {
		if (shouldBeRevealed(row, col)) {
			board[row][col] = board[row][col].changeState(State.EMPTY);
			emptyCellsRevealed++;
			
			if (emptyCellsRevealed == rowCount * colCount - mineCount) {
				state = GameState.WON;
			}
			
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
				Cell.State.UNKNOWN == board[row][col].getState() &&
				!mines[row][col];
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
