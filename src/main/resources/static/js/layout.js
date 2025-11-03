document.addEventListener('DOMContentLoaded', function() {
    // Đường dẫn hiện tại
    const currentPath = window.location.pathname;

    // Lấy tất cả các mục menu trong sidebar
    const menuItems = document.querySelectorAll('.sidebar .menu-item');

    // Duyệt qua từng mục menu để kiểm tra và thêm lớp 'active' nếu trùng khớp
    menuItems.forEach(item => {
        item.classList.remove('active');

        // Lấy thuộc tính href của mục menu
        const href = item.getAttribute('href');
        if (href && currentPath.includes(href)) {
            item.classList.add('active');
        }
    });
});

