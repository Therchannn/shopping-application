package app.com.shoppingapp.controller;

import app.com.shoppingapp.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/orders")
public class OrderAdminController extends BaseAdminController {

    private final OrderService orderService;


    @PostMapping("/update-status/{orderId}")
    @ResponseBody
    public Map<String, Object> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody Map<String, String> request,
            HttpSession session) {

        if (!isAuthenticated(session)) {
            return unauthorizedResponse();
        }

        String newStatus = request.get("status");
        String result = orderService.updateStatus(orderId, newStatus);
        return createResponse(result);
    }
}

