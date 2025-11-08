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
    // Result: [Integer month, BigDecimal sum]
    @Query("SELECT MONTH(o.createdAt), COALESCE(SUM(o.total), 0) " +
           "FROM Order o WHERE o.status = 'Completed' AND YEAR(o.createdAt) = :year " +
           "GROUP BY MONTH(o.createdAt) ORDER BY MONTH(o.createdAt)")
    List<Object[]> getMonthlyRevenue(@Param("year") int year);

    // Tính doanh thu tháng hiện tại
    // JPA trả về BigDecimal cho SUM(), Service sẽ convert sang Double
    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o " +
           "WHERE o.status = 'Completed' AND MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    Object getRevenueByMonth(@Param("month") int month, @Param("year") int year);
}
