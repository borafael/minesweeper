package ar.com.rbo.minesweeper.controller;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableList;

import ar.com.rbo.minesweeper.domain.Game;
import ar.com.rbo.minesweeper.domain.GameService;

/**
 * Controller for all {@link Game} related endpoints
 */
@RestController
public class GameController {
	
	@Autowired
	private GameMapper mapper;
	
	@Autowired
	private GameService service;
	
	/**
	 * Needed for testing
	 */
	public GameController() {}
	
	/**
	 * Initializes the controller with a {@link GameService} instance (constructor meant for dependency injection)
	 */
	public GameController(GameService service) {
		this(new GameMapper(), service);
	}
	

	GameController(GameMapper mapper, GameService service) {
		this.mapper = mapper;
		this.service = service;
	}
	
	@GetMapping("/games")
	public @ResponseBody GamesPayload findGames() {
		return new GamesPayload(service.findGames().stream()
				.map(mapper::toPayload)
				.collect(ImmutableList.toImmutableList()));
	}
	
	@GetMapping("/games/{gameId}")
	public @ResponseBody GamePayload findGame(@PathVariable(value="gameId") UUID gameId) {
		return mapper.toPayload(service.findGame(gameId).orElseThrow(() -> new NoSuchElementException("Could not find game with id " + gameId)));
	}

	@PostMapping("/games/{gameId}/moves")
	public @ResponseBody GamePayload udpateGame(@PathVariable(value="gameId") UUID gameId, @RequestBody MovePayload payload) throws IllegalAccessException {
		return mapper.toPayload(service.updateGame(gameId, mapper.toDomain(payload)));
	}

	@PostMapping("/games")
	public @ResponseBody GamePayload createGame(@RequestBody GameCreationPayload payload) {
		return mapper.toPayload(service.createGame(payload.getRowCount(), payload.getColCount(), payload.getMineCount()));
	}
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalAccessException.class)
	public void invalidPayload() {}
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchElementException.class)
	public void entityNotFound() {}

}
