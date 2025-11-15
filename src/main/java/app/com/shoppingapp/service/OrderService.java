package app.com.shoppingapp.service;

import app.com.shoppingapp.dto.OrderToChange;
import app.com.shoppingapp.entity.Order;
import app.com.shoppingapp.entity.OrderItem;
import app.com.shoppingapp.entity.Product;
import app.com.shoppingapp.entity.ProductVariant;
import app.com.shoppingapp.mapper.OrderMapper;
import app.com.shoppingapp.repository.OrderRepository;
import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.repository.ProductVariantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductVariantsRepository productVariantsRepository;

    public List<OrderDTO> get(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getById(String id){
        List<Order> orders = orderRepository.findOrdersByUser_IdOrderByCreatedAtDesc(id);
        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void update(String orderId, String status, String payment){
        Order order = orderRepository.findOrderById(orderId);
        order.setStatus(status);
        order.setPaymentMethod(Order.PaymentMethod.valueOf(payment));
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    public long count(){
        return orderRepository.count();
    }

    public List<OrderDTO> search(String keyword){
        return orderRepository.searchOrders(keyword).stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public long countByStatus(String status){
        return orderRepository.countByStatus(status);
    }

    public String updateStatus(String orderId, String newStatus) {
        try {
            // Validate input
            if (newStatus == null || newStatus.isBlank()) {
                return "Trạng thái không được để trống";
            }

            // Validate status value
            if (!List.of("Pending", "Confirmed", "Completed", "Cancelled").contains(newStatus)) {
                return "Trạng thái không hợp lệ";
            }

            Optional<Order> orderOpt = orderRepository.findById(orderId);

            if (orderOpt.isEmpty()) {
                return "Không tìm thấy đơn hàng";
            }

            Order order = orderOpt.get();
            String oldStatus = order.getStatus();

            // Nếu trạng thái giống nhau thì không cần update
            if (oldStatus.equals(newStatus)) {
                return "Trạng thái đã được cập nhật trước đó";
            }

            // Xử lý logic inventory khi chuyển sang Pending (đã ghi nhận)
            if (newStatus.equals("Confirmed") && oldStatus.equals("Pending")) {
                List<OrderItem> items = order.getItems();
                for (OrderItem item : items) {
                    ProductVariant variant = item.getProductVariant();
                    variant.setQuantity(variant.getQuantity() - item.getQuantity());
                    productVariantsRepository.save(variant);
                }
            }

            // Xử lý logic inventory khi hủy đơn
            if (newStatus.equals("Cancelled") && oldStatus.equals("Confirmed")) {
                List<OrderItem> items = order.getItems();
                for (OrderItem item : items) {
                    ProductVariant variant = item.getProductVariant();
                    variant.setQuantity(variant.getQuantity() + item.getQuantity());
                    productVariantsRepository.save(variant);
                }
            }

            // Cập nhật trạng thái
            order.setStatus(newStatus);
            orderRepository.save(order);

            return "Cập nhật trạng thái thành công";
        } catch (Exception e) {
            return "Có lỗi xảy ra: " + e.getMessage();
        }
    }

    public OrderDTO getOrderDetailById(String orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isEmpty()) {
            return null;
        }

        return OrderMapper.toDTO(orderOpt.get());
    }

    // Lấy tổng doanh thu
    public Double getTotalRevenue() {
        Object result = orderRepository.getTotalRevenue();

        if (result == null) {
            return 0.0;
        }

        // JPA trả về BigDecimal cho SUM(), cần convert sang Double
        if (result instanceof java.math.BigDecimal) {
            return ((java.math.BigDecimal) result).doubleValue();
        } else if (result instanceof Double) {
            return (Double) result;
        }

        return 0.0;
    }

    // Lấy doanh thu theo tháng cho năm hiện tại
    public List<Double> getMonthlyRevenue(int year) {
        List<Object[]> results = orderRepository.getMonthlyRevenue(year);

        // Khởi tạo mảng 12 tháng với giá trị 0
        Double[] monthlyData = new Double[12];
        for (int i = 0; i < 12; i++) {
            monthlyData[i] = 0.0;
        }

        // Fill dữ liệu thực tế
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            // JPA trả về BigDecimal cho SUM(), cần convert sang Double
            Object revenueObj = result[1];
            Double revenue = 0.0;

            if (revenueObj instanceof java.math.BigDecimal) {
                revenue = ((java.math.BigDecimal) revenueObj).doubleValue();
            } else if (revenueObj instanceof Double) {
                revenue = (Double) revenueObj;
            }

            if (month != null && month >= 1 && month <= 12) {
                monthlyData[month - 1] = revenue;
            }
        }

        return java.util.Arrays.asList(monthlyData);
    }

    // Lấy doanh thu tháng hiện tại
    public Double getCurrentMonthRevenue(int month, int year) {
        Object result = orderRepository.getRevenueByMonth(month, year);

        if (result == null) {
            return 0.0;
        }

        // JPA trả về BigDecimal cho SUM(), cần convert sang Double
        if (result instanceof java.math.BigDecimal) {
            return ((java.math.BigDecimal) result).doubleValue();
        } else if (result instanceof Double) {
            return (Double) result;
        }

        return 0.0;
    }
}
