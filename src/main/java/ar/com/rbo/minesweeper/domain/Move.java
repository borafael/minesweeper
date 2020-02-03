package ar.com.rbo.minesweeper.domain;

/**
 * Abstract representation of a move that can be made in the game
 */
public abstract class Move {
	
	private int row;
	private int col;
	
	/**
	 * {@link Move}'s reveal implementation
	 */
	public static class Reveal extends Move {
		
		/**
		 * Initializes the move
		 */
		public Reveal(int row, int col) {
			super(row, col);
		}
		
		@Override
		public void apply(Game game) throws IllegalAccessException {
			game.reveal(getRow(), getCol());
		}
	}
	
	/**
	 * {@link Move}'s flag implementation
	 */
	public static class Flag extends Move {
		
		/**
		 * Initializes the move
		 */
		public Flag(int row, int col) {
			super(row, col);
		}
		
		@Override
		public void apply(Game game) throws IllegalAccessException {
			game.flag(getRow(), getCol());
		}
	}
	
	/**
	 * {@link Move}'s mark implementation
	 */
	public static class Mark extends Move {
		
		/**
		 * Initializes the move
		 */
		public Mark(int row, int col) {
			super(row, col);
		}
		
		@Override
		public void apply(Game game) throws IllegalAccessException {
			game.mark(getRow(), getCol());
		}
	}
	
	/**
	 * {@link Move}'s clear implementation
	 */
	public static class Clear extends Move {
		
		/**
		 * Initializes the move
		 */
		public Clear(int row, int col) {
			super(row, col);
		}
		
		@Override
		public void apply(Game game) throws IllegalAccessException {
			game.clear(getRow(), getCol());
		}
	}

	/**
	 * Initializes the move
	 */
	private Move(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Applies the move to the given {@link Game}
	 */
	public abstract void apply(Game game) throws IllegalAccessException;

	/**
	 * Returns the move's row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the move's column
	 */
	public int getCol() {
		return col;
	}
}
