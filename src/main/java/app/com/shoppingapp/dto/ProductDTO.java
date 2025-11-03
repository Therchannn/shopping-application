package app.com.shoppingapp.dto;

import app.com.shoppingapp.entity.Product;
import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private Product.Status status;
    private List<ProductVariantDTO> variants;
    private Map<String, List<ProductVariantDTO>> groupedVariants;
}
