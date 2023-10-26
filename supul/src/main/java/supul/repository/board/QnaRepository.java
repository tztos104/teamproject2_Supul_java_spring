package supul.repository.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.board.QnaBoard;

public interface QnaRepository extends JpaRepository<QnaBoard, Integer>{
	
	List<QnaBoard> findByTitle(String title);
	
	Page<QnaBoard> findByTitleContaining(String title, Pageable pageable);
	Page<QnaBoard> findByContentContaining(String Conent,Pageable pageable);
	Page<QnaBoard> findByContentContainingOrTitleContaining(String Conent, String title, Pageable pageable);
	Page<QnaBoard> findByUserUserIdContaining(String userName,Pageable pageable);
	

}
 