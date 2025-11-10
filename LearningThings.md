# So sánh AJAX vs Thymeleaf Rendering

## Tổng quan

Trong dự án có **2 cách tiếp cận** để hiển thị dữ liệu:

### 1️ **Thymeleaf Server-Side Rendering** (SSR)
- Dữ liệu được xử lý và render **HOÀN TOÀN ở server**
- HTML đã có sẵn dữ liệu khi gửi về browser
- Ví dụ: Trang dashboard, customers (lần đầu load)

### 2️ **AJAX Client-Side Rendering** (CSR)
- HTML ban đầu chỉ là **template rỗng**
- JavaScript gọi API để lấy dữ liệu
- Browser render dữ liệu vào template
- Ví dụ: Modals chi tiết đơn hàng, sản phẩm bán chạy

---

##  Tại sao dùng AJAX cho Modals?

###  **Lý do kỹ thuật:**

#### 1. **Performance - Hiệu suất tốt hơn**
```
 Thymeleaf SSR cho Modal:
- Load trang → Server render TẤT CẢ dữ liệu (có thể 1000+ đơn hàng)
- HTML file rất lớn (5MB+)
- Người dùng chỉ xem 1-2 modal → Lãng phí tài nguyên

 AJAX CSR:
- Load trang → Chỉ render template rỗng (50KB)
- Click xem chi tiết → Gọi API lấy đúng 1 đơn hàng
- Nhanh, nhẹ, tiết kiệm bandwidth
```

#### 2. **User Experience - Trải nghiệm người dùng**
```
Thymeleaf:
- Click → Reload toàn bộ trang → Mất thời gian
- Scroll position bị mất
- Form input bị clear

AJAX:
- Click → Chỉ load phần modal (0.5s)
- Không reload trang
- Smooth, mượt mà
```

#### 3. **Scalability - Khả năng mở rộng**
```
AJAX:
- Có thể tái sử dụng API cho Mobile App
- Có thể cache data ở client
- Dễ thêm tính năng real-time (WebSocket)

Thymeleaf:
- Chỉ dùng cho web
- Không cache được
```
## So sánh 2 cách tiếp cận

### **Khi nào dùng Thymeleaf SSR?**
 Trang chính (Dashboard, Product List)
 Dữ liệu ít thay đổi  
 Load lần đầu

### **Khi nào dùng AJAX CSR?**
 Modals, Popups  
 Dữ liệu thay đổi thường xuyên  
 Tương tác nhiều (filter, search, pagination)  
 Cần real-time updates

---
## Kết luận

**Tại sao Modal dùng AJAX thay vì Thymeleaf?**

1. **Performance**  - Chỉ load khi cần
2. **UX**  - Mượt mà, không reload trang
3. **Maintainability**  - Code rõ ràng, dễ debug


---
