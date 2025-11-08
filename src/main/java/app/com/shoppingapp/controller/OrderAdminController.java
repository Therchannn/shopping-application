package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
                return createResponse(false, "Không tìm thấy đơn hàng");
            }

            return createResponse(true, "Lấy thông tin đơn hàng thành công", order);

        } catch (Exception e) {
            return createResponse(false, "Lỗi: " + e.getMessage());
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
