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


app.use("/", apiRoutes);

// app.use("/", router);

app.get('/', (req, res) => {
  res.json({
    message: 'Chao'
  });
});




const PORT = process.env.PORT || 9999;
app.listen(PORT, () => console.log(`Server running on port http://localhost:${PORT}`));
