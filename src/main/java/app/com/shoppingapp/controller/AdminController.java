package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.OrderDTO;
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
public class AdminController extends BaseAdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;


    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }
        addUsernameToModel(model, session);

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
    public String orders(Model model, HttpSession session,
                        @RequestParam(name = "q", required = false) String q) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }
        addUsernameToModel(model, session);

        List<OrderDTO> orders = (q != null && !q.isBlank())
                ? orderService.search(q)
                : orderService.get();

        // Count orders by status
        long pendingCount = orderService.countByStatus("Pending");
        long confirmedCount = orderService.countByStatus("Confirmed");
        long completedCount = orderService.countByStatus("Completed");
        long cancelledCount = orderService.countByStatus("Cancelled");

        model.addAttribute("orders", orders);
        model.addAttribute("q", q);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("confirmedCount", confirmedCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("cancelledCount", cancelledCount);
        model.addAttribute("currentPage", "orders");

        return "admin/orders";
    }


    @GetMapping("/customers")
    public String customers(Model model, HttpSession session,
                           @RequestParam(name = "q", required = false) String q) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }
        addUsernameToModel(model, session);

        List<UserDTO> customers = (q != null && !q.isBlank())
                ? userService.search(q)
                : userService.get();

        long activeCount = userService.countByStatus(true);
        long bannedCount = userService.countByStatus(false);
        long userCount = userService.countByRole(false);
        long adminCount = userService.countByRole(true);

        model.addAttribute("customers", customers);
        model.addAttribute("q", q);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("bannedCount", bannedCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("adminCount", adminCount);
        model.addAttribute("currentPage", "customers");

        return "admin/customers";
    }

    @GetMapping("/statistics")
    public String statistics(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }
        addUsernameToModel(model, session);

        model.addAttribute("currentPage", "statistics");
        return "admin/statistics";
    }

    @GetMapping("/settings")
    public String settings(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return ADMIN_LOGIN_URL;
        }
        addUsernameToModel(model, session);

        model.addAttribute("currentPage", "settings");
        return "admin/settings";
    }
}

