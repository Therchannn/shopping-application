package app.com.shoppingapp.service;

import app.com.shoppingapp.dto.UserToSignIn;
import app.com.shoppingapp.entity.User;
import app.com.shoppingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    public boolean authenticateAdmin(UserToSignIn request) {
        User user = userRepository
                .findUserByUsernameAndPassword(request.getUsername(), request.getPassword())
                .orElse(null);
        if (user == null) return false;

        // true = admin
        return user.isRole();
    }

    // LOGIN USER
    public boolean authenticateUser(UserToSignIn request) {
        User user = userRepository
                .findUserByUsernameAndPassword(request.getUsername(), request.getPassword())
                .orElse(null);
        if (user == null) return false;

        return !user.isRole();
    }
}
