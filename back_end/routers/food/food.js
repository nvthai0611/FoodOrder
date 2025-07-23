const express = require('express');
const router = express.Router();
const Food = require("../../models/Food");
const Category = require("../../models/Category");
router.get('/all-foods-best-sellers', async (req, res) => {
  try {
    const bestSellers = await Food.find({ isBestSeller: true }).populate('category'); 
    res.json(bestSellers);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// GET /api/food/all-food
router.get('/all-foods', async (req, res) => {
    try {
    const allFoods = await Food.find({ is_available: true, isBestSeller: { $ne: true }}).populate('category'); 
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
    const foodsByCategory = await Food.find({
      category: categoryId,
      is_available: true
      , isBestSeller: { $ne: true }
    }).populate('category');

    res.json(foodsByCategory);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.get('/by-id/:foodId', async (req, res) => {
  const { foodId } = req.params;

  try {
    const food = await Food.findById(foodId).populate('category');

    if (!food) {
      return res.status(404).json({ error: 'Food not found' });
    }

    res.json(food);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.get("/relatefood-by-categoryId/:categoryId/:foodId",async (req, res) => {
  const { categoryId, foodId } = req.params;
  // console.log("relete food id:" , categoryId, foodId);
  
  try {
    const food = await Food.find({ category: categoryId, is_available: true, _id : { $ne: foodId }}).populate('category');
    // console.log("relete food check:" , food);
    
    if (!food) {
      return res.status(404).json({ error: 'Food not found' });
    }

    res.json(food);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
})
module.exports = router;
