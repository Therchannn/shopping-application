package app.com.shoppingapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String address;
    private boolean role;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
