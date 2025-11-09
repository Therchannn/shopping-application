package app.com.shoppingapp.repository;

import app.com.shoppingapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, String>{
    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p.category, COUNT(p) FROM Product p GROUP BY p.category")
    List<Object[]> countByCategory();

    // Thống kê theo danh mục: category, productCount, soldCount (All except Cancelled), revenue (Completed only), stockCount
    // JPQL không hỗ trợ subquery phức tạp trong SELECT nên phải dùng Native SQL
    // Phải backtick để tránh conflict với keyword ORDER trùng với ORDER BY
    @Query(value = "SELECT p.category, " +
           "COUNT(DISTINCT p.id), " +
           // Đã vào đơn = TỔNG QUANTITY trong tất cả đơn (trừ Cancelled)
           "(SELECT COALESCE(SUM(oi2.quantity), 0) " +
           " FROM Order_items oi2 " +
           " JOIN Product_variants pv2 ON pv2.id_product_variant = oi2.id_product_variant " +
           " JOIN `Order` o2 ON o2.id = oi2.id_order " +
           " WHERE pv2.id_product IN (SELECT p2.id FROM Product p2 WHERE p2.category = p.category) " +
           " AND o2.status NOT IN ('Cancelled')), " +
           // Doanh thu (Completed only) - lấy giá từ ProductVariant
           "(SELECT COALESCE(SUM(oi3.quantity * pv3.price), 0) " +
           " FROM Order_items oi3 " +
           " JOIN Product_variants pv3 ON pv3.id_product_variant = oi3.id_product_variant " +
           " JOIN `Order` o3 ON o3.id = oi3.id_order " +
           " WHERE pv3.id_product IN (SELECT p3.id FROM Product p3 WHERE p3.category = p.category) " +
           " AND o3.status = 'Completed'), " +
           // Tồn kho
           "(SELECT COALESCE(SUM(pv4.quantity), 0) " +
           " FROM Product_variants pv4 " +
           " WHERE pv4.id_product IN (SELECT p4.id FROM Product p4 WHERE p4.category = p.category)) " +
           "FROM Product p " +
           "GROUP BY p.category " +
           "ORDER BY COUNT(DISTINCT p.id) DESC", nativeQuery = true)
    List<Object[]> findCategoryStatistics();

    // Lấy danh sách sản phẩm theo danh mục
    // JPQL không hỗ trợ LIMIT trong subquery và tính toán phức tạp nên phải dùng Native SQL
    // Phải backtick để tránh conflict với keyword ORDER trùng với ORDER BY
    @Query(value = "SELECT p.id, p.name, " +
           // Ảnh từ variant size S
           "(SELECT pv2.image_url FROM Product_variants pv2 " +
           " WHERE pv2.id_product = p.id AND pv2.size = 'S' LIMIT 1), " +
           // Số lượng đã bán (TẤT CẢ ĐƠN: Pending + Confirmed + Completed)
           "(SELECT COALESCE(SUM(oi2.quantity), 0) " +
           " FROM Order_items oi2 " +
           " JOIN Product_variants pv2 ON pv2.id_product_variant = oi2.id_product_variant " +
           " JOIN `Order` o2 ON o2.id = oi2.id_order " +
           " WHERE pv2.id_product = p.id " +
           " AND o2.status NOT IN ('Cancelled')) as sold_count, " +
           // Tồn kho khả dụng = Tổng - (Pending + Confirmed + Completed)
           "((SELECT COALESCE(SUM(pv3.quantity), 0) " +
           "  FROM Product_variants pv3 WHERE pv3.id_product = p.id) - " +
           " (SELECT COALESCE(SUM(oi3.quantity), 0) " +
           "  FROM Order_items oi3 " +
           "  JOIN Product_variants pv4 ON pv4.id_product_variant = oi3.id_product_variant " +
           "  JOIN `Order` o3 ON o3.id = oi3.id_order " +
           "  WHERE pv4.id_product = p.id " +
           "  AND o3.status NOT IN ('Cancelled'))) as stock_count, " +
           "p.status " +
           "FROM Product p " +
           "WHERE p.category = ?1 " +
           "ORDER BY sold_count DESC", nativeQuery = true)
    List<Object[]> findProductsByCategory(String category);
}