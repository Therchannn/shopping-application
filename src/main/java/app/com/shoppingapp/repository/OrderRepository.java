package app.com.shoppingapp.repository;

import app.com.shoppingapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository <Order, String> {
    List<Order> findOrdersByUser_Id(String id);
    Optional<Order> findOrderByIdAndUserId(String orderId, String userId);

    @Query("SELECT o FROM Order o JOIN o.user u WHERE " +
           "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Order> searchOrders(@Param("keyword") String keyword);

    long countByStatus(String status);

    // Tính tổng doanh thu từ đơn hàng hoàn thành
    // JPA trả về BigDecimal cho SUM(), Service sẽ convert sang Double
    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.status = 'Completed'")
    Object getTotalRevenue();

    // Lấy doanh thu theo tháng (chỉ đơn Completed)
    @Query("SELECT MONTH(o.createdAt), COALESCE(SUM(o.total), 0) " +
           "FROM Order o WHERE o.status = 'Completed' AND YEAR(o.createdAt) = :year " +
           "GROUP BY MONTH(o.createdAt) ORDER BY MONTH(o.createdAt)")
    List<Object[]> getMonthlyRevenue(@Param("year") int year);

    // Tính doanh thu tháng hiện tại
    // JPA trả về BigDecimal cho SUM(), Service sẽ convert sang Double
    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o " +
           "WHERE o.status = 'Completed' AND MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    Object getRevenueByMonth(@Param("month") int month, @Param("year") int year);

    // Lấy doanh thu theo quý
    @Query("SELECT COALESCE(SUM(o.total), 0.0) FROM Order o " +
           "WHERE o.status = 'Completed' AND YEAR(o.createdAt) = :year " +
           "AND MONTH(o.createdAt) BETWEEN :startMonth AND :endMonth")
    Double findCompletedOrdersByYearAndQuarter(
        @Param("year") int year,
        @Param("startMonth") int startMonth,
        @Param("endMonth") int endMonth);

    // Top sản phẩm bán chạy nhất
    // JPQL không hỗ trợ LIMIT trong subquery và tính toán phức tạp nên phải dùng Native SQL
    // Phải backtick để tránh conflict với keyword ORDER trùng với ORDER BY
    @Query(value = "SELECT p.id, p.name, " +
           "(SELECT pv2.image_url FROM Product_variants pv2 " +
           " WHERE pv2.id_product = p.id AND pv2.size = 'S' LIMIT 1), " +
           "SUM(oi.quantity), " +
           "SUM(oi.quantity * pv.price) " +
           "FROM `Order` o " +
           "JOIN Order_items oi ON oi.id_order = o.id " +
           "JOIN Product_variants pv ON pv.id_product_variant = oi.id_product_variant " +
           "JOIN Product p ON p.id = pv.id_product " +
           "WHERE o.status = 'Completed' " +
           "GROUP BY p.id, p.name " +
           "ORDER BY SUM(oi.quantity) DESC " +
           "LIMIT ?1", nativeQuery = true)
    List<Object[]> findTopSellingProducts(int limit);

    // Lấy danh sách đơn hàng chứa sản phẩm cụ thể
    // Dùng Native SQL vì cần tính toán giá trị đơn hàng từ variant price
    @Query(value = "SELECT o.id, u.name, u.username, o.created_at, pv.size, pv.color, oi.quantity, " +
           "(oi.quantity * pv.price) as value " +
           "FROM `Order` o " +
           "JOIN User u ON u.id = o.id_user " +
           "JOIN Order_items oi ON oi.id_order = o.id " +
           "JOIN Product_variants pv ON pv.id_product_variant = oi.id_product_variant " +
           "JOIN Product p ON p.id = pv.id_product " +
           "WHERE p.id = ?1 AND o.status = 'Completed' " +
           "ORDER BY o.created_at DESC", nativeQuery = true)
    List<Object[]> findOrdersByProductId(String productId);

    // Đếm số khách hàng quay lại (mua từ 2 lần trở lên)
    @Query(value = "SELECT COUNT(*) FROM (" +
           "SELECT o.id_user FROM `Order` o " +
           "WHERE o.status IN ('Pending', 'Confirmed', 'Completed') " +
           "GROUP BY o.id_user " +
           "HAVING COUNT(o.id) >= 2" +
           ") as returning_customers", nativeQuery = true)
    long countReturningCustomers();
}
