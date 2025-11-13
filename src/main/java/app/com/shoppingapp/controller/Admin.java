package app.com.shoppingapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

public class Admin {

    protected static final String AUTH_SESSION_KEY = "isAdmin";
    protected static final String ADMIN_LOGIN_URL = "redirect:/admin/login";
    protected static final String KEY_SUCCESS = "success";
    protected static final String KEY_MESSAGE = "message";
    protected static final String KEY_DATA = "data";

    protected boolean isAuthenticated(HttpSession session) {
        return session.getAttribute(AUTH_SESSION_KEY) != null;
    }

    protected void addUsernameToModel(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("loggedInUsername", username);
        }
    }

    protected Map<String, Object> unauthorizedResponse() {
        return createResponse(false, "Unauthorized", null);
    }

//    Tạo response từ service result (String message)
    protected Map<String, Object> createResponse(String serviceResult) {
        boolean isSuccess = serviceResult != null &&
                serviceResult.toLowerCase().contains("thành công");
        return createResponse(isSuccess, serviceResult, null);
    }

//  Tạo response với success + message
    protected Map<String, Object> createResponse(boolean success, String message) {
        return createResponse(success, message, null);
    }

    // Tạo response đầy đủ: success + message + data
    protected Map<String, Object> createResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_SUCCESS, success);
        response.put(KEY_MESSAGE, message);
        if (data != null) {
            response.put(KEY_DATA, data);
        }
        return response;
    }
}
