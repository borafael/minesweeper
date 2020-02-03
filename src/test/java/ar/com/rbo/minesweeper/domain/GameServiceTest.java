package ar.com.rbo.minesweeper.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests for {@link GameService}
 */
public class GameServiceTest {
	
	private static final UUID GAME_ID = UUID.randomUUID();
	
	@Mock
	private Game gameMock;
	
	@Mock
	private Game anotherGameMock;
	
	@Mock
	private Map<UUID, Game> gameMapMock;
	
	private GameService service;
	
	/**
	 * Initializes the test instance
	 */
	public GameServiceTest() {
		MockitoAnnotations.initMocks(this);
		
		this.service = new GameService(gameMapMock);
	}

	@Test
	public void testFindGameById() {
		service.findGame(GAME_ID);
		verify(gameMapMock).get(GAME_ID);
	}
	
	@Test
	public void testFindGameByIdThatDoesNotExist() {
		assertEquals(Optional.empty(), service.findGame(UUID.randomUUID()));
	}
	
	@Test
	public void testFindGames() {
		service.findGames();
		verify(gameMapMock).values();
	}
	
	@Test
	public void testCreateGame() {
		service.createGame(10, 20, 30);
		verify(gameMapMock).put(any(UUID.class), argThat(new ArgumentMatcher<Game>() {

			@Override
			public boolean matches(Game game) {
				return 
						game.getRowCount() == 10 && 
						game.getColCount() == 20 && 
						game.getMineCount() == 30;
			}
		}));
	}
	
	@Test
	public void testUpdateGame() throws IllegalAccessException {
		Move moveMock = mock(Move.class);
		Game gameMock = mock(Game.class);
		
		when(gameMapMock.get(GAME_ID)).thenReturn(gameMock);
		
		service.updateGame(GAME_ID, moveMock);
		
		verify(gameMapMock).get(GAME_ID);
		verify(moveMock).apply(gameMock);
	}
}
