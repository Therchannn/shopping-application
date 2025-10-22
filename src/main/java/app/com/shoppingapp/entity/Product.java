package app.com.shoppingapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @Column(length = 50)
    private String id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 10)
    private String category;

    public enum Status {
        ACTIVE, INACTIVE
    }
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVE','INACTIVE')")
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> variants;
}
