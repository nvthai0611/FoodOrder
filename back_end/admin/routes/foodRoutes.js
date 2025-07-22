const express = require('express');
const router = express.Router();
const Food = require('../../models/Food');
const Category = require('../../models/Category');
const uploadCloud = require('../../middlewares/servers/imageUpload');

// Danh sách sản phẩm với filter + phân trang
router.get('/', async (req, res) => {
    const page = parseInt(req.query.page) || 1;
    const limit = 5;
    const search = req.query.search || '';
    const categoryId = req.query.category || '';

    const query = {
        ...(search && { name: new RegExp(search, 'i') }),
        ...(categoryId && { category: categoryId })
    };

    const total = await Food.countDocuments(query);
    const foods = await Food.find(query)
        .populate('category')
        .skip((page - 1) * limit)
        .limit(limit);

    const categories = await Category.find({ is_available: true });

    const message = req.session.message;
    delete req.session.message;

    if (req.xhr) {
        return res.render('foods/_table', {
            foods,
            currentPage: page,
            totalPages: Math.ceil(total / limit)
        });
    }

    res.render('foods/index', {
        foods,
        categories,
        currentPage: page,
        totalPages: Math.ceil(total / limit),
        search,
        categoryId,
        message
    });
});


// Form tạo sản phẩm
router.get('/new', async (req, res) => {
    const categories = await Category.find({ is_available: true });
    res.render('foods/create', { categories });
});

// Xử lý tạo sản phẩm
router.post('/create', uploadCloud.single('image'), async (req, res) => {
    await Food.create(req.body);
    req.session.message = 'Thêm sản phẩm thành công!';
    res.redirect('/admin/product');
});

// Form chỉnh sửa sản phẩm
router.get('/:id/edit', async (req, res) => {
    const food = await Food.findById(req.params.id);
    const categories = await Category.find({ is_available: true });
    res.render('foods/edit', { food, categories });
});

// Xử lý cập nhật sản phẩm
router.post('/:id/update', uploadCloud.single('image'), async (req, res) => {
    await Food.findByIdAndUpdate(req.params.id, req.body);

    const food = await Food.findById(req.params.id); // Lấy lại bản ghi mới
    const categories = await Category.find({ is_available: true });

    res.render('foods/edit', {
        food,
        categories,
        message: 'Cập nhật sản phẩm thành công!'
    });
});

// Xử lý xoá sản phẩm
router.post('/:id/delete', async (req, res) => {
    await Food.findByIdAndDelete(req.params.id);
    res.redirect('/admin/product');
});

module.exports = router;
