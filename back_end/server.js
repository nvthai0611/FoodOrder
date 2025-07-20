const express = require("express");
const app = express();
const cors = require("cors");
const apiRoutes = require("./routers/api");
const connectDB = require("./config/db");
const path = require('path'); // Import the path module
const methodOverride = require('method-override');
const userRoutes = require('./admin/routes/userRoutes');
const productRoutes = require('./admin/routes/foodRoutes')
const session = require('express-session');

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

app.get("/", (req, res) => {
  res.json({
    message: "Chao",
  });
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
// Start the server
const PORT = process.env.PORT || 9999;
app.listen(PORT, "0.0.0.0", () =>
  console.log(`Server running on port http://localhost:${PORT}`)
);
