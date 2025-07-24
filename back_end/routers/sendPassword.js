const express = require("express");
const router = express.Router();
const sendEmail = require("../utils/email");
const crypto = require("crypto");

const User = require("../models/User");

router.post("/", async (req, res) => {
  const { email } = req.body;

  if (!email) {
    return res.status(400).json({ message: "Thiếu địa chỉ email." });
  }

  try {
    const user = await User.findOne({ email });

    if (!user) {
      return res.status(404).json({ message: "Không tìm thấy người dùng với email này." });
    }

    const newPassword = crypto.randomBytes(4).toString("hex");

    user.password = newPassword;
    await user.save();

    const html = `
      <h3>Mật khẩu mới của bạn</h3>
      <p>Email: <strong>${email}</strong></p>
      <p>Mật khẩu mới: <strong>${newPassword}</strong></p>
      <p>Bạn nên đổi mật khẩu sau khi đăng nhập.</p>
    `;

    await sendEmail(email, "Mật khẩu mới từ hệ thống", html);

    res.json({ message: "Mật khẩu mới đã được gửi tới email." });
  } catch (err) {
    console.error("Lỗi gửi mật khẩu:", err);
    res.status(500).json({ message: "Đã xảy ra lỗi khi xử lý yêu cầu." });
  }
});

module.exports = router;
