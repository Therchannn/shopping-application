function viewOrderDetail(orderId) {
    const modal = document.getElementById('orderDetailModal');

    // Hiển thị modal
    modal.classList.add('show');

    const pathParts = window.location.pathname.split('/').filter(p => p);
    let baseUrl = '';

    if (pathParts.length > 0 && pathParts[0] !== 'admin') {
        baseUrl = `/${pathParts[0]}`;
    }

    // Gọi API để lấy chi tiết đơn hàng
    fetch(`${baseUrl}/admin/orders/detail/${orderId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                renderOrderDetail(data.data);
            } else {
                showError(data.message || 'Không thể tải thông tin đơn hàng');
            }
        })
        .catch(error => {
            showError('Có lỗi xảy ra khi tải thông tin đơn hàng');
        });
}

function renderOrderDetail(order) {
    // Get base URL
    const pathParts = window.location.pathname.split('/').filter(p => p);
    let baseUrl = '';
    if (pathParts.length > 0 && pathParts[0] !== 'admin') {
        baseUrl = `/${pathParts[0]}`;
    }

    // Format số tiền
    const formatMoney = (value) => {
        if (!value) return '0 đ';
        return new Intl.NumberFormat('vi-VN').format(value) + ' đ';
    };

    // Format ngày
    const formatDate = (dateStr) => {
        if (!dateStr) return 'N/A';
        const date = new Date(dateStr);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${day}/${month}/${year} ${hours}:${minutes}`;
    };

    // Color mapping - Chuyển tên màu thành mã hex
    const getColorDisplay = (colorName, showText = true) => {
        const colorMap = {
            'Đen': '#000000',
            'Trắng': '#FFFFFF',
            'Xanh Navy': '#001f3f',
            'Xanh Dương': '#0074D9',
            'Xanh Lá': '#2ECC40',
            'Xanh Cổ Vịt': '#39CCCC',
            'Đỏ': '#FF4136',
            'Hồng': '#F012BE',
            'Nâu': '#8B4513',
            'Be': '#F5DEB3',
            'Xám': '#AAAAAA',
            'Xám Đậm': '#808080',
            'Cam': '#FF851B',
            'Vàng': '#FFDC00',
            'Tím': '#B10DC9'
        };

        const hexColor = colorMap[colorName] || '#CCCCCC';
        const borderColor = colorName === 'Trắng' ? '#d1d5db' : 'transparent';

        return `
            <div style="display: inline-flex; align-items: center; gap: 8px; justify-content: center;">
                <div style="
                    width: 28px;
                    height: 28px;
                    background-color: ${hexColor};
                    border: 2px solid ${borderColor};
                    border-radius: 6px;
                    box-shadow: 0 2px 4px rgba(0,0,0,0.15);
                " title="${colorName}"></div>
                ${showText ? `<span style="font-size: 13px; color: #6b7280;">${colorName}</span>` : ''}
            </div>
        `;
    };

    // Status badge
    const getStatusBadge = (status) => {
        const badges = {
            'Cancelled': { text: 'Đã hủy', bg: '#ef4444' },
            'Pending': { text: 'Chờ xác nhận', bg: '#f59e0b' },
            'Confirmed': { text: 'Đã xác nhận', bg: '#3b82f6' },
            'Completed': { text: 'Hoàn thành', bg: '#10b981' }
        };
        const badge = badges[status] || { text: 'N/A', bg: '#6b7280' };
        return `<span style="background-color: ${badge.bg}; color: white; padding: 6px 16px; border-radius: 20px; font-size: 14px; font-weight: 600;">${badge.text}</span>`;
    };

    // Payment method
    const getPaymentMethod = (method) => {
        if (method === 'TIEN_MAT') {
            return '<i class="bi bi-cash" style="color: #5eead4; font-size: 20px; margin-right: 8px;"></i>Tiền mặt';
        } else if (method === 'CHUYEN_KHOAN') {
            return '<i class="bi bi-bank" style="color: #22d3ee; font-size: 20px; margin-right: 8px;"></i>Chuyển khoản';
        }
        return 'N/A';
    };

    // Fill thông tin đơn hàng
    document.getElementById('detail-order-id').textContent = order.id || 'N/A';
    document.getElementById('detail-created-at').textContent = formatDate(order.createdAt);
    document.getElementById('detail-payment-method').innerHTML = getPaymentMethod(order.paymentMethod);
    document.getElementById('detail-shipping-fee').textContent = formatMoney(order.shippingFee);
    document.getElementById('detail-status').innerHTML = getStatusBadge(order.status);

    // Fill thông tin khách hàng
    document.getElementById('detail-user-name').textContent = order.userName || 'N/A';
    document.getElementById('detail-user-username').textContent = order.userUsername || 'N/A';
    document.getElementById('detail-user-phone').textContent = order.userPhone || 'Chưa cập nhật';
    document.getElementById('detail-user-address').textContent = order.userAddress || 'Chưa cập nhật';

    // Render danh sách sản phẩm bằng cách clone template
    const itemsTbody = document.getElementById('detail-items-tbody');
    const itemTemplate = document.getElementById('item-row-template');

    // Xóa các rows cũ (trừ template)
    const oldRows = itemsTbody.querySelectorAll('tr:not(#item-row-template)');
    oldRows.forEach(row => row.remove());

    if (order.items && order.items.length > 0) {
        order.items.forEach((item, index) => {
            // Clone template
            const row = itemTemplate.cloneNode(true);
            row.removeAttribute('id');
            row.style.display = '';

            // Fill dữ liệu
            row.querySelector('.item-index').textContent = index + 1;
            row.querySelector('.item-name').textContent = item.productName || 'N/A';
            row.querySelector('.item-color').innerHTML = getColorDisplay(item.color || 'N/A', false); // Chỉ hiện ô màu
            row.querySelector('.item-size').textContent = item.size || 'N/A';
            row.querySelector('.item-price').textContent = formatMoney(item.price);
            row.querySelector('.item-quantity').textContent = item.quantity;
            row.querySelector('.item-total').textContent = formatMoney(item.totalPrice);

            // Xử lý image - DB đã có path đầy đủ, chỉ cần thêm baseUrl
            const img = row.querySelector('.item-image');
            const imgPath = item.productImage || '/images/default-product.png';
            img.src = baseUrl ? baseUrl + imgPath : imgPath;
            img.alt = item.productName || 'Product';
            img.onerror = function() {
                this.src = baseUrl + '/images/default-product.png';
            };

            itemsTbody.appendChild(row);
        });
    }

    // Fill tổng tiền
    const subtotal = order.total && order.shippingFee ? order.total - order.shippingFee : order.total;
    document.getElementById('detail-subtotal').textContent = formatMoney(subtotal);
    document.getElementById('detail-shipping-fee-2').textContent = formatMoney(order.shippingFee);
    document.getElementById('detail-total').textContent = formatMoney(order.total);

    // Render action buttons bằng cách clone template
    const actionsDiv = document.getElementById('detail-actions');
    const progressBtnTemplate = document.getElementById('btn-progress-template');
    const cancelBtnTemplate = document.getElementById('btn-cancel-template');

    // Xóa buttons cũ (trừ template)
    const oldBtns = actionsDiv.querySelectorAll('button:not([id$="-template"])');
    oldBtns.forEach(btn => btn.remove());

    if (order.status !== 'Cancelled') {
        // Nút tiến độ (nếu chưa hoàn thành)
        if (order.status !== 'Completed') {
            const progressBtn = progressBtnTemplate.cloneNode(true);
            progressBtn.removeAttribute('id');
            progressBtn.style.display = '';

            const nextStatusText = order.status === 'Pending' ? 'Xác nhận đơn hàng' : 'Đánh dấu hoàn thành';
            progressBtn.querySelector('.btn-text').textContent = nextStatusText;
            progressBtn.onclick = function() {
                closeOrderDetailModal();
                progressOrder(order.id, order.status);
            };

            actionsDiv.appendChild(progressBtn);
        }

        // Nút hủy
        const cancelBtn = cancelBtnTemplate.cloneNode(true);
        cancelBtn.removeAttribute('id');
        cancelBtn.style.display = '';
        cancelBtn.onclick = function() {
            closeOrderDetailModal();
            cancelOrder(order.id);
        };

        actionsDiv.appendChild(cancelBtn);
    }
}

function closeOrderDetailModal() {
    const modal = document.getElementById('orderDetailModal');
    modal.classList.remove('show');
}

function progressOrder(orderId, currentStatus) {
    let nextStatus = '';
    let statusText = '';

    // Xác định trạng thái tiếp theo
    if (currentStatus === 'Pending') {
        nextStatus = 'Confirmed';
        statusText = 'Đã xác nhận';
    } else if (currentStatus === 'Confirmed') {
        nextStatus = 'Completed';
        statusText = 'Hoàn thành';
    } else {
        showWarning('Đơn hàng đã ở trạng thái cuối cùng!');
        return;
    }

    // Hiển thị modal xác nhận
    openStatusModal(orderId, nextStatus, currentStatus, 'progress', statusText);
}

function cancelOrder(orderId) {
    // Hiển thị modal xác nhận
    openStatusModal(orderId, 'Cancelled', null, 'cancel', 'Đã hủy');
}

function openStatusModal(orderId, newStatus, currentStatus, action, statusText) {
    const modal = document.getElementById('statusModal');
    const title = document.getElementById('statusModalTitle');
    const message = document.getElementById('statusModalMessage');
    const confirmBtn = document.getElementById('btnConfirmStatus');

    document.getElementById('statusOrderId').value = orderId;
    document.getElementById('statusAction').value = action;
    document.getElementById('statusCurrentStatus').value = currentStatus || '';

    if (action === 'cancel') {
        title.textContent = 'Xác nhận hủy đơn hàng';
        message.innerHTML = `Bạn có chắc chắn muốn <strong style="color: #ef4444;">HỦY</strong> đơn hàng này?<br><span style="font-size: 0.875em; color: #6b7280;">Hành động này không thể hoàn tác.</span>`;
        confirmBtn.textContent = 'Hủy đơn';
        confirmBtn.className = 'btn btn-danger';
    } else {
        title.textContent = 'Xác nhận đẩy tiến độ';
        message.innerHTML = `Xác nhận chuyển đơn hàng sang trạng thái <strong style="color: #10b981;">"${statusText}"</strong>?<br><span style="font-size: 0.875em; color: #6b7280;">Trạng thái sẽ được cập nhật ngay lập tức.</span>`;
        confirmBtn.textContent = 'Xác nhận';
        confirmBtn.className = 'btn btn-primary';
    }

    modal.classList.add('show');
}
function updateOrderStatus(orderId, newStatus) {
    const pathParts = window.location.pathname.split('/').filter(p => p);
    let baseUrl = '';

    if (pathParts.length > 0 && pathParts[0] !== 'admin') {
        baseUrl = `/${pathParts[0]}`;
    }

    fetch(`${baseUrl}/admin/orders/update-status/${orderId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ status: newStatus })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Cập nhật trạng thái thất bại');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            showSuccess('Cập nhật trạng thái thành công!');
            // Reload trang sau 1 giây
            setTimeout(() => {
                location.reload();
            }, 1000);
        } else {
            showError('Cập nhật thất bại: ' + (data.message || 'Lỗi không xác định'));
        }
    })
    .catch(error => {
        showError('Có lỗi xảy ra khi cập nhật trạng thái: ' + error.message);
    });
}

function openFilterModal() {
    const modal = document.getElementById('filterModal');
    modal.classList.add('show');
}

function closeFilterModal() {
    const modal = document.getElementById('filterModal');
    modal.classList.remove('show');
}

function resetFilter() {
    document.getElementById('filterForm').reset();
    document.querySelectorAll('#filterForm input[type="checkbox"]').forEach(cb => cb.checked = true);
    applyFilter();
}

function applyFilter() {
    const statusFilters = Array.from(document.querySelectorAll('input[name="status"]:checked')).map(cb => cb.value);
    const paymentFilters = Array.from(document.querySelectorAll('input[name="payment"]:checked')).map(cb => cb.value);
    const dateFrom = document.getElementById('dateFrom').value;
    const dateTo = document.getElementById('dateTo').value;
    const priceFrom = parseFloat(document.getElementById('priceFrom').value) || 0;
    const priceTo = parseFloat(document.getElementById('priceTo').value) || Infinity;

    filterOrders(statusFilters, paymentFilters, dateFrom, dateTo, priceFrom, priceTo);
    closeFilterModal();
}

function filterOrders(statusFilters, paymentFilters, dateFrom, dateTo, priceFrom, priceTo) {
    const rows = document.querySelectorAll('.data-table tbody tr[data-status]');
    let visibleCount = 0;

    rows.forEach((row) => {
        // Lấy giá trị từ data attributes
        const status = row.getAttribute('data-status');
        const payment = row.getAttribute('data-payment');
        const total = parseFloat(row.getAttribute('data-total')) || 0;
        const dateStr = row.getAttribute('data-date');

        let orderDate = null;
        if (dateStr) {
            orderDate = new Date(dateStr);
        }

        let show = true;

        // Lọc theo trạng thái
        if (statusFilters.length > 0 && !statusFilters.includes(status)) {
            show = false;
        }

        // Lọc theo phương thức thanh toán
        if (paymentFilters.length > 0 && !paymentFilters.includes(payment)) {
            show = false;
        }

        // Lọc theo ngày
        if (dateFrom && orderDate) {
            const fromDate = new Date(dateFrom);
            fromDate.setHours(0, 0, 0, 0);
            if (orderDate < fromDate) show = false;
        }

        if (dateTo && orderDate) {
            const toDate = new Date(dateTo);
            toDate.setHours(23, 59, 59, 999);
            if (orderDate > toDate) show = false;
        }

        // Lọc theo giá
        if (total < priceFrom || total > priceTo) {
            show = false;
        }

        row.style.display = show ? '' : 'none';
        if (show) visibleCount++;
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const filterBtn = document.getElementById('btnFilter');
    filterBtn.addEventListener('click', function(e) {
        e.preventDefault();
        openFilterModal();
    });

    const filterModal = document.getElementById('filterModal');
    const closeFilterBtn = filterModal.querySelector('.close');
    closeFilterBtn.addEventListener('click', closeFilterModal);

    const statusModal = document.getElementById('statusModal');
    const closeStatusBtn = statusModal.querySelector('.close-delete');
    const btnCancelStatus = document.getElementById('btnCancelStatus');
    const btnConfirmStatus = document.getElementById('btnConfirmStatus');

    // Close status modal - Nút X
    closeStatusBtn.addEventListener('click', function() {
        statusModal.classList.remove('show');
    });

    // Cancel status modal - Nút Hủy
    btnCancelStatus.addEventListener('click', function() {
        statusModal.classList.remove('show');
    });

    btnConfirmStatus.addEventListener('click', function() {
        const orderId = document.getElementById('statusOrderId').value;
        const action = document.getElementById('statusAction').value;
        const currentStatus = document.getElementById('statusCurrentStatus').value;

        let newStatus = '';

        if (action === 'cancel') {
            newStatus = 'Cancelled';
        } else if (action === 'progress') {
            if (currentStatus === 'Pending') {
                newStatus = 'Confirmed';
            } else if (currentStatus === 'Confirmed') {
                newStatus = 'Completed';
            }
        }

        if (newStatus) {
            statusModal.classList.remove('show');
            updateOrderStatus(orderId, newStatus);
        }
    });
});