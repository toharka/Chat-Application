//services/chat

const User = require('../models/user'); 
const Chat = require('../models/chat');
const Message = require('../models/message');
const mongoose = require('mongoose');

const getMaxChatId = async () => {
    const chat = await Chat.findOne().sort('-chatId');
    return chat ? chat.chatId : 0;
};

const getMaxMessageId = async () => {
    const message = await Message.findOne().sort('-messageId');
    return message ? message.messageId : 0;
};

const addMessageToChat = async (chatId, message) => {
    const chat = await Chat.findOne({chatId});
    if (!chat) {
        throw new Error('Chat not found');
    }
    chat.messages.push(message._id);
    await chat.save();
    return await Chat.findById(chat._id).populate('messages').populate('users');
};

const getChatsByUsername = async (username) => {
    const user = await User.findOne({ username });
    if (!user) throw new Error('User not found');
    return await Chat.find({ users: user._id }).populate('messages').populate('users');
};


const getChatById = async (id) => {
    return await Chat.findOne({chatId: id}).populate('messages').populate('users');
};

const deleteChat = async (chatId, userId) => {
    const chat = await Chat.findOne({chatId}).populate('users');

    // Check if the user is a member of the chat
    if (!chat.users.some(user => user._id.toString() === userId.toString())) {
        throw new Error('User not authorized to delete this chat.');
    }

    await Chat.deleteOne({ chatId });
};

module.exports = { getMaxChatId, addMessageToChat, getMaxMessageId, getChatsByUsername, getChatById, deleteChat };