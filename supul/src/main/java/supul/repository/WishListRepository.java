package supul.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.Thema;
import supul.model.User;
import supul.model.WishList;


public interface WishListRepository extends JpaRepository<WishList, Integer>{

	List<WishList> findByUser(User user);
	WishList findByThemaAndUser(Thema thema ,User user);
	List<WishList> findByThema(Thema thema);
	List<WishList> findByUserId(int userId);
	
}
