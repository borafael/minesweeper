package ar.com.rbo.minesweeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ar.com.rbo.minesweeper.controller.GameCreationPayload;
import ar.com.rbo.minesweeper.controller.GamePayload;
import ar.com.rbo.minesweeper.controller.GamesPayload;
import ar.com.rbo.minesweeper.controller.MovePayload;
import ar.com.rbo.minesweeper.domain.Game;

@SpringBootTest(classes = MinesweeperApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class GamesApiTest {
	
	private static final int ROW_COUNT = 10;
	private static final int COL_COUNT = 20;
	private static final int MINE_COUNT = 30;
	
	private static final int ROW = 0;
	private static final int COL = 0;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testCreateGame() {		
		GamePayload gamePayload = createGame(ROW_COUNT, COL_COUNT, MINE_COUNT);
		
		assertNotNull(gamePayload.getId());
		assertEquals(ROW_COUNT, gamePayload.getRowCount());
		assertEquals(COL_COUNT, gamePayload.getColCount());
		assertEquals(MINE_COUNT, gamePayload.getMineCount());
		assertEquals(Game.GameState.IN_PROGRESS, gamePayload.getState());
		
		IntStream.range(0, ROW_COUNT)
			.forEach(row -> IntStream.range(0, COL_COUNT)
					.forEach(col -> {
						assertEquals(Game.CellState.UNKNOWN, gamePayload.getBoard()[row][col]);
					}));		
	}
	
	@Test
	public void testFindGame() {		
		GamePayload gamePayload = createGame(ROW_COUNT, COL_COUNT, MINE_COUNT);
		
		ResponseEntity<GamePayload> response = restTemplate.getForEntity(getBaseURL() + "/games/" + gamePayload.getId(), GamePayload.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		GamePayload foundGamePayload = response.getBody();

		assertEquals(gamePayload.getId(), foundGamePayload.getId());
		assertEquals(gamePayload.getRowCount(), foundGamePayload.getRowCount());
		assertEquals(gamePayload.getColCount(), foundGamePayload.getColCount());
		assertEquals(gamePayload.getMineCount(), foundGamePayload.getMineCount());
		assertEquals(gamePayload.getState(), foundGamePayload.getState());
		
		IntStream.range(0, ROW_COUNT)
			.forEach(row -> IntStream.range(0, COL_COUNT)
					.forEach(col -> {
						assertEquals(gamePayload.getBoard()[row][col], foundGamePayload.getBoard()[row][col]);
					}));
	}
	
	@Test
	public void testFindAllGames() {
		GamePayload gamePayload = createGame(ROW_COUNT, COL_COUNT, MINE_COUNT);
		GamePayload anotherGamePayload = createGame(ROW_COUNT, COL_COUNT, MINE_COUNT);
		GamePayload yetAnotherGamePayload = createGame(ROW_COUNT, COL_COUNT, MINE_COUNT);
		
		ResponseEntity<GamesPayload> response = restTemplate.getForEntity(getBaseURL() + "/games/", GamesPayload.class);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		List<GamePayload> games = response.getBody().getGames();
		
		games.stream()
			.filter(game -> gamePayload.getId().equals(game.getId()))
			.findFirst()
			.orElseThrow(IllegalStateException::new);
		
		games.stream()
			.filter(game -> anotherGamePayload.getId().equals(game.getId()))
			.findFirst()
			.orElseThrow(IllegalStateException::new);
		
		games.stream()
			.filter(game -> yetAnotherGamePayload.getId().equals(game.getId()))
			.findFirst()
			.orElseThrow(IllegalStateException::new);
	}
	
	@Test
	public void testRevealMoveOnBoardFilledCompletelyWithMines() {
		GamePayload gamePayload = createGame(ROW_COUNT, COL_COUNT, ROW_COUNT * COL_COUNT);
		
		ResponseEntity<GamePayload> response = restTemplate.postForEntity(getBaseURL() + "/games/" + gamePayload.getId() + "/moves", new MovePayload.RevealPayload(ROW, COL), GamePayload.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		GamePayload updatedGame = response.getBody();
		
		assertEquals(Game.GameState.LOST, updatedGame.getState());
		assertEquals(Game.CellState.MINED, updatedGame.getBoard()[ROW][COL]);
	}
	
	@Test
	public void testRevealMoveOnBoardDevoidOfMines() {
		GamePayload gamePayload = createGame(ROW_COUNT, COL_COUNT, 0);
		
		ResponseEntity<GamePayload> response = restTemplate.postForEntity(getBaseURL() + "/games/" + gamePayload.getId() + "/moves", new MovePayload.RevealPayload(ROW, COL), GamePayload.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		GamePayload updatedGame = response.getBody();
		
		assertEquals(Game.GameState.WON, updatedGame.getState());
		assertEquals(Game.CellState.EMPTY, updatedGame.getBoard()[ROW][COL]);
	}
	
	@Test
	public void testFlagMove() {
		GamePayload gamePayload = createGame(ROW_COUNT, COL_COUNT, 0);
		
		ResponseEntity<GamePayload> response = restTemplate.postForEntity(getBaseURL() + "/games/" + gamePayload.getId() + "/moves", new MovePayload.FlagPayload(ROW, COL), GamePayload.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		GamePayload updatedGame = response.getBody();
		
		assertEquals(Game.GameState.IN_PROGRESS, updatedGame.getState());
		assertEquals(Game.CellState.FLAGGED, updatedGame.getBoard()[ROW][COL]);
	}
	
	@Test
	public void testMarkMove() {
		GamePayload gamePayload = createGame(ROW_COUNT, COL_COUNT, 0);
		
		ResponseEntity<GamePayload> response = restTemplate.postForEntity(getBaseURL() + "/games/" + gamePayload.getId() + "/moves", new MovePayload.MarkPayload(ROW, COL), GamePayload.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		GamePayload updatedGame = response.getBody();
		
		assertEquals(Game.GameState.IN_PROGRESS, updatedGame.getState());
		assertEquals(Game.CellState.MARKED, updatedGame.getBoard()[ROW][COL]);
	}
	
	@Test
	public void testClearMove() {
		GamePayload gamePayload = createGame(ROW_COUNT, COL_COUNT, 0);
		
		ResponseEntity<GamePayload> response = restTemplate.postForEntity(getBaseURL() + "/games/" + gamePayload.getId() + "/moves", new MovePayload.FlagPayload(ROW, COL), GamePayload.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		GamePayload updatedGame = response.getBody();
		
		assertEquals(Game.GameState.IN_PROGRESS, updatedGame.getState());
		assertEquals(Game.CellState.FLAGGED, updatedGame.getBoard()[ROW][COL]);
		
		response = restTemplate.postForEntity(getBaseURL() + "/games/" + gamePayload.getId() + "/moves", new MovePayload.ClearPayload(ROW, COL), GamePayload.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		updatedGame = response.getBody();
		
		assertEquals(Game.GameState.IN_PROGRESS, updatedGame.getState());
		assertEquals(Game.CellState.UNKNOWN, updatedGame.getBoard()[ROW][COL]);
	}
	
	/**
	 * Creates a new game
	 */
	private GamePayload createGame(int rowCount, int colCount, int mineCount) {
		ResponseEntity<GamePayload> response = restTemplate.postForEntity(getBaseURL() + "/games", new GameCreationPayload(rowCount, colCount, mineCount), GamePayload.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		return response.getBody();
	}
	
	/**
	 * Returns base URL
	 */
	private String getBaseURL() {
		return "http://localhost:" + port;
	}
}