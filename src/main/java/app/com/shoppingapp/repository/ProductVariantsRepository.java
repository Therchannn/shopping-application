package app.com.shoppingapp.repository;

import app.com.shoppingapp.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantsRepository extends JpaRepository<ProductVariant, String>{
    ProductVariant findByIdProductVariant(String id);
}
