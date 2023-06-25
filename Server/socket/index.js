
const setSocketConnection = (io) => {
    // Handle incoming socket connections
    io.on('connection', (socket) => {
      console.log('User connected:', socket.id);

      // Handle joining a chat room
      socket.on('joinRoom', (roomId) => {
        console.log("joined room", roomId)
        socket.join(roomId);
        console.log(`User ${socket.id} joined room ${roomId}`);
      });

      // Handle leaving a chat room
      socket.on('leaveRoom', (roomId) => {
        console.log("left room", roomId)
        socket.leave(roomId);
        console.log(`User ${socket.id} left room ${roomId}`);
      });

      // Handle chat messages
      socket.on('chat', (data) => {
        console.log("got message on chat", data)
        const { roomId, message } = data;
        // Emit the received message to all clients in the same room
        io.to(roomId).emit('chat', message);
      });

      // Handle disconnection
      socket.on('disconnect', () => {
        console.log('User disconnected:', socket.id);
      });
    });
}

module.exports = { setSocketConnection }
