package ar.com.rbo.minesweeper.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GameCreationPayload}
 */
public class GameCreationPayloadTest {

	@Test
	public void testCreation() {
		GameCreationPayload gameCreationPayload = new GameCreationPayload(10, 20, 30);
		
		assertEquals(10, gameCreationPayload.getRowCount());
		assertEquals(20, gameCreationPayload.getColCount());
		assertEquals(30, gameCreationPayload.getMineCount());
	}
}
