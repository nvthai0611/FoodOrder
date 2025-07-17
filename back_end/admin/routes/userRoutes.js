const express = require('express');
const router = express.Router();
const User = require('../../models/User');

// Danh sách users
router.get('/', async (req, res) => {
    const page = parseInt(req.query.page) || 1;
    const limit = 5;
    const search = req.query.search || '';

    const query = {
        is_deleted: false,
        $or: [
            { name: new RegExp(search, 'i') },
            { email: new RegExp(search, 'i') }
        ]
    };

    const totalUsers = await User.countDocuments(query);
    const totalPages = Math.ceil(totalUsers / limit);

    const users = await User.find(query)
        .skip((page - 1) * limit)
        .limit(limit);

    const message = req.session.message;
    delete req.session.message; // Xoá sau khi sử dụng 1 lần

    res.render('User/index', {
        users,
        currentPage: page,
        totalPages,
        search,
        message
    });
});

// Form tạo user
router.get('/new', (req, res) => {
    res.render('User/create');
});

// Xử lý tạo user
router.post('/User/create', async (req, res) => {
    const { name, email, password, phone, role } = req.body;
    await User.create({ name, email, password, phone, role });

    // Lưu message vào session
    req.session.message = 'Thêm người dùng thành công!';
    res.redirect('/admin');
});
// Form sửa user
router.get('/:id/edit', async (req, res) => {
    const user = await User.findById(req.params.id);
    res.render('User/edit', { user });
});

// Xử lý cập nhật user
router.post('/User/:id/update', async (req, res) => {
    const { name, email, password, phone, role } = req.body;
    const checkUser = await User.findById(req.params.id);
    if (checkUser.email !== email) {
        res.render('User/edit', {
            user,
            message: 'Không được sửa email!'
        })
    };

    if (checkUser.password != password) {
        res.render('User/edit', {
            user,
            message: 'Không được sửa password!'
        })
    }
    if (checkUser.phone != phone) {
        res.render('User/edit', {
            user,
            message: 'Không được sửa số diện thoại!'
        })
    }
    await User.findByIdAndUpdate(req.params.id, { name, role });

    const user = await User.findById(req.params.id);
    res.render('User/edit', {
        user,
        message: 'Cập nhật thành công!'
    });
});

// Xoá mềm user
router.post('/:id/delete', async (req, res) => {
    await User.findByIdAndUpdate(req.params.id, { is_deleted: true });
    res.redirect('/admin');
});

module.exports = router;
