package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.service.ProductService;
import app.com.shoppingapp.service.OrderService;
import app.com.shoppingapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String AUTH_SESSION_KEY = "isAdmin";
    private static final String ADMIN_LOGIN_URL = "redirect:/admin/login";

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute(AUTH_SESSION_KEY) != null;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }

        model.addAttribute("productCount", productService.count());
        model.addAttribute("orderCount", orderService.count());
        model.addAttribute("userCount", userService.count());


        model.addAttribute("currentPage", "dashboard");

        Map<String, Long> categoryCounts = new HashMap<>();
        List<Object[]> counts = productService.countByCategory();
        for (Object[] count : counts) {
            String category = (String) count[0];
            Long cnt = (Long) count[1];
            categoryCounts.put(category, cnt);
        }

        List<Integer> monthlyRevenue = List.of(500, 700, 800, 650, 900, 1200, 1500);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("categoryLabels", categoryCounts.keySet());
        model.addAttribute("categoryData", categoryCounts.values());


        Integer revenue = monthlyRevenue.isEmpty() ? 0 : monthlyRevenue.get(monthlyRevenue.size() - 1);
        model.addAttribute("revenue", revenue);

        return "admin/dashboard";
    }

    @GetMapping("/orders")
    public String orders(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }

        model.addAttribute("orders", List.of());
        model.addAttribute("currentPage", "orders");

        return "admin/orders";
    }

    @GetMapping("/customers")
    public String customers(Model model, HttpSession session,
                           @RequestParam(name = "q", required = false) String q) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }

        List<UserDTO> customers = (q != null && !q.isBlank())
                ? userService.search(q)
                : userService.get();

        model.addAttribute("customers", customers);
        model.addAttribute("q", q);
        model.addAttribute("currentPage", "customers");

        return "admin/customers";
    }

    @GetMapping("/statistics")
    public String statistics(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }

        model.addAttribute("currentPage", "statistics");
        return "admin/statistics";
    }

    @GetMapping("/settings")
    public String settings(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }

        model.addAttribute("currentPage", "settings");
        return "admin/settings";
    }
}

