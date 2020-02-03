package ar.com.rbo.minesweeper.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ar.com.rbo.minesweeper.controller.MovePayload.ClearPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.FlagPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.MarkPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.RevealPayload;
import ar.com.rbo.minesweeper.controller.MovePayload.Visitor;

/**
 * Tests for {@link MovePayload}
 */
public class MovePayloadTest {
	
	/**
	 * {@link Visitor}'s implementation for testing
	 */
	private static final Visitor<MovePayload> TEST_VISITOR = new Visitor<MovePayload>() {

		@Override
		public MovePayload visit(RevealPayload payload) {
			return payload;
		}

		@Override
		public MovePayload visit(FlagPayload payload) {
			return payload;
		}

		@Override
		public MovePayload visit(MarkPayload payload) {
			return payload;
		}

		@Override
		public MovePayload visit(ClearPayload payload) {
			return payload;
		}
	};

	@Test
	public void testRevealPayloadCreation() {
		RevealPayload payload = new MovePayload.RevealPayload(10, 15);
		
		assertEquals(10, payload.getRow());
		assertEquals(15, payload.getCol());
	}
	
	@Test
	public void testFlagPayloadCreation() {
		FlagPayload payload = new MovePayload.FlagPayload(10, 15);
		
		assertEquals(10, payload.getRow());
		assertEquals(15, payload.getCol());
	}
	
	@Test
	public void testMarkPayloadCreation() {
		MarkPayload payload = new MovePayload.MarkPayload(10, 15);
		
		assertEquals(10, payload.getRow());
		assertEquals(15, payload.getCol());
	}
	
	@Test
	public void testClearPayloadCreation() {
		ClearPayload payload = new MovePayload.ClearPayload(10, 15);
		
		assertEquals(10, payload.getRow());
		assertEquals(15, payload.getCol());
	}
	
	@Test
	public void testVisitor() {
		RevealPayload revealPayload = new RevealPayload();
		FlagPayload flagPayloaad = new FlagPayload();
		MarkPayload markPayload = new MarkPayload();
		ClearPayload clearPayload = new ClearPayload();
		
		assertEquals(revealPayload, revealPayload.accept(TEST_VISITOR));
		assertEquals(flagPayloaad, flagPayloaad.accept(TEST_VISITOR));
		assertEquals(markPayload, markPayload.accept(TEST_VISITOR));
		assertEquals(clearPayload, clearPayload.accept(TEST_VISITOR));
	}
}
