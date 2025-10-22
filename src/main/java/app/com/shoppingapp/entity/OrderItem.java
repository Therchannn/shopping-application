package app.com.shoppingapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @EmbeddedId
    private OrderItemId orderId;

    private int quantity;

    @ManyToOne
    @MapsId("idOrder")
    @JoinColumn(name = "id_order")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @MapsId("idProductVariant")
    @JoinColumn(name = "id_product_variant")
    @JsonManagedReference
    private ProductVariant productVariant;
}

