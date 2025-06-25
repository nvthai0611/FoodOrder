const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const User = require("../../models/User");
const Food = require("../../models/Food");
const data = require('../../data'); // import danh sách users từ data.js


const JWT_SECRET = "your_jwt_secret_key";

router.get("/login/:id", async (req, res) => {
  try {
    const id = req.params.id;
    const query = req.query;
    const keyword = 'cơm'; // ví dụ từ khóa
    const users = await User.find({
      name: { $regex: query, $options: 'i' } // 'i' = không phân biệt hoa thường
    });
    // const body = req.body;

    return res.json({
      message: "Chao user",
      // body: body,
      users
    });
  } catch (error) {

  }
});

router.get('/:id', async (req, res) => {
  try {
    const users = [
      { id: 1, username: 'admin', password: '123456', token: 'token_admin_abc123' },
      { id: 2, username: 'user1', password: 'password1', token: 'token_user1_xyz456' }
    ];
    const id = req.params.id;
    const user = users.find(user => user.id == id);
    console.log(user);

    return res.json(user);
  } catch (error) {
    return res.status(500).json({ error: error.message });
  }
})

router.get('/', async (req, res) => {
  try {
    res.send({ message: 'Chào user' });
  } catch (error) {
    res.send({ error: error.message });
  }
});

// POST /reset-password
router.post('/reset-password', (req, res) => {
  console.log(data);

  const { email } = req.body;

  if (!email) {
    return res.status(400).json({ success: false, message: 'Email không được để trống' });
  }

  // Tìm user theo email
  const user = data.users.find(u => u.email === email);

  if (!user) {
    return res.status(404).json({ success: false, message: 'Email không tồn tại trong hệ thống' });
  }

  // Tạo mật khẩu mới (giả lập)
  const newPassword = Math.random().toString(36).slice(-8);
  user.password = newPassword; // cập nhật password mới trong RAM

  // Trả về client (app Android) để hiển thị mật khẩu mới
  return res.json({
    success: true,
    message: 'Đã đặt lại mật khẩu mới',
    newPassword: newPassword
  });
});

module.exports = router;
