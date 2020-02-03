package ar.com.rbo.minesweeper.controller;

/**
 * Payload for creating a {@link Game}
 */
public class GameCreationPayload {
	
	private int rowCount;
	private int colCount;
	private int mineCount;
	
	/**
	 * Needed by Jackson
	 */
	public GameCreationPayload() {}
	
	/**
	 * Initializes the payload
	 */
	public GameCreationPayload(int rowCount, int colCount, int mineCount) {
		this.rowCount = rowCount;
		this.colCount = colCount;
		this.mineCount = mineCount;
	}

	/**
	 * Returns the row count the game should be created with
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Returns the column count the game should be created with
	 */
	public int getColCount() {
		return colCount;
	}

	/**
	 * Returns the mine count the game should be created with
	 */
	public int getMineCount() {
		return mineCount;
	}
}
