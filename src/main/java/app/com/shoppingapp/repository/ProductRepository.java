package app.com.shoppingapp.repository;

import app.com.shoppingapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, String>{
    List<Product> findByNameContainingIgnoreCase(String name);
}