package supul.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

import lombok.Data;

@Entity
@Data
public class DeleteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    String userId;
    String userName;
    String userPw;
    String birth;
    String email;
    String phone;
    LocalDateTime regDate;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
}