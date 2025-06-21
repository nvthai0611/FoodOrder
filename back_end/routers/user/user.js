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


module.exports = router;
