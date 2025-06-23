const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const User = require("../../models/User");
const Food = require("../../models/Food");

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


module.exports = router;
