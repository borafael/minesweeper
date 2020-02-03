package ar.com.rbo.minesweeper.controller;

import ar.com.rbo.minesweeper.controller.MovePayload.ClearPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.FlagPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.MarkPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.RevealPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.Visitor;
import ar.com.rbo.minesweeper.domain.Game;
import ar.com.rbo.minesweeper.domain.Move;

/**
 * Mapper for translation of {@link Game} objects to different payloads and back
 */
public class GameMapper {

	/**
	 * Translates a {@link Game} to a {@link GamePayload}
	 * @param create
	 */
	public GamePayload toPayload(Game game) {
		return new GamePayload(
				game.getId(),
				game.getRowCount(), 
				game.getColCount(), 
				game.getMineCount(), 
				game.getState(), 
				game.getBoard());	
	}
	
	/**
	 * Translates a {@link MovePayload} to a {@link Move}
	 */
	public Move toDomain(MovePayload payload) {
		return payload.accept(new Visitor<Move>() {

			@Override
			public Move visit(RevealPayload payload) {
				return new Move.Reveal(payload.getRow(), payload.getCol());
			}

			@Override
			public Move visit(FlagPayload payload) {
				return new Move.Flag(payload.getRow(), payload.getCol());
			}

			@Override
			public Move visit(MarkPayload payload) {
				return new Move.Mark(payload.getRow(), payload.getCol());
			}

			@Override
			public Move visit(ClearPayload payload) {
				return new Move.Clear(payload.getRow(), payload.getCol());
			}
		});
	}
}
