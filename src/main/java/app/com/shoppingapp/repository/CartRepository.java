package app.com.shoppingapp.repository;

import app.com.shoppingapp.dto.CartDTO;
import app.com.shoppingapp.dto.CartToGet;
import app.com.shoppingapp.entity.Cart;
import app.com.shoppingapp.entity.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {
    @Query(
            value = """
                    SELECT 
                        v.id_product_variant AS id,
                        p.name, 
                        p.description, 
                        p.category, 
                        v.code_product_variant AS code, 
                        v.size, 
                        v.color, 
                        v.image_url, 
                        v.price, 
                        c.quantity,
                        v.quantity as amount
                    FROM cart c
                    JOIN product_variants v ON c.id_product_variant = v.id_product_variant
                    JOIN product p ON p.id = v.id_product
                    WHERE c.id_user = :userId
            """,
            nativeQuery = true
    )
    List<CartToGet> getAllCart(@Param("userId") String userId);
    Optional<Cart> findByIdProductVariantIdAndIdUserId(String productVariantId, String userId);
}
