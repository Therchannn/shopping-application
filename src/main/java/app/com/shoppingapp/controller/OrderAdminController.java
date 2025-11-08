package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/orders")
public class OrderAdminController extends Admin {

    private final OrderService orderService;

    @GetMapping("/detail/{orderId}")
    @ResponseBody
    public Map<String, Object> getOrderDetail(
            @PathVariable String orderId,
            HttpSession session) {

        if (!isAuthenticated(session)) {
            return unauthorizedResponse();
        }

        try {
            OrderDTO order = orderService.getOrderDetailById(orderId);

            if (order == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Không tìm thấy đơn hàng");
                return response;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", order);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return response;
        }
    }

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

