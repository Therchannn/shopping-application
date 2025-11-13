package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.dto.UserToSignIn;
import app.com.shoppingapp.dto.UserToSignUp;
import app.com.shoppingapp.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // @PostMapping("/signIn")
    // public UserDTO findUser(@RequestBody UserToSignIn data){
    //     return userService.getUser(data);
    // }

    // @PostMapping("/signUp")
    // public String createUser(@RequestBody UserToSignUp data){
    //     return userService.setUser(data);
    // }

    @GetMapping("/info")
    public UserDTO getInfoUser(@Param("id") String id){
        return userService.getInfo(id);
    }

    @PatchMapping("/update")
    public String updateInfo(@RequestBody UserDTO data){
        return userService.update(data);
    }
}
