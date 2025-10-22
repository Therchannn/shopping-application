package app.com.shoppingapp.dto;

import lombok.Data;

@Data
public class UserToSignUp {
    String username;
    String name;
    String password;
    String phone;
    String address;
}
