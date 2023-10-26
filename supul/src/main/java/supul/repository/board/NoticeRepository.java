package supul.repository.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.board.NoticeBoard;
import supul.model.board.QnaBoard;




public interface NoticeRepository extends JpaRepository<NoticeBoard, Integer>{
	
	List<NoticeBoard> findByTitle(String title);
	Page<NoticeBoard> findByTitleContaining(String title, Pageable pageable);
	Page<NoticeBoard> findByContentContaining(String Conent,Pageable pageable);
	Page<NoticeBoard> findByContentContainingOrTitleContaining(String Conent, String title, Pageable pageable);
	Page<NoticeBoard> findByWriterContaining(String Writer,  Pageable pageable);
	
	// 게시판 작성작 검색이 총,괄,총괄일때 adminId가 null인 게시글 불러오기
	
	// 파일 관련
	NoticeBoard findByFileName(String fileName);
	List<NoticeBoard> findTop5ByOrderByRegDateDesc();
	

}
 