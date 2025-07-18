const express = require('express');
const router = express.Router();
const Food = require("../../models/Food");
const Category = require("../../models/Category");
// GET /category/all-categories
router.get('/all-categories', async (req, res) => {
    try {
    const allCategories = await Category.find({ is_available: true });
    res.json(allCategories);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;