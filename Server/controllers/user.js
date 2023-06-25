//controller/user
const jwt = require('jsonwebtoken'); 
const userService = require('../services/user');

const createUser = async (req, res) => {
    try {
        const user = await userService.createUser(req.body);
        res.status(200).json({
            username: user.username,
            displayName: user.displayName,
            profilePic: user.profilePic
        });
    } catch (error) {
        console.error(error);
        if (error.message === 'Username already exists') {
            // Return a 409 Conflict response if the username already exists.
            return res.status(409).json({
                type: "https://tools.ietf.org/html/rfc7231#section-6.5.8",
                title: "Conflict",
                status: 409,
                traceId: "00-5c4f82df596a35979b7dce0f0a516cfb-02fecb226a1e7d57-00"
            });
        }
        // If it's another error, return a 500 Internal Server Error response.
        res.status(500).send();
    }
};

const getUserDetails = async (req, res) => {
    const token = req.headers.authorization.split(' ')[1];
    try {
        jwt.verify(token, 'your_jwt_secret'); // Verify token first
        const user = await userService.getUserByUsername(req.params.username);
        if (!user) {
            return res.status(401).json({
                type: "https://tools.ietf.org/html/rfc7235#section-3.1",
                title: "Unauthorized",
                status: 401,
                traceId: "00-6976b401b61f601ac1c278c1557598a8-aa3a1c5609c9c51b-00"
            });
        }
        return res.status(200).json({
            username: user.username,
            displayName: user.displayName,
            profilePic: user.profilePic
        });
    } catch (err) {
        return res.status(401).json({
            type: "https://tools.ietf.org/html/rfc7235#section-3.1",
            title: "Unauthorized",
            status: 401,
            traceId: "00-6976b401b61f601ac1c278c1557598a8-aa3a1c5609c9c51b-00"
        });
    }
};

module.exports = { createUser, getUserDetails };
