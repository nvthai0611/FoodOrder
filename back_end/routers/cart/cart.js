const express = require("express");
const User = require("../../models/User");
const Cart = require("../../models/Cart");
const router = express.Router();

router.get("/:id", async (req, res) => {
  try {
    const userId = req.params.id;
    const user = await User.findById(userId);

    if (user == null) {
      return res.status(404).json("Không tìm thấy người dùng");
    }

    const cart = await Cart.findOne({ userId: user.id });
    if (!cart) {
      return res.status(404).json("Không tồn tại giỏ hàng");
    }

    console.log(JSON.stringify(cart, null, 2));

    return res.status(200).json(cart);
  } catch (err) {
    return res.status(403).json("Token không hợp lệ");
  }
});

router.post("/", async (req, res) => {
  try {
    const { userId, cartItems } = req.body;

    if (!userId || !cartItems) {
      return res.status(400).json({ error: "Thông tin cart không hợp lệ" });
    }

    const user = await User.findById(userId);

    if (!user) {
      return res.status(404).json({ error: "User không hợp lệ" });
    }

    const currentCart = await Cart.findOne({ userId: userId });

    const parsedCartItems = cartItems.map((item) => ({
      food: item.food,
      name: item.name,
      price: item.price,
      quantity: item.quantity,
      image: item.image,
    }));

    if (currentCart) {
      for (const newItem of parsedCartItems) {
        const existingItem = currentCart.cartItems.find(
          (item) => item.food.toString() === newItem.food
        );

        if (existingItem) {
          existingItem.quantity += newItem.quantity;
        } else {
          currentCart.cartItems.push(newItem);
        }
      }
      await currentCart.save();
      return res.status(200).json(currentCart);
    } else {
      const newCart = new Cart({
        userId: userId,
        cartItems: parsedCartItems,
      });

      await newCart.save();
      return res.status(201).json(newCart);
    }
  } catch (err) {
    console.error("Không tạo được giỏ hàng:", err);
    res.status(500).json({ error: "Internal server error" });
  }
});

router.put("/", async (req, res) => {
  try {
    const { userId, cartItems } = req.body;

    if (!userId || !Array.isArray(cartItems)) {
      return res.status(400).json({ error: "Invalid cart data" });
    }

    const cart = await Cart.findOne({ userId });

    if (!cart) {
      return res.status(404).json({ error: "Cart not found" });
    }

    const parsedCartItems = cartItems
      .filter((item) => item.quantity > 0)
      .map((item) => ({
        food: item.food,
        name: item.name,
        price: item.price,
        quantity: item.quantity,
        image: item.image,
      }));
    cart.set("cartItems", parsedCartItems);

    if (parsedCartItems.length === 0) {
      await cart.deleteOne();
    } else {
      await cart.save();
    }

    return res.status(200).json(cart);
  } catch (err) {
    console.error("Cart update failed:", err);
    res.status(500).json({ error: "Internal server error" });
  }
});

module.exports = router;
