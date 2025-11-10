function getBaseUrl() {
    const pathParts = window.location.pathname.split('/').filter(p => p);
    if (pathParts.length > 0 && pathParts[0] !== 'admin') {
        return `/${pathParts[0]}`;
    }
    return '';
}

function initRevenueChart() {
    const ctx = document.getElementById('revenueQuarterlyChart');
    if (!ctx) return;

    const baseUrl = getBaseUrl();

    fetch(`${baseUrl}/admin/api/statistics/quarterly-revenue`)
        .then(response => response.json())
        .then(data => {
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: [
                        'Quý 1\n(Tháng 1-3)',
                        'Quý 2\n(Tháng 4-6)',
                        'Quý 3\n(Tháng 7-9)',
                        'Quý 4\n(Tháng 10-12)'
                    ],
                    datasets: [{
                        label: 'Doanh thu (triệu đồng)',
                        data: data.revenues || [0, 0, 0, 0],
                        backgroundColor: ['#60a5fa', '#34d399', '#facc15', '#f59e0b'],
                        borderColor: ['#3b82f6', '#10b981', '#eab308', '#f97316'],
                        borderWidth: 2,
                        maxBarThickness: 80
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            ticks: {
                                font: {
                                    size: 11
                                }
                            }
                        },
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) {
                                    return value.toLocaleString('vi-VN') + ' tr';
                                }
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                            labels: {
                                font: {
                                    size: 11
                                },
                                padding: 12,
                                boxWidth: 40,
                                boxHeight: 12,
                                generateLabels: function() {
                                    return [
                                        {
                                            text: 'Quý 1',
                                            fillStyle: '#60a5fa',
                                            strokeStyle: '#3b82f6',
                                            lineWidth: 2,
                                            hidden: false,
                                            index: 0
                                        },
                                        {
                                            text: 'Quý 2',
                                            fillStyle: '#34d399',
                                            strokeStyle: '#10b981',
                                            lineWidth: 2,
                                            hidden: false,
                                            index: 1
                                        },
                                        {
                                            text: 'Quý 3',
                                            fillStyle: '#facc15',
                                            strokeStyle: '#eab308',
                                            lineWidth: 2,
                                            hidden: false,
                                            index: 2
                                        },
                                        {
                                            text: 'Quý 4',
                                            fillStyle: '#f59e0b',
                                            strokeStyle: '#f97316',
                                            lineWidth: 2,
                                            hidden: false,
                                            index: 3
                                        }
                                    ];
                                }
                            }
                        },
                        tooltip: {
                            callbacks: {
                                title: function(context) {
                                    const labels = [
                                        'Quý 1 (Tháng 1, 2, 3)',
                                        'Quý 2 (Tháng 4, 5, 6)',
                                        'Quý 3 (Tháng 7, 8, 9)',
                                        'Quý 4 (Tháng 10, 11, 12)'
                                    ];
                                    return labels[context[0].dataIndex];
                                },
                                label: function(context) {
                                    return 'Doanh thu: ' +
                                           context.parsed.y.toLocaleString('vi-VN') + ' triệu đồng';
                                }
                            }
                        }
                    }
                }
            });
        })
}

// ============================================
// TOP SELLING PRODUCTS
// ============================================
function loadTopSellingProducts() {
    const baseUrl = getBaseUrl();

    fetch(`${baseUrl}/admin/api/statistics/top-selling-products`)
        .then(response => response.json())
        .then(data => {
            // Render danh sách sản phẩm bằng cách clone template
            const tbody = document.querySelector('#topProductsTbody');
            const template = document.getElementById('product-row-template');

            if (!tbody || !template) return;

            // Xóa các rows cũ (trừ template)
            const oldRows = tbody.querySelectorAll('tr:not(#product-row-template)');
            oldRows.forEach(row => row.remove());

            if (data && data.length > 0) {
                data.forEach((item, index) => {
                    // Clone template
                    const row = template.cloneNode(true);
                    row.removeAttribute('id');
                    row.style.display = '';

                    // Fill dữ liệu
                    row.querySelector('.product-stt').textContent = index + 1;
                    row.querySelector('.product-name').textContent = item.productName || 'N/A';
                    row.querySelector('.product-quantity').textContent = item.quantitySold || 0;
                    row.querySelector('.product-revenue').textContent = formatCurrency(item.revenue);

                    // Xử lý image
                    const img = row.querySelector('.product-image');
                    const imgPath = item.imageUrl || '/images/default-product.png';
                    img.src = baseUrl ? baseUrl + imgPath : imgPath;
                    img.alt = item.productName || 'Product';
                    img.onerror = function() {
                        this.src = baseUrl + '/images/default-product.png';
                    };

                    // Xử lý nút chi tiết
                    const btnDetail = row.querySelector('.btn-view-detail');
                    btnDetail.onclick = () => viewProductDetail(item);

                    tbody.appendChild(row);
                });
            } else {
                // Hiển thị empty state
                const emptyRow = document.createElement('tr');
                emptyRow.innerHTML = '<td colspan="6" style="text-align: center; color: #9ca3af; padding: 30px;">Chưa có dữ liệu</td>';
                tbody.appendChild(emptyRow);
            }
        })
}

// ============================================
// CATEGORY STATISTICS
// ============================================
function loadCategoryStatistics() {
    const baseUrl = getBaseUrl();

    fetch(`${baseUrl}/admin/api/statistics/category-stats`)
        .then(response => response.json())
        .then(data => {
            // Render danh sách danh mục bằng cách clone template
            const tbody = document.querySelector('#categoryStatsTbody');
            const template = document.getElementById('category-row-template');

            if (!tbody || !template) return;

            // Xóa các rows cũ (trừ template)
            const oldRows = tbody.querySelectorAll('tr:not(#category-row-template)');
            oldRows.forEach(row => row.remove());

            // Lọc chỉ lấy Áo và Quần
            const filteredData = data.filter(item => {
                const catName = (item.categoryName || '').toLowerCase();
                return catName === 'áo' || catName === 'quần';
            });

            if (filteredData && filteredData.length > 0) {
                filteredData.forEach(item => {
                    // Clone template
                    const row = template.cloneNode(true);
                    row.removeAttribute('id');
                    row.style.display = '';

                    // Fill dữ liệu (không có icon nữa)
                    row.querySelector('.category-name').textContent = item.categoryName;
                    row.querySelector('.category-product-count').textContent = item.productCount || 0;
                    row.querySelector('.category-sold-count').textContent = item.soldCount || 0;
                    row.querySelector('.category-revenue').textContent = formatCurrency(item.revenue);
                    row.querySelector('.category-stock-count').textContent = item.stockCount || 0;

                    // Xử lý nút chi tiết
                    const btnDetail = row.querySelector('.btn-view-category-detail');
                    btnDetail.onclick = () => viewCategoryDetail(item);

                    tbody.appendChild(row);
                });
            } else {
                // Hiển thị empty state
                const emptyRow = document.createElement('tr');
                emptyRow.innerHTML = '<td colspan="6" style="text-align: center; color: #9ca3af; padding: 20px;">Chưa có dữ liệu</td>';
                tbody.appendChild(emptyRow);
            }
        })
}

// ============================================
// FORMAT FUNCTION
// ============================================
function formatCurrency(amount) {
    if (!amount && amount !== 0) return 'N/A';
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount).replace('₫', 'đ');
}

// Color mapping
function getColorDisplay(colorName, showText = true) {
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
}

// ============================================
// VIEW PRODUCT DETAIL - MODAL
// ============================================
function viewProductDetail(product) {
    const modal = document.getElementById('productDetailModal');
    const baseUrl = getBaseUrl();

    // Hiển thị modal
    modal.classList.add('show');

    // Fill thông tin sản phẩm
    document.getElementById('product-detail-name').textContent = product.productName || 'N/A';
    document.getElementById('product-detail-quantity').textContent = product.quantitySold || 0;
    document.getElementById('product-detail-revenue').textContent = formatCurrency(product.revenue);

    // Gọi API lấy danh sách đơn hàng chứa sản phẩm này (dùng productId)
    fetch(`${baseUrl}/admin/api/statistics/product-orders?productId=${encodeURIComponent(product.productId)}`)
        .then(response => response.json())
        .then(data => {
            renderProductOrders(data);
        })
}

function renderProductOrders(orders) {
    const tbody = document.getElementById('product-orders-tbody');
    const template = document.getElementById('product-order-row-template');

    // Xóa rows cũ (trừ template)
    const oldRows = tbody.querySelectorAll('tr:not(#product-order-row-template)');
    oldRows.forEach(row => row.remove());

    if (orders && orders.length > 0) {
        orders.forEach((order, index) => {
            const row = template.cloneNode(true);
            row.removeAttribute('id');
            row.style.display = '';

            // Fill data
            row.querySelector('.order-index').textContent = index + 1;
            row.querySelector('.order-customer-name').textContent = order.customerName || 'N/A';
            row.querySelector('.order-customer-username').textContent = '@' + (order.customerUsername || 'N/A');
            row.querySelector('.order-date').textContent = order.orderDate || 'N/A';

            // Size và Màu
            row.querySelector('.order-size').textContent = order.size || 'N/A';

            // Giá tiền
            row.querySelector('.order-price').textContent = formatCurrency(order.price);

            // Màu - dùng getColorDisplay như orders.js
            const colorCell = row.querySelector('.order-color');
            colorCell.innerHTML = getColorDisplay(order.color || 'N/A', false); // false = không hiện text

            row.querySelector('.order-quantity').textContent = order.quantity || 0;
            row.querySelector('.order-value').textContent = formatCurrency(order.value);

            // Mã đơn hàng (hiển thị full như Orders)
            const orderCodeCell = row.querySelector('.order-code');
            orderCodeCell.textContent = order.orderId || 'N/A';

            tbody.appendChild(row);
        });
    } else {
        const emptyRow = document.createElement('tr');
        emptyRow.innerHTML = '<td colspan="8" style="text-align: center; color: #9ca3af; padding: 20px;">Chưa có đơn hàng nào</td>';
        tbody.appendChild(emptyRow);
    }
}

function closeProductDetailModal() {
    const modal = document.getElementById('productDetailModal');
    modal.classList.remove('show');
}

// ============================================
// VIEW CATEGORY DETAIL - MODAL
// ============================================
function viewCategoryDetail(category) {
    const modal = document.getElementById('categoryDetailModal');
    const baseUrl = getBaseUrl();

    // Hiển thị modal
    modal.classList.add('show');

    // Fill thông tin danh mục
    document.getElementById('category-detail-name').textContent = category.categoryName || 'N/A';
    document.getElementById('category-detail-product-count').textContent = category.productCount || 0;
    document.getElementById('category-detail-sold-count').textContent = category.soldCount || 0;
    document.getElementById('category-detail-stock-count').textContent = category.stockCount || 0;
    document.getElementById('category-detail-revenue').textContent = formatCurrency(category.revenue);

    // Gọi API lấy danh sách sản phẩm trong danh mục
    fetch(`${baseUrl}/admin/api/statistics/category-products?category=${encodeURIComponent(category.categoryName)}`)
        .then(response => response.json())
        .then(data => {
            renderCategoryProducts(data);
        })
}

function renderCategoryProducts(products) {
    const tbody = document.getElementById('category-products-tbody');
    const template = document.getElementById('category-product-row-template');
    const baseUrl = getBaseUrl();

    // Xóa rows cũ (trừ template)
    const oldRows = tbody.querySelectorAll('tr:not(#category-product-row-template)');
    oldRows.forEach(row => row.remove());

    if (products && products.length > 0) {
        products.forEach((product, index) => {
            const row = template.cloneNode(true);
            row.removeAttribute('id');
            row.style.display = '';

            // Fill data
            row.querySelector('.cat-product-index').textContent = index + 1;
            row.querySelector('.cat-product-name').textContent = product.name || 'N/A';
            row.querySelector('.cat-product-sold').textContent = product.soldCount || 0;
            row.querySelector('.cat-product-stock').textContent = product.stockCount || 0;

            // Image
            const img = row.querySelector('.cat-product-image');
            const imgPath = product.imageUrl || '/images/default-product.png';
            img.src = baseUrl ? baseUrl + imgPath : imgPath;
            img.alt = product.name || 'Product';
            img.onerror = function() {
                this.src = baseUrl + '/images/default-product.png';
            };

            // Status badge
            const statusCell = row.querySelector('.cat-product-status');
            const statusBadge = product.status === 'ACTIVE'
                ? '<span style="background: #10b981; color: white; padding: 4px 12px; border-radius: 12px; font-size: 0.875rem;">Hoạt động</span>'
                : '<span style="background: #ef4444; color: white; padding: 4px 12px; border-radius: 12px; font-size: 0.875rem;">Ngừng bán</span>';
            statusCell.innerHTML = statusBadge;

            tbody.appendChild(row);
        });
    } else {
        const emptyRow = document.createElement('tr');
        emptyRow.innerHTML = '<td colspan="6" style="text-align: center; color: #9ca3af; padding: 20px;">Chưa có sản phẩm nào</td>';
        tbody.appendChild(emptyRow);
    }
}

function closeCategoryDetailModal() {
    const modal = document.getElementById('categoryDetailModal');
    modal.classList.remove('show');
}

// ============================================
// HELPER: GET STATUS BADGE
// ============================================
function getStatusBadge(status) {
    const badges = {
        'Cancelled': '<span style="background: #ef4444; color: white; padding: 4px 12px; border-radius: 12px; font-size: 0.875rem;">Đã hủy</span>',
        'Pending': '<span style="background: #f59e0b; color: white; padding: 4px 12px; border-radius: 12px; font-size: 0.875rem;">Chờ xác nhận</span>',
        'Confirmed': '<span style="background: #3b82f6; color: white; padding: 4px 12px; border-radius: 12px; font-size: 0.875rem;">Đã xác nhận</span>',
        'Completed': '<span style="background: #10b981; color: white; padding: 4px 12px; border-radius: 12px; font-size: 0.875rem;">Hoàn thành</span>'
    };
    return badges[status] || '<span style="background: #6b7280; color: white; padding: 4px 12px; border-radius: 12px; font-size: 0.875rem;">N/A</span>';
}

// ============================================
// INITIALIZE ON PAGE LOAD
// ============================================
document.addEventListener('DOMContentLoaded', function() {

    initRevenueChart();
    loadTopSellingProducts();
    loadCategoryStatistics();
});

