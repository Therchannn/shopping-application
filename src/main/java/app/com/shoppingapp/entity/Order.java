package app.com.shoppingapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`Order`")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Order {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name= "total")
    private BigDecimal total;

    private String status;

    @Column(name= "shipping_fee")
    private BigDecimal shippingFee;

    public enum PaymentMethod {
        TIEN_MAT,
        CHUYEN_KHOAN
    }
    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_method",
            columnDefinition = "ENUM('TIEN_MAT', 'CHUYEN_KHOAN')"
    )

    private PaymentMethod paymentMethod;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;
}
