// middleware/auth
const jwt = require('jsonwebtoken');

const validateToken = (req, res, next) => {
    const authHeader = req.headers.authorization;

    if (req.url.includes("socket")) {
        next();
        return;
    }

    if (!authHeader) {
        return res.status(401).json({
            type: "https://tools.ietf.org/html/rfc7235#section-3.1",
            title: "Unauthorized",
            status: 401,
            traceId: "00-6976b401b61f601ac1c278c1557598a8-aa3a1c5609c9c51b-00"
        });
    }

    const token = authHeader.split(' ')[1];

    try {
        jwt.verify(token, 'your_jwt_secret');
        next(); // Call next middleware function if token is valid
    } catch (err) {
        return res.status(401).json({
            type: "https://tools.ietf.org/html/rfc7235#section-3.1",
            title: "Unauthorized",
            status: 401,
            traceId: "00-6976b401b61f601ac1c278c1557598a8-aa3a1c5609c9c51b-00"
        });
    }
};

module.exports = { validateToken };
