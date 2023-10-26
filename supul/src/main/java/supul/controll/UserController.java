package supul.controll;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import supul.mapper.ThemaMapper;
import supul.mapper.UserMapper;
import supul.model.Branch;
import supul.model.Coupon;
import supul.model.DeleteUser;
import supul.model.PageData;
import supul.model.Ranking;
import supul.model.Reservation;

import supul.model.Thema;
import supul.model.User;
import supul.model.WishList;
import supul.model.board.ReviewBoard;
import supul.repository.CouponRepository;
import supul.repository.DeleteUserRepository;
import supul.repository.RankingRepository;

import supul.repository.ReservationRepository;
import supul.repository.ThemaRepository;
import supul.repository.UserRepository;
import supul.repository.WishListRepository;
import supul.repository.board.ReviewRepository;
import supul.service.EmailService;
import supul.service.PasswordChange;

@Controller
@RequestMapping("user")
public class UserController {

	@Resource
	UserMapper usermapper;
	@Resource
	ThemaMapper tmMapper;
	@Autowired
	ReviewRepository rBoardRepository;
	@Autowired
	ThemaRepository themaRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	WishListRepository wishRepository;
	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	RankingRepository rankingRepository;
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	DeleteUserRepository deleteUserRepository;

	@Autowired
	private Coupon welcomeCoupon; // 쿠폰빈

	@Resource
	EmailService emailService; // EmailService 주입

	// ============== 정현수정 ===============
	// 매일 정각에 실행되도록 스케줄링
	@Scheduled(cron = "0 0 0 * * *")
	public void checkAndDeleteExpiredCoupons() {
		LocalDateTime currentDate = LocalDateTime.now().minusMonths(3); // 현재 날짜에서 3개월을 뺍니다.
		List<Coupon> expiredCoupons = couponRepository.findByRegDateBeforeAndUsedIsFalse(currentDate);
		int deletedCount = 0; // 삭제된 쿠폰의 개수를 세기 위한 변수

		for (Coupon coupon : expiredCoupons) {
			String couponName = coupon.getCouponName(); // 쿠폰의 이름 가져오기
			couponRepository.delete(coupon);
			deletedCount++; // 각 쿠폰을 삭제할 때마다 개수를 증가
			System.out.println(couponName + " 쿠폰이 삭제되었습니다.");

		}

		System.out.println(deletedCount + "개의 쿠폰이 삭제되었습니다.");
	}

	// ============== 정현수정 끝 ===============
	// =======회원가입 시작==================================//
	@GetMapping("signup")
	public String showSignupForm(Model model, HttpSession session) {
		model.addAttribute("member", new User());
		String username = (String) session.getAttribute("username");
		model.addAttribute("username", username);
		return "user/signup";
	}

	@PostMapping("signup")
	public String processSignupForm(HttpSession session, @Valid @ModelAttribute("member") User user, BindingResult br,
			@RequestParam("userPw1") String userPw1, @RequestParam("emailDomain") String emailDomain)
			throws MessagingException, IOException {
		if (br.hasErrors()) {
			return "user/signup";
		}
		if (!user.getUserPw().equals(userPw1)) {
			br.rejectValue("userPw", null, "비밀번호가 일치하지 않습니다.");
			return "user/signup";
		}

		// 이메일 주소와 도메인을 합쳐서 전체 이메일 주소 생성
		String fullEmail = user.getEmail() + emailDomain;
		user.setEmail(fullEmail);

		userRepository.save(user);
		emailService.sendMail(fullEmail);

		// ============== 정현수정 ===============

		String user_id = user.getUserId();
		User Ouser = userRepository.findByUserId(user_id);

		Coupon coupon = new Coupon();
		coupon.setCouponName("가입환영쿠폰");

		Random ran = new Random();
		while (true) {
			int i = ran.nextInt(1, 100000000);

			String cpnum = String.format("%08d", i);
			// 데이터베이스에서 해당 쿠폰 번호가 이미 존재하는지 확인
			boolean isDuplicate = couponRepository.existsByCouponNum(cpnum);
			if (!isDuplicate) {
				welcomeCoupon.setCouponNum(cpnum);
				break;
			}
		}

		welcomeCoupon.setUser(Ouser);
		couponRepository.save(welcomeCoupon);

		// ============== 정현수정 ===============

		return "redirect:/main";
	}

	@GetMapping("/signuppopup")
	public String popup() {
		return "user/signupPopup";
	}

	@GetMapping("/privacy")
	public String privacy() {
		return "inc/privacy";
	}

	@GetMapping("/check-username")
	@ResponseBody
	public boolean checkUsernameAvailability(@RequestParam String username) {
		// 사용자 ID 중복 확인
		User user = userRepository.findByUserId(username);
		return user == null; // 중복되지 않으면 true, 중복되면 false 반환
	}

	// 이메일 중복 확인 컨트롤러 메서드
	@GetMapping("/check-email")
	@ResponseBody
	public boolean checkEmailAvailability(@RequestParam String email) {
		// 이메일 중복 확인
		List<User> users = userRepository.findByEmail(email);
		return users.isEmpty(); // 중복되지 않으면 true, 중복되면 false 반환
	}

	@GetMapping("/check-deleted-username")
	@ResponseBody
	public boolean checkDeletedUsername(@RequestParam String username) {
		// 해당 사용자 이름이 DeleteUser 저장소에 존재하는지 확인합니다.
		return deleteUserRepository.existsByUserId(username);
	}

//=======로그인 관련==================================//    
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// 세션에서 사용자 정보 삭제
		session.invalidate();

		return "redirect:/main"; // 로그아웃 후 메인 페이지로 리디렉션
	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "user/login";
	}

	@PostMapping("/login")
	public String processLogin(PageData pageData, @RequestParam String userid, @RequestParam String userpw,
			HttpSession session, Model model) {
		User user = userRepository.findByUserId(userid);
		// 탈퇴 여부 확인
		DeleteUser deleteUser = deleteUserRepository.findByUserId(userid);
		if (deleteUser != null && deleteUser.getUserId().equals(userid) && deleteUser.getUserPw().equals(userpw)) {
			session.setAttribute("user", user); // 사용자 정보를 세션에 저장

			// 회원 복구 페이지로 이동
			return "redirect:/user/restoreuser/" + deleteUser.getUserId();
		}
		if (user != null && user.getUserPw().equals(userpw)) {
			session.setAttribute("user", user); // 사용자 정보를 세션에 저장
			session.setAttribute("Id", user.getId()); // 사용자 정보를 세션에 저장

			return "redirect:/main"; // 메인 페이지로 리디렉션
		} else {
			pageData.setMsg("로그인이 실패했습니다.");
			pageData.setGoUrl("/user/login");

			return "inc/alert"; // 로그인 실패 시 alert 페이지로 리디렉션
		}
	}

	@GetMapping("/restoreuser/{userId}")
	public String restoreUserPage(@PathVariable("userId") String userId, Model model, PageData pageData) {
		// userId를 사용하여 DeleteUser 엔티티에서 해당 사용자를 검색
		DeleteUser deleteUser = deleteUserRepository.findByUserId(userId);

		if (deleteUser != null) {
			// DeleteUser 엔티티에서 필요한 정보를 가져와서 User 엔티티로 복구
			User user = new User();
			user.setUserId(deleteUser.getUserId());
			user.setBirth(deleteUser.getBirth());
			user.setEmail(deleteUser.getEmail());
			user.setPhone(deleteUser.getPhone());
			user.setRegDate(deleteUser.getRegDate());
			user.setUserName(deleteUser.getUserName());
			user.setUserPw(deleteUser.getUserPw());

			// 'user' 모델에 추가
			model.addAttribute("user", user);

			return "/user/mypage/restoreuser"; // 회원 복구 결과 화면으로 이동
		}

		return "redirect:/main"; // 해당 사용자가 없으면 메인 페이지로 이동
	}

	@PostMapping("/restoreuser/{userId}")
	public String restoreUser(@PathVariable("userId") String userId, Model model, PageData pageData) {
		// userId를 사용하여 DeleteUser 엔티티에서 해당 사용자를 검색
		DeleteUser deleteUser = deleteUserRepository.findByUserId(userId);

		if (deleteUser != null) {
			// DeleteUser 엔티티에서 필요한 정보를 가져와서 User 엔티티로 복구
			User user = new User();
			user.setUserId(deleteUser.getUserId());
			user.setBirth(deleteUser.getBirth());
			user.setEmail(deleteUser.getEmail());
			user.setPhone(deleteUser.getPhone());
			user.setRegDate(deleteUser.getRegDate());
			user.setUserName(deleteUser.getUserName());
			user.setUserPw(deleteUser.getUserPw());

			// User 엔티티 저장
			userRepository.save(user);

			// DeleteUser 엔티티 삭제
			deleteUserRepository.delete(deleteUser);

			// 'user' 모델에 추가
			model.addAttribute("user", user);
			pageData.setMsg("회원복구에 성공하셨습니다.");
			pageData.setGoUrl("/main");

			return "/inc/alert"; // 회원 복구 결과 화면으로 이동
		}

		return "redirect:/main"; // 해당 사용자가 없으면 메인 페이지로 이동
	}

	@GetMapping("/delete")
	public String showDeleteForm(Model model, HttpSession session) {
		// 처음 페이지에 접근할 때는 세션에서 오류 메시지 초기화d
		session.removeAttribute("deleteError");
		model.addAttribute("user", session.getAttribute("user"));
		System.out.println("이거뭐임?" + session.getAttribute("user"));
		return "user/delete";
	}

	@PostMapping("/delete")
	public String processDelete(PageData pageData, @RequestParam("userPw") String userPw, HttpSession session,
			Model model) {
		User user = (User) session.getAttribute("user");
		System.out.println("현재유저 비밀번호 =>" + user.getUserPw());

		// 사용자의 모든 예약 내역을 가져옵니다.
		List<Reservation> reservations = reservationRepository.findAllByUserId(user.getId());

		LocalDate currentDate = LocalDate.now();
		boolean hasCurrentReservations = false;
		boolean hasPastReservations = false;

		// 예약 내역을 확인하며 현재 예약과 과거 예약 여부를 검사합니다.
		for (Reservation reservation : reservations) {
			LocalDate reservationDate = reservation.getDate();
			if (reservationDate.isEqual(currentDate) || reservationDate.isAfter(currentDate)) {
				// 예약 일자가 현재 날짜와 같거나 이후인 경우 (현재 예약)
				hasCurrentReservations = true;
			} else {
				// 예약 일자가 현재 날짜보다 이전인 경우 (과거 예약)
				hasPastReservations = true;
			}
		}

		// 현재 예약 내역이 있는 경우 회원 탈퇴를 막습니다.
		if (hasCurrentReservations) {
			pageData.setMsg("현재 예약 내역이 있어서 회원 탈퇴를 진행할 수 없습니다.");
			pageData.setGoUrl("/user/delete");
			return "inc/alert";
		}

		// 사용자가 입력한 비밀번호가 일치하는지 확인합니다.
		if (user != null && user.getUserPw().equals(userPw)) {
			DeleteUser deleteUser = new DeleteUser();
			deleteUser.setUserId(user.getUserId());
			deleteUser.setBirth(user.getBirth());
			deleteUser.setEmail(user.getEmail());
			deleteUser.setPhone(user.getPhone());
			deleteUser.setRegDate(user.getRegDate());
			deleteUser.setUserName(user.getUserName());
			deleteUser.setUserPw(user.getUserPw());
			deleteUser.setDeletedAt(LocalDateTime.now()); // 회원 탈퇴한 날짜 저장
			deleteUser.setId(user.getId());
			// DeleteUser 엔티티 저장
			deleteUserRepository.save(deleteUser);

			// User 엔티티에서 삭제
			userRepository.delete(user);

			// 사용자 삭제 및 로그아웃을 수행합니다.
			userRepository.delete(user);
			session.removeAttribute("user");
			session.removeAttribute("Id");
			session.removeAttribute("userRole2");
			pageData.setMsg("회원 탈퇴가 성공적으로 완료되었습니다.");
			pageData.setGoUrl("/main");
			return "inc/alert";
		} else {
			// 사용자가 입력한 비밀번호가 일치하지 않는 경우 오류 메시지를 표시합니다.
			pageData.setMsg("비밀번호가 일치하지 않습니다.");
			pageData.setGoUrl("/user/delete");
			return "inc/alert";
		}
	}

	@GetMapping("/search")
	public String search() {

		return "user/search";
	}

	@GetMapping("/idresult")
	public String showFindIdForm() {
		return "user/idresult";
	}

	@PostMapping("/idresult")
	public String findIdByEmail(@RequestParam("email") String email, Model model) {
		User foundId = userRepository.findUserIdByEmail(email);
		if (foundId != null) {
			model.addAttribute("foundId", foundId);
		} else {
			model.addAttribute("error", "일치하는 ID가 없습니다.");
		}
		return "user/idresult";
	}

	@GetMapping("/pwresult")
	public String showResetPasswordForm() {
		return "user/pwresult";
	}

	@Transactional
	@PostMapping("/pwresult")
	public String findPasswordByEmail(@RequestParam("userId") String userId, @RequestParam("email") String email,
			Model model) {
		User user = userRepository.findByUserIdAndEmail(userId, email);

		if (user != null) {
			// 임시 비밀번호 생성
			String temporaryPassword = generateTemporaryPassword();

			// 임시 비밀번호를 사용자 엔터티에 저장
			user.setUserPw(temporaryPassword);
			userRepository.save(user);

			model.addAttribute("changedPw", temporaryPassword);
			return "user/pwresult";
		} else {
			model.addAttribute("error", "ID 또는 이메일이 일치하지 않습니다.");
			return "user/pwresult";
		}
	}

	private String generateTemporaryPassword() {
		String characters = "abcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";

		StringBuilder temporaryPassword = new StringBuilder();

		// 임시 비밀번호 길이를 설정합니다. 예: 12자리
		int length = 12;

		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			char randomChar = characters.charAt(index);
			temporaryPassword.append(randomChar);
		}

		return temporaryPassword.toString();
	}

	@GetMapping("mypage")
	public String main(Model model, HttpSession session) {
		model.addAttribute("user", session.getAttribute("user"));
		return "user/mypage/main";
	}

	@RequestMapping("mypage/rankList/{branchName}/{title}")
	String list(Model model, Ranking rd, Branch bd, HttpSession session, @PathVariable String branchName,
			@PathVariable String title) {

		int userId = (int) session.getAttribute("Id");

		// 랭킹 데이터 전체
		List<Ranking> rankData = rankingRepository.findAll();
		List<Branch> brnList = tmMapper.brList(bd);
		List<Thema> themaList = themaRepository.findAll();
		List<Ranking> filterRankData = new ArrayList<>();
		for (Ranking rank : rankData) {
			if ("전체".equals(branchName) || branchName.equals(rank.getBranchName())) {
				if ("전체".equals(title) || title.equals(rank.getThemaName())) {
					filterRankData.add(rank);
				}
			}
		}
		model.addAttribute("filterRankData", filterRankData);
		model.addAttribute("title", title);
		model.addAttribute("user", session.getAttribute("user"));
		model.addAttribute("brnList", brnList);
		// 유저아이디
		model.addAttribute("userId", userId);
		model.addAttribute("themaList", themaList);
		// 해당하는 아이디만 보이는 데이터
		model.addAttribute("rankData", rankData);
		// model.addAttribute("rankList", rankData);
		model.addAttribute("branchName", branchName); // 선택된 branchName을 모델에 추가

		return "user/mypage/rankList";
	}

//	@RequestMapping("mypage/delete/{rankId}/{branchName}")
//	String deleteReg(@PathVariable("rankId") int rankId, @PathVariable("branchName") String branchName,
//			HttpSession session, Model model, PageData pageData) {
//		model.addAttribute("user", session.getAttribute("user"));
//		Ranking delData = usermapper.rankId(rankId);
//		int cnt = usermapper.rankdel(delData);
//		if (cnt > 0) {
//
//			pageData.setMsg("삭제되었습니다.");
//			pageData.setGoUrl("/admin/rankList/" + branchName + "/전체");
//
//		}
//
//		return "inc/alert";
//	}
//
//	@GetMapping("mypage/modify/{branchName}/{rankId}")
//	String modify(Model model, @PathVariable int rankId, @RequestParam int rvId, Branch bd, Thema td,
//			@PathVariable String branchName, HttpSession session) {
//
//		List<Thema> themaList = tmMapper.tmList(td);
//		List<Branch> brnList = tmMapper.brList(bd);
//		Reservation rv = reservationRepository.findByRvId(rvId);
//		model.addAttribute("rv", rv);
//		Ranking modiData = usermapper.rankId(rankId);
//		model.addAttribute("username", session.getAttribute("username"));
//		model.addAttribute("brnList", brnList);
//		model.addAttribute("themaList", themaList);
//		model.addAttribute("ranking", modiData);
//		model.addAttribute("user", session.getAttribute("user"));
//		return "user/mypage/rankModify";
//	}
//
//	@PostMapping("mypage/modify/{branchName}/{rankId}")
//	String modifyReg(Ranking rd, PageData pageData, HttpSession session, @RequestParam int rvId,
//			@PathVariable String branchName) {
//		Reservation rv = reservationRepository.findByRvId(rvId);
//		rd.setPay(rv.getPay());
//		rd.setThemaName(rv.getThemaName());
//		rd.setThema(rv.getThema());
//		rd.setUser(rv.getUser());
//
//		rankingRepository.save(rd);
//		List<Ranking> ranking = rankingRepository.findByThemaThemaId(rv.getThema().getThemaId());
//		ranking.sort(Comparator.comparing(Ranking::getMinutes));
//		for (Ranking rank : ranking) {
//			rank.calcRank(ranking);
//			System.out.println("rank" + rank.getThemaRank());
//			rankingRepository.save(rank);
//		}
//		pageData.setMsg("수정 되었습니다.");
//		pageData.setGoUrl("/admin/rankList/" + branchName + "/전체");
//
//		return "inc/alert";
//	}

	@GetMapping("mypage/writeList")
	public String writeList(Model model, HttpSession session,
			@PageableDefault(size = 15, sort = "rvId", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
			@RequestParam(name = "sortBy", defaultValue = "regDate") String sortBy) {

		Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		int userId = (int) session.getAttribute("Id");
		System.out.println("userId : " + userId);

		// 로그인한 아이디가 작성한 게시글 검색
		Page<ReviewBoard> userBoardData = rBoardRepository.findByUserId(userId, pageable);
		System.out.println("userBoardData : " + userBoardData);

		model.addAttribute("username", session.getAttribute("username"));
		model.addAttribute("userBoardData", userBoardData);
		model.addAttribute("user", session.getAttribute("user"));
		return "user/mypage/writeList";
	}

	// 내정보 수정 앞쪽에 비밀번호 확인
	@GetMapping("mypage/modiPw")
	public String modiPw(Model model, HttpSession session) {

		int id = (int) session.getAttribute("Id");
		System.out.println("pw id : " + id);
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			User userData = user.get();
			model.addAttribute("userData", userData);
		}
		model.addAttribute("user", session.getAttribute("user"));
		model.addAttribute("username", session.getAttribute("username"));
		System.out.println(user);
		return "user/mypage/userModifyPw"; // 비밀번호 확인 페이지 템플릿 경로
	}

	// 비밀번호 확인 처리
	@PostMapping("mypage/modiPw")
	public String confirmPassword(@ModelAttribute("user") User user, HttpSession session, Model model,
			RedirectAttributes redirectAttributes) {

		int id = (int) session.getAttribute("Id");

		Optional<User> userOptional = userRepository.findById(id);

		if (userOptional.isPresent()) {
			User Nuser = userOptional.get();

			if (user.getUserPw().equals(Nuser.getUserPw())) {
				session.setAttribute("userPw", user.getUserPw());

				return "redirect:/user/mypage/userModify";
			} else {
				PageData pageData = new PageData();
				pageData.setMsg("비밀번호가 일치하지 않습니다.");
				pageData.setGoUrl("/user/mypage/modiPw");
				model.addAttribute("user", session.getAttribute("user"));
				model.addAttribute("username", session.getAttribute("username"));
				model.addAttribute("pageData", pageData);
				return "inc/alert";
			}
		}

		return "user/mypage/main";
	}

	@GetMapping("mypage/userModify")
	public String userModify(Model model, HttpSession session) {
		int id = (int) session.getAttribute("Id");

		Optional<User> user = userRepository.findById(id);

		if (user.isPresent()) {
			User userData = user.get();
			String currentEmail = userData.getEmail();

			model.addAttribute("userData", userData);
			model.addAttribute("currentEmail", currentEmail);
			model.addAttribute("user", session.getAttribute("user"));
			model.addAttribute("username", session.getAttribute("username"));
			return "user/mypage/userModify";
		}
		return "user/mypage/userModify";
	}

	@PostMapping("mypage/userModify")
	public String userModify(PageData pageData, @Valid @ModelAttribute("userData") User updatedUser,
			BindingResult bindingResult, HttpSession session, Model model) {

		String userId = updatedUser.getUserId();

		// 유효성 검사에서 오류가 발생한 경우
		if (bindingResult.hasErrors()) {

			return "user/mypage/userModify";
		}

		User user = userRepository.findByUserId(userId);

		user.setUserName(updatedUser.getUserName());
		user.setUserPw(updatedUser.getUserPw());
		user.setEmail(updatedUser.getEmail());
		user.setPhone(updatedUser.getPhone());
		System.out.println("새유저" + user);
		userRepository.save(user);

		session.setAttribute("userId", user.getUserId());
		session.setAttribute("userPw", updatedUser.getUserPw());

		model.addAttribute("user", session.getAttribute("user"));
		model.addAttribute("userId", session.getAttribute("userId"));
		model.addAttribute("username", session.getAttribute("username"));
		model.addAttribute("userrole2", session.getAttribute("userrole2"));
		pageData.setMsg("수정 완료, 다시 로그인해주세요.");
		pageData.setGoUrl("/user/login");
		session.invalidate();

		return "inc/alert";
	}

	@GetMapping("mypage/wishlist")
	public String wishThema(Model model, HttpSession session) {
		// String userId = (String) session.getAttribute("userId");
		int id = (int) session.getAttribute("Id");

		// List<WishThema> userWishThemes = wishRepository.findByUser(user);
		List<WishList> userWishThemes = wishRepository.findByUserId(id);
		for (WishList a : userWishThemes) {
			System.out.println(a.getThema());
		}
		model.addAttribute("user", session.getAttribute("user"));
		model.addAttribute("wishThemes", userWishThemes);
		model.addAttribute("username", session.getAttribute("username"));
		return "user/mypage/wishThema";
	}

	@GetMapping("mypage/rvList")
	public String rvList(Model model, HttpSession session) {
		LocalDate today = LocalDate.now();

		List<Reservation> todayReservations = reservationRepository.findByDate(today);
		int id = (int) session.getAttribute("Id");
		Optional<User> Ouser = userRepository.findById(id);
		User userData = Ouser.get();
		List<Reservation> reservations = reservationRepository.findByUser(userData);
		System.out.println(id);
		for (Reservation a : reservations) {
			System.out.println(a);
		}
		model.addAttribute("today", today);
		model.addAttribute("id", id);
		model.addAttribute("reservations", reservations);
		model.addAttribute("username", session.getAttribute("username"));

		model.addAttribute("user", session.getAttribute("user"));
		return "user/mypage/rvList";
	}

	// ============== 정현수정 ===============
	@RequestMapping("mypage/couponList")
	public String couponList(Model model, HttpSession session) {

		int id = (int) session.getAttribute("Id");
		Optional<User> Ouser = userRepository.findById(id);

		// 사용하지 않은 쿠폰을 가져오고 날짜순으로 정렬
		List<Coupon> unusedCoupons = couponRepository.findByUserAndUsedIsFalseOrderByRegDate(Ouser.get());

		// 사용한 쿠폰을 가져오고 날짜순으로 정렬
		List<Coupon> usedCoupons = couponRepository.findByUserAndUsedIsTrueOrderByRegDate(Ouser.get());

		// 두 리스트를 합칩니다.
//	        List<Coupon> userCoupons = new ArrayList<>();
//	        userCoupons.addAll(unusedCoupons);
//	        userCoupons.addAll(usedCoupons);

		model.addAttribute("user", session.getAttribute("user"));
		model.addAttribute("unusedCoupons", unusedCoupons); // 모델에 쿠폰 목록을 추가합니다.
		model.addAttribute("usedCoupons", usedCoupons); // 모델에 쿠폰 목록을 추가합니다.
		return "user/mypage/couponList";
	}

	// ============== 정현수정 ===============

}
