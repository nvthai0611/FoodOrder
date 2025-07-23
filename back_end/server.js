const express = require("express");
const http = require("http"); // Thêm để tạo server HTTP
const { Server } = require("socket.io"); // socket.io v2.4.1
const app = express();
const cors = require("cors");
const apiRoutes = require("./routers/api");
const connectDB = require("./config/db");
const path = require('path'); // Import the path module
const methodOverride = require('method-override');
const userRoutes = require('./admin/routes/userRoutes');
const productRoutes = require('./admin/routes/foodRoutes')
const session = require('express-session');
const User = require("./models/User");
const Order = require("./models/Order");
const Cart = require("./models/Cart");
const axios = require('axios');
// --- Socket Configuration ---
const server = http.createServer(app); // tạo server HTTP

// --- EJS Configuration ---
app.set('view engine', 'ejs'); // Set EJS as the template engine
app.set('views', path.join(__dirname, '/admin/views')); // Specify the directory for your EJS files
// You might also want to serve static files (CSS, JS, images)
app.use(express.static(path.join(__dirname, 'public')));
// --- End EJS Configuration ---
app.use(
  cors({
    origin: "*",
    methods: ["GET", "POST", "DELETE", "UPDATE", "PUT", "PATCH"],
  })
);
app.use(express.json());
app.use(
  express.urlencoded({
    extended: true,
  })
);
app.use(session({
  secret: 'your-secret-key', // thay bằng key bảo mật
  resave: false,
  saveUninitialized: true
}));
app.use(methodOverride('_method'));


app.use("/", apiRoutes);

// app.use("/", router);

app.post("/create-payment-info", async (req, res) => {
  const { userId, totalPrice } = req.body;
  console.log("Create payment info:", { userId, totalPrice });

  if (!userId || !totalPrice) {
    return res.status(400).json({ error: "Thiếu userId hoặc totalPrice" });
  }

  const orderCode = `FOODPAY_${userId}_${Date.now()}`;

  const paymentInfo = {
    bankName: "BIDV",
    accountNumber: "96247232323232323",
    accountName: "NGUYEN VAN TIEN HAI",
    amount: totalPrice,
    note: orderCode
  };
  const qrUrl = `https://qr.sepay.vn/img?acc=${paymentInfo.accountNumber}&bank=${paymentInfo.bankName}&amount=${paymentInfo.amount}&des=${paymentInfo.note}&template=simple&download=true`;
  return res.json({
    ...paymentInfo,
    qrUrl
  });
});

app.post("/check-payment-status", async (req, res) => {
  const { orderCode, amount, userId } = req.body;
  console.log("Check payment status:", { orderCode, amount, userId });

  if (!orderCode || !amount || !userId) {
    return res.status(400).json({ error: "Thiếu dữ liệu" });
  }

  try {
    const response = await axios.get("https://my.sepay.vn/userapi/transactions/list", {
      params: { account_number: "4271033504", limit: 20 },
      headers: { Authorization: `Bearer ${process.env.SEPAY_API_KEY}` }
    });

    const normalizedOrderCode = orderCode.replace(/_/g, "");
    console.log("Normalized order code:", normalizedOrderCode);

    const found = response.data.transactions?.find(tx =>
      tx.transaction_content?.trim() === normalizedOrderCode &&
      parseInt(tx.amount_in) === parseInt(amount)
    );

    console.log("Found transaction:", found);
    if (!found) return res.json({ success: false });

    const existing = await Order.findOne({ note: orderCode });
    console.log("Existing order:", existing);

    if (existing) return res.json({ success: true, message: "Đã ghi nhận trước đó", transaction: found });

    const cart = await Cart.findOne({ userId });
    if (!cart || cart.cartItems.length === 0) {
      return res.status(400).json({ error: "Không tìm thấy giỏ hàng" });
    }

    const newOrder = await Order.create({
      user_id: userId,
      items: cart.cartItems.map(item => ({
        food_id: item.food,
        quantity: item.quantity,
        price: item.price
      })),
      total_price: amount,
      status_payment: "paid",
      status: "pending",
      note: orderCode
    });

    await Cart.deleteOne({ userId });

    return res.json({ success: true, transaction: found });
  } catch (err) {
    console.error("Error checking payment status:", err);
    return res.status(500).json({ error: "Internal server error" });
  }
});


// --- Example EJS Route ---
app.use('/admin', userRoutes);
app.use('/admin/product', productRoutes);
app.use('/admin/orders', require('./admin/routes/order'));
// --- End Example EJS Route ---

// Connect to MongoDB
connectDB()
  .then(() => console.log("Connected to MongoDB"))
  .catch((err) => console.error("MongoDB connection error:", err));

const io = new Server(server, {
  cors: { origin: '*' }         
});

io.on('connection', socket => {
  console.log('Client connected:', socket.id);

  socket.on('messageFromClient', data => {
    console.log('Nhận từ client: ', data);
    socket.emit('messageFromServer', `Đã nhận: ${data}`);
  });

  socket.on('disconnect', () =>
    console.log('Client disconnected:', socket.id)
  );
});
// Start the server
const PORT = process.env.PORT || 9999;
server.listen(PORT, "0.0.0.0", () =>
  console.log(`Server running on port http://localhost:${PORT}`)
);
