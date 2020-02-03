package ar.com.rbo.minesweeper.controller;

import org.junit.Test;

import ar.com.rbo.minesweeper.domain.Game;
import ar.com.rbo.minesweeper.domain.Move;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link GameMapper}
 */
public class GameMapperTest {
	
	private GameMapper mapper = new GameMapper();
	
	@Test
	public void testGameToPayloadMapping() {
		GamePayload gamePayload = mapper.toPayload(new Game(10, 20, 30));
		assertEquals(10, gamePayload.getRowCount());
		assertEquals(20, gamePayload.getColCount());
		assertEquals(30, gamePayload.getMineCount());
	}
	
	@Test
	public void testRevealMoveToDomainMapping() {
		Move move = mapper.toDomain(new MovePayload.RevealPayload(10, 15));
		assertEquals(10, move.getRow());
		assertEquals(15, move.getCol());
		assertTrue(move instanceof Move.Reveal);
	}
	
	@Test
	public void testFlaglMoveToDomainMapping() {
		Move move = mapper.toDomain(new MovePayload.FlagPayload(10, 15));
		assertEquals(10, move.getRow());
		assertEquals(15, move.getCol());
		assertTrue(move instanceof Move.Flag);
	}
	
	@Test
	public void testMarklMoveToDomainMapping() {
		Move move = mapper.toDomain(new MovePayload.MarkPayload(10, 15));
		assertEquals(10, move.getRow());
		assertEquals(15, move.getCol());
		assertTrue(move instanceof Move.Mark);
	}
	
	@Test
	public void testClearMoveToDomainMapping() {
		Move move = mapper.toDomain(new MovePayload.ClearPayload(10, 15));
		assertEquals(10, move.getRow());
		assertEquals(15, move.getCol());
		assertTrue(move instanceof Move.Clear);
	}
}
