//controllers/deviceToken.js

const Token = require('../models/deviceToken');
const { deleteTokenByUsername } = require('../services/deviceToken');

const saveToken = async (req, res) => {
    const { username, token } = req.body;

    // Find token by username and update it, if it doesn't exist, create a new one
    try {
        const tokenData = await Token.findOneAndUpdate(
            { username },
            { token },
            { new: true, upsert: true }
        );
        res.status(201).json(tokenData);
    } catch (err) {
        console.error(err);
        res.status(400).json({ message: err.message });
    }
};

const deleteToken = async (req, res) => {
    const username = req.params.username;

    try {
        const result = await deleteTokenByUsername(username);
        if (result.n > -1) {
            res.status(200).json({ message: "Token deleted successfully" });
        } else {
            res.status(404).json({ message: "Token not found" });
        }
    } catch (err) {
        console.error(err);
        res.status(500).json({ message: err.message });
    }
};

module.exports = { saveToken, deleteToken };