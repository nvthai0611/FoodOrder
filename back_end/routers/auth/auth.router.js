const express = require("express");
const User = require("../../models/User");
const router = express.Router();

router.post("/login", async (req, res) => {
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
    const user = await User.findOne({
      name: username,
    });
    // Kiểm tra nếu không tìm thấy user
    if (!user) {
      return res.status(404).json({ message: "Người dùng không tồn tại" });
    }
 
    
    if (user.password !== password) {
      return res.status(401).json({ message: "Sai mật khẩu" });
    }
    // Kiểm tra nếu user đã bị xóa
    if (user.is_deleted) {
      return res
        .status(403)
        .json({ message: "Tài khoản này không được phép truy cập" });
    }
    // Trả về user info (không gửi password)
    res.json({
      success: true,
      message: "Đăng nhập thành công",
      user: {
        id: user?._id,
        name: user?.name,
        email: user?.email,
        phone: user?.phone,
        role: user.role,
      },
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Server error" });
  }
});

module.exports = router;
