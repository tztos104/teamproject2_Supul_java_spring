package supul.service;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import supul.model.board.NoticeBoard;
import supul.model.board.QnaBoard;
import supul.model.board.QnaComment;
import supul.model.board.ReviewBoard;
import supul.repository.board.NoticeRepository;
import supul.repository.board.QnaRepository;
import supul.repository.board.ReviewRepository;
import supul.repository.board.CommentRepository;

@Service
public class BoardService {
	@Autowired
	private NoticeRepository boardNRepository;

	@Autowired
	private ReviewRepository boardRRepository;
	
	@Autowired
	private QnaRepository qnaRepository;

	@Transactional
	public void uploadAndSave(NoticeBoard bn, String pathN, MultipartFile newFile, HttpServletRequest request) {
	    if (newFile != null && !newFile.isEmpty()) {
	        String originalFileName = newFile.getOriginalFilename();
	        String fileName = StringUtils.cleanPath(originalFileName);
	        System.out.println("내가 경로에용 => "+pathN);
	        
	        try {
	            File uploadPath = new File(pathN);
	            if (!uploadPath.exists()) {
	                uploadPath.mkdirs();
	            }

	            // 파일 이름 중복 검사 및 중복 처리
	            int number = 1;
	            while (new File(uploadPath, fileName).exists()) {
	                int dotIndex = originalFileName.lastIndexOf(".");
	                String baseName = (dotIndex != -1) ? originalFileName.substring(0, dotIndex) : originalFileName;
	                String extension = (dotIndex != -1) ? originalFileName.substring(dotIndex) : "";
	                fileName = baseName + "_" + number + extension;
	                number++;
	            }

	            File destFile = new File(uploadPath, fileName);
	            newFile.transferTo(destFile);

	            // 기존 이미지 파일 삭제
	            if (bn.getFileName() != null) {
	                String oldFileName = bn.getFileName();
	                File oldImageFile = new File(pathN, oldFileName);
	                if (oldImageFile.exists()) {
	                    oldImageFile.delete();
	                }
	            }

	            bn.setFileName(fileName);
	            bn.setFilePath(pathN); // 파일 경로 저장
	            boardNRepository.save(bn);

	        } catch (Exception e) {
	            e.printStackTrace();
	            // 파일 업로드 실패 처리
	        }
	    }
	}
	
	@Transactional
	public void uploadAndSave(ReviewBoard br, String pathR, MultipartFile file, HttpServletRequest request) {
		if (file != null && !file.isEmpty()) {
			String originalFileName = file.getOriginalFilename();
			String fileName = StringUtils.cleanPath(originalFileName);
	        System.out.println("내가 경로에용 => "+pathR);
			
			try {
				File uploadPath = new File(pathR);
				if (!uploadPath.exists()) {
					uploadPath.mkdirs();
				}

				// 파일 이름 중복 검사 및 중복 처리
				int number = 1;
				while (new File(uploadPath, fileName).exists()) {
					int dotIndex = originalFileName.lastIndexOf(".");
					String baseName = (dotIndex != -1) ? originalFileName.substring(0, dotIndex) : originalFileName;
					String extension = (dotIndex != -1) ? originalFileName.substring(dotIndex) : "";
					fileName = baseName + "_" + number + extension;
					number++;
				}

				File destFile = new File(uploadPath, fileName);
				file.transferTo(destFile);
				
	            // 기존 이미지 파일 삭제
	            if (br.getFileName() != null) {
	                String oldFileName = br.getFileName();
	                File oldImageFile = new File(pathR, oldFileName);
	                if (oldImageFile.exists()) {
	                    oldImageFile.delete();
	                }
	            }
				
				br.setFileName(fileName);
				br.setFilePath(pathR); // 파일 경로 저장
				boardRRepository.save(br);

			} catch (Exception e) {
				e.printStackTrace();
				// 파일 업로드 실패 처리
			}
		}
	}

	// 파일 삭제 메서드
	public void deleteFile(String fileName, String path, HttpServletRequest request) {
	    if (fileName != null) {
	        System.out.println("path2테스트 => "+path);
	        
	        try {
	            new File(path+"\\"+fileName).delete();
	            
	            System.out.println("삭제성공");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } else {
	        System.err.println("파일 삭제 실패");
	    }
	}

	@Transactional
	public void deleteAndRemoveFile(int id) {
		Optional<NoticeBoard> optionalNotice = boardNRepository.findById(id);

		if (optionalNotice.isPresent()) {
			NoticeBoard notice = optionalNotice.get();

			// 파일 경로와 파일 이름을 합쳐서 파일 객체 생성
			File fileToDelete = new File(notice.getFilePath(), notice.getFileName());

			if (fileToDelete.exists()) {
				if (fileToDelete.delete()) {
					// 파일 삭제에 성공한 경우
					System.out.println("파일 삭제 성공: " + notice.getFileName());
				} else {
					// 파일 삭제에 실패한 경우
					System.out.println("파일 삭제 실패: " + notice.getFileName());
				}
			} else {
				// 파일이 존재하지 않는 경우
				System.out.println("파일이 존재하지 않습니다: " + notice.getFileName());
			}

			// 게시물 삭제
			boardNRepository.deleteById(id);
		}
	}
	
	
	
	@Autowired
    private CommentRepository commQRepository;

    @Autowired
    public BoardService(CommentRepository commQRepository) {
        this.commQRepository = commQRepository;
    }
    
    public List<QnaComment> getAllBoardQComments() {
		return commQRepository.findAll();
	}

    @Transactional
    public boolean deleteQnaBoard(int qnaId) {
        try {
            // QnaRepository를 사용하여 게시물을 가져옵니다.
            Optional<QnaBoard> optionalQna = qnaRepository.findById(qnaId);

            if (optionalQna.isPresent()) {
                QnaBoard qna = optionalQna.get();

                // 게시물과 연관된 댓글도 삭제합니다.
                List<QnaComment> comments = qna.getComment();
                if (comments != null && !comments.isEmpty()) {
                    commQRepository.deleteAll(comments);
                }

                // 게시물 삭제
                qnaRepository.delete(qna);

                return true; // 게시물 삭제 성공 시 true 반환
            } else {
                return false; // 게시물이 존재하지 않을 경우 false 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 게시물 삭제 실패 시 false 반환
        }
    }
 
	
	
	

}