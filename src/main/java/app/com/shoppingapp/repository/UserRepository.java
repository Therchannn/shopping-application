package app.com.shoppingapp.repository;

import app.com.shoppingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsernameAndPassword(String username, String password);
    Optional<User> findUserByUsername(String username);
    User findUserById(String id);

    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);
}