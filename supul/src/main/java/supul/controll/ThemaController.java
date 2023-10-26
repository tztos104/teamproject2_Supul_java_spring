package supul.controll;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import supul.mapper.BranchMapper;
import supul.mapper.ReserveMapper;
import supul.mapper.ThemaMapper;
import supul.model.Branch;
import supul.model.PageData;
import supul.model.Ranking;
import supul.model.Reservation;
import supul.model.Reservation_backup;
import supul.model.Thema;
import supul.model.User;
import supul.model.WishList;
import supul.model.board.ReviewBoard;
import supul.repository.BranchRepository;
import supul.repository.RankingRepository;
import supul.repository.ReservationBackUpRepository;
import supul.repository.ReservationRepository;
import supul.repository.ThemaRepository;
import supul.repository.UserRepository;
import supul.repository.WishListRepository;
import supul.repository.board.ReviewRepository;
import supul.service.SaleService;

@Controller
@RequestMapping("/thema")
public class ThemaController {

	@Autowired
	ThemaMapper themaMapper;
	@Autowired
	ThemaRepository themaRepository;
	@Autowired
	BranchMapper branchMapper;
	@Autowired
	private SaleService stat;
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	ReservationBackUpRepository reservationBackUpRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BranchRepository branchRepository;
	@Autowired
	RankingRepository rankingRepository;
	@Autowired
	ReviewRepository reviewRepository;

	@Autowired
	WishListRepository wishListRepository;
	PageData pageData;

	@GetMapping("intro")
	public String introduceForm(Model model, Branch bd,
			@RequestParam(required = false, defaultValue = "전체") String branchName,

			@RequestParam(name = "sortBy", defaultValue = "themaId") String sortBy,
			@RequestParam(value = "date", required = false) LocalDate date, HttpSession session,
			@RequestParam(required = false) String title,
			@PageableDefault(size = 40, sort = "date", direction = Direction.DESC) Pageable pageable, Reservation rv) {

		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.DESC, sortBy));

		if (date == null) {
			date = LocalDate.now();
		}
		if (session.getAttribute("Id") != null) {
			int id = (int) session.getAttribute("Id");
			User user = null;
			Optional<User> Ouser = userRepository.findById(id);
			if (Ouser.isPresent()) {
				user = Ouser.get();
			} else {
				System.out.println("널값이야");
			}

			model.addAttribute("username", session.getAttribute("username"));
			model.addAttribute("user", user);
		}

		// 테마 데이터, 매장 데이터
		Page<Thema> themaList;

		if (branchName.equals("전체") || branchName == null) {

			if (title == null || title.isEmpty() || title.equals("null")) {
				themaList = themaRepository.findAll(pageable);
				
			} else {
				themaList = themaRepository.findAllByTitleContaining(title, pageable);
				
			}
		} else {
			if (title == null || title.isEmpty() || title.equals("null")) {
				// 사용자 이름과 지점으로 검색하고 페이징을 적용합니다.
				themaList = themaRepository.findByBranchName(branchName, pageable);
				
			} else {
				themaList = themaRepository.findByBranchNameAndTitleContaining(branchName, title, pageable);
			}
		}
		// 테마별 링크 재정렬
		for (Thema thema : themaList.getContent()) {
			List<Ranking> ranking = thema.getRanking();
			ranking.sort(Comparator.comparing(Ranking::getThemaRank).reversed());
		}

		// 테마별 리뷰 재정렬
		for (Thema thema : themaList.getContent()) {
			List<ReviewBoard> reviews = thema.getReview();
			reviews.sort(Comparator.comparing(ReviewBoard::getRegDate).reversed());
		}

		List<Branch> brnList = branchRepository.findAll();
		// 세션 등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션 등록 ==end==

		
		model.addAttribute("branchName", branchName);
		model.addAttribute("title", title);
		model.addAttribute("date", date);
		model.addAttribute("themaList", themaList);

		model.addAttribute("brnList", brnList);
		

		return "thema/introForm";
	}

	@PostMapping("/addWishList")
	public String addWishList(int themaId, HttpSession session, Model model) {
		int id = (int) session.getAttribute("Id");

		Optional<User> Ouser = userRepository.findById(id);
		User user = Ouser.get();

		Optional<Thema> Othema = themaRepository.findById(themaId);
		Thema thema = new Thema();
		if (Othema.isPresent()) {
			thema = Othema.get();
		} else {
			System.out.println("널값이야");
		}

		WishList wishList = new WishList();
		wishList.setThema(thema);
		wishList.setUser(user);
		wishListRepository.save(wishList);

		return "redirect:/thema/intro#thema" + themaId; // 사용자를 이전 페이지로 리디렉션하거나 다른 페이지로 이동
	}

	@PostMapping("/minusWishList")
	public String minusWishList(int themaId, HttpSession session, Model model) {
		int id = (int) session.getAttribute("Id");
		model.addAttribute("user", session.getAttribute("user"));
		Optional<User> Ouser = userRepository.findById(id);
		User user = Ouser.get();
		Optional<Thema> Othema = themaRepository.findById(themaId);
		Thema thema = new Thema();
		if (Othema.isPresent()) {
			thema = Othema.get();
		} else {
			System.out.println("널값이야");
		}

		WishList a = wishListRepository.findByThemaAndUser(thema, user);
		System.out.println(a);
		wishListRepository.deleteById(a.getId());

		return "redirect:/thema/intro#thema" + themaId;
	}

	// ================== 테마 관련 ===================//

	@GetMapping("/themaList")
	public String listThemas(@RequestParam(name = "branchName", required = false) String branchName,
			@RequestParam(name = "page", defaultValue = "0") int page, // 현재 페이지
			@RequestParam(name = "size", defaultValue = "10") int size, // 페이지당 아이템 수
			Model model, HttpSession session) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "themaId"));

		List<Thema> themas;
		Page<Thema> themaPage;
		List<Branch> branchList = branchRepository.findAll();

		if (branchName != null) {
			// 선택한 지점에 따라 테마를 필터링합니다.

			Branch branch = branchRepository.findByName(branchName);

			themas = themaRepository.findByBranch(branch);
			// 페이지 객체를 다시 설정합니다.
			themaPage = new PageImpl<>(themas, pageable, themas.size());
		} else {
			// 지점이 선택되지 않은 경우 기존 코드와 동일하게 처리합니다.
			themaPage = themaRepository.findAll(pageable);

		}

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		model.addAttribute("branchName", branchName); // Page 객체로 뷰에 전달
		model.addAttribute("page", themaPage); // Page 객체로 뷰에 전달
		model.addAttribute("branchList", branchList);
		model.addAttribute("themas", themaPage);
		// 선택한 지점 ID를 뷰로 전달

		return "thema/themaList";
	}

	@RequestMapping("/detail/{id}")
	String detail(Model model, @PathVariable int id, PageData pageData, HttpSession session) {

		Optional<Thema> Othema = themaRepository.findById(id);

		if (Othema.isPresent()) {
			Thema thema = Othema.get();
			model.addAttribute("thema", thema);
		} else {

		}
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		model.addAttribute("branchList", branchMapper.selectList());
		model.addAttribute("timeTable", themaMapper.timetablebyId(id));
		model.addAttribute("pageData", pageData);
		return "thema/themaDetail";
	}

	@GetMapping("/form")
	public String ThemaForm(Model model, HttpSession session) {
		model.addAttribute("thema", new Thema());
		model.addAttribute("branchList", branchMapper.selectList());
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==
		return "thema/themaForm";
	}

	@PostMapping("/reg")
	public String submitThema(@Valid @ModelAttribute Thema thema, BindingResult br, Model model,
			HttpServletRequest request, String[] time, HttpSession session, String branchId) {
		model.addAttribute("thema", new Thema());
		model.addAttribute("branchList", branchMapper.selectList());

		if (br.hasErrors()) {
			System.out.println(br.getFieldError("price").getDefaultMessage());
			model.addAttribute("titleerror", br.getFieldError("title"));
			model.addAttribute("contenterror", br.getFieldError("content"));
			model.addAttribute("playTimeerror", br.getFieldError("playTime"));
			model.addAttribute("priceerror", br.getFieldError("price"));
			model.addAttribute("rvPriceerror", br.getFieldError("rvPrice"));
			return "thema/themaForm";
		}
//		System.out.println("이미지냐?"+thema.isImg());
//		if(thema.isImg()==false) { //pw1과 pw2가 일치하지 않으면
//			br.rejectValue("poster", null, "이미지만 가능합니다!");
//			return "thema/themaForm";
//		}

		Branch branch = branchMapper.selectById(Integer.parseInt(branchId));
		System.out.println(branch);
		fileSave(thema, request);
		thema.setBranch(branch);
		themaRepository.save(thema);

		System.out.println("이건테마" + thema);

		// fileSave(thema,request);
		for (String a : time) {
			LocalTime localTime = LocalTime.parse(a, DateTimeFormatter.ofPattern("HH:mm"));
			themaMapper.insertTimetable(themaMapper.timeId(), localTime);
		}

		return "redirect:/thema/themaList";
	}

	@GetMapping("/update/{themaId}")
	public String showThemaUpdateForm(@PathVariable("themaId") int themaId, Model model, HttpSession session) {
		// Thema thema = themaMapper.selectById(themaId);
		Optional<Thema> Othema = themaRepository.findById(themaId);
		System.out.println(Othema);
		if (Othema.isPresent()) {
			Thema thema = Othema.get();
			model.addAttribute("thema", thema);
		} else {

		}

		model.addAttribute("branchList", branchMapper.selectList());

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==

		return "thema/themaUpdate";
	}

	@PostMapping("/update")
	public String submitThemaUpdate(@ModelAttribute Thema thema, String[] time, HttpSession session,
			HttpServletRequest request, PageData pageData) {
		System.out.println("테마야" + time);
		List<LocalTime> tt = new ArrayList<>();
		for (String a : time) {
			LocalTime localTime = LocalTime.parse(a, DateTimeFormatter.ofPattern("HH:mm"));
			tt.add(localTime);
		}

		int cnt = themaMapper.idPwChk(thema);
		System.out.println("cnt : " + cnt);

		if (cnt > 0) {
			if (thema.getPoster() == null) {
				// 새파일을 업로드하는 함수
				fileSave(thema, request);
			}
			// 수정한 내용 db에 저장
			thema.setTimetable(tt);
			themaRepository.save(thema);

			String branchName = thema.getBranch().getName();
			pageData.setMsg("수정되었습니다.");
			pageData.setGoUrl("/thema/themaList?branchName=" + branchName);
		}

		// int branchId = (int) session.getAttribute("adminBranchId");

		// return "redirect:/thema/themaList";
		return "inc/alert";
	}

	@GetMapping("/delete/{themaId}")
	public String showThemaDeleteForm(@PathVariable("themaId") int themaId, Model model, HttpSession session) {
		Thema thema = themaMapper.selectById(themaId);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==
		System.out.println("딜리트겟" + thema);
		model.addAttribute("thema", thema);
		return "thema/themaDelete";
	}

	// 테마 정보 삭제 처리
	@PostMapping("/delete/{themaId}")
	public String submitThemaDelete(@PathVariable("themaId") int themaId, HttpServletRequest request,
			PageData pageData) {

		Thema delThema = themaMapper.detail(themaId);
		System.out.println("매퍼로한 테마 너도 오니?" + delThema);
		Optional<Thema> oThema = themaRepository.findById(themaId);
		Thema a = oThema.get();
		a.getSale();

		System.out.println("테마왔냐?" + oThema.get());
		List<Reservation> themaRvs = reservationRepository.findByThemaThemaId(a.getThemaId());

		if (themaRvs != null) {
			for (Reservation rv : themaRvs) {
				Reservation_backup bvBack = new Reservation_backup();
				bvBack.setBranchName(rv.getBranch().getName());
				bvBack.setDate(rv.getDate());
				bvBack.setNoShow(rv.isNoShow());
				bvBack.setPaid(rv.isPaid());
				bvBack.setThemaName(rv.getThemaName());
				bvBack.setRvPrice(rv.getRvPrice());
				bvBack.setPrice(rv.getPrice());
				bvBack.setRvNum(rv.getRvNum());
				bvBack.setUserName(rv.getUserName());
				if (rv.getRvpay() != null) {
					bvBack.setRvpay_id(rv.getRvpay().getRvPay_id());
					bvBack.setImp_uid(rv.getRvpay().getImp_uid());

				}
				bvBack.setUser_id(rv.getUser().getUserId());
				reservationBackUpRepository.save(bvBack);
				reservationRepository.delete(rv);

			}
			themaRepository.delete(a);
			pageData.setMsg("예약중인 내역도 삭제되었습니다.");
			pageData.setGoUrl("/thema/themaList");
			return "inc/alert";

		}
		themaRepository.delete(a);
		System.out.println("테마연결된 예약확인:" + themaRvs);

		return "redirect:/thema/themaList";
	}

	@PostMapping("fileDelete/{themaId}")
	// 게시판 글에 첨부된 파일을 삭제하는 동작을 처리
	String fileDelete(@PathVariable("themaId") int themaId, HttpServletRequest request, PageData pageData,
			Model model) {

		Thema delThema = themaMapper.detail(themaId);
		pageData.setMsg("파일 삭제실패");
		// 실패시 갈 경로 설정
		pageData.setGoUrl("/thema/update/" + themaId);
		int cnt = themaMapper.fileDelete(themaId);
		System.out.println("modifyReg:" + cnt);
		if (cnt > 0) {
			fileDeleteModule(delThema, request);
			pageData.setMsg("파일 삭제되었습니다.");
		}
		model.addAttribute("pageData", pageData);

		return "inc/alert";
	}

	// ============파일 관리,저장 및 삭제 ============
	void fileSave(Thema thema, HttpServletRequest request) {

		// 파일 업로드 유무 확인
		if (thema.getMmff().isEmpty()) {

			return;
		}

		String path = request.getServletContext().getRealPath("up/thema");
		System.out.println("자동경로찾기 =>" + path);
		// path = "C:\\1조proj\\Supul\\supul\\src\\main\\webapp\\up\\thema";

		int dot = thema.getMmff().getOriginalFilename().lastIndexOf(".");
		String fDomain = thema.getMmff().getOriginalFilename().substring(0, dot);
		String ext = thema.getMmff().getOriginalFilename().substring(dot);

		thema.setPoster(fDomain + ext);
		File ff = new File(path + "\\" + thema.getPoster());
		int cnt = 1;
		while (ff.exists()) {

			thema.setPoster(fDomain + "_" + cnt + ext);
			ff = new File(path + "\\" + thema.getPoster());
			cnt++;
		}

		try {
			FileOutputStream fos = new FileOutputStream(ff);

			fos.write(thema.getMmff().getBytes());

			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void fileDeleteModule(Thema delThema, HttpServletRequest request) {
		if (delThema.getPoster() != null) {
			String path = request.getServletContext().getRealPath("up/thema");

			new File(path + "\\" + delThema.getPoster()).delete();
		}
	}

	@RequestMapping("com")
	String complite() {

		return "inc/alert";
	}

}
