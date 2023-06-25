//models/chat
const mongoose = require('mongoose');

const ChatSchema = new mongoose.Schema({
    chatId: {
        type: Number,
        required: true,
        unique: true
    },
    users: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    }],
    messages: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Message'
    }]
});

module.exports = mongoose.model('Chat', ChatSchema);
