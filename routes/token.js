//routes/token
const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const userService = require('../services/user');

router.post('/', async (req, res) => {
    const { username, password } = req.body;
    // validate user's credentials
    const user = await userService.validateUser(username, password);
    if (!user) {
        // return status 404 and a custom error message
        return res.status(404).send('Incorrect username and/or password');
    }
    // generate JWT
    const token = jwt.sign(
        {
            username: user.username,
            // add more user properties as needed
        },
        'your_jwt_secret',
        { expiresIn: '1h' } // token will expire in 1 hour
    );
    res.send(token);
});

module.exports = router;
