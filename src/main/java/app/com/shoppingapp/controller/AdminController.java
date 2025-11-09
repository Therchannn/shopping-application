package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.service.ProductService;
import app.com.shoppingapp.service.OrderService;
import app.com.shoppingapp.service.UserService;
import app.com.shoppingapp.service.StatisticsService;
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
public class AdminController extends Admin {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final StatisticsService statisticsService;


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

        Map<String, Long> categoryCounts = productService.getCategoryCounts();

        int currentYear = java.time.Year.now().getValue();
        int currentMonth = java.time.LocalDate.now().getMonthValue();

        List<Double> monthlyRevenue = orderService.getMonthlyRevenue(currentYear);
        Double currentMonthRevenue = orderService.getCurrentMonthRevenue(currentMonth, currentYear);

        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("categoryLabels", categoryCounts.keySet());
        model.addAttribute("categoryData", categoryCounts.values());

        // Doanh thu tháng hiện tại
        model.addAttribute("revenue", currentMonthRevenue != null ? currentMonthRevenue : 0.0);

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

        model.addAttribute("revenueGrowth", statisticsService.getRevenueGrowthRate());
        model.addAttribute("returningCustomers", statisticsService.getReturningCustomersCount());
        model.addAttribute("completedOrderRate", statisticsService.getCompletedOrderRate());
        model.addAttribute("cancelledOrderRate", statisticsService.getCancelledOrderRate());

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

