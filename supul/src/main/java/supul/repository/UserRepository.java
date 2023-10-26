package supul.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserName(String username);

	List<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	User findByUserId(String userId);

	User findByUserIdAndEmail(String userId, String email);
	
	User findByUserPw(String userpw);
	
	User findByUserNameAndEmailAndPhone(String userName, String email, String phone);
	
	User findUserIdByEmail(String email);
	
	User findUserPwByEmail( String email);
	
	User findUserPwByEmailAndUserId(String email, String userId);
	
	List<User> findByBlacklist(Boolean b);
	
	
//===	영준 추가===//
	List<User> findByUserNameContaining(String userName);
	Page<User> findByUserNameContaining(String userName, Pageable pageable);
	

}