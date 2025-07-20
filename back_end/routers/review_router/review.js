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
    const reviews = await Review.find({ food_id: foodId }).populate('user_id').sort({ created_at: -1 });
     // Nếu bạn muốn gộp tên vào kết quả
    const response = reviews.map((review) => ({
      _id: review._id,
      user_id: review.user_id._id, 
      userName: review.user_id.name, 
      food_id: review.food_id,
      rating: review.rating,
      comment: review.comment,
      created_at: review.created_at
    }));
    console.log(response);
    
    res.json(response);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;