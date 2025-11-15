package app.com.shoppingapp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.com.shoppingapp.dto.ProductVariantDTO;
import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.entity.Order;
import app.com.shoppingapp.entity.Product;
import app.com.shoppingapp.entity.ProductVariant;
import app.com.shoppingapp.entity.User;
import app.com.shoppingapp.mapper.OrderMapper;
import app.com.shoppingapp.repository.OrderRepository;
import app.com.shoppingapp.repository.ProductVariantsRepository;
import app.com.shoppingapp.service.UserService;
import app.com.shoppingapp.service.OrderService;
import app.com.shoppingapp.service.CartService;
import app.com.shoppingapp.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import app.com.shoppingapp.dto.*;
import app.com.shoppingapp.repository.UserRepository;
import app.com.shoppingapp.service.CartService;
import app.com.shoppingapp.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Optional;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class PageController extends Admin{

    private static final String ADMIN_DASHBOARD_URL = "redirect:/admin/dashboard";

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final AuthService authService;
    private final OrderService orderService;
    private final ProductVariantsRepository productVariantsRepository;
    private final OrderRepository orderRepository;

    @ModelAttribute
    public void addCartData(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("id");
        if (userId != null) {
            List<CartToGet> cartItems = cartService.get(userId);

            BigDecimal total = BigDecimal.ZERO;
            for (CartToGet item : cartItems) {
                total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            model.addAttribute("isAuth", false);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("total", total);
        }
        else{
            model.addAttribute("isAuth", true);
        }
    }

    @GetMapping("/home")
    public String homePage(Model model){
        List<ProductDTO> allProducts = productService.get();

        allProducts = allProducts.stream()
                .filter(p -> p.getStatus() == Product.Status.ACTIVE)
                .toList();

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

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
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

        if (authService.authenticateUser(req) != null) {
            String id = authService.authenticateUser(req);
            session.setAttribute("id", id);
            return "redirect:/home";
        } else {
            session.removeAttribute("id");
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

    @PostMapping("/confirm/checkout")
    public String confirmCheckout(HttpServletRequest req, HttpSession session, RedirectAttributes attr){
        String userId = (String) session.getAttribute("id");
        UserDTO user = userService.getInfo(userId);

        if(user == null){
            return "redirect:/signIn";
        }

        List<CartToGet> items = cartService.get(userId);
        if(items == null || items.isEmpty()){
            attr.addFlashAttribute("messageType", "error");
            attr.addFlashAttribute("message", "Vui lòng thêm sản phẩm vào giỏ hàng");

            return "redirect:"+ req.getHeader("Referer");
        }

        if (user.getAddress() == null || user.getAddress().isEmpty()){
            attr.addFlashAttribute("messageType", "error");
            attr.addFlashAttribute("message", "Vui lòng cập nhật địa chỉ");

            return "redirect:/info";
        }

        if(user.getPhone().isEmpty()){
            attr.addFlashAttribute("messageType", "error");
            attr.addFlashAttribute("message", "Vui lòng cập nhật số điện thoại");

            return "redirect:/info";
        }

        return "redirect:/checkout";
    }

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, HttpServletRequest request, Model model, @RequestParam(required = false) String orderId, RedirectAttributes attr) {
        String userId = (String) session.getAttribute("id");

        if(userId == null ){
            return "redirect:/login";
        }

        List<CartToGet> items;
        UserDTO info = userService.getInfo(userId);
        BigDecimal total = BigDecimal.ZERO;

        if(orderId == null){
            items = cartService.get(userId);
        }

        else{
            List<OrderDTO> orders = orderService.getById(userId);
            OrderDTO order = orders.stream()
                    .filter(o -> o.getId().equals(orderId))
                    .findFirst()
                    .orElse(null);

            if(order == null){
                return "redirect:" + request.getHeader("Referer");
            }

            List<CartToGet> cartOrder = cartService.getFromOrder(orders, orderId);
            model.addAttribute("orderId", orderId);

            List<CartToGet> newUpdateOrder = new ArrayList<>();

            for(OrderItemDTO item : order.getItems()){
                ProductVariant variant = productVariantsRepository.findByIdProductVariant(item.getVariantId());

                if(variant.getQuantity() < item.getQuantity()){
                    item.setQuantity(variant.getQuantity());
                }

                if(variant.getQuantity() == 0){
                    item.setQuantity(0);
                }
            }

            for(CartToGet item : cartOrder){
                ProductVariant variant = productVariantsRepository.findByIdProductVariant(item.getId());

                if(variant.getQuantity() == 0){
                    item.setQuantity(0);
                    continue;
                }

                if(variant.getQuantity() < item.getQuantity()){
                    item.setQuantity(variant.getQuantity());
                }

                newUpdateOrder.add(item);
                BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                total = total.add(totalPrice);
            }

            if(newUpdateOrder.isEmpty()){
                attr.addFlashAttribute("messageType", "error");
                attr.addFlashAttribute("message", "Đơn hàng hiện không còn khả dụng");
                return "redirect:"+ request.getHeader("Referer");
            }
            else{
                items = newUpdateOrder;
                order.setTotal(total);
                orderRepository.save(OrderMapper.toEntity(order));
            }
        }

        for(CartToGet item : items){
            BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(totalPrice);
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("info", info);

        return "checkout";
    }

        @GetMapping("/admin")

        public String adminRoot() {
            return "redirect:/admin/login";
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
            session.setAttribute("username", username); // Lưu username vào session
            return ADMIN_DASHBOARD_URL;
        } else {
            session.removeAttribute(AUTH_SESSION_KEY);
            model.addAttribute("loginError", "Wrong username or password");
            return "admin/login";
        }
    }

    @PostMapping("/admin/product/add")
    public String handleAddForm(@ModelAttribute("product") ProductDTO product, Model model) {
        String message =productService.addProduct(product);

        model.addAttribute("message", message);
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/product/modify")
    public String modifyProduct(@ModelAttribute("editProduct") ProductDTO data){
        productService.updateProduct(data);

        return "redirect:/admin/products";
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
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return ADMIN_LOGIN_URL;
    }

    @GetMapping("/about")
    public String aboutPage(){
        return "about";
    }

    @GetMapping("/layoutInfo")
    public String layoutInfoPage(){
        return "layoutInfo";
    }

    @GetMapping("/order")
    public String layoutOrderPage(Model model, HttpSession session, @RequestParam(required = false, defaultValue = "all") String type){
        String userId = (String) session.getAttribute("id");

        if (userId == null || userId.isEmpty()) {
            return "redirect:/signIn";
        }

        List<OrderDTO> orders = orderService.getById(userId);

        if (!type.equalsIgnoreCase("all")) {
            orders = orders.stream()
                    .filter(o -> o.getStatus().equalsIgnoreCase(type))
                    .toList();
        }

        model.addAttribute("orders", orders);

        return "layoutOrder";
    }

    @PostMapping("/order/update")
    public String updateOrder(@RequestParam("status") String status, @RequestParam("orderId") String orderId, @RequestParam("payment") String payment){
        orderService.update(orderId, status, payment);

        return "redirect:/order?type=" + status.toLowerCase();
    }

    @GetMapping("/info")
    public String infoPage(Model model, HttpSession session){
        String id = (String) session.getAttribute("id");
        if(id == null){
            model.addAttribute("error", "Vui lòng đăng nhập tài khoản");
            return "redirect:/signIn";
        }
        else{
        UserDTO info = userService.getInfo(id);
        model.addAttribute("info", info);
        }
        return "info";
    }

    @PostMapping("/logout")
    public String logoutPage(HttpSession session){
        session.invalidate();
        return "redirect:/home";
    }

    @PostMapping("/info/update")
    public String infoPage(@ModelAttribute("user") UserDTO updateInfo, HttpSession session){
        String id = (String) session.getAttribute("id");
        if(id == null) return "/info";

        UserDTO user = userService.getInfo(id);
        user.setName(updateInfo.getName());
        user.setPhone(updateInfo.getPhone());
        user.setAddress(updateInfo.getAddress());
        userService.update(user);

        return "redirect:/info";
    }

    @PostMapping("/cart/add")
    public String addToCart(HttpSession session, @ModelAttribute ProductAddToCart data, RedirectAttributes attr){
        String id = (String) session.getAttribute("id");
        if(id == null){
            attr.addFlashAttribute("messageType", "error");
            attr.addFlashAttribute("message", "Vui lòng đăng nhâp tài khoàn");
        }
        else{
            data.setUserId(id);
            String message = cartService.add(data);
            attr.addFlashAttribute("messageType", "success");
            attr.addFlashAttribute("message", message);
        }

        return "redirect:/product/" + data.getProductId();
    }

    @GetMapping("/product")
    public String productPage(Model model, @RequestParam(required = false) String search, @RequestParam(required = false) String type){
        List<ProductDTO> products;

        if(search == null){
            products = productService.get();
        }
        else{
            products = productService.search(search);
        }

        products = products.stream()
                .filter(p -> p.getStatus() == Product.Status.ACTIVE)
                .toList();

        if(type != null){
            products = products.stream()
                    .filter(v -> v.getCategory().equals(type))
                    .toList();
        }

        model.addAttribute("products", products);

        return "product";
    }


    @GetMapping("/product/{id}")
    public String productDetailsPage(Model model, @PathVariable("id") String id){
        ProductDTO product = productService.getById(id);

        ProductAddToCart newCart = new ProductAddToCart();
        newCart.setProductId(id);
        newCart.setQuantity(1);

        model.addAttribute("product", product);
        model.addAttribute("cart", newCart);

        return "productDetails";
    }

}
