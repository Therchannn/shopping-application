package app.com.shoppingapp.service;

import app.com.shoppingapp.dto.UserToSignIn;
import app.com.shoppingapp.dto.UserToSignUp;
import app.com.shoppingapp.entity.User;
import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.mapper.UserMapper;
import app.com.shoppingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

    @Service
    @RequiredArgsConstructor
    public class UserService {
        private final UserRepository userRepository;

        public UserDTO getUser(UserToSignIn data){
            Optional<User> optionalUser = userRepository.findUserByUsernameAndPassword(data.getUsername(), data.getPassword());
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                return UserMapper.toDTO(user);
            }

            return null;
        }

        public String setUser(UserToSignUp data){
            try{
                Optional<User> result = userRepository.findUserByUsername(data.getUsername());

            if (result.isPresent()) {
                return "Username has been used";
            }

            User user = User.builder()
                    .username(data.getUsername())
                    .name(data.getName())
                    .password(data.getPassword())
                    .phone(data.getPhone())
                    .address(data.getAddress())
                    .build();

            userRepository.save(user);
            return "Create account successfully";
        } catch (Exception e) {
            return "Something is error: " + e.getMessage();
        }
    }

    public UserDTO getInfo(String id) {
        User user = userRepository.findUserById(id);
        return UserMapper.toDTO(user);
    }

    public String update(UserDTO data) {
        try {
            User user = userRepository.findUserById(data.getId());

            user.setUsername(data.getUsername());
            user.setName(data.getName());
            user.setPhone(data.getPhone());
            user.setAddress(data.getAddress());
            user.setUpdatedAt(LocalDateTime.now());

            userRepository.save(user);

            return "Update information successfully";
        } catch (Exception e) {
            return "Something is error: " + e.getMessage();
        }
    }

    public long count() {
        return userRepository.count();
    }
}
