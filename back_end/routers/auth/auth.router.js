const express = require("express");
const User = require("../../models/User");
const router = express.Router();

// Dữ liệu giả lập user, dùng thực database khi triển khai thật
const users = [
  { id: 1, username: "admin", password: "123456", token: "token_admin_abc123" },
  {
    id: 2,
    username: "user1",
    password: "password1",
    token: "token_user1_xyz456",
  },
];

router.post("/login", (req, res) => {
  const { username, password } = req.body;
  console.log(req.body);

  // Kiểm tra dữ liệu đầu vào
  if (!username || !password) {
    return res
      .status(400)
      .json({ message: "Username và password không được để trống" });
  }
  try {
    // Tìm user trong database giả lập
    const user = users.find((u) => u.username === username);

    //   const user = User.findOne({
    //     name: username
    //   }).then(user => {
    //     if (!user) {
    //       return res.status(401).json({ message: 'User không tồn tại' });
    //     }

    if (user.password !== password) {
      return res.status(401).json({ message: "Sai mật khẩu" });
    }

    // Trả về user info (không gửi password)
    res.json({
      id: user._id,
      name: user.username,
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Server error" });
  }
});

module.exports = router;
