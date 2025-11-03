package app.com.shoppingapp.mapper;

import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.isRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
