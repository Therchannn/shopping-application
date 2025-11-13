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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                    .role(false) // Mặc định là user thường
                    .status(true) // Mặc định là active
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

    public List<UserDTO> get(){
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> search(String keyword){
        return userRepository.searchUsers(keyword).stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public String create(UserDTO data){
        try{
            Optional<User> result = userRepository.findUserByUsername(data.getUsername());

            if (result.isPresent()) {
                return "Username đã tồn tại";
            }

            // Validate password
            if (data.getPassword() == null || data.getPassword().trim().isEmpty()) {
                return "Mật khẩu không được để trống";
            }

            if (data.getPassword().length() < 6) {
                return "Mật khẩu phải có ít nhất 6 ký tự";
            }

            User user = User.builder()
                    .username(data.getUsername())
                    .name(data.getName())
                    .password(data.getPassword())
                    .phone(data.getPhone())
                    .address(data.getAddress())
                    .role(data.isRole())
                    .status(true)
                    .build();

            userRepository.save(user);
            return "Thêm khách hàng thành công";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    public String updateCustomer(UserDTO data) {
        try {
            User user = userRepository.findUserById(data.getId());

            if (user == null) {
                return "Không tìm thấy khách hàng";
            }

            user.setName(data.getName());
            user.setPhone(data.getPhone());
            user.setAddress(data.getAddress());
            user.setRole(data.isRole());
            user.setUpdatedAt(LocalDateTime.now());

            userRepository.save(user);

            return "Cập nhật thông tin thành công";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    public String ban(String id) {
        try {
            User user = userRepository.findUserById(id);

            if (user == null) {
                return "Không tìm thấy khách hàng";
            }

            user.setStatus(!user.isStatus());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            String statusText = user.isStatus() ? "mở khóa" : "khóa";
            return "Đã " + statusText + " tài khoản thành công";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    public long countByStatus(boolean status) {
        return userRepository.countByStatus(status);
    }

    public long countByRole(boolean role) {
        return userRepository.countByRole(role);
    }

}
