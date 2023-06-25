//app.js
const express = require('express');
const bodyParser = require('body-parser');
const http = require('http');
const { Server } = require('socket.io');
const cors = require('cors');
const mongoose = require('mongoose');
const userRoute = require('./routes/user');
const tokenRoute = require('./routes/token');
const chatRoute = require('./routes/chat');
const customEnv = require('custom-env');
const deviceTokenRoute = require('./routes/deviceToken')
const { validateToken } = require('./middlewares/auth');
const { setSocketConnection } = require('./socket');
const admin =require('./controllers/notificationController')

customEnv.env(process.env.NODE_ENV, './config');
admin.in
mongoose.connect(process.env.CONNECTION_STRING, {
    useNewUrlParser: true,
    useUnifiedTopology: true
}).then(()=> {
    console.log(`Connected to MongoDB ${process.env.CONNECTION_STRING}`);
}).catch((err) => { console.log("mogno error:", err) });

const app = express();

// setting up socket io
const httpServer = http.createServer(app);
const io = new Server(httpServer, {
    transports: ['websocket'],
    cors: {
        allowedHeaders: '*',
        origin: '*',
    }
});
setSocketConnection(io);

app.use(express.static('public'));
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true ,limit:'50mb'}));
app.use(express.json({limit:'50mb'}));


app.use('/api/Users', userRoute);
app.use('/api/tokens', tokenRoute);
app.use('/api/device',deviceTokenRoute);


app.use(validateToken);


app.use('/api/Chats', chatRoute);

httpServer.listen(process.env.PORT, () => {
    console.log(`Server started on port ${process.env.PORT}`);
});
