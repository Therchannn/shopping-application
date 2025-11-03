package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CustomerAdminController {

    private static final String AUTH_SESSION_KEY = "isAdmin";
    private final UserService userService;

    @PostMapping("/admin/customers/add")
    public Map<String, Object> addCustomer(@RequestBody UserDTO customerData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (session.getAttribute(AUTH_SESSION_KEY) == null) {
            response.put("success", false);
            response.put("message", "Bạn chưa đăng nhập");
            return response;
        }

        String result = userService.create(customerData);

        if (result.contains("thành công")) {
            response.put("success", true);
            response.put("message", result);
        } else {
            response.put("success", false);
            response.put("message", result);
        }

        return response;
    }

    @PostMapping("/admin/customers/edit/{id}")
    public Map<String, Object> editCustomer(@PathVariable String id,
                                            @RequestBody UserDTO customerData,
                                            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (session.getAttribute(AUTH_SESSION_KEY) == null) {
            response.put("success", false);
            response.put("message", "Bạn chưa đăng nhập");
            return response;
        }

        customerData.setId(id);
        String result = userService.updateCustomer(customerData);

        if (result.contains("thành công")) {
            response.put("success", true);
            response.put("message", result);
        } else {
            response.put("success", false);
            response.put("message", result);
        }

        return response;
    }

    @PostMapping("/admin/customers/delete/{id}")
    public Map<String, Object> deleteCustomer(@PathVariable String id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (session.getAttribute(AUTH_SESSION_KEY) == null) {
            response.put("success", false);
            response.put("message", "Bạn chưa đăng nhập");
            return response;
        }

        String result = userService.delete(id);

        if (result.contains("thành công")) {
            response.put("success", true);
            response.put("message", result);
        } else {
            response.put("success", false);
            response.put("message", result);
        }
        return response;
    }
}

