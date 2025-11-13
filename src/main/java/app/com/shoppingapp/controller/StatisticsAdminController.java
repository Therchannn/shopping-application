package app.com.shoppingapp.controller;

import app.com.shoppingapp.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/statistics")
public class StatisticsAdminController {
    private final StatisticsService statisticsService;

    @GetMapping("/quarterly-revenue")
    public ResponseEntity<Map<String, Object>> getQuarterlyRevenue() {
        Map<String, Object> result = statisticsService.getQuarterlyRevenue();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top-selling-products")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingProducts() {
        List<Map<String, Object>> products = statisticsService.getTopSellingProducts(10);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category-stats")
    public ResponseEntity<List<Map<String, Object>>> getCategoryStatistics() {
        List<Map<String, Object>> stats = statisticsService.getCategoryStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/product-orders")
    public ResponseEntity<List<Map<String, Object>>> getProductOrders(@RequestParam String productId) {
        List<Map<String, Object>> orders = statisticsService.getProductOrders(productId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/category-products")
    public ResponseEntity<List<Map<String, Object>>> getCategoryProducts(@RequestParam String category) {
        List<Map<String, Object>> products = statisticsService.getCategoryProducts(category);
        return ResponseEntity.ok(products);
    }
}

