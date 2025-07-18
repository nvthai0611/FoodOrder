const express = require('express');
const router = express.Router();
const Review = require('../../models/Review');

// POST /api/review/create
router.post('/create', async (req, res) => {
  try {
    const { user_id, food_id, rating, comment } = req.body;

    console.log("Create review...");
    // Kiểm tra dữ liệu đầu vào
    if (!user_id || !food_id || !rating) {
      return res.status(400).json({ error: 'user_id, food_id và rating là bắt buộc.' });
    }

    if (rating < 1 || rating > 5) {
      return res.status(400).json({ error: 'Rating phải nằm trong khoảng từ 1 đến 5.' });
    }

    // Tạo review mới
    const newReview = new Review({
      user_id,
      food_id,
      rating,
      comment
    });

    // Lưu vào database
    const savedReview = await newReview.save();

    res.status(201).json({
      message: 'Review đã được tạo thành công!',
      review: savedReview
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.get('/getByFoodId/:foodId', async (req, res) => {
  const { foodId } = req.params;
  console.log("Get reviews for foodId:", foodId);
  try {
    const reviews = await Review.find({ food_id: foodId }).sort({ created_at: -1 });
    res.json(reviews);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;