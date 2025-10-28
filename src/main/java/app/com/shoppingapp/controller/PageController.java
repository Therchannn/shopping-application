package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.dto.UserToSignIn;
import app.com.shoppingapp.service.ProductService;
import app.com.shoppingapp.service.AuthService;
import app.com.shoppingapp.service.OrderService;
import app.com.shoppingapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class PageController {

    private static final String AUTH_SESSION_KEY = "isAdmin";
    private static final String ADMIN_LOGIN_URL = "redirect:/admin/login";
    private static final String ADMIN_DASHBOARD_URL = "redirect:/admin/dashboard";

    private final ProductService productService;
    private final AuthService authService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/home")
    public String homePage(Model model){
         List<ProductDTO> products = productService.get();

         model.addAttribute("products", products);

        return "home";
    }

    @GetMapping("/layout")
    public String layoutPage(){
        return "layout";
    }

    @GetMapping("/layoutProduct")
    public String layoutProductPage(Model model){

        List<ProductDTO> products = productService.get();

        model.addAttribute("products", products);

        return "layoutProduct";
    }

    @GetMapping("/admin")
    public String adminRoot() {
        return ADMIN_LOGIN_URL;
    }

    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String adminLoginForm(@RequestParam String username,
                                 @RequestParam String password,
                                 HttpSession session,
                                 Model model) {
        UserToSignIn req = new UserToSignIn();
        req.setUsername(username);
        req.setPassword(password);

        if (authService.authenticateAdmin(req)) {
            session.setAttribute(AUTH_SESSION_KEY, true);
            return ADMIN_DASHBOARD_URL;
        } else {
            session.removeAttribute(AUTH_SESSION_KEY);
            model.addAttribute("loginError", "Wrong username or password");
            return "admin/login";
        }
    }

    @GetMapping("/admin/dashboard")
    public String dashBoard(Model model, HttpSession session,
                            @RequestParam(name = "q", required = false) String q) {
        if(session.getAttribute(AUTH_SESSION_KEY) == null){
            return ADMIN_LOGIN_URL;
        }

        List<ProductDTO> products = (q != null && !q.isBlank())
                ? productService.search(q)
                : productService.get();

        model.addAttribute("products", products);
        model.addAttribute("q", q);

        // dashboard metrics
        model.addAttribute("productCount", productService.count());
        model.addAttribute("orderCount", orderService.count());
        model.addAttribute("userCount", userService.count());

        Map<String, Long> categoryCounts = new HashMap<>();
        List<Object[]> counts = productService.countByCategory();
        for (Object[] count : counts) {
            String category = (String) count[0];
            Long cnt = (Long) count[1];
            categoryCounts.put(category, cnt);
        }

        List<Integer> monthlyRevenue = List.of(500,700,800,650,900,1200,1500);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("categoryLabels", categoryCounts.keySet());
        model.addAttribute("categoryData", categoryCounts.values());

        // show the latest month revenue as KPI
        Integer revenue = monthlyRevenue.isEmpty() ? 0 : monthlyRevenue.get(monthlyRevenue.size() - 1);
        model.addAttribute("revenue", revenue);

        return "admin/dashboard";
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return ADMIN_LOGIN_URL;
    }
}
