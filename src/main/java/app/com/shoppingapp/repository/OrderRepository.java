package app.com.shoppingapp.repository;

import app.com.shoppingapp.dto.OrderToGet;
import app.com.shoppingapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findOrdersByUser_Id(String id);

    Optional<Order> findOrderByIdAndUserId(String orderId, String userId);

    List<Order> findOrdersByStatusAndUserId(String type, String userId);

    Order findOrderById(String orderId);

    @Query(
            value = """
            SELECT
                o.id AS id,
                o.total AS total,
                o.status AS status,
                o.shipping_fee AS shippingFee,
                o.payment_method AS methodPayment,
                p.name AS nameProduct,
                v.id_product_variant AS idVariant,
                v.color AS color,
                t.quantity AS quantity,
                v.size AS `size`,
                v.price AS price,
                v.image_url AS image,
                o.created_at AS createdAt
            FROM `order` o
            JOIN order_items t ON t.id_order = o.id
            JOIN product_variants v ON v.id_product_variant = t.id_product_variant
            JOIN product p ON p.id = v.id_product
            WHERE o.id_user = :userId
        """,
            nativeQuery = true
    )
    List<OrderToGet> getOrder(@Param("userId") String userId);
}
