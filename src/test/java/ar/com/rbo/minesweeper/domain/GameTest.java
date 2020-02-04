package ar.com.rbo.minesweeper.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.stream.IntStream;

import org.junit.Test;

import ar.com.rbo.minesweeper.domain.Game.GameState;

/**
 * Tests for {@link Game} class
 */
public class GameTest {

	@Test
	public void testGameCreation() {
		Game game = new Game(10, 20, 30);
		
		// test that all game parameters are set correctly
		assertEquals(10, game.getRowCount());
		assertEquals(20, game.getColCount());
		assertEquals(30, game.getMineCount());
		
		// test that game was created in "IN PROGRESS" state
		assertEquals(Game.GameState.IN_PROGRESS, game.getState());
		
		// test that all cells of the board are seen as UNKNOWN for the player
		IntStream.range(0, 200)
			.forEach(cellIndex -> {
				int row = cellIndex / 20;
				int col = cellIndex - row * 20;
				assertEquals(Cell.State.UNKNOWN, game.getCell(row, col).getState());
			});
	}
	
	@Test
	public void testRevealEmptyCell() throws IllegalAccessException {
		Game game = new Game(10, 10, 0);
		game.reveal(0, 0);
		
		assertEquals(Cell.State.EMPTY, game.getCell(0, 0).getState());
	}
	
	@Test
	public void testRevealMinedCell() throws IllegalAccessException {
		Game game = new Game(10, 10, 100);
		game.reveal(0, 0);
		
		assertEquals(Cell.State.MINED, game.getCell(0, 0).getState());
	}
	
	@Test
	public void testFlagCell() throws IllegalAccessException {
		Game game = new Game(10, 10, 10);
		game.flag(0, 0);
		
		assertEquals(Cell.State.FLAGGED, game.getCell(0, 0).getState());
	}
	
	@Test
	public void testMarkCell() throws IllegalAccessException {
		Game game = new Game(10, 10, 100);
		game.mark(0, 0);
		assertEquals(Cell.State.MARKED, game.getCell(0, 0).getState());
	}
	
	@Test
	public void testClearCell() throws IllegalAccessException {
		Game game = new Game(10, 10, 100);
		game.mark(0, 0);
		game.clear(0, 0);
		assertEquals(Cell.State.UNKNOWN, game.getCell(0, 0).getState());
	}
	
	@Test
	public void testRevealOutsideTheBoardValidation() {
		Game game = new Game(10, 10, 100);
		
		try {
			game.reveal(-1, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (-1, 0) outside existing board", e.getMessage());
		}
		
		try {
			game.reveal(0, -1);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (0, -1) outside existing board", e.getMessage());
		}
		
		try {
			game.reveal(11, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (11, 0) outside existing board", e.getMessage());
		}
		
		try {
			game.reveal(0, 11);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (0, 11) outside existing board", e.getMessage());
		}
	}
	
	@Test
	public void testRevealWhenGameIsNotInProgress() throws IllegalAccessException {
		Game game = new Game(10, 10, 100);
		game.reveal(0, 0);
		
		assertEquals(GameState.LOST, game.getState());
		
		try {
			game.reveal(0, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Game is no longer in progress", e.getMessage());
		}
	}
	
	@Test
	public void testFlagWhenGameIsNotInProgress() throws IllegalAccessException {
		Game game = new Game(10, 10, 100);
		game.reveal(0, 0);
		
		assertEquals(GameState.LOST, game.getState());
		
		try {
			game.flag(0, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Game is no longer in progress", e.getMessage());
		}
	}
	
	@Test
	public void testMarkWhenGameIsNotInProgress() throws IllegalAccessException {
		Game game = new Game(10, 10, 100);
		game.reveal(0, 0);
		
		assertEquals(GameState.LOST, game.getState());
		
		try {
			game.mark(0, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Game is no longer in progress", e.getMessage());
		}
	}
	
	@Test
	public void testClearWhenGameIsNotInProgress() throws IllegalAccessException {
		Game game = new Game(10, 10, 100);
		game.reveal(0, 0);
		
		assertEquals(GameState.LOST, game.getState());
		
		try {
			game.clear(0, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Game is no longer in progress", e.getMessage());
		}
	}
	
	@Test
	public void testFlagOutsideTheBoardValidation() {
		Game game = new Game(10, 10, 100);
		
		try {
			game.flag(-1, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (-1, 0) outside existing board", e.getMessage());
		}
		
		try {
			game.flag(0, -1);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (0, -1) outside existing board", e.getMessage());
		}
		
		try {
			game.flag(11, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (11, 0) outside existing board", e.getMessage());
		}
		
		try {
			game.flag(0, 11);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (0, 11) outside existing board", e.getMessage());
		}
	}
	
	@Test
	public void testMarkOutsideTheBoardValidation() {
		Game game = new Game(10, 10, 100);
		
		try {
			game.mark(-1, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (-1, 0) outside existing board", e.getMessage());
		}
		
		try {
			game.mark(0, -1);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (0, -1) outside existing board", e.getMessage());
		}
		
		try {
			game.mark(11, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (11, 0) outside existing board", e.getMessage());
		}
		
		try {
			game.mark(0, 11);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (0, 11) outside existing board", e.getMessage());
		}
	}
	
	@Test
	public void testClearOutsideTheBoardValidation() {
		Game game = new Game(10, 10, 100);
		
		try {
			game.clear(-1, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (-1, 0) outside existing board", e.getMessage());
		}
		
		try {
			game.clear(0, -1);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (0, -1) outside existing board", e.getMessage());
		}
		
		try {
			game.clear(11, 0);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (11, 0) outside existing board", e.getMessage());
		}
		
		try {
			game.clear(0, 11);
			fail();
		} catch (IllegalAccessException e) {
			assertEquals("Cell coordinates (0, 11) outside existing board", e.getMessage());
		}
	}
	
	@Test
	public void testAdjacentMineCount() {
		Game game = new Game(10, 10, 100);
		
		assertEquals(3, game.getCell(0, 0).getAdjacentMines());
		assertEquals(3, game.getCell(0, 9).getAdjacentMines());
		assertEquals(3, game.getCell(9, 0).getAdjacentMines());
		assertEquals(3, game.getCell(9, 9).getAdjacentMines());
		assertEquals(8, game.getCell(1, 1).getAdjacentMines());
	}
}
