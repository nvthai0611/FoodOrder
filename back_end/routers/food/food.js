const express = require("express");
const router = express.Router();
router.get('/', async(req, res)=>{
    try {
        res.send({message: 'Chào food'});
    } catch (error) {
        res.send({error: error.message});
    }
});

router.post('/create', async(req, res)=>{
    
})

module.exports = router;