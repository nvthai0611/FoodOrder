const express = require('express');
const router = express.Router();
const Food = require('../../models/Food');
const Category = require('../../models/Category');
const uploadCloud = require('../../middlewares/servers/imageUpload'); // Đảm bảo đường dẫn đúng đến file imageUpload.js của bạn

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
router.post('/create', uploadCloud.single('image_upload'), async (req, res) => { // Sử dụng 'image_upload' như trong EJS
    try {
        const { name, category, price, description, isBestSeller } = req.body;
        let finalImageUrl = '';

        // Kiểm tra xem có file ảnh được tải lên không
        if (!req.file) {
            const categories = await Category.find({ is_available: true });
            req.session.message = 'Vui lòng tải lên một ảnh cho sản phẩm.';
            return res.render('foods/create', { categories: categories, message: req.session.message });
        }

        // imageUpload middleware đã đặt secure_url vào req.file.path
        finalImageUrl = req.file.path;

        const newFood = await Food.create({
            name,
            category,
            price,
            image_url: finalImageUrl,
            description,
            isBestSeller: !!isBestSeller // Đảm bảo chuyển đổi sang boolean
        });

        req.session.message = 'Thêm sản phẩm thành công!';
        res.redirect('/admin/product');
    } catch (error) {
        console.error('Lỗi khi thêm sản phẩm:', error);
        req.session.message = 'Lỗi khi thêm sản phẩm: ' + error.message;
        res.redirect('/admin/product/new'); // Chuyển hướng về trang tạo mới với lỗi
    }
});

// Form chỉnh sửa sản phẩm
router.get('/:id/edit', async (req, res) => {
    try {
        const food = await Food.findById(req.params.id);
        if (!food) {
            req.session.message = 'Không tìm thấy sản phẩm!';
            return res.redirect('/admin/product');
        }
        const categories = await Category.find({ is_available: true });
        res.render('foods/edit', { food, categories });
    } catch (error) {
        console.error('Lỗi khi tải trang chỉnh sửa sản phẩm:', error);
        req.session.message = 'Lỗi khi tải trang chỉnh sửa sản phẩm: ' + error.message;
        res.redirect('/admin/product');
    }
});

// Xử lý cập nhật sản phẩm
router.post('/:id/update', uploadCloud.single('image_upload'), async (req, res) => { // Sử dụng 'image_upload' như trong EJS
    try {
        const foodId = req.params.id;
        const { name, category, price, description, isBestSeller } = req.body; // Bỏ image_url khỏi req.body

        let updateData = {
            name,
            category,
            price,
            description,
            isBestSeller: !!isBestSeller
        };

        // Nếu có file ảnh mới được tải lên
        if (req.file) {
            updateData.image_url = req.file.path; // imageUpload middleware đã đặt secure_url vào req.file.path
            // Tùy chọn: Xóa ảnh cũ trên Cloudinary nếu bạn muốn
            // Để làm điều này, bạn cần lưu public_id của ảnh cũ và gọi cloudinary.uploader.destroy(publicId);
            // Bạn sẽ cần import cloudinary.v2 vào file này nếu chưa có.
            // Ví dụ:
            const cloudinary = require('cloudinary').v2;
            const existingFood = await Food.findById(foodId);
            if (existingFood && existingFood.image_url) {
                const publicId = existingFood.image_url.split('/').pop().split('.')[0];
                await cloudinary.uploader.destroy(`Food/${publicId}`);
            }
        } else {
            // Nếu không có file mới, giữ nguyên image_url hiện có của sản phẩm
            // Lấy dữ liệu sản phẩm hiện tại để giữ lại URL ảnh cũ
            const existingFood = await Food.findById(foodId);
            if (existingFood) {
                updateData.image_url = existingFood.image_url;
            }
        }

        const updatedFood = await Food.findByIdAndUpdate(foodId, updateData, { new: true });

        if (!updatedFood) {
            req.session.message = 'Không tìm thấy sản phẩm để cập nhật!';
            return res.redirect('/admin/product');
        }

        const categories = await Category.find({ is_available: true });

        // Render lại trang chỉnh sửa với thông báo thành công và dữ liệu sản phẩm mới nhất
        res.render('foods/edit', {
            food: updatedFood,
            categories,
            message: 'Cập nhật sản phẩm thành công!'
        });

    } catch (error) {
        console.error('Lỗi khi cập nhật sản phẩm:', error);
        req.session.message = 'Lỗi khi cập nhật sản phẩm: ' + error.message;
        res.redirect(`/admin/product/${req.params.id}/edit`);
    }
});

// Xử lý xoá sản phẩm
router.post('/:id/delete', async (req, res) => {
    try {
        await Food.findByIdAndDelete(req.params.id);
        req.session.message = 'Xóa sản phẩm thành công!';
        res.redirect('/admin/product');
    } catch (error) {
        console.error('Lỗi khi xóa sản phẩm:', error);
        req.session.message = 'Lỗi khi xóa sản phẩm: ' + error.message;
        res.redirect('/admin/product');
    }
});

module.exports = router;