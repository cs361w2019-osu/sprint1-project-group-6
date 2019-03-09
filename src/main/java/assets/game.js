var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;

var isSonar = false;
var sonarCount = 2;

var isSpaceLaser = false;
var spaceLaserCount = 2;

var playerHIT = 0;
var playerMISS = 0;
var playerSUNK = 0;
var opponentHIT = 0;
var opponentMISS = 0;
var opponentSUNK = 0;
var numMovesLeft = 2;

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

    if(board.pulsed) {
        board.pulsed.forEach((sonar) => {
            let className;
            if (sonar.result === "SHOWNOSHIP") {
                className = "empty";
            } else if (sonar.result === "SHOWSHIP") {
                className = "occupied";
            }
            document.getElementById(elementId).rows[sonar.location.row - 1].cells[sonar.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
        });
    }

    if(board.spaceL){
        board.spaceL.forEach((space) => {
            let className;
            if(space.result == "SHOWNOSHIP"){
                className = "empty";
            }else if (space.result == "SHOWSHIP"){
                className = "occupied";
            }
        document.getElementById(elementId).rows[space.location.row - 1].cells[space.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
        });
    }

    board.attacks.forEach((attack) => {
        this[elementId + attack.result] += 1;

        let className;
        if (attack.result === "MISS")
            className = "miss";
        else if (attack.result === "BANG") {
            className = "bang";
        }
        else if (attack.result === "HIT")
            className = "hit";
        else if (attack.result === "SUNK") {
            this[elementId + "HIT"] += 1;
            className = "hit"

            // Hide the sonar button until the player has sunk a ship
            if(elementId === "opponent")
            {
                document.getElementById("sonarButton").style.display = 'block';
                document.getElementById("spaceLaserButton").style.display = 'block';
                if(opponentSUNK >= 2) {
                    document.getElementById("moveBtnDiv").style.visibility = 'visible';
                }
            }

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
            if (placedShips == 4) {
                isSetup = false;
                document.getElementById("playerNameDisp").innerHTML = displayName();
                document.getElementById("scoreCardDiv").style.display = "block";
                document.getElementById("placeShipsDiv").style.display = "none";
                registerCellListener((e) => {});
            }
        });
    } else if(isSonar) {
        sendXhr("POST", "/sonar", {game: game, x: row, y: col}, function (data) {
            game = data;
            redrawGrid();
            sonarCount--;
            isSonar = false;
        });
    }

        else if(isSpaceLaser){
            sendXhr("POST", "/space", {game: game, x: row, y: col},function(data) {
                game = data;
                redrawGrid();
                spaceLaserCount--;
                isSpaceLaser = false;
            });
        }

    else {
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

function place(size, is_sub) {
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

        if (is_sub) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row + 2];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    return;
                }
                cell = tableRow.cells[col + 1];
            } else {
                cell = table.rows[row - 1].cells[col + 2];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                return;
            }
            cell.classList.toggle("placed");
        }
    }
}

function displayName() {
    var x = document.getElementById("playerName");
    name = x.value;
    return name;
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);

    document.getElementById("sonarButton").style.display = 'none';
    document.getElementById("spaceLaserButton").style.display = 'none';

    document.getElementById("moveBtnDiv").style.visibility = 'hidden';

    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2, false));
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3, false));
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener(place(4, false));
    });
    document.getElementById("place_submarine").addEventListener("click", function(e) {
            shipType = "SUBMARINE";
           registerCellListener(place(4, true));
    });

    document.getElementById("sonarButton").addEventListener("click", function(e) {
        if(sonarCount > 0) {
            isSonar = true;
        }
        else {
            printMsgCustom("You cannot use a sonar more than twice", "red");
        }
    });

    document.getElementById("spaceLaserButton").addEventListener("click", function(e) {
        if(spaceLaserCount > 0){
            isSpaceLaser = true;
        }
        if(spaceLaserCount > 0 && spaceLaserCount < 2){
            printMsgCustom("Activation Code:0000","purple");
        }

        else {
            printMsgCustom("You can only use Space Laser twice", "red");
        }
    });

    document.getElementById("moveNorthButton").addEventListener("click", function(e) {
        if(numMovesLeft !== 0) {
            numMovesLeft--;
            sendXhr("POST", "/move", {game: game, direction: 0}, function(data) {
                game = data;
                redrawGrid();
                sonarCount--;
                isSonar = false;
            });
        } else {
            printMsgCustom('You can only use 2 moves', "red");
        }
    });

    document.getElementById("moveSouthButton").addEventListener("click", function(e) {
        if(numMovesLeft !== 0) {
            numMovesLeft--;
            sendXhr("POST", "/move", {game: game, direction: 2}, function(data) {
                game = data;
                redrawGrid();
                sonarCount--;
                isSonar = false;
            });
        } else {
            printMsgCustom('You can only use 2 moves', "red");
        }
    });

    document.getElementById("moveWestButton").addEventListener("click", function(e) {
        if(numMovesLeft !== 0) {
            numMovesLeft--;
            sendXhr("POST", "/move", {game: game, direction: 1}, function(data) {
                game = data;
                redrawGrid();
                sonarCount--;
                isSonar = false;
            });
        } else {
            printMsgCustom('You can only use 2 moves', "red");
        }
    });

    document.getElementById("moveEastButton").addEventListener("click", function(e) {
        if(numMovesLeft !== 0) {
            numMovesLeft--;
            sendXhr("POST", "/move", {game: game, direction: 3}, function(data) {
                game = data;
                redrawGrid();
                sonarCount--;
                isSonar = false;
            });
        } else {
            printMsgCustom('You can only use 2 moves', "red");
        }
    });

    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};