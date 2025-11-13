    package app.com.shoppingapp.entity;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.*;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "Cart")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Cart {

        @EmbeddedId
        private CartId id;

        private int quantity;
        private LocalDateTime createdAt;

        @ManyToOne
        @MapsId("userId")
        @JoinColumn(name = "id_user")
        @JsonBackReference
        private User user;

        @ManyToOne
        @MapsId("productVariantId")
        @JoinColumn(name = "id_product_variant")
        @JsonManagedReference
        private ProductVariant productVariant;
    }

