# Minesweeper

Minesweeper API and Web UI

## How to run

If **gradle** is available, the application can be ran directly in the following way:

```bash
gradle run
```

In case **gradle** is not available in the local environment, a **gradle wrapper** is provided so the application can be ran anyways:

```bash
./gradlew run
```

Once the application is running the url **http://localhost:8080/index.html** can be used to access it

## API

### /games

A **POST** request to this endpoint with the following payload:

```json
{
	"rowCount": 4,
	"colCount": 5,
	"mineCount": 6
}
```

Will create a new game with a board comprised of 4 (four) rows and 5 (five) columns, with 6 (six) mines randomly placed.

A successful request will yield a response payload with the state of the newly created game like the following one:

```json
{
    "id": "dd98252b-d8b2-40bb-b777-bb0ab1b2a92c",
    "creationDate": "2020-02-04T07:40:05.332+0000",
    "rowCount": 2,
    "colCount": 2,
    "mineCount": 1,
    "state": "IN_PROGRESS",
    "board": [
        [
            {
                "state": "UNKNOWN",
                "adjacentMines": -1
            },
            {
                "state": "UNKNOWN",
                "adjacentMines": -1
            }
        ],
        [
            {
                "state": "UNKNOWN",
                "adjacentMines": -1
            },
            {
                "state": "UNKNOWN",
                "adjacentMines": -1
            }
        ]
    ]
}
```

NOTE: **adjacentMines** property will become the actual number of adjacent mines once the cell is revealed (unless it turned out to have a mine)

This endpoint also supports **GET** requests, which will return a list of payloads like the previous one

### /games/{gameId}

This endpoint only supports **GET** requests and will respond with a payload (again, like the last described) that will reflect the status of the game that matches the parameter **gameId** (which must be in **UUID** format)

### /games/{gameId}/moves

This endpoint only supports **POST** requests and is meant to advance the game by making moves, in order to do so, a move payload is required (like the following one):

```json
{
	"row": 1,
	"col": 2,
	"type": 'reveal'
}
```

If such a payload is received the cell located at the second row and third column will be revealed (if it contained a mine then the player will loose the game, if not, the adjacent cells without mine will be revealed, other supported types of move are 'flag', 'mark' and 'clear'). The response will be a game payload with an updated board.

Both a move beyond the dimensions of the board and any move after the game is no longer in progress will yield a 400 (BAD REQUEST) response. 

## Possible future improvements/features

 - Return 201 (CREATED) instead of 200 (OK) when a game is created
 - More descriptive error messages to go with 400 (BAD REQUEST) responses 
 - Clean magic numbers in source code
 - Implement user support (registration)
 - Implement session support (login)
 - Implement flag support (already supported by the API)
 - Implement mark support (already supported by the API)
 - Persistence of domain entities (most probably, to add a repository layer)
 - Improve Web UI
 - Store time when game is won or lost
 - Support pausing a game
 