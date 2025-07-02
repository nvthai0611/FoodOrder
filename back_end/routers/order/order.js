const express = require("express");
const router = express.Router();
const Order = require("../../models/Order");

// Fake order data
const fakeOrders = [
  {
    _id: "order_001",
    user_id: "user_123",
    total_price: 215000,
    status: "done",
    note: "Giao trước 12h",
    scheduled_time: "2025-07-02T11:30:00.000Z",
    created_at: "2025-07-02T10:15:00.000Z",
    food_images: ["sample_food", "sample_drink", "sample_dessert"],
    items: [
      { food_id: "f001", name: "Bún bò", quantity: 1, price: 65000 },
      { food_id: "f002", name: "Trà đào", quantity: 2, price: 30000 },
      { food_id: "f003", name: "Bánh flan", quantity: 1, price: 20000 }
    ]
  },
  {
    _id: "order_002",
    user_id: "user_123",
    total_price: 187000,
    status: "preparing",
    note: "Thêm ống hút",
    scheduled_time: "2025-07-01T19:30:00.000Z",
    created_at: "2025-07-01T18:50:00.000Z",
    food_images: ["sample_food", "sample_drink"],
    items: [
      { food_id: "f001", name: "Bún bò", quantity: 1, price: 65000 },
      { food_id: "f002", name: "Trà đào", quantity: 2, price: 30000 }
    ]
  },
  {
    _id: "order_003",
    user_id: "user_456",
    total_price: 99000,
    status: "pending",
    note: "",
    scheduled_time: "2025-07-03T08:00:00.000Z",
    created_at: "2025-07-02T22:30:00.000Z",
    food_images: ["sample_dessert"],
    items: [
      { food_id: "f003", name: "Bánh flan", quantity: 3, price: 33000 }
    ]
  }
];

// 🔹 GET /api/orders → Trả về tất cả đơn hàng
router.get('/', (req, res) => {
  res.json(fakeOrders);
});

// 🔹 GET /api/orders/:userId → Trả về đơn hàng theo userId
router.get('/:userId', (req, res) => {
  const userId = req.params.userId;
  const userOrders = fakeOrders.filter(order => order.user_id === userId);
  res.json(userOrders);
});

// 🔹 GET /api/orders/detail/:orderId → Trả về chi tiết đơn hàng
router.get('/detail/:orderId', (req, res) => {
  const orderId = req.params.orderId;
  const order = fakeOrders.find(o => o._id === orderId);

  if (!order) {
    return res.status(404).json({ message: 'Đơn hàng không tồn tại' });
  }

  res.json(order);
});

module.exports = router;
