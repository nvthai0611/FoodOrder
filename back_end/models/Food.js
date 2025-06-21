const mongoose = require('mongoose');

const foodSchema = new mongoose.Schema({
  name: { type: String, required: true },
  description: String,
  image_url: String,
  category: String,
  price: { type: Number, required: true },
  is_available: { type: Boolean, default: true },
  created_at: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Food', foodSchema);
