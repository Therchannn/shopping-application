function viewOrderDetail(orderId) {
    showInfo('Chức năng xem chi tiết đơn hàng đang được phát triển...');
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
        console.error('Error:', error);
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

