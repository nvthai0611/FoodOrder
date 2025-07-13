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


router.get('/by-category/:categoryId', async (req, res) => {
  const categoryId = req.params.categoryId;

  try {
    console.log(`Fetching foods in category ${categoryId}...`);
    const foodsByCategory = await Food.find({
      category: categoryId,
      is_available: true // optional: lọc theo is_available nếu cần
    }).populate('category');

    res.json(foodsByCategory);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.get('/by-id/:foodId', async (req, res) => {
  const { foodId } = req.params;

  try {
    console.log(`Fetching food with id ${foodId}...`);
    const food = await Food.findById(foodId).populate('category');

    if (!food) {
      return res.status(404).json({ error: 'Food not found' });
    }

    res.json(food);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;
