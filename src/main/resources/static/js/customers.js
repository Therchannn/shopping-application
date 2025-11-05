document.addEventListener('DOMContentLoaded', function() {
    // Lấy các phần tử DOM
    const customerModal = document.getElementById('customerModal');
    const deleteModal = document.getElementById('deleteModal');
    const btnAddCustomer = document.getElementById('btnAddCustomer');
    const btnCancel = document.getElementById('btnCancel');
    const btnCancelDelete = document.getElementById('btnCancelDelete');
    const closeBtn = document.querySelector('.close');
    const closeDeleteBtn = document.querySelector('.close-delete');
    const customerForm = document.getElementById('customerForm');
    const btnConfirmDelete = document.getElementById('btnConfirmDelete');

    // lấy vai trò
    const roleCheckbox = document.getElementById('role');
    const roleToggle = document.querySelector('.role-toggle');
    const roleIcon = document.getElementById('roleIcon');
    const roleText = document.getElementById('roleText');
    const passwordGroup = document.getElementById('passwordGroup');
    const passwordInput = document.getElementById('password');
    const usernameInput = document.getElementById('username');


    let isEditMode = false;
    let currentCustomerId = null;

    // Xử lý hiển thị vai trò
    function updateRoleDisplay(isAdmin) {
        if (isAdmin) {
            roleToggle.classList.add('admin');
            roleIcon.className = 'bi bi-shield-fill-check';
            roleText.textContent = 'Quản trị viên';
        } else {
            roleToggle.classList.remove('admin');
            roleIcon.className = 'bi bi-person-fill';
            roleText.textContent = 'Người dùng';
        }
    }

    // Chuyển đổi vai trò khi nhấp vào
    roleToggle.addEventListener('click', function(e) {
        e.preventDefault();
        roleCheckbox.checked = !roleCheckbox.checked;
        updateRoleDisplay(roleCheckbox.checked);
    });

    // Khởi tạo hiển thị vai trò default
    updateRoleDisplay(false);

    // xử lý thêm khách hàng
    btnAddCustomer.addEventListener('click', function() {
        isEditMode = false;
        document.getElementById('modalTitle').textContent = 'Thêm khách hàng';
        customerForm.reset();
        document.getElementById('customerId').value = '';
        document.getElementById('btnTitle').textContent = 'Thêm';
        // Hiện trường mật khẩu cho chế độ tạo
        passwordGroup.style.display = 'block';
        passwordInput.required = true;

        // Cho phép nhập username khi thêm mới
        usernameInput.removeAttribute('readonly');
        usernameInput.style.backgroundColor = '';
        usernameInput.style.cursor = '';

        updateRoleDisplay(false);
        customerModal.classList.add('show');
    });

    // Xử lý chỉnh sửa khách hàng
    const editButtons = document.querySelectorAll('.btn-edit');
    editButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            isEditMode = true;
            currentCustomerId = this.dataset.id;

            document.getElementById('modalTitle').textContent = 'Chỉnh sửa khách hàng';
            document.getElementById('btnTitle').textContent = 'Lưu';
            document.getElementById('customerId').value = currentCustomerId;
            document.getElementById('username').value = this.dataset.username || '';
            document.getElementById('name').value = this.dataset.name || '';
            document.getElementById('phone').value = this.dataset.phone || '';
            document.getElementById('address').value = this.dataset.address || '';

            const isAdmin = this.dataset.role === 'true';
            roleCheckbox.checked = isAdmin;
            updateRoleDisplay(isAdmin);

            // Ẩn trường mật khẩu cho chế độ chỉnh sửa
            passwordGroup.style.display = 'none';
            passwordInput.required = false;

            // Không cho phép sửa username khi chỉnh sửa
            usernameInput.setAttribute('readonly', 'readonly');
            usernameInput.style.backgroundColor = '#f3f4f6';
            usernameInput.style.cursor = 'not-allowed';
            customerModal.classList.add('show');
        });
    });

    // Xử lý xóa khách hàng
    const deleteButtons = document.querySelectorAll('.btn-delete');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            currentCustomerId = this.dataset.id; // This is UUID
            document.getElementById('deleteCustomerName').textContent = this.dataset.username;
            document.getElementById('deleteCustomerId').value = currentCustomerId;
            deleteModal.classList.add('show');
        });
    });

    // đóng modal khi nhấp vào nút đóng hoặc hủy
    if (closeBtn) {
        closeBtn.addEventListener('click', () => customerModal.classList.remove('show'));
    }
    if (closeDeleteBtn) {
        closeDeleteBtn.addEventListener('click', () => deleteModal.classList.remove('show'));
    }
    if (btnCancel) {
        btnCancel.addEventListener('click', () => customerModal.classList.remove('show'));
    }
    if (btnCancelDelete) {
        btnCancelDelete.addEventListener('click', () => deleteModal.classList.remove('show'));
    }

    // xử lý gửi form thêm/chỉnh sửa khách hàng
    customerForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        const formData = {
            username: document.getElementById('username').value.trim(),
            name: document.getElementById('name').value.trim(),
            phone: document.getElementById('phone').value.trim(),
            address: document.getElementById('address').value.trim(),
            role: roleCheckbox.checked
        };

        if (!isEditMode) {
            const password = document.getElementById('password').value.trim();
            if (!password || password.length < 6) {
                alert('Mật khẩu phải có ít nhất 6 ký tự!');
                return;
            }
            formData.password = password;
        }

        // Validate
//        if (!formData.username || !formData.name) {
//            alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
//            return;
//        }

        try {
            const pathParts = window.location.pathname.split('/').filter(p => p);
            let baseUrl = '';

            if (pathParts.length > 0 && pathParts[0] !== 'admin') {
                baseUrl = `/${pathParts[0]}`;
            }

            let url;
            if (isEditMode) {
                url = `${baseUrl}/admin/customers/edit/${currentCustomerId}`;
            } else {
                url = `${baseUrl}/admin/customers/add`;
            }

            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData)
            });

            const result = await response.json();

            if (result.success) {
                alert(result.message);
                customerModal.classList.remove('show');
                location.reload(); // Reload để cập nhật danh sách khách hàng
            } else {
                alert(result.message || 'Có lỗi xảy ra!');
            }
        } catch (error) {
            alert('Có lỗi xảy ra khi xử lý yêu cầu!');
        }
    });

    // Xử lý xác nhận xóa khách hàng
    if (btnConfirmDelete) {
        btnConfirmDelete.addEventListener('click', async function() {
            const customerId = document.getElementById('deleteCustomerId').value;
            try {
                const pathParts = window.location.pathname.split('/').filter(p => p);
                let baseUrl = '';
                if (pathParts.length > 0 && pathParts[0] !== 'admin') {
                    baseUrl = `/${pathParts[0]}`;
                }

                const url = `${baseUrl}/admin/customers/delete/${customerId}`;

                const response = await fetch(url, {
                    method: 'POST'
                });

                const result = await response.json();

                if (result.success) {
                    alert(result.message);
                    deleteModal.classList.remove('show');
                    location.reload();
                } else {
                    alert(result.message || 'Có lỗi xảy ra khi xóa!');
                }
            } catch (error) {
                alert('Có lỗi xảy ra khi xử lý yêu cầu!');
            }
        });
    }
});

