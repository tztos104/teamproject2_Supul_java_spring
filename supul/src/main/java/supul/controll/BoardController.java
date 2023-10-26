package supul.controll;

import java.io.File;


import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import supul.model.Admin;
import supul.model.Branch;
import supul.model.PageData;
import supul.model.Reservation;
import supul.model.Thema;
import supul.model.User;
import supul.model.board.NoticeBoard;
import supul.model.board.QnaComment;
import supul.model.board.QnaBoard;
import supul.model.board.ReviewBoard;
import supul.repository.AdminRepository;
import supul.repository.BranchRepository;
import supul.repository.ReservationRepository;
import supul.repository.ThemaRepository;
import supul.repository.UserRepository;
import supul.repository.board.NoticeRepository;
import supul.repository.board.QnaRepository;
import supul.repository.board.ReviewRepository;

import supul.repository.board.CommentRepository;
import supul.service.BoardService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Resource
	private NoticeRepository boardNRepository;
	@Resource
	private ReviewRepository boardRRepository;
	@Resource
	private QnaRepository boardQRepository;
	@Resource
	private CommentRepository commQRepository;
	@Resource
	private BranchRepository branchRepository;
	@Resource
	private AdminRepository adminRepository;
	@Resource
	private UserRepository userRepository;
	@Resource
	private ThemaRepository themaRepository;
	@Resource
	private ReservationRepository reservationRepository;;

	@Autowired // BoardNoticeService를 주입
	private BoardService service;

	// 공지사항 리스트
	@GetMapping("notice/list")
	public String noticeList(HttpSession session, Model model,
			@RequestParam(name = "sortBy", defaultValue = "regDate") String sortBy,
			@RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
			@PageableDefault(size = 15, sort = "regDate", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "type", defaultValue = "") String type, // 검색 유형 (content, title, user)
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {

		Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Page<NoticeBoard> nboard;
		if (!keyword.isEmpty()) {
			// 선택한 검색 조건에 따라 검색 메서드 호출
			if ("title".equals(type)) {
				nboard = boardNRepository.findByTitleContaining(keyword, pageable);
			} else if ("content".equals(type)) {
				nboard = boardNRepository.findByContentContaining(keyword, pageable);
			} else if ("user".equals(type)) {

				nboard = boardNRepository.findByWriterContaining(keyword, pageable);
			} else if ("titleAndContent".equals(type)) {
				nboard = boardNRepository.findByContentContainingOrTitleContaining(keyword, keyword, pageable);
			} else {
				nboard = boardNRepository.findAll(pageable);
			}
		} else {
			nboard = boardNRepository.findAll(pageable);
		}
		model.addAttribute("nboard", nboard);

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==

		return "board_notice/notice_list";
	}

	// 공지사항 글쓰기 get
	@GetMapping("notice/write")
	public String noticeWrite(Model model, HttpSession session, PageData pageData) {

		List<Branch> branches = branchRepository.findAll(); // 모든 매장 정보 가져오기
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==
		model.addAttribute("branches", branches); // 매장 정보를 템플릿으로 전달

		return "board_notice/notice_write";
	}

	// 공지사항 글쓰기 post
	@PostMapping("notice/write")
	public String writeReg(@Valid NoticeBoard bn, BindingResult bindingResult, HttpSession session, Model model,
			HttpServletRequest request) {
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==
		// 영준 수정 @Valid NoticeBoard bn 에 추가
		if (bindingResult.hasErrors()) {
			// 유효성 검사 오류가 있는 경우 처리 (예: 오류 메시지를 모델에 추가)
			System.out.println(bindingResult.getAllErrors());
			System.out.println(bindingResult.getFieldError().getDefaultMessage());
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("titleerror", bindingResult.getFieldError("title"));
			model.addAttribute("contenterror", bindingResult.getFieldError("content"));
			return "board_notice/notice_write";
		}
		// 영준 수정 끝
		if (session.getAttribute("userRole") != null) {

			bn.setWriter("super");
			bn.setBranchName("총괄");

		} else if (session.getAttribute("admin") != null) {
			Admin admin = (Admin) session.getAttribute("admin");

			bn.setWriter(admin.getAdminId());
			bn.setBranchName(admin.getBranchName());
		}
		bn.setRegDate(LocalDateTime.now());
		MultipartFile file = bn.getFile();
		String path = request.getServletContext().getRealPath("/up/boardN");
		service.uploadAndSave(bn, path, file, request);
		boardNRepository.save(bn);

		return "redirect:/board/notice/list";
	}

	// 공지사항 상세
	@GetMapping("notice/detail/{id}")
	public String noticeDetail(@PathVariable int id, Model model, HttpSession session) {
		Optional<NoticeBoard> nboard = boardNRepository.findById(id);
		model.addAttribute("username", session.getAttribute("username"));
		if (nboard.isPresent()) {
			NoticeBoard board = nboard.get();

			model.addAttribute("nDetail", board);
			// 세션등록 ==start==
			model.addAttribute("userRole", session.getAttribute("userRole"));
			model.addAttribute("admin", session.getAttribute("admin"));
			model.addAttribute("adminBn", session.getAttribute("adminBn"));
			model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
			model.addAttribute("user", session.getAttribute("user"));
			// 세션등록 ==end==
			return "board_notice/notice_detail";
		} else {
			return "redirect:/board/notice/list";
		}
	}

	// 공지사항 삭제
	@GetMapping("notice/delete/{id}")
	public String noticeDelete(@PathVariable int id, HttpServletRequest request, Model model, HttpSession session) {
		Optional<NoticeBoard> nboard = boardNRepository.findById(id);

		if (nboard.isPresent()) {
			NoticeBoard board = nboard.get();

			String fileName = board.getFileName();
			if (fileName != null) {
				String path = request.getServletContext().getRealPath("/up/boardN");
				service.deleteFile(fileName, path, request);
			}

			// 세션등록 ==start==
			model.addAttribute("userRole", session.getAttribute("userRole"));
			model.addAttribute("admin", session.getAttribute("admin"));
			model.addAttribute("adminBn", session.getAttribute("adminBn"));
			model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
			model.addAttribute("user", session.getAttribute("user"));
			// 세션등록 ==end==
			boardNRepository.delete(board);
			System.out.println("공지사항 삭제>>> ");
		}

		return "redirect:/board/notice/list";
	}

	// 공지사항 수정 get
	@GetMapping("notice/modify/{id}")
	public String noticeModify(@PathVariable int id, Model model, HttpSession session) {
		Optional<NoticeBoard> nboard = boardNRepository.findById(id);
		List<Admin> admins = adminRepository.findAll();
		List<Branch> branchs = branchRepository.findAll();
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==

		if (nboard.isPresent()) {
			NoticeBoard board = nboard.get();
			model.addAttribute("admins", admins);
			model.addAttribute("branchs", branchs);

			model.addAttribute("board", board);
			return "board_notice/notice_modify";

		} else {
			return "redirect:/board/notice/list";
		}
	}

	// 공지사항 수정 post
	@PostMapping("notice/modify/{id}")
	public String noticeModifyOrDelete(@PathVariable int id, @RequestParam String action,@Valid NoticeBoard bd,
			BindingResult bindingResult, Model model,
			HttpServletRequest request, @RequestParam("file") MultipartFile file, HttpSession session) throws Exception {

		if (bindingResult.hasErrors()) {
			Optional<NoticeBoard> nboard = boardNRepository.findById(id);
			List<Admin> admins = adminRepository.findAll();
			List<Branch> branchs = branchRepository.findAll();
			// 세션등록 ==start==
			model.addAttribute("userRole", session.getAttribute("userRole"));
			model.addAttribute("admin", session.getAttribute("admin"));
			model.addAttribute("adminBn", session.getAttribute("adminBn"));
			model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
			model.addAttribute("user", session.getAttribute("user"));
			// 세션등록 ==end==

			if (nboard.isPresent()) {
				NoticeBoard board = nboard.get();
				model.addAttribute("admins", admins);
				model.addAttribute("branchs", branchs);

				model.addAttribute("board", board);

			} 
			// 유효성 검사 오류가 있는 경우 처리 (예: 오류 메시지를 모델에 추가)
			System.out.println(bindingResult.getAllErrors());
			System.out.println(bindingResult.getFieldError().getDefaultMessage());
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("titleerror", bindingResult.getFieldError("title"));
			model.addAttribute("contenterror", bindingResult.getFieldError("content"));
			return "board_notice/notice_modify";
		}
		
		
		Optional<NoticeBoard> nboard = boardNRepository.findById(id);

		if (nboard.isPresent()) {
			NoticeBoard newboard = nboard.get();

			if ("수정".equals(action)) {
				newboard.setContent(bd.getContent());
				newboard.setTitle(bd.getTitle());
				newboard.setModiDate(LocalDateTime.now());

				if (!file.isEmpty()) {
					// 파일이 업로드되었을 때만 파일 업로드 및 정보 업데이트
					String pathN = request.getServletContext().getRealPath("/up/boardN");
					service.uploadAndSave(newboard, pathN, file, request);
				}

				boardNRepository.save(newboard);

				return "redirect:/board/notice/detail/" + id; // 수정 후 상세 페이지로 리디렉션
			} else if ("삭제".equals(action)) {
				// 파일 삭제

				String pathN = request.getServletContext().getRealPath("/up/boardN");
				String fileName = newboard.getFileName();
				if (fileName != null) {
					service.deleteFile(fileName, pathN, request);
				}

				boardNRepository.delete(newboard);
				return "redirect:/board/notice/list"; // 삭제 후 목록 페이지로 리디렉션
			}
		}

		return "redirect:/board/notice/detail/" + id; // 수정 및 삭제 실패 시 상세 페이지로 리디렉션
	}

	// 후기 리스트
	@GetMapping("review/list")
	public String reviewList(HttpSession session, Model model,
			@RequestParam(name = "sortBy", defaultValue = "regDate") String sortBy,
			@RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
			@PageableDefault(size = 15, sort = "regDate", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "type", defaultValue = "") String type, // 검색 유형 (content, title, user)
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {

		// 페이지 및 사이즈를 기준으로 Pageable 객체 생성
		Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Page<ReviewBoard> rboard;
		if (!keyword.isEmpty()) {
			// 선택한 검색 조건에 따라 검색 메서드 호출
			if ("title".equals(type)) {
				rboard = boardRRepository.findByTitleContaining(keyword, pageable);
			} else if ("content".equals(type)) {
				rboard = boardRRepository.findByContentContaining(keyword, pageable);
			} else if ("user".equals(type)) {
				rboard = boardRRepository.findByUserUserIdContaining(keyword, pageable);
			} else if ("thema".equals(type)) {
				rboard = boardRRepository.findByThemaTitleContaining(keyword, pageable);
			} else if ("titleAndContent".equals(type)) {
				rboard = boardRRepository.findByContentContainingOrTitleContaining(keyword, keyword, pageable);
			} else {
				rboard = boardRRepository.findAll(pageable);
			}
		} else {
			rboard = boardRRepository.findAll(pageable);
		}

		// 페이지 관련 정보를 Thymeleaf로 전달
		model.addAttribute("rboard", rboard);

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==
		System.out.println("후기 리스트>>> ");
		return "board_review/review_list";
	}

	// 후기 글쓰기 get
	@GetMapping("review/write")
	public String reviewWrite(Model model, HttpSession session, PageData pageData, @RequestParam String branchName,
			@RequestParam int themaId, @RequestParam(name = "rvId") int rvId) {
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));

		// 세션등록 ==end==
		Branch branch = branchRepository.findByName(branchName);
		Optional<Thema> Othema = themaRepository.findById(themaId);
		Thema thema = Othema.get();

		model.addAttribute("branch", branch);
		model.addAttribute("thema", thema);

		if (session.getAttribute("Id") == null) {
			pageData.setMsg("로그인이 필요합니다.");
			pageData.setGoUrl("/user/login");
			return "inc/alert";
		}

		int id = (int) session.getAttribute("Id");
		Optional<User> Ouser = userRepository.findById(id);
		User user = Ouser.get();
		model.addAttribute("user", user);
		List<Branch> branches = branchRepository.findAll();
		List<Thema> themas = themaRepository.findAll();

		model.addAttribute("id", session.getAttribute("Id")); // 지점Id 정보를 템플릿으로 전달
		model.addAttribute("branches", branches); // 매장 정보를 템플릿으로 전달
		model.addAttribute("themas", themas);
		model.addAttribute("rvId", rvId);
		model.addAttribute("themaId", themaId);
		model.addAttribute("branchName", branchName);
		model.addAttribute("username", session.getAttribute("username"));
		System.out.println("후기 글쓰기>>> ");
		return "board_review/review_write";
	}

	// 후기 글쓰기 post
	@PostMapping("review/write")
	public String writeReg(@Valid ReviewBoard br, BindingResult bindingResult, HttpSession session, Model model,
			HttpServletRequest request, @RequestParam String branchName,@RequestParam String themaId, @RequestParam String id, @RequestParam String rvId) {
	
		if (bindingResult.hasErrors()) {
			Branch branch = branchRepository.findByName(branchName);
			Optional<Thema> Othema = themaRepository.findById(Integer.parseInt(themaId));
			Thema thema = Othema.get();
			Optional<User> Ouser = userRepository.findById(Integer.parseInt(id));
			User user = Ouser.get();
			model.addAttribute("user", user);
			model.addAttribute("branch", branch);
			model.addAttribute("thema", thema);
			model.addAttribute("rvId", rvId);
			model.addAttribute("themaId", themaId);
			model.addAttribute("branchName", branchName);
			// 세션등록 ==start==
			model.addAttribute("userRole", session.getAttribute("userRole"));
			model.addAttribute("admin", session.getAttribute("admin"));
			model.addAttribute("adminBn", session.getAttribute("adminBn"));
			model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));

			// 세션등록 ==end==
			System.out.println(bindingResult.getAllErrors());
			System.out.println(bindingResult.getFieldError().getDefaultMessage());
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("titleerror", bindingResult.getFieldError("title"));
			model.addAttribute("contenterror", bindingResult.getFieldError("content"));
			model.addAttribute("escTimeerror", bindingResult.getFieldError("content"));
			return "board_review/review_write";
		}
		Branch branch = branchRepository.findByName(branchName);
		
		Optional<Thema> Othema = themaRepository.findById(Integer.parseInt(themaId));
		Thema thema = Othema.get();
		System.out.println("여기가 문제니3");
		Optional<User> Ouser = userRepository.findById(Integer.parseInt(id));
		User user = Ouser.get();
		br.setBranch(branch);
		br.setThema(thema);
		br.setUser(user);
		br.setRegDate(LocalDateTime.now());
		System.out.println("여기가 문제니4");
		MultipartFile file = br.getFile();
		String pathR = request.getServletContext().getRealPath("/up/boardR");
		fileSave(br, request);
		System.out.println("여기가 br => " + br);

		Reservation rv = reservationRepository.findById(Integer.parseInt(rvId))
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
		rv.setReviewYN(true);
		reservationRepository.save(rv);
		boardRRepository.save(br);
		return "redirect:/board/review/list";
	}

	void fileSave(ReviewBoard br, HttpServletRequest request) {

		// 파일 업로드 유무 확인
		if (br.getFile().isEmpty()) {

			return;
		}

		String path = request.getServletContext().getRealPath("up/boardR");
		System.out.println("자동경로찾기 =>" + path);
		// path = "C:\\1조proj\\Supul\\supul\\src\\main\\webapp\\up\\thema";

		int dot = br.getFile().getOriginalFilename().lastIndexOf(".");
		String fDomain = br.getFile().getOriginalFilename().substring(0, dot);
		String ext = br.getFile().getOriginalFilename().substring(dot);

		br.setFileName(fDomain + ext);
		File ff = new File(path + "\\" + br.getFileName());
		int cnt = 1;
		while (ff.exists()) {

			br.setFileName(fDomain + "_" + cnt + ext);
			ff = new File(path + "\\" + br.getFileName());
			cnt++;
		}

		try {
			FileOutputStream fos = new FileOutputStream(ff);

			fos.write(br.getFile().getBytes());

			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 후기 상세
	@Transactional
	@GetMapping("review/detail/{id}")
	public String reviewDetail(@PathVariable int id, Model model, HttpSession session) {
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));

		// 세션등록 ==end==
		Optional<ReviewBoard> rboard = boardRRepository.findById(id);

		if (session.getAttribute("Id") != null) {
			int userId = (int) session.getAttribute("Id");
			Optional<User> Ouser = userRepository.findById(userId);
			User user = Ouser.get();
			model.addAttribute("username", session.getAttribute("username"));
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("user", user);

			System.out.println(userId);
		}
		if (rboard.isPresent()) {
			ReviewBoard board = rboard.get();

			// 게시물을 조회할 때마다 cnt 값을 증가시킴
			board.setCnt(board.getCnt() + 1);

			// 데이터베이스에 cnt 값을 업데이트
			boardRRepository.save(board);

			model.addAttribute("rDetail", board);
			System.out.println("후기 상세페이지>>> ");
			return "board_review/review_detail";

		} else {
			return "redirect:/board/review/list";
		}
	}

	// 후기 삭제
	@GetMapping("review/delete/{id}")
	public String reviewDelete(@PathVariable int id, HttpServletRequest request, HttpSession session, Model model) {
		// 게시물 삭제 시 해당 파일도 삭제
		Optional<ReviewBoard> rboard = boardRRepository.findById(id);

		if (rboard.isPresent()) {
			ReviewBoard board = rboard.get();

			// 파일 삭제
			String fileName = board.getFileName();
			if (fileName != null) {
				String pathR = request.getServletContext().getRealPath("/up/boardR");
				service.deleteFile(fileName, pathR, request);
			}
			// 세션등록 ==start==
			model.addAttribute("userRole", session.getAttribute("userRole"));
			model.addAttribute("admin", session.getAttribute("admin"));
			model.addAttribute("adminBn", session.getAttribute("adminBn"));
			model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
			model.addAttribute("user", (User) session.getAttribute("user"));
			// 세션등록 ==end==

			boardRRepository.deleteById(id);
			System.out.println("후기 삭제>>> ");
		}
		return "redirect:/board/review/list";
	}

	// 후기 수정 get
	@GetMapping("review/modify/{id}")
	public String reviewModify(@PathVariable int id, Model model, HttpSession session) {
		Optional<ReviewBoard> rboard = boardRRepository.findById(id);
		List<User> users = userRepository.findAll();
		List<Branch> branchs = branchRepository.findAll();

		
		model.addAttribute("username", session.getAttribute("username"));
		ReviewBoard board = rboard.get();
		
		model.addAttribute("users", users);
		model.addAttribute("branchs", branchs);
		model.addAttribute("rModify", board);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==
		System.out.println("후기 수정>>> ");
		return "board_review/review_modify";

	}

	// 후기 수정 post
	@PostMapping("review/modify/{id}")
	public String reviewModifyOrDelete(@PathVariable int id, @RequestParam("action") String action, // action 파라미터 추가
			@Valid ReviewBoard bd, BindingResult bindingResult, Model model, HttpSession session, HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {
		
		if (bindingResult.hasErrors()) {
			Optional<ReviewBoard> rboard = boardRRepository.findById(id);
			List<User> users = userRepository.findAll();
			List<Branch> branchs = branchRepository.findAll();
			if (rboard.isPresent()) {
			ReviewBoard board = rboard.get();
			model.addAttribute("rModify", board);
				
			}
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("titleerror", bindingResult.getFieldError("title"));
			model.addAttribute("contenterror", bindingResult.getFieldError("content"));			
			model.addAttribute("escTimeerror", bindingResult.getFieldError("escTime"));			
			model.addAttribute("username", session.getAttribute("username"));
		
			
			model.addAttribute("users", users);
			model.addAttribute("branchs", branchs);
			
			// 세션등록 ==start==
			model.addAttribute("userRole", session.getAttribute("userRole"));
			model.addAttribute("admin", session.getAttribute("admin"));
			model.addAttribute("adminBn", session.getAttribute("adminBn"));
			model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
			model.addAttribute("user", session.getAttribute("user"));
			// 세션등록 ==end==
			System.out.println("후기 수정>>> ");
			return "board_review/review_modify";
		
		}
		Optional<ReviewBoard> rboard = boardRRepository.findById(id);

		if (rboard.isPresent()) {
			ReviewBoard newboard = rboard.get();

			if ("수정".equals(action)) {
				newboard.setContent(bd.getContent());
				newboard.setTitle(bd.getTitle());
				newboard.setGrade(bd.getGrade());
				newboard.setClear(bd.isClear());
				newboard.setModiDate(LocalDateTime.now());

				if (!file.isEmpty()) {
					// 파일이 업로드되었을 때만 파일 업로드 및 정보 업데이트
					String pathR = request.getServletContext().getRealPath("/up/boardR");
					service.uploadAndSave(newboard, pathR, file, request);
				}

				boardRRepository.save(newboard);

				return "redirect:/board/review/detail/" + id; // 수정 후 상세 페이지로 리디렉션
			} else if ("삭제".equals(action)) {
				// 파일 삭제
				String pathR = request.getServletContext().getRealPath("/up/boardR");
				String fileName = newboard.getFileName();
				if (fileName != null) {
					service.deleteFile(fileName, pathR, request);
				}

				boardRRepository.delete(newboard);
				return "redirect:/board/review/list"; // 삭제 후 목록 페이지로 리디렉션
			}
		}
		return "redirect:/board/review/detail/" + id; // 수정 및 삭제 실패 시 상세 페이지로 리디렉션
	}

	// 게시물 작성 폼을 불러오는 핸들러
	@GetMapping("qna/write")
	public String showCreateForm(HttpSession session, Model model) {

		List<Branch> branchlist = branchRepository.findAll();
		model.addAttribute("qnaBoard", new QnaBoard());
		model.addAttribute("branchlist", branchlist);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==
		return "board_Qna/qna_write";
	}

	// 게시물 작성 처리를 위한 핸들러
	@PostMapping("qna/write")
	public String createQnaBoard(@Valid @ModelAttribute("qnaBoard") QnaBoard qnaBoard, BindingResult result,
			HttpSession session, Model model, String branchId) {
		if (result.hasErrors()) {
			// 세션등록 ==start==
			model.addAttribute("userRole", session.getAttribute("userRole"));
			model.addAttribute("admin", session.getAttribute("admin"));
			model.addAttribute("adminBn", session.getAttribute("adminBn"));
			model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
			List<Branch> branchlist = branchRepository.findAll();
			model.addAttribute("branchlist", branchlist);
			// 세션등록 ==end==
			System.out.println(result.getAllErrors());
			System.out.println(result.getFieldError().getDefaultMessage());
			model.addAttribute("errors", result.getAllErrors());
			model.addAttribute("titleerror", result.getFieldError("title"));
			model.addAttribute("contenterror", result.getFieldError("content"));
			return "board_Qna/qna_write";
		}
		Branch branch = branchRepository.findById(Integer.parseInt(branchId))
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
		User user = (User) session.getAttribute("user");
		qnaBoard.setBranch(branch);
		qnaBoard.setUser(user);
		qnaBoard.setRegDate(LocalDateTime.now());

		System.out.println(qnaBoard);
		boardQRepository.save(qnaBoard);
		return "redirect:/board/qna/list";
	}

	@GetMapping("qna/list")
	public String questionList(HttpSession session, Model model,
			@RequestParam(name = "sortBy", defaultValue = "regDate") String sortBy,
			@RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
			@PageableDefault(size = 15, sort = "regDate", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "type", defaultValue = "") String type, // 검색 유형 (content, title, user)
			@RequestParam(name = "keyword", defaultValue = "") String keyword)// 검색어) {
	{
		Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		// Spring Data JPA를 사용하여 페이징 처리된 공지사항 목록을 가져옴
		Page<QnaBoard> QnaList;

		if (!keyword.isEmpty()) {
			// 선택한 검색 조건에 따라 검색 메서드 호출
			if ("title".equals(type)) {
				QnaList = boardQRepository.findByTitleContaining(keyword, pageable);
			} else if ("content".equals(type)) {
				QnaList = boardQRepository.findByContentContaining(keyword, pageable);
			} else if ("user".equals(type)) {
				QnaList = boardQRepository.findByUserUserIdContaining(keyword, pageable);
			} else if ("titleAndContent".equals(type)) {
				QnaList = boardQRepository.findByContentContainingOrTitleContaining(keyword, keyword, pageable);
			} else {
				QnaList = boardQRepository.findAll(pageable);
			}
		} else {
			QnaList = boardQRepository.findAll(pageable);
		}
		model.addAttribute("qnaComment", new QnaComment());
		model.addAttribute("QnaList", QnaList);
		model.addAttribute("type", type);
		model.addAttribute("keyword", keyword);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==

		return "board_Qna/qna_list";
	}

	@GetMapping("qna/detail/{qnaId}")
	public String showQnaDetail(@PathVariable int qnaId, HttpSession session, Model model) {

		QnaBoard qnaBoard = boardQRepository.findById(qnaId)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

		System.out.println(qnaBoard);
		model.addAttribute("qnaBoard", qnaBoard);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", (User) session.getAttribute("user"));
		// 세션등록 ==end==

		return "board_Qna/qnaDetail";
	}

	@PostMapping("qna/comment")
	public String addComment(HttpSession session, Model model, @ModelAttribute QnaComment qnaComment, int qnaId) {

		QnaBoard qnaBoard = boardQRepository.findById(qnaId)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
		qnaComment.setRegDate(LocalDateTime.now());
		qnaComment.setQna(qnaBoard);
		if (session.getAttribute("admin") != null) {
			Branch branch = branchRepository.findById((int) session.getAttribute("adminBranchId"))
					.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
			Admin admin = (Admin) session.getAttribute("admin");
			qnaComment.setWriter(admin.getAdminId());
			qnaComment.setBranch(branch);
		}
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			qnaComment.setWriter(user.getUserId());
		}
		if (session.getAttribute("userRole") != null) {

			qnaComment.setWriter("총괄");
		}

		commQRepository.save(qnaComment);

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==

		return "redirect:/board/qna/detail/" + qnaComment.getQna().getQnaId(); // 댓글 작성 후 상세 페이지로 리다이렉트
	}

	// 댓글 수정 폼으로 이동
	@GetMapping("qna/comment/{commentId}/modify")
	public String modifyCommentForm(@PathVariable int commentId, Model model) {
		QnaComment qnaComment = commQRepository.findById(commentId)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

		model.addAttribute("qnaComment", qnaComment);
		return "board_Qna/qna_comment"; // 댓글 수정 폼 템플릿
	}

	// 댓글 수정 처리
	@PostMapping("qna/comment/{commentId}/modify")
	public String modifyCommentReg(@PathVariable int commentId, @ModelAttribute QnaComment editedComment) {
		QnaComment qnaComment = commQRepository.findById(commentId)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
		// 수정된 내용으로 댓글 업데이트
		qnaComment.setContent(editedComment.getContent());
		commQRepository.save(qnaComment);
		return "redirect:/board/qna/detail/" + qnaComment.getQna().getQnaId();
	}

	// 댓글 삭제 처리
	@PostMapping("qna/comment/{commentId}/delete")
	public String deleteComment(@PathVariable int commentId) {
		QnaComment qnaComment = commQRepository.findById(commentId)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
		commQRepository.delete(qnaComment);
		return "redirect:/board/qna/detail/" + qnaComment.getQna().getQnaId();
	}

	// 문의 수정
	@GetMapping("qna/modify/{qnaId}")
	public String modifyForm(@PathVariable("qnaId") int qnaId, QnaBoard newqnaBoard, HttpSession session, Model model) {
	    QnaBoard qnaBoard = boardQRepository.findById(qnaId)
	            .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

	    // 세션에 branchlist가 없는 경우에만 추가
	    if (session.getAttribute("branchlist") == null) {
	        List<Branch> branchlist = branchRepository.findAll();
	        session.setAttribute("branchlist", branchlist);
	        
	    }

	    model.addAttribute("branchlist", session.getAttribute("branchlist"));
	    model.addAttribute("qnaBoard", qnaBoard);

	    // 세션등록 ==start==
	    model.addAttribute("userRole", session.getAttribute("userRole"));
	    model.addAttribute("admin", session.getAttribute("admin"));
	    model.addAttribute("adminBn", session.getAttribute("adminBn"));
	    model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
	    model.addAttribute("user", session.getAttribute("user"));
	    // 세션등록 ==end==
	    return "board_Qna/qna_modify"; // 수정 폼 템플릿의 경로를 반환합니다.
	}

	// 문의 수정을 처리하는 메서드
	@PostMapping("qna/modify/{qnaId}")
	public String modifyReg(@Valid @ModelAttribute("newqnaBoard") QnaBoard newqnaBoard, BindingResult result,
	        HttpSession session, String branchId, Model model,@PathVariable("qnaId") int qnaId) {
	    // 유효성 검사 결과를 확인하고 오류가 있을 경우 수정 폼으로 이동
	    if (result.hasErrors()) {
	    	   List<Branch> branchlist = branchRepository.findAll();
	    	   model.addAttribute("branchlist",branchlist);
	        // 오류가 있을 때 필요한 처리를 수행하세요.
	        // 예를 들어 오류 메시지를 모델에 추가하여 화면에 표시할 수 있습니다.
	    	 QnaBoard qnaBoard = boardQRepository.findById(qnaId)
	 	            .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
	   
	        model.addAttribute("qnaBoard", qnaBoard);
	        // 세션 정보 등을 모델에 추가
	        model.addAttribute("userRole", session.getAttribute("userRole"));
	        model.addAttribute("admin", session.getAttribute("admin"));
	        model.addAttribute("adminBn", session.getAttribute("adminBn"));
	        model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
	        model.addAttribute("user", session.getAttribute("user"));

	        model.addAttribute("errors", result.getAllErrors());
	        model.addAttribute("titleerror", result.getFieldError("title"));
	        model.addAttribute("contenterror", result.getFieldError("content"));

	        // 수정 폼으로 이동
	        return "board_Qna/qna_modify";
	    }

	    // 여기에서 게시물을 수정하는 로직을 구현하세요.
	    // 예를 들어, QnaBoardService를 사용하여 게시물을 업데이트할 수 있습니다.
	    Branch branch = branchRepository.findById(Integer.parseInt(branchId))
	            .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
	    User user = (User) session.getAttribute("user");

	    newqnaBoard.setBranch(branch);
	    newqnaBoard.setUser(user);
	 
	    newqnaBoard.setModiDate(LocalDateTime.now());
	    boardQRepository.save(newqnaBoard);

	    return "redirect:/board/qna/detail/" + newqnaBoard.getQnaId(); // 수정 후 상세 페이지로 리다이렉트합니다.
	}

	// 문의게시물 삭제를 위한 GET 메서드
	@GetMapping("qna/delete/{qnaId}")
	public String deleteQnaBoard(@PathVariable("qnaId") int qnaId, RedirectAttributes redirectAttributes) {
		boolean isDeleted = service.deleteQnaBoard(qnaId);

		if (isDeleted) {
			redirectAttributes.addFlashAttribute("successMessage", "문의게시물이 삭제되었습니다.");
			return "redirect:/board/qna/list";
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "문의게시물 삭제에 실패했습니다.");
			return "redirect:/error";
		}
	}

	// 문의 삭제
	@PostMapping("qna/delete/{qnaId}")
	public String deleteQnaBoard(@PathVariable("qnaId") int qnaId) {
		boolean isDeleted = service.deleteQnaBoard(qnaId);

		if (isDeleted) {
			return "redirect:/board/qna/list";
		} else {
			return "redirect:/error";
		}
	}

}