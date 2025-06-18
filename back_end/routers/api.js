const express = require("express");
const router = express.Router();
router.get('/', async(req, res)=>{
    try {
        res.send({message: 'hello world'});
    } catch (error) {
        res.send({error: error.message});
    }
});

module.exports = router;