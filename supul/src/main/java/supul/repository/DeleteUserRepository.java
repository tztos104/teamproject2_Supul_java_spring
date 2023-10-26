package supul.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.DeleteUser;
import supul.model.User;


public interface DeleteUserRepository extends JpaRepository<DeleteUser, Integer> {

   List<DeleteUser> findByDeletedAtBefore(LocalDateTime threeMonthsAgo);

   DeleteUser findByUserId(String userId);
   
   List<DeleteUser> findAll();

   void save(User user);
   
   boolean existsByUserId(String userId);
}