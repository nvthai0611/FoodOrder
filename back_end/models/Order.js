const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const OrderSchema = new Schema({
    user_id: {
        type: Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    items: [
        {
            food_id: {
                type: Schema.Types.ObjectId,
                ref: 'Food',
                required: true
            },
            quantity: {
                type: Number,
                required: true
            },
            price: {
                type: Number,
                required: true
            }
        }
    ]
    ,
    total_price: {
        type: Number,
        required: true
    },
    status: {
        type: String,
        default: 'pending',
        enum: ['pending', 'preparing', 'done', 'canceled']
    },
    status_payment: {
        type: String,
        default: 'pending',
        enum: ['pending', 'paid', 'failed'] //pending la thanh toan khi nhan hang(hơi ngố tí nhưng k sao)
    },
    note: {
        type: String
    },
    scheduled_time: {
        type: Date
    },
    created_at: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('Order', OrderSchema);