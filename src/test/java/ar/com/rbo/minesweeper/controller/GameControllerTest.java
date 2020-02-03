package ar.com.rbo.minesweeper.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableList;

import ar.com.rbo.minesweeper.domain.Game;
import ar.com.rbo.minesweeper.domain.GameService;
import ar.com.rbo.minesweeper.domain.Move;

/**
 * Tests for {@link GameController}
 */
public class GameControllerTest {
	
	@Rule 
	public ExpectedException exception = ExpectedException.none();
	
	private static final UUID GAME_ID = UUID.randomUUID();
	private static final UUID NON_EXISTING_GAME_ID = UUID.randomUUID();
	
	@Mock
	private Game aGameMock;
	
	@Mock
	private Game anotherGameMock;

	@Mock
	private GamePayload aGamePayloadMock;
	
	@Mock
	private GamePayload anotherGamePayloadMock;

	@Mock
	private MovePayload movePayloadMock;
		
	@Mock
	private Move moveMock;
	
	@Mock
	private GameService serviceMock;
	
	@Mock
	private GameMapper mapperMock;
	
	private GameController controller;
	
	/**
	 * Initializes mocks and test instance
	 */
	public GameControllerTest() throws IllegalAccessException {
		MockitoAnnotations.initMocks(this);
		
		when(mapperMock.toPayload(aGameMock)).thenReturn(aGamePayloadMock);
		when(mapperMock.toPayload(anotherGameMock)).thenReturn(anotherGamePayloadMock);
		when(mapperMock.toDomain(movePayloadMock)).thenReturn(moveMock);
		
		when(serviceMock.findGames()).thenReturn(ImmutableList.of(aGameMock, anotherGameMock));
		when(serviceMock.findGame(GAME_ID)).thenReturn(Optional.of(aGameMock));
		when(serviceMock.findGame(NON_EXISTING_GAME_ID)).thenReturn(Optional.empty());
		when(serviceMock.createGame(10, 20, 30)).thenReturn(aGameMock);
		when(serviceMock.updateGame(GAME_ID, moveMock)).thenReturn(aGameMock);
		
		controller = new GameController(mapperMock, serviceMock);
	}
	
	@Test
	public void testFindGames() {
		GamesPayload games = controller.findGames();
		
		verify(serviceMock).findGames();
		verify(mapperMock).toPayload(aGameMock);
		verify(mapperMock).toPayload(anotherGameMock);
		
		assertEquals(2, games.getGames().size());
		assertEquals(aGamePayloadMock, games.getGames().get(0));
		assertEquals(anotherGamePayloadMock, games.getGames().get(1));
	}
	
	@Test
	public void testFindGame() {
		GamePayload game = controller.findGame(GAME_ID);
		
		verify(serviceMock).findGame(GAME_ID);
		verify(mapperMock).toPayload(aGameMock);
		
		assertEquals(aGamePayloadMock, game);
	}
	
	@Test
	public void testFindNonExistingGame() {
		exception.expect(NoSuchElementException.class);
		exception.expectMessage("Could not find game with id " + NON_EXISTING_GAME_ID);
		
		controller.findGame(NON_EXISTING_GAME_ID);
	}
	
	@Test
	public void testCreateGame() {
		GameCreationPayload gameCreationPayloadMock = mock(GameCreationPayload.class);
		
		when(gameCreationPayloadMock.getRowCount()).thenReturn(10);
		when(gameCreationPayloadMock.getColCount()).thenReturn(20);
		when(gameCreationPayloadMock.getMineCount()).thenReturn(30);
		
		GamePayload game = controller.createGame(gameCreationPayloadMock);
		
		verify(gameCreationPayloadMock).getRowCount();
		verify(gameCreationPayloadMock).getColCount();
		verify(gameCreationPayloadMock).getMineCount();
		verify(serviceMock).createGame(10, 20, 30);
		verify(mapperMock).toPayload(aGameMock);
		
		assertEquals(aGamePayloadMock, game);
	}	

	@Test
	public void testUpdateGame() throws IllegalAccessException {
		GamePayload game = controller.udpateGame(GAME_ID, movePayloadMock);
		
		verify(mapperMock).toDomain(movePayloadMock);
		verify(serviceMock).updateGame(GAME_ID, moveMock);
		verify(mapperMock).toPayload(aGameMock);
		
		assertEquals(aGamePayloadMock, game);
	}
}
