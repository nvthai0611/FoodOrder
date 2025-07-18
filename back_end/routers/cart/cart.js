const express = require("express");
const User = require("../../models/User");
const router = express.Router();

router.get("/cart/:id", async (req, res) => {
  const userId = req.params.id;
  const user = await User.find({
    id: userId,
  });

  if (user == null) {
    return res.status(404).json("Không tìm thấy người dùng");
  }

  const cart = await Cart.find({
    user_id: user.id,
  });

  if (cart == null) {
    return res.status(404).json("Không tồn tại giỏ hàng");
  } else {
    return res.status(200).json(cart);
  }
});

router.post("/cart/create", async(req, res) => {
    const cart = req.body({cart});
    
    
})

module.exports = router;