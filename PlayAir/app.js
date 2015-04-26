var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes/index');
var users = require('./routes/users');

var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);
app.use('/users', users);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});

io.on('connection', function (socket) {
  socket.on('enter', function (msg) {
    switch (msg) {
      case 'picture':
        console.log('picture enter');
        socket.broadcast.emit('res enter', 'picutre enter');
            break;
      case 'video':
        console.log('video enter');
        socket.broadcast.emit('res enter', 'video enter');
            break;
      case 'note':
        console.log('note enter');
        socket.broadcast.emit('res enter', 'note enter');
            break;
      default :
        console.log('default enter');
            break;
    }
  });

  socket.on('exit', function (msg) {
    switch (msg) {
      case 'picture':
        console.log('picture exit');
        socket.broadcast.emit('res exit', 'picture exit');
            break;
      case 'video':
        console.log('video exit');
        socket.broadcast.emit('res exit', 'video exit');
            break;
      case 'note':
        console.log('note exit');
        socket.broadcast.emit('res exit', 'note exit');
            break;
      default :
        console.log('default exit');
            break;
    }
  });

  socket.on('picture sliding', function () {
    console.log('picture sliding');
    socket.broadcast.emit('res picture sliding', 'next');
  });

  socket.on('video play', function () {
    console.log('video play');
    socket.broadcast.emit('res video play', 'play video');
  });

  socket.on('video pause', function () {
    console.log('video pause');
    socket.broadcast.emit('res video pause', 'pause video');
  });

  socket.on('video barrage', function (msg) {
    console.log('video barrage: ' + msg);
    socket.broadcaset.emit('res video barrage', msg);
  });

  socket.on('note footprint', function (msg) {
    console.log('note footprint: ' + msg);
    socket.broadcast.emit('res note footprint', msg);
  });

});


http.listen(8000, function () {
  console.log('listening on *:8000');
});

module.exports = app;
