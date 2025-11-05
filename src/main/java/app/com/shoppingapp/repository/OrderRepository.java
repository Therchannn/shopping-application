package app.com.shoppingapp.repository;

import app.com.shoppingapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository <Order, String> {
    List<Order> findOrdersByUser_Id(String id);
    Optional<Order> findOrderByIdAndUserId(String orderId, String userId);

}
