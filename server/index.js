/**
 * Node.JS server, run with command 'node index' from terminal
 * 
 * socket.emit - emtis to current user only
 * io.emit - emits to all
 * socket.broadcast.emit - emits to all but current user
 * 
 * TODO: look for method to have classes for jsons
 */

var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var config = require('../core/assets/jsons/constants.json');

var user = {
    id: '',
    x: 0,
    y: 0,
    speed: 120
};

// init server
server.listen(config.port, function () {
    console.log('Server is running on port ' + config.port);
});


//connect
io.on('connection', function (socket) {
    var sid = socket.id;
    console.log('Player connected ' + sid);
    // send userId to current user
    socket.emit(config.userIdString, { id: sid });
    user.id = sid;
    socket.emit('user', user);

    // disconnect
    socket.on('disconnect', function () {
        console.log('Player disconnected');
    });

    // user moved
    socket.on('move', function (playerDto) {
        console.log(playerDto);

        // emit user move to others
        socket.broadcast.emit('userMove', playerDto);
    });

    // key received
    // socket.on(config.keyClientString, function (key) {
    //     // console.log("key pressed: " + key);

    //     // emit key to other users
    //     io.emit(config.keyServerString, { id: sid, key: key });
    // });
});