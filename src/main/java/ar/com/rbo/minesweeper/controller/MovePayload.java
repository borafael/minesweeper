package ar.com.rbo.minesweeper.controller;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import ar.com.rbo.minesweeper.controller.MovePayload.ClearPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.FlagPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.MarkPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.RevealPayload;

/**
 * Abstract representation of a move's payload 
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = RevealPayload.class, name = "reveal"),
    @JsonSubTypes.Type(value = FlagPayload.class, name = "flag"),
    @JsonSubTypes.Type(value = MarkPayload.class, name = "mark"),
    @JsonSubTypes.Type(value = ClearPayload.class, name = "clear")
})
public abstract class MovePayload {
	
	/**
	 * Visitor interface for {@link MovePayload} implementations 
	 */
	public interface Visitor<T> {
		
		/**
		 * Visit {@link RevealPayload}
		 */
		public T visit(RevealPayload payload);
		
		/**
		 * Visit {@link FlagPayload}
		 */
		public T visit(FlagPayload payload);

		/**
		 * Visit {@link MarkPayload}
		 */
		public T visit(MarkPayload payload);
		
		/**
		 * Visit {@link ClearPayload}
		 */
		public T visit(ClearPayload payload);
	}
	
	/**
	 * Reveal move payload implementation
	 */
	public static class RevealPayload extends MovePayload {
		
		/**
		 * Required by Jackson
		 */
		public RevealPayload() { super(); }
		
		/**
		 * Initializes the payload
		 */
		public RevealPayload(int row, int col) {
			super(row, col);
		}
		
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	/**
	 * Flag move payload implementation
	 */
	public static class FlagPayload extends MovePayload {
		
		/**
		 * Required by Jackson
		 */
		public FlagPayload() { super(); }

		/**
		 * Initializes the payload
		 */
		public FlagPayload(int row, int col) {
			super(row, col);
		}
		
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	/**
	 * Mark move payload implementation
	 */
	public static class MarkPayload extends MovePayload {
		
		/**
		 * Required by Jackson
		 */
		public MarkPayload() { super(); }
		
		/**
		 * Initializes the payload
		 */
		public MarkPayload(int row, int col) {
			super(row, col);
		}
		
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	/**
	 * Clear move payload implementation
	 */
	public static class ClearPayload extends MovePayload {
		
		/**
		 * Required by Jackson
		 */
		public ClearPayload() { super(); }

		/**
		 * Initializes the payload
		 */
		public ClearPayload(int row, int col) {
			super(row, col);
		}
		
		@Override
		public <T> T accept(Visitor<T> visitor) {
			return visitor.visit(this);
		}
	}
	
	private int row;
	private int col;
	
	/**
	 * Required by Jackson
	 */
	private MovePayload() {}

	/**
	 * Initializes the payload
	 */
	private MovePayload(int row, int col) {
		this.row = row;
		this.col = col;
	}

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
	
	/**
	 * Accepts an instance of {@link Visitor}
	 */
	public abstract <T> T accept(Visitor<T> visitor);
}
