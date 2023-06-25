//services/deviceToken
const Token = require('../models/deviceToken');

const getTokenByUsername = async (username) => {
    const tokenData = await Token.findOne({ username });
    return tokenData;
};


const deleteTokenByUsername = async (username) => {
    const result = await Token.deleteOne({ username });
    return result;
};

module.exports = { getTokenByUsername, deleteTokenByUsername };
