package app.com.shoppingapp.service;

import app.com.shoppingapp.repository.OrderRepository;
import app.com.shoppingapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    //Lấy doanh thu theo quý
    public Map<String, Object> getQuarterlyRevenue() {
        Map<String, Object> result = new HashMap<>();
        List<Double> revenues = new ArrayList<>();

        int currentYear = LocalDateTime.now().getYear();

        // Tính doanh thu cho từng quý
        for (int quarter = 1; quarter <= 4; quarter++) {
            int startMonth = (quarter - 1) * 3 + 1;
            int endMonth = quarter * 3;

            Double revenue = orderRepository.findCompletedOrdersByYearAndQuarter(
                currentYear, startMonth, endMonth);

            double revenueInMillions = (revenue != null ? revenue : 0.0) / 1_000_000.0;
            revenues.add(Math.round(revenueInMillions * 100.0) / 100.0);
        }

        result.put("revenues", revenues);
        result.put("year", currentYear);
        return result;
    }

    // Lấy danh sách sản phẩm bán chạy nhất
    public List<Map<String, Object>> getTopSellingProducts(int limit) {
        List<Object[]> topProducts = orderRepository.findTopSellingProducts(limit);

        return topProducts.stream().map(row -> {
            Map<String, Object> product = new HashMap<>();
            product.put("productId", row[0]);        // ID sản phẩm
            product.put("productName", row[1]);      // Tên sản phẩm

            // Image URL
            String imageUrl = (String) row[2];
            product.put("imageUrl", imageUrl != null ? imageUrl : "/images/default-product.png");

            product.put("quantitySold", row[3]);

            Object revenueObj = row[4];
            double revenue = 0.0;
            if (revenueObj instanceof BigDecimal bigDecimal) {
                revenue = bigDecimal.doubleValue();
            } else if (revenueObj instanceof Double doubleValue) {
                revenue = doubleValue;
            }
            product.put("revenue", revenue);

            return product;
        }).toList();
    }

    // Lấy thống kê theo danh mục sản phẩm
    public List<Map<String, Object>> getCategoryStatistics() {
        List<Object[]> categoryData = productRepository.findCategoryStatistics();

        return categoryData.stream().map(row -> {
            Map<String, Object> stat = new HashMap<>();

            String categoryCode = (String) row[0];
            stat.put("categoryName", getCategoryName(categoryCode));
            stat.put("productCount", row[1] != null ? ((Number) row[1]).longValue() : 0L);
            stat.put("soldCount", row[2] != null ? ((Number) row[2]).longValue() : 0L);

            Object revenueObj = row[3];
            Double revenue = 0.0;
            if (revenueObj instanceof BigDecimal) {
                revenue = ((BigDecimal) revenueObj).doubleValue();
            } else if (revenueObj instanceof Double) {
                revenue = (Double) revenueObj;
            }
            stat.put("revenue", revenue);

            stat.put("stockCount", row[4] != null ? ((Number) row[4]).longValue() : 0L);

            return stat;
        }).collect(Collectors.toList());
    }

    // Lấy danh sách đơn hàng theo sản phẩm
    public List<Map<String, Object>> getProductOrders(String productId) {
        List<Object[]> orderData = orderRepository.findOrdersByProductId(productId);

        return orderData.stream().map(row -> {
            Map<String, Object> order = new HashMap<>();
            order.put("orderId", row[0]);
            order.put("customerName", row[1]);
            order.put("customerUsername", row[2]);
            order.put("orderDate", row[3] != null ? row[3].toString() : "");
            order.put("size", row[4]);
            order.put("color", row[5]);
            order.put("quantity", row[6]);

            Object valueObj = row[7];
            double value = 0.0;
            if (valueObj instanceof BigDecimal bigDecimal) {
                value = bigDecimal.doubleValue();
            } else if (valueObj instanceof Double doubleValue) {
                value = doubleValue;
            }
            order.put("value", value);

            return order;
        }).toList();
    }

   // Lấy danh sách sản phẩm theo danh mục
    public List<Map<String, Object>> getCategoryProducts(String category) {
        String categoryCode = switch (category.toLowerCase()) {
            case "áo" -> "AO";
            case "quần" -> "QUAN";
            default -> category.toUpperCase();
        };

        List<Object[]> productData = productRepository.findProductsByCategory(categoryCode);

        return productData.stream().map(row -> {
            Map<String, Object> product = new HashMap<>();
            product.put("productId", row[0]);
            product.put("name", row[1]);
            product.put("imageUrl", row[2] != null ? row[2] : "/images/default-product.png");
            product.put("soldCount", row[3] != null ? ((Number) row[3]).longValue() : 0L);
            product.put("stockCount", row[4] != null ? ((Number) row[4]).longValue() : 0L);
            product.put("status", row[5]);

            return product;
        }).toList();
    }

    private String getCategoryName(String code) {
        if (code == null) return "Khác";

        return switch (code.toUpperCase()) {
            case "AO" -> "Áo";
            case "QUAN" -> "Quần";
            default -> code;
        };
    }

    //Tính tỷ lệ tăng trưởng doanh thu theo quý (%)
    public double getRevenueGrowthRate() {
        int currentYear = LocalDateTime.now().getYear();
        int currentMonth = LocalDateTime.now().getMonthValue();

        // Xác định quý hiện tại và quý trước
        int currentQuarter = (currentMonth - 1) / 3 + 1;
        int previousQuarter = currentQuarter - 1;
        int previousYear = currentYear;

        if (previousQuarter == 0) {
            previousQuarter = 4;
            previousYear = currentYear - 1;
        }

        // Doanh thu quý hiện tại
        int currentStartMonth = (currentQuarter - 1) * 3 + 1;
        int currentEndMonth = currentQuarter * 3;
        Double currentRevenue = orderRepository.findCompletedOrdersByYearAndQuarter(
            currentYear, currentStartMonth, currentEndMonth);

        // Doanh thu quý trước
        int previousStartMonth = (previousQuarter - 1) * 3 + 1;
        int previousEndMonth = previousQuarter * 3;
        Double previousRevenue = orderRepository.findCompletedOrdersByYearAndQuarter(
            previousYear, previousStartMonth, previousEndMonth);

        if (previousRevenue == null || previousRevenue == 0) {
            return currentRevenue != null && currentRevenue > 0 ? 100.0 : 0.0;
        }

        double growth = ((currentRevenue != null ? currentRevenue : 0.0) - previousRevenue) / previousRevenue * 100;
        return Math.round(growth * 10.0) / 10.0; // Làm tròn 1 chữ số thập phân
    }

    // Đếm số khách hàng quay lại mua hàng
    public long getReturningCustomersCount() {
        return orderRepository.countReturningCustomers();
    }

   // Tính tỷ lệ đơn hàng hoàn thành (%)
    public double getCompletedOrderRate() {
        long totalOrders = orderRepository.count();
        long completedOrders = orderRepository.countByStatus("Completed");

        if (totalOrders == 0) return 0.0;

        double rate = (double) completedOrders / totalOrders * 100;
        return Math.round(rate * 10.0) / 10.0; // Làm tròn 1 chữ số thập phân
    }

    // Tính tỷ lệ đơn hàng bị hủy (%)
    public double getCancelledOrderRate() {
        long totalOrders = orderRepository.count();
        long cancelledOrders = orderRepository.countByStatus("Cancelled");

        if (totalOrders == 0) return 0.0;

        double rate = (double) cancelledOrders / totalOrders * 100;
        return Math.round(rate * 10.0) / 10.0; // Làm tròn 1 chữ số thập phân
    }
}
