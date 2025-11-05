package app.com.shoppingapp.controller;

import java.util.Arrays;
import java.util.List;

import app.com.shoppingapp.dto.ProductVariantDTO;
import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.service.UserService;
import app.com.shoppingapp.service.OrderService;
import app.com.shoppingapp.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.dto.UserToSignIn;
import app.com.shoppingapp.dto.UserToSignUp;
import app.com.shoppingapp.dto.CartToGet;
import app.com.shoppingapp.dto.CartDTO;
import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.service.AuthService;
import app.com.shoppingapp.service.ProductService;
import jakarta.servlet.http.HttpSession;    
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PageController {

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final AuthService authService;
    private final OrderService orderService;

    @GetMapping("/home")
    public String homePage(Model model){
        List<ProductDTO> allProducts = productService.get();
        List<ProductDTO> products = allProducts.subList(0, Math.min(8, allProducts.size()));
        List<ProductDTO> newProducts = allProducts.subList(Math.min(allProducts.size(), allProducts.size() - 8),  allProducts.size());

        model.addAttribute("products", products);
        model.addAttribute("newProducts", newProducts);

        return "home";
    }

    @GetMapping("/layout")
    public String layoutPage() {
        return "layout";
    }

    @GetMapping("/layoutProduct")
    public String layoutProductPage(Model model) {
        List<ProductDTO> products = productService.get();
        model.addAttribute("products", products);
        return "layoutProduct";
    }
    
    @GetMapping("/signIn")
    public String signInPage(Model model) {
        model.addAttribute("loginError", null);
        return "signIn";
    }

    @PostMapping("/signIn")
    public String signInForm(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        UserToSignIn req = new UserToSignIn();
        req.setUsername(username);
        req.setPassword(password);
        
        if (authService.authenticateUser(req)) { 
            session.setAttribute("isUser", true);
            session.setAttribute("username", username);
            return "redirect:/home"; 
        } else {
            session.removeAttribute("isUser");
            model.addAttribute("loginError", "Sai tên đăng nhập hoặc mật khẩu");
            return "signIn"; 
        }
    }

        @GetMapping("/signUp")
    public String signUpPage(Model model) {
        model.addAttribute("message", null);
        model.addAttribute("user", new UserToSignUp());
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUpForm(@RequestParam String username,
                            @RequestParam String name,
                            @RequestParam String password,
                            @RequestParam String phone,
                            Model model) {

        UserToSignUp req = new UserToSignUp();
        req.setUsername(username);
        req.setName(name);
        req.setPassword(password);
        req.setPhone(phone);

        String result = userService.setUser(req);
        
        if (result.equals("Create account successfully")) {
            model.addAttribute("message", "Tạo tài khoản thành công! Hãy đăng nhập để tiếp tục.");
            return "signIn";
        } else {
            model.addAttribute("message", result);
            return "signUp";
        }
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }
    @GetMapping("/cart")
    public String cartPage(Model model) {
        String userId = "U001";
        List<CartToGet> cartItems = cartService.get(userId);

    BigDecimal total = BigDecimal.ZERO;
    for (CartToGet item : cartItems) {
        BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        total = total.add(itemTotal);
    }


        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    @GetMapping("/checkout")
public String checkoutPage(Model model) {
    String userId = "U001";  

    // Lấy danh sách đơn hàng của user
    List<OrderDTO> orders = orderService.get(userId);
    model.addAttribute("orders", orders);

    // Lấy thông tin user
    UserDTO user = userService.getInfo(userId);
    model.addAttribute("user", user);

    // Lấy phương thức thanh toán (ví dụ từ đơn hàng đầu tiên)
    if (!orders.isEmpty()) {
        model.addAttribute("paymentMethod", orders.get(0).getPaymentMethod());
    }

        return "checkout";
    }
    
        @GetMapping("/admin")
        public String adminRoot() {
            return "redirect:/admin/login";
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
                session.setAttribute("isAdmin", true);
                return "redirect:/admin/home";
            } else {
                session.removeAttribute("isAdmin");
                model.addAttribute("loginError", "Wrong username or password");
                return "admin/login";
            }
        } 
    
        @GetMapping("/admin/home")
        public String adminHome(Model model, HttpSession session) {
            Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return "redirect:/admin/login";
            }
            List<ProductDTO> products = productService.get();
            model.addAttribute("products", products);
            return "admin/home";
        }
        
    
        @GetMapping("/admin/logout")
        public String adminLogout(HttpSession session) {
            session.invalidate();
            return "redirect:/admin/login";
        }

    @GetMapping("/admin/product")
    public String adminProduct(Model model){
        List<ProductDTO> products = productService.get();
        model.addAttribute("products", products);

        return "admin/product";
    }

    @PostMapping("/admin/variant/add")
    public String addVariant(@RequestParam("variantsJson") String variantsJson) {
        ObjectMapper mapper = new ObjectMapper();
        List<ProductVariantDTO> variants;
        try {
            variants = mapper.readValue(variantsJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        productService.addVariant(variants);
        return "redirect:/admin/product";
    }


    @GetMapping("/about")
    public String aboutPage(){
        return "about";
    }

    @GetMapping("/layoutInfo")
    public String layoutInfoPage(){
        return "layoutInfo";
    }
    @GetMapping("/layoutOrder")
    public String layoutOrderPage(){
        return "layoutOrder";
    }

    @GetMapping("/info")
    public String infoPage(Model model){
        UserDTO info = userService.getInfo("U002");
        model.addAttribute("info", info);
        return "info";
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "logout";
    }

    @PostMapping("/update")
    public String infoPage(@ModelAttribute("user") UserDTO updateInfo){
        updateInfo.setId("U002");
        userService.update(updateInfo);

        return "redirect:/info";
    }


    @GetMapping("/product")
    public String productPage(Model model){
        List<ProductDTO> products = productService.get();

        model.addAttribute("products", products);

        return "product";
    }

    @GetMapping("/product/{id}")
    public String productDetailsPage(Model model, @PathVariable("id") String id){
        ProductDTO product = productService.getById(id);

        model.addAttribute("product", product);

        return "productDetails";
    }


}
