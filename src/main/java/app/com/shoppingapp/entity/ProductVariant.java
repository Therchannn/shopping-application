package app.com.shoppingapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Product_variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @Column(name = "id_product_variant", length = 50, nullable = false)
    private String idProductVariant;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    @JsonBackReference
    private Product product;

    @Column(unique = true, nullable = false)
    private String codeProductVariant;

    @Column(nullable = false)
    private String color;

    @Column(length = 2, nullable = false)
    private String size;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Cart> carts;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<OrderItem> orderItems;
}
