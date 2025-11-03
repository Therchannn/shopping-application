# Admin Layout System - Hướng dẫn sử dụng

## Tổng quan
Hệ thống layout admin đã được tách riêng để dễ dàng tái sử dụng cho các trang khác nhau. Menu sidebar sẽ tự động highlight mục đang active dựa trên URL hiện tại.

## Cấu trúc file

### Templates
- `admin/adminLayout.html` - Layout chính chứa sidebar và header
- `admin/dashboard.html` - Trang dashboard (ví dụ sử dụng layout)
- `admin/products.html` - Trang quản lý sản phẩm (ví dụ sử dụng layout)

### Controllers
- `AdminController.java` - Controller xử lý các trang admin
- `PageController.java` - Controller xử lý login/logout admin

### CSS
- `css/dashboard.css` - Style cho layout và dashboard
- `css/admin.css` - Style chung cho các component (buttons, tables, badges...)
- `css/base.css` - Style cơ bản

### JavaScript
- `js/admin-common.js` - Script xử lý active menu tự động
- `js/dashboard.js` - Script riêng cho dashboard
- `js/products.js` - Script riêng cho trang products

## Cách tạo trang admin mới

### 1. Tạo file HTML template

```html
<!DOCTYPE html>
<html lang="vi" xmlns:th="http://www.thymeleaf.org"
      th:replace="~{admin/adminLayout :: layout(~{::title}, ~{::content}, ~{::scripts})}">
<head>
  <title>Tên trang</title>
</head>
<body>

<!-- CONTENT -->
<div th:fragment="content">
  <h2>Tiêu đề trang</h2>
  
  <!-- Nội dung của bạn ở đây -->
  
</div>
<!-- END CONTENT -->

<!-- SCRIPTS (tùy chọn) -->
<th:block th:fragment="scripts">
  <script th:src="@{/js/your-script.js}"></script>
</th:block>
<!-- END SCRIPTS -->

</body>
</html>
```

### 2. Thêm route trong AdminController

```java
@GetMapping("/your-page")
public String yourPage(Model model, HttpSession session) {
    if (!isAuthenticated(session)) {
        return ADMIN_LOGIN_URL;
    }
    
    // Thêm dữ liệu vào model
    model.addAttribute("data", yourData);
    
    return "admin/your-page";
}
```

### 3. Thêm menu item vào adminLayout.html (nếu cần)

```html
<a th:href="@{/admin/your-page}" 
   class="menu-item"
   th:classappend="${#strings.contains(#httpServletRequest.requestURI, '/admin/your-page')} ? 'active' : ''">
  <i class="bi bi-icon-name"></i> Tên menu
</a>
```

## Tính năng chính

### 1. Auto Active Menu
Menu sidebar tự động highlight mục đang được chọn dựa trên URL hiện tại nhờ:
- Thymeleaf: `th:classappend` trong template
- JavaScript: `admin-common.js` xử lý bổ sung

### 2. Layout Fragments
Layout sử dụng Thymeleaf fragments với 3 tham số:
- `title` - Tiêu đề trang
- `content` - Nội dung chính
- `scripts` - JavaScript riêng cho trang

### 3. Authentication Check
Mọi trang admin đều kiểm tra authentication qua:
```java
if (!isAuthenticated(session)) {
    return ADMIN_LOGIN_URL;
}
```

## Component có sẵn

### Buttons
```html
<button class="btn btn-primary">
  <i class="bi bi-plus-circle"></i> Thêm mới
</button>

<button class="btn-icon btn-edit">
  <i class="bi bi-pencil"></i>
</button>

<button class="btn-icon btn-delete">
  <i class="bi bi-trash"></i>
</button>
```

### Search Box
```html
<div class="search-box">
  <i class="bi bi-search"></i>
  <input type="text" placeholder="Tìm kiếm...">
</div>
```

### Table
```html
<section class="table-section">
  <table class="data-table">
    <thead>
      <tr>
        <th>Cột 1</th>
        <th>Cột 2</th>
      </tr>
    </thead>
    <tbody>
      <tr th:each="item : ${items}">
        <td th:text="${item.field1}">Data</td>
        <td th:text="${item.field2}">Data</td>
      </tr>
    </tbody>
  </table>
</section>
```

### Badges
```html
<span class="badge badge-success">Thành công</span>
<span class="badge badge-danger">Lỗi</span>
<span class="badge badge-warning">Cảnh báo</span>
```

## Routes hiện có

- `/admin` → Redirect đến login
- `/admin/login` → Trang đăng nhập
- `/admin/dashboard` → Dashboard chính
- `/admin/products` → Quản lý sản phẩm
- `/admin/orders` → Quản lý đơn hàng (TODO)
- `/admin/customers` → Quản lý khách hàng (TODO)
- `/admin/statistics` → Thống kê (TODO)
- `/admin/settings` → Cài đặt (TODO)
- `/admin/logout` → Đăng xuất

## Bootstrap Icons

Sử dụng Bootstrap Icons cho các icon:
```html
<i class="bi bi-activity"></i>
<i class="bi bi-bag"></i>
<i class="bi bi-receipt"></i>
<i class="bi bi-people"></i>
<i class="bi bi-bar-chart"></i>
<i class="bi bi-gear"></i>
```

Xem thêm: https://icons.getbootstrap.com/

