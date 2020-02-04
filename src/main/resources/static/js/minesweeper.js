var MINESWEEPER = {}

// show elapsed time below the board (it will start showing once the game is in progress)
setInterval(function() { 
	if ('IN_PROGRESS' == MINESWEEPER.state) {
		MINESWEEPER.updateElapsedTime();
	}
}, 1);

/**
 * Validates game parameters and if they are valid a request to create new game is made
 */
MINESWEEPER.newGame = function() {
	var rows = document.getElementById('rows').value;
	var cols = document.getElementById('cols').value;
	var mines = document.getElementById('mines').value;

	var validationMessages = new Array();

	if (rows.length < 0 || rows < 1 || rows > 30 || isNaN(rows)) {
		validationMessages.push('You must input a valid amount of rows');
	}

	if (cols.length < 0 || cols < 1 || cols > 30 || isNaN(cols)) {
		validationMessages.push('You must input a valid amount of columns');
	}

	if (mines.length == 0 || mines < 0 || mines > rows * cols || isNaN(mines)) {
		validationMessages.push('You must input a valid amount of mines');
	}

	if (validationMessages.length > 0) {
		alert(validationMessages.join('\n'));
	} else {
		this.createGame(rows, cols, mines);
	}
}

/**
 * Makes a request to create a new game, if successful the new game is rendered
 */
MINESWEEPER.createGame = function(rows, cols, mines) { 
	var payload = {
		    'rowCount': rows,
		    'colCount': cols,
		    'mineCount': mines
		};
	
	this.request('POST', 'games', this.update, payload);
}

/**
 * Updates the state of the game and renders accordingly
 */
MINESWEEPER.update = function(game) {
	
	// game status is updated
	MINESWEEPER.gameId = game.id;
	MINESWEEPER.startTime = Date.parse(game.creationDate);
	MINESWEEPER.state = game.state;
	
	// state change is handled
	if ('LOST' == MINESWEEPER.state) {
		alert('BOOM!!!');
	}
	
	if ('WON' == MINESWEEPER.state) {
		alert('You discovered all the mines!!!');
	}
	
	// updated board is rendered
	MINESWEEPER.renderBoard(game);
}

/**
 * Renders the board according to the state of the game
 */
MINESWEEPER.renderBoard = function(game) {
	var board = document.getElementById('board');
	
	this.removeElementChildren(board);
	
	var table = document.createElement('table');
	var tableBody = document.createElement('tbody');
	
	for (var rowIndex = 0; rowIndex < game.rowCount; rowIndex++) {
		
		var row = document.createElement('tr');
		
		for (var colIndex = 0; colIndex < game.colCount; colIndex++) {
			
			var cell = document.createElement('td');
			
			var button = document.createElement('button');
			button.innerHTML = this.renderCell(game.board[rowIndex][colIndex]);
			button.onclick = this.createOnClickHandler(rowIndex, colIndex);
			
			cell.appendChild(button);
			
			row.appendChild(cell);
		}
		
		tableBody.appendChild(row)
	}
	
	table.appendChild(tableBody);
	
	board.appendChild(table);
}

/**
 * Returns the character that should be used to show the state of the cell for the player
 */
MINESWEEPER.renderCell = function(cell) {
	return {
		'UNKNOWN': 'x', 
		'EMPTY': cell.adjacentMines,
		'FLAGGED': '!',
		'MARKED': '?',
		'MINED': 'm'
	}[cell.state];
}

/**
 * Shows elapsed time since the start of the game below the board
 */
MINESWEEPER.updateElapsedTime = function() {
	var statusDiv = document.getElementById('status');
	
	this.removeElementChildren(statusDiv);
	
	var elapsed = new Date().getTime() - MINESWEEPER.startTime;
	
	var elapsedTime = document.createTextNode(new Date(elapsed).toISOString().slice(11, -1));

	statusDiv.appendChild(elapsedTime);	
}

/**
 * Makes a request to reveal the cell in the received coordinates, if successful, the game is updated
 */
MINESWEEPER.reveal = function(row, col) {
	var payload = {
		    'row': row,
		    'col': col,
		    'type': 'reveal'
		};
	
	this.request('POST', 'games/' + MINESWEEPER.gameId + '/moves', this.update, payload);
}

/**
 * Auxiliary function to create a handler for the onclick event 
 */
MINESWEEPER.createOnClickHandler = function(row, col) {
	return function() {
		MINESWEEPER.reveal(row, col);
	}
}

/**
 * Auxiliary method to clear child nodes of an element
 */
MINESWEEPER.removeElementChildren = function (element) {
	while (element.firstChild) {
		element.removeChild(element.firstChild);
	}
}

/**
 * Auxiliary method to make an ajax request to the server
 */
MINESWEEPER.request = function(method, url, onSuccess, payload) {
	var xhr = new XMLHttpRequest();

	xhr.open(method, url);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.onload = function() {
	    if (xhr.status === 200) {
	    	onSuccess(JSON.parse(xhr.responseText));
	    }
	};
	xhr.send(JSON.stringify(payload));
}