//routes/deviceToken

const tokenController = require('../controllers/deviceToken');

const express = require('express');
var router = express.Router();

router.post('/', tokenController.saveToken);
router.delete('/:username', tokenController.deleteToken);


module.exports = router;
