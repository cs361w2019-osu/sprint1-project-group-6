var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;

var playerHIT = 0;
var playerMISS = 0;
var playerSUNK = 0;
var opponentHIT = 0;
var opponentMISS = 0;
var opponentSUNK = 0;

function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function markHits(board, elementId, surrenderText) {
    let oldHit = this[elementId + "HIT"];
    let oldMiss = this[elementId + "MISS"];
    let oldSunk = this[elementId + "SUNK"];
    this[elementId + "HIT"] = 0;
    this[elementId + "MISS"] = 0;
    this[elementId + "SUNK"] = 0;

    board.attacks.forEach((attack) => {
        this[elementId + attack.result] += 1;

        let className;
        if (attack.result === "MISS")
            className = "miss";
        else if (attack.result === "HIT")
            className = "hit";
        else if (attack.result === "SUNK") {
            this[elementId + "HIT"] += 1;
            className = "hit"
        } else if (attack.result === "SURRENDER") {
            let color = "red";
            if(elementId === "opponent") {
                color = "green";
            }
            printMsgCustom(surrenderText, color);
            alert(surrenderText);
        }
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
    });

    if(oldMiss - this[elementId + "MISS"] !== 0) {
        printMsg(elementId, "MISS");
    } else if(oldSunk - this[elementId + "SUNK"] !== 0) {
        printMsg(elementId, "SUNK");
    } else if(oldHit - this[elementId + "HIT"] !== 0) {
        printMsg(elementId, "HIT");
    }
}

function printMsg(elementId, result) {
    let msg = "";

    if(elementId === "opponent") {
        if(result === "HIT") {
            msg = ("You hit their ship!");
        } else if(result === "MISS") {
            msg = ("You missed their ship!");
        } else if(result === "SUNK") {
            msg = ("You sunk their ship!");
        }
    } else if(elementId === "player") {
        if(result === "HIT") {
            msg = ("They hit your ship!");
        } else if(result === "MISS") {
            msg = ("They missed your ship!");
        } else if(result === "SUNK") {
            msg = ("They sunk your ship!");
        }
    }

    printMsgCustom(msg, "black");
}

function printMsgCustom(msg, color) {

    var out = document.getElementById("messageBoxDiv");
    const isScrolledToBottom = out.scrollHeight - out.clientHeight <= out.scrollTop + 1

    var span = document.createElement("SPAN");
    span.textContent = msg;
    span.style.color = color;
    let br = document.createElement('br');
    span.appendChild(br);
    out.appendChild(span);

    // scroll to bottom if isScrolledToBottom is true
    if (isScrolledToBottom) {
      out.scrollTop = out.scrollHeight - out.clientHeight
    }
}

function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
    }));
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");

    updateScoreCard();
}

function updateScoreCard() {
    // assign playerMiss to opponentMISS because opponent miss is misses on ai board, but playerMiss is list of how many misses player has on ai board
    document.getElementById("playerMiss").innerHTML = opponentMISS;
    document.getElementById("playerHits").innerHTML = opponentHIT;
    document.getElementById("playerSunk").innerHTML = opponentSUNK;
    document.getElementById("aiMiss").innerHTML = playerMISS;
    document.getElementById("aiHits").innerHTML = playerHIT;
    document.getElementById("aiSunk").innerHTML = playerSUNK;
}

var oldListener;
function registerCellListener(f) {
    let el = document.getElementById("player");
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    if (isSetup) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            redrawGrid();
            placedShips++;
            if (placedShips == 3) {
                isSetup = false;
                registerCellListener((e) => {});
            }
        });
    } else {
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
        })
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            printMsgCustom("Cannot complete the action", "red");
            return;
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        let table = document.getElementById("player");
        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            cell.classList.toggle("placed");
        }
    }
}

function displayName() {
    var x = document.getElementById("myName");
    var n = "";
    var i;
    for(i=0;i<x.length;i++){
        n += x.elements[i].value + "<br>";
    }
    document.getElementById("print").innerHTML = n;
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2));
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3));
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener(place(4));
    });
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};