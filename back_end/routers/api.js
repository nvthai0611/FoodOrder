const express = require("express");
const router = express.Router();

// routers

// auth
router.use("/auth", require("./auth/auth.router"));

// user
router.use("/user", require("./user/user"));

// food
router.use("/food", require("./food/food"));

// Category
router.use("/category", require("./category/category"));

// order
router.use("/order", require("./order/order"));

module.exports = router;