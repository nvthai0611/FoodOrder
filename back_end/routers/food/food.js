const express = require('express');
const router = express.Router();
const Food = require("../../models/Food");
const Category = require("../../models/Category");
router.get('/all-foods-best-sellers', async (req, res) => {
  try {
    console.log("Fetching best sellers...");
    const bestSellers = await Food.find({ isBestSeller: true }).populate('category'); 
    console.log(bestSellers);
    res.json(bestSellers);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// GET /api/food/all-food
router.get('/all-foods', async (req, res) => {
    try {
    console.log("Fetching all...");
    const allFoods = await Food.find({ is_available: true }).populate('category'); 
    console.log(allFoods);
    res.json(allFoods);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// POST /api/food/create (optional: for adding new food)
router.post('/create', (req, res) => {
    const newFood = req.body;
    newFood._id = Date.now().toString();
    newFood.created_at = new Date();
    foods.push(newFood);
    res.json({ message: 'Food created', food: newFood });
});

module.exports = router;
