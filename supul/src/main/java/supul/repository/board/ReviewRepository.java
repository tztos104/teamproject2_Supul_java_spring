package supul.repository.board;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.board.NoticeBoard;
import supul.model.board.ReviewBoard;




public interface ReviewRepository extends JpaRepository<ReviewBoard, Integer>{
	
	List<ReviewBoard> findByTitle(String title);
	Page<ReviewBoard> findByUserId(int userId, Pageable pageable);	
	
	Page<ReviewBoard> findByTitleContaining(String title, Pageable pageable);
	Page<ReviewBoard> findByContentContaining(String Conent,Pageable pageable);
	Page<ReviewBoard> findByThemaTitleContaining(String thematitle,Pageable pageable);
	Page<ReviewBoard> findByContentContainingOrTitleContaining(String Conent, String title, Pageable pageable);
	Page<ReviewBoard> findByUserUserIdContaining(String adminId,Pageable pageable);
	List<ReviewBoard> findTop5ByOrderByRegDateDesc();
	
}
