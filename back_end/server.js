const express = require("express");
const app = express();
const cors = require("cors");
const apiRoutes = require("./routers/api");


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

app.use("/api", apiRoutes);

// app.use("/", router);

app.get('/', (req, res) => {
  res.json({
    message: 'Chao'
  });
});

// Dữ liệu giả lập user, dùng thực database khi triển khai thật
const users = [
  { id: 1, username: 'admin', password: '123456', token: 'token_admin_abc123' },
  { id: 2, username: 'user1', password: 'password1', token: 'token_user1_xyz456' }
];

// API POST /login
app.post('/login', (req, res) => {
  const { username, password } = req.body;
  console.log(req.body);
  
  // Kiểm tra dữ liệu đầu vào
  if (!username || !password) {
    return res.status(400).json({ message: 'Username và password không được để trống' });
  }

  // Tìm user trong database giả lập
  const user = users.find(u => u.username === username);

  if (!user) {
    return res.status(401).json({ message: 'User không tồn tại' });
  }

  if (user.password !== password) {
    return res.status(401).json({ message: 'Sai mật khẩu' });
  }

  // Trả về user info (không gửi password)
  res.json({
    id: user.id,
    username: user.username,
    token: user.token
  });
});


const PORT = process.env.PORT || 9999;
app.listen(PORT, () => console.log(`Server running on port http://localhost:${PORT}`));
