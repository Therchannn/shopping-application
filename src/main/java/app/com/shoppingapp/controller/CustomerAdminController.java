package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CustomerAdminController extends Admin {

    private final UserService userService;


    @PostMapping("/admin/customers/add")
    public Map<String, Object> addCustomer(@RequestBody UserDTO customerData, HttpSession session) {
        if (!isAuthenticated(session)) {
            return unauthorizedResponse();
        }

        String result = userService.create(customerData);
        return createResponse(result);
    }

    @PostMapping("/admin/customers/edit/{id}")
    public Map<String, Object> editCustomer(@PathVariable String id,
                                            @RequestBody UserDTO customerData,
                                            HttpSession session) {
        if (!isAuthenticated(session)) {
            return unauthorizedResponse();
        }

        customerData.setId(id);
        String result = userService.updateCustomer(customerData);
        return createResponse(result);
    }

    @PostMapping("/admin/customers/delete/{id}")
    public Map<String, Object> banCustomer(@PathVariable String id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return unauthorizedResponse();
        }

        String result = userService.ban(id);
        return createResponse(result);
    }
}

