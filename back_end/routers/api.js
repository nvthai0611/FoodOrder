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

// Review
router.use("/review", require("./review_router/review"));

// order
router.use("/orders", require("./order/order"));

// cart
router.use("/cart", require("./cart/cart"));

module.exports = router;
