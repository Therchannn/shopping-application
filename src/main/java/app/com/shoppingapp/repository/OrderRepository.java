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
}
