// /routes/admin/order.js
const express = require('express');
const router = express.Router();
const mongoose = require('mongoose'); // Thêm mongoose để xử lý ObjectId
const Order = require('../../models/Order');
const Food = require('../../models/Food');
const User = require('../../models/User');

// Helper function để lấy khoảng thời gian của tháng/năm
const getMonthYearRange = (year, month) => {
    let startDate, endDate;
    const currentYear = new Date().getFullYear();

    // Nếu không có năm, mặc định là năm hiện tại
    const targetYear = year ? parseInt(year) : currentYear;

    if (month) {
        // Lọc theo tháng và năm cụ thể
        const targetMonth = parseInt(month) - 1; // Tháng trong JS là 0-11
        startDate = new Date(targetYear, targetMonth, 1);
        endDate = new Date(targetYear, targetMonth + 1, 0, 23, 59, 59, 999); // Ngày cuối cùng của tháng
    } else {
        // Lọc theo cả năm
        startDate = new Date(targetYear, 0, 1); // Ngày 1 tháng 1 của năm
        endDate = new Date(targetYear + 1, 0, 0, 23, 59, 59, 999); // Ngày cuối cùng của năm đó
    }
    return { startDate, endDate };
};

// --- Route chính để hiển thị Dashboard EJS và truyền dữ liệu ban đầu ---
router.get('/', async (req, res) => {
    try {
        const page = parseInt(req.query.page) || 1;
        const limit = parseInt(req.query.limit) || 10;
        const search = req.query.search || '';
        const month = req.query.month || '';
        const year = req.query.year || new Date().getFullYear().toString(); // Mặc định là năm hiện tại nếu không có
        const status = req.query.status || '';
        const query = {};

        // 1. Lọc theo thời gian (năm/tháng)
        const { startDate, endDate } = getMonthYearRange(year, month);
        if (startDate && endDate) {
            query.created_at = { $gte: startDate, $lte: endDate };
        }

        // 2. Lọc theo trạng thái
        if (status && ['pending', 'preparing', 'done', 'canceled'].includes(status)) {
            query.status = status;
        }

        // 3. Lọc theo tìm kiếm (note hoặc _id)
        if (search) {
            const searchRegex = new RegExp(search, 'i');
            const orConditions = [
                { 'note': searchRegex }
            ];

            // Kiểm tra nếu chuỗi search có thể là một ObjectId hợp lệ
            if (mongoose.Types.ObjectId.isValid(search)) {
                orConditions.push({ '_id': new mongoose.Types.ObjectId(search) });
            }
            query.$or = orConditions;
        }

        console.log('--- Query được xây dựng: ---');
        console.log(JSON.stringify(query, null, 2)); // Log query dưới dạng JSON dễ đọc

        // Đếm tổng số đơn hàng phù hợp với query để tính tổng số trang
        const totalOrders = await Order.countDocuments(query);
        console.log(`Tổng số đơn hàng tìm thấy: ${totalOrders}`);

        const totalPages = Math.ceil(totalOrders / limit);

        // Lấy dữ liệu đơn hàng cho trang hiện tại
        const orders = await Order.find(query)
            .sort({ created_at: -1 }) // Sắp xếp theo thời gian tạo mới nhất
            .skip((page - 1) * limit)
            .limit(limit)
            .populate([
                { path: 'user_id', select: 'name email' },
                { path: 'items.food_id', select: 'name' }
            ]);
        console.log(`Đơn hàng trên trang hiện tại (${orders.length} bản ghi):`, orders);


        res.render('orders/index', {
            orders: orders,
            currentPage: page,
            totalPages: totalPages,
            limit: limit,
            initialSearch: search,
            initialMonth: month,
            initialYear: year,
            initialStatus: status,
        });

    } catch (error) {
        console.error('Lỗi khi tải trang Dashboard:', error);
        res.status(500).send('Lỗi server khi tải Dashboard: ' + error.message);
    }
});

// --- API endpoint để lấy dữ liệu đơn hàng JSON (cho AJAX sau này) ---
router.get('/data', async (req, res) => { // Dùng /data để lấy JSON cho AJAX
    try {
        const page = parseInt(req.query.page) || 1;
        const limit = parseInt(req.query.limit) || 10;
        const search = req.query.search || '';
        const month = req.query.month || '';
        const year = req.query.year || ''; // Để trống để cho phép không lọc theo năm
        const status = req.query.status || '';

        const query = {};
        const { startDate, endDate } = getMonthYearRange(year, month);
        if (startDate && endDate) {
            query.created_at = { $gte: startDate, $lte: endDate };
        }
        if (status && ['pending', 'preparing', 'done', 'canceled'].includes(status)) {
            query.status = status;
        }
        if (search) {
            const searchRegex = new RegExp(search, 'i');
            const orConditions = [
                { 'note': searchRegex }
            ];
            if (mongoose.Types.ObjectId.isValid(search)) {
                orConditions.push({ '_id': new mongoose.Types.ObjectId(search) });
            }
            query.$or = orConditions;
        }
        console.log(status);

        const totalOrders = await Order.countDocuments(query);
        const totalPages = Math.ceil(totalOrders / limit);

        const orders = await Order.find(query)
            .sort({ created_at: -1 })
            .skip((page - 1) * limit)
            .limit(limit)
            .populate([
                { path: 'user_id', select: 'name email' },
                { path: 'items.food_id', select: 'name' }
            ]);

        // Trả về dữ liệu cần thiết cho client
        res.json({
            docs: orders,
            page: page,
            totalPages: totalPages,
            limit: limit,
            totalDocs: totalOrders
        });
    } catch (error) {
        console.error('Lỗi khi lấy danh sách đơn hàng (AJAX):', error);
        res.status(500).json({ message: 'Lỗi server', error: error.message });
    }
});

// --- API để lấy chi tiết một Đơn hàng (dùng cho Modal AJAX) ---
router.get('/:id', async (req, res) => {
    try {
        // Kiểm tra xem ID có phải là ObjectId hợp lệ không để tránh lỗi Mongoose
        if (!mongoose.Types.ObjectId.isValid(req.params.id)) {
            return res.status(400).json({ message: 'ID đơn hàng không hợp lệ.' });
        }

        const order = await Order.findById(req.params.id)
            .populate({ path: 'user_id', select: 'name email' })
            .populate({ path: 'items.food_id', select: 'name' });

        if (!order) {
            return res.status(404).json({ message: 'Không tìm thấy đơn hàng' });
        }
        res.json(order);
    } catch (error) {
        console.error('Lỗi khi lấy chi tiết đơn hàng:', error);
        res.status(500).json({ message: 'Lỗi server', error: error.message });
    }
});

// --- API để cập nhật trạng thái đơn hàng (DÙNG POST thay PUT) ---
router.post('/:id/update-status', async (req, res) => { // Thay đổi đường dẫn và phương thức thành POST
    try {
        const orderId = req.params.id;
        const { newStatus } = req.body; // Lấy trạng thái mới từ body request

        // Validate order ID
        if (!mongoose.Types.ObjectId.isValid(orderId)) {
            return res.status(400).json({ message: 'ID đơn hàng không hợp lệ.' });
        }

        // Validate new status
        const validStatuses = ['pending', 'preparing', 'done', 'canceled'];
        if (!newStatus || !validStatuses.includes(newStatus)) {
            return res.status(400).json({ message: 'Trạng thái mới không hợp lệ hoặc bị thiếu.' });
        }

        const updatedOrder = await Order.findByIdAndUpdate(
            orderId,
            { status: newStatus },
            { new: true } // Trả về tài liệu đã cập nhật
        );

        if (!updatedOrder) {
            return res.status(404).json({ message: 'Không tìm thấy đơn hàng để cập nhật.' });
        }

        res.json({
            message: 'Cập nhật trạng thái đơn hàng thành công!',
            order: updatedOrder
        });

    } catch (error) {
        console.error('Lỗi khi cập nhật trạng thái đơn hàng:', error);
        res.status(500).json({ message: 'Lỗi server khi cập nhật trạng thái đơn hàng', error: error.message });
    }
});


// --- API cho dữ liệu biểu đồ (Doanh thu theo tháng) ---
router.get('/charts/monthly-sales', async (req, res) => {
    try {
        const { year } = req.query;
        // Sử dụng năm hiện tại nếu không có hoặc không hợp lệ
        const targetYear = (year && !isNaN(parseInt(year))) ? parseInt(year) : new Date().getFullYear();

        const salesData = await Order.aggregate([
            {
                $match: {
                    created_at: {
                        $gte: new Date(targetYear, 0, 1),
                        $lt: new Date(targetYear + 1, 0, 1)
                    },
                    status: { $in: ['done', 'preparing'] } // Chỉ tính doanh thu từ đơn hàng hoàn thành/đang chuẩn bị
                }
            },
            {
                $group: {
                    _id: { month: { $month: "$created_at" } },
                    totalSales: { $sum: "$total_price" }
                }
            },
            {
                $sort: { "_id.month": 1 }
            }
        ]);

        const monthlySales = Array(12).fill(0);
        salesData.forEach(data => {
            if (data._id.month >= 1 && data._id.month <= 12) {
                monthlySales[data._id.month - 1] = data.totalSales;
            }
        });

        res.json(monthlySales);
    } catch (error) {
        console.error('Lỗi khi lấy dữ liệu doanh thu hàng tháng:', error);
        res.status(500).json({ message: 'Lỗi server', error: error.message });
    }
});

// --- API cho dữ liệu biểu đồ (Phân bổ trạng thái đơn hàng) - THÊM BỘ LỌC THÁNG/NĂM ---
router.get('/charts/status-distribution', async (req, res) => {
    try {
        const { year, month } = req.query; // Lấy tháng và năm từ query
        const matchQuery = {};

        // Nếu có năm hoặc tháng, áp dụng bộ lọc thời gian
        if (year || month) {
            const { startDate, endDate } = getMonthYearRange(year, month);
            if (startDate && endDate) {
                matchQuery.created_at = { $gte: startDate, $lte: endDate };
            }
        }

        const aggregationPipeline = [];
        if (Object.keys(matchQuery).length > 0) {
            aggregationPipeline.push({ $match: matchQuery });
        }

        aggregationPipeline.push({
            $group: {
                _id: "$status",
                count: { $sum: 1 }
            }
        });

        const statusDistribution = await Order.aggregate(aggregationPipeline);
        res.json(statusDistribution);
    } catch (error) {
        console.error('Lỗi khi lấy dữ liệu phân bổ trạng thái:', error);
        res.status(500).json({ message: 'Lỗi server', error: error.message });
    }
});

module.exports = router;