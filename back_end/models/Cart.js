const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const CartItemSchema = new Schema({
    food: {
        type: Schema.Types.ObjectId,
        ref: 'Food',
        required: true
    },
    name: String,       
    price: Number, 
    quantity: {
        type: Number,
        default: 1
    },
    image: String       // Optional thumbnail reference
}, { _id: false });

const CartSchema = new Schema({
    userId: {
        type: Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    cartItems: [CartItemSchema]
}, { timestamps: true });

module.exports = mongoose.model('Cart', CartSchema);