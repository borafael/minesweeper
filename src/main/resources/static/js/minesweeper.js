var gameId;
var startTime;
var state;

/**
 * Validates game parameters and if they are valid a request to create new game is made
 */
function newGame() {
	var rows = document.getElementById('rows').value;
	var cols = document.getElementById('cols').value;
	var mines = document.getElementById('mines').value;

	var validationMessages = new Array();

	if (rows.length == 0 || isNaN(rows)) {
		validationMessages.push('You must input a valid amount of rows');
	}

	if (cols.length == 0 || isNaN(cols)) {
		validationMessages.push('You must input a valid amount of columns');
	}

	if (mines.length == 0 || isNaN(mines)) {
		validationMessages.push('You must input a valid amount of mines');
	}

	if (validationMessages.length > 0) {
		alert(validationMessages.join('\n'));
	} else {
		createGame(rows, cols, mines);
	}
}

/**
 * Makes a request to create a new game 
 */
function createGame(rows, cols, mines) { 
	var xhr = new XMLHttpRequest();

	xhr.open('POST', 'games');
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.onload = function() {
	    if (xhr.status === 200) {
	        loadGame(JSON.parse(xhr.responseText));
	    }
	};
	xhr.send(JSON.stringify({
	    'rowCount': rows,
	    'colCount': cols,
	    'mineCount': mines
	}));
}

function loadGame(game) {
	
	gameId = game.id;
	startTime = Date.parse(game.creationDate);
	state = game.state;
	
	var board = document.getElementById('board');
	
	while (board.firstChild) {
		board.removeChild(board.firstChild);
	}
	
	var table = document.createElement('table');
	var tableBody = document.createElement('tbody');
	
	for (var rowIndex = 0; rowIndex < game.rowCount; rowIndex++) {
		
		var row = document.createElement('tr');
		
		for (var colIndex = 0; colIndex < game.colCount; colIndex++) {
			
			var cell = document.createElement('td');
			
			var button = document.createElement('button');
			button.innerHTML = 
				{
					'UNKNOWN': 'x', 
					'EMPTY': '&nbsp;',
					'FLAGGED': '!',
					'MARKED': '?',
					'MINED': 'm'
				}[game.board[rowIndex][colIndex]];
			button.onclick = createOnClickHandler(rowIndex, colIndex);
			
			cell.appendChild(button);
			
			row.appendChild(cell);
		}
		
		tableBody.appendChild(row)
	}
	
	table.appendChild(tableBody);
	
	board.appendChild(table);
		
	setInterval(function() { 
		if ('IN_PROGRESS' == state) {
			updateElapsedTime();
		}
	}, 1);
}

function updateElapsedTime() {
	var statusDiv = document.getElementById('status');
	
	removeElementChildren(statusDiv);
	
	var elapsed = new Date().getTime() - startTime;
	
	var elapsedTime = document.createTextNode(new Date(elapsed).toISOString().slice(11, -1));

	statusDiv.appendChild(elapsedTime);	
}

function createOnClickHandler(row, col) {
	return function() {
		reveal(row, col);
	}
}

function reveal(row, col) {
	var xhr = new XMLHttpRequest();

	xhr.open('POST', 'games/' + gameId + '/moves');
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.onload = function() {
	    if (xhr.status === 200) {
	        loadGame(JSON.parse(xhr.responseText));
	    }
	};
	xhr.send(JSON.stringify({
	    'row': row,
	    'col': col,
	    'type': 'reveal'
	}));
}

function removeElementChildren(element) {
	while (element.firstChild) {
		element.removeChild(element.firstChild);
	}

}
