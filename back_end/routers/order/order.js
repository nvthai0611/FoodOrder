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

// 🔹 PUT /api/orders/status/:orderId → Cập nhật trạng thái đơn hàng
router.put("/status/:orderId", async (req, res) => {
  try {
    const { orderId } = req.params;
    const { status, status_payment } = req.body;

    // Kiểm tra giá trị hợp lệ
    const validStatus = ['pending', 'preparing', 'done', 'canceled'];
    const validPaymentStatus = ['pending', 'paid', 'failed'];

    const updateData = {};

    if (status) {
      if (!validStatus.includes(status)) {
        return res.status(400).json({ message: "Trạng thái đơn hàng không hợp lệ" });
      }
      updateData.status = status;
    }

    if (status_payment) {
      if (!validPaymentStatus.includes(status_payment)) {
        return res.status(400).json({ message: "Trạng thái thanh toán không hợp lệ" });
      }
      updateData.status_payment = status_payment;
    }

    if (Object.keys(updateData).length === 0) {
      return res.status(400).json({ message: "Không có trường nào để cập nhật" });
    }

    const updatedOrder = await Order.findByIdAndUpdate(
      orderId,
      { $set: updateData },
      { new: true }
    );

    if (!updatedOrder) {
      return res.status(404).json({ message: "Không tìm thấy đơn hàng" });
    }

    res.json({
      message: "Cập nhật trạng thái thành công",
      order: updatedOrder
    });
  } catch (err) {
    console.error("Lỗi khi cập nhật trạng thái đơn hàng:", err);
    res.status(500).json({ message: "Lỗi server", error: err.message });
  }
});


module.exports = router;
