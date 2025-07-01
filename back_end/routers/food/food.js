const express = require('express');
const router = express.Router();

// Fake category data
const categories = [
    {
        _id: "cat1",
        name: "Fast Food",
        is_available: true,
        created_at: new Date()
    },
    {
        _id: "cat2",
        name: "Healthy",
        is_available: true,
        created_at: new Date()
    }
];

// Fake food data
const foods = [
    {
        _id: "1",
        name: "Double Decker",
        description: "Beef Burger",
        image_url: "https://res.cloudinary.com/demo/image/upload/burger.jpg",
        category: categories[0],
        price: 35.0,
        isBestSeller: true,
        rating: 4.5,
        is_available: true,
        created_at: new Date()
    },
    {
        _id: "2",
        name: "Vegetable Salad",
        description: "Fresh lettuce and tomatoes",
        image_url: "https://res.cloudinary.com/demo/image/upload/salad.jpg",
        category: categories[1],
        price: 15.0,
        isBestSeller: false,
        rating: 4.0,
        is_available: true,
        created_at: new Date()
    },
     {
        _id: "3",
        name: "Vegetable Salad",
        description: "Fresh lettuce and tomatoes",
        image_url: "https://res.cloudinary.com/demo/image/upload/salad.jpg",
        category: categories[1],
        price: 15.0,
        isBestSeller: false,
        rating: 4.0,
        is_available: true,
        created_at: new Date()
    }
];

// GET /api/food/all-food
router.get('/all-food', async (req, res) => {
    try {
        res.json(foods);
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
