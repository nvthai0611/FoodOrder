const express = require("express"); 
const router = express.Router(); 
const Order = require("../../models/Order"); 
const mongoose = require("mongoose");

// 🔹 GET /api/orders → Trả về tất cả đơn hàng (kèm thông tin món ăn)
router.get("/", async (req, res) => {
  try {
    const orders = await Order.find()
      .populate({
        path: 'items.food_id',
        select: 'name image_url price'
      });

    res.json(orders);
  } catch (err) {
    console.error("Lỗi khi lấy danh sách đơn hàng:", err);
    res.status(500).json({ message: "Lỗi server" });
  }
});

// 🔹 GET /api/orders/:userId → Trả về đơn hàng theo userId (kèm thông tin món ăn)
router.get("/:userId", async (req, res) => {
  try {
    const userId = req.params.userId;

    const orders = await Order.find({ user_id: userId })
      .populate({
        path: 'items.food_id',
        select: 'name image_url price'
      });

    if (!orders || orders.length === 0) {
      return res.status(404).json({ message: "Không có đơn hàng nào cho user này" });
    }

    res.json(orders);
  } catch (err) {
    console.error("❌ Lỗi khi lấy đơn hàng theo userId:", err);
    res.status(500).json({ message: "Lỗi server", error: err.message });
  }
});

// 🔹 GET /api/orders/detail/:orderId → Trả về chi tiết đơn hàng (kèm thông tin món ăn)
router.get("/detail/:orderId", async (req, res) => {
  try {
    const orderId = req.params.orderId;

    const order = await Order.findById(orderId)
      .populate({
        path: 'items.food_id',
        select: 'name image_url price'
      });

    if (!order) {
      return res.status(404).json({ message: "Đơn hàng không tồn tại" });
    }

    res.json(order);
  } catch (err) {
    console.error("Lỗi khi lấy chi tiết đơn hàng:", err);
    res.status(500).json({ message: "Lỗi server" });
  }
});

module.exports = router;
