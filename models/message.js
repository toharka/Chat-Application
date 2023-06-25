
//models/message
const mongoose = require('mongoose');

const MessageSchema = new mongoose.Schema({
    messageId: {
        type: Number,
        required: true,
        unique: true
    },
    created: {
        type: Date,
        required: true,
        default: Date.now
    },
    sender: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    }, 
    content: String,
    chatId: { // reference to chat
        type: Number,
        required: true
    }
});
module.exports = mongoose.model('Message', MessageSchema);
