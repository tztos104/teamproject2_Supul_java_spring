package supul.repository.board;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.board.QnaComment;
import supul.model.board.QnaBoard;



public interface CommentRepository extends JpaRepository<QnaComment, Integer>{
	// 댓글 삭제
	


}
