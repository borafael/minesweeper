package ar.com.rbo.minesweeper.domain;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link Move}'s implementations
 */
public class MoveTest {
	
	@Mock
	private Game gameMock;
	
	/**
	 * Initializes game mock
	 */
	public MoveTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testRevealMove() throws IllegalAccessException {
		Move move = new Move.Reveal(10, 15);
		
		assertEquals(10, move.getRow());
		assertEquals(15, move.getCol());
		
		move.apply(gameMock);
		
		verify(gameMock).reveal(10, 15);
	}
	
	@Test
	public void testFlagMove() throws IllegalAccessException {
		Move move = new Move.Flag(10, 15);
		
		assertEquals(10, move.getRow());
		assertEquals(15, move.getCol());
		
		move.apply(gameMock);
		
		verify(gameMock).flag(10, 15);
	}
	
	@Test
	public void testMarkMove() throws IllegalAccessException {
		Move move = new Move.Mark(10, 15);
		
		assertEquals(10, move.getRow());
		assertEquals(15, move.getCol());
		
		move.apply(gameMock);
		
		verify(gameMock).mark(10, 15);
	}

	@Test
	public void testClearMove() throws IllegalAccessException {
		Move move = new Move.Clear(10, 15);
		
		assertEquals(10, move.getRow());
		assertEquals(15, move.getCol());
		
		move.apply(gameMock);
		
		verify(gameMock).clear(10, 15);
	}
}
