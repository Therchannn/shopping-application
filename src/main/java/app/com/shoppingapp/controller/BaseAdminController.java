package app.com.shoppingapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseAdminController {

    protected static final String AUTH_SESSION_KEY = "isAdmin";
    protected static final String ADMIN_LOGIN_URL = "redirect:/admin/login";
    protected static final String KEY_SUCCESS = "success";
    protected static final String KEY_MESSAGE = "message";


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
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_SUCCESS, false);
        response.put(KEY_MESSAGE, "Unauthorized");
        return response;
    }

    protected Map<String, Object> createResponse(String serviceResult) {
        Map<String, Object> response = new HashMap<>();
        boolean isSuccess = serviceResult.contains("thành công");
        response.put(KEY_SUCCESS, isSuccess);
        response.put(KEY_MESSAGE, serviceResult);
        return response;
    }
    
}

