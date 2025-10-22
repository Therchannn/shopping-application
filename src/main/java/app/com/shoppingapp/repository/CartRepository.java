package app.com.shoppingapp.repository;

import app.com.shoppingapp.entity.Cart;
import app.com.shoppingapp.entity.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {
    List<Cart> findCartsByIdUserId(String id);
    Optional<Cart> findCartByIdProductVariantId(String id);
}
