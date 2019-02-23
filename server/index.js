/**
 * Node.JS server, run with command 'node index' from terminal
 * 
 * socket.emit - emtis to current user only
 * io.emit - emits to all
 * socket.broadcast.emit - emits to all but current user
 * 
 * TODO: look for method to have classes for jsons
 */
var serverPort = 5050;
var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var user = {
    id: '',
    x: 0,
    y: 0,
    speed: 120
};

var users = [];

// init server
server.listen(serverPort, function () {
    console.log('Server is running on port ' + serverPort);
});


//connect
io.on('connection', function (socket) {
    var sid = socket.id;
    console.log('Player connected ' + sid);

    user = {
        id: sid,
        x: 0,
        y: 0,
        speed: 120
    };
    // send new user data to him
    socket.emit('user', user);

    // send other users to current user
    socket.emit('otherPlayers', users);
    console.log(users);
    users.push(user);

    // send connected user to other users
    socket.broadcast.emit('newPlayerConnected', user);

    // disconnect
    socket.on('disconnect', function () {
        console.log('Player disconnected');
        users.pop();
    });

    // user moved
    socket.on('move', function (playerDto) {

        for (var u of users) {
            if (u.id === playerDto.id) {
                u.x = playerDto.x;
                u.y = playerDto.y;
                break;
            }
        }
        // emit user move to others
        socket.broadcast.emit('userMove', playerDto);
    });
});
