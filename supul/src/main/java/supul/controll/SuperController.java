package supul.controll;

import java.io.File;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.ResponseBody;

import supul.mapper.BranchMapper;
import supul.mapper.HomepageMapper;
import supul.mapper.ThemaMapper;
import supul.model.Admin;
import supul.model.Branch;
import supul.model.Coupon;
import supul.model.DeleteUser;
import supul.model.HomepageDTO;
import supul.model.ModiBranch;
import supul.model.PageData;
import supul.model.Reservation;
import supul.model.Reservation_backup;
import supul.model.Thema;
import supul.model.User;
import supul.repository.AdminRepository;
import supul.repository.BranchRepository;
import supul.repository.CouponRepository;
import supul.repository.DeleteUserRepository;
import supul.repository.ModiBranchRepository;
import supul.repository.ReservationBackUpRepository;
import supul.repository.ReservationRepository;
import supul.repository.SuperRepository;
import supul.repository.ThemaRepository;
import supul.repository.UserRepository;

import supul.service.SaleService;
import supul.service.SuperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("super")
public class SuperController {
	@Autowired
	HomepageMapper homepageMapper;
	@Autowired
	ThemaMapper themaMapper;
	@Autowired
	BranchMapper branchMapper;
	@Autowired
	SuperService superService;
	@Autowired
	SaleService stat;
	@Autowired
	ThemaRepository themaRepository;
	@Autowired
	SuperRepository superrepository;
	@Autowired
	BranchRepository branchRepository;
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	ReservationBackUpRepository reservationBackUpRepository;
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	DeleteUserRepository deleteUserRepository;
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	ModiBranchRepository modiBranchRepository;

	// 슈퍼 계정 정보 고정 값 설정
	static final String SUPERUSERNAME = "super";
	static final String SUPERPASSWORD = "super";

	@RequestMapping("")
	public String supermain(Model model, HttpSession session,
			@RequestParam(required = false, defaultValue = "전체") String branchName,
			@RequestParam(required = false, defaultValue = "전체") String title) {
		// 슈퍼 계정 로그인 여부 확인
		boolean isSuperLoggedIn = superService.isSuperLoggedIn();
		
		// 지점별 매출 계산
		
		List<Branch> brnList = branchRepository.findAll();
		List<Thema> themaList = themaRepository.findAll();
		// 통계 그래프
		List<Reservation> rvList = reservationRepository.findByDateBetween(LocalDate.now().minusDays(30),
				LocalDate.now());
		Map<String, Integer> dailyStats = stat.generateDailyStats(rvList);
		Map<String, Integer> sortedDailyStats = dailyStats.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		model.addAttribute("dailyStats", sortedDailyStats);
		Map<String, Integer> weeklyStats = stat.generateWeeklyStats(rvList);
				
				Map<String, Integer> sortedWeeklyStats = new LinkedHashMap<>();
				String[] daysOfWeek = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};

				for (String dayOfWeek : daysOfWeek) {
				    if (weeklyStats.containsKey(dayOfWeek)) {
				        sortedWeeklyStats.put(dayOfWeek, weeklyStats.get(dayOfWeek));
				    } else {
				        sortedWeeklyStats.put(dayOfWeek, 0); // 해당 요일의 매출이 없으면 0으로 설정
				    }
				}
				model.addAttribute("weeklyStats", sortedWeeklyStats);
		Map<String, Integer> MonthlyStats = stat.MonthlyStats(rvList);
		model.addAttribute("MonthlyStats", MonthlyStats);

		Map<Object, Integer> themeSales = rvList.stream().collect(Collectors.groupingBy(
				reservation -> reservation.getThema().getTitle(), Collectors.summingInt(Reservation::getRvPrice)));
		Map.Entry<Object, Integer> maxEntry = themeSales.entrySet().stream()
			    .max(Comparator.comparing(Map.Entry::getValue))
			    .orElse(null);

		Thema topThema = themaRepository.findByTitle(maxEntry.getKey());
		model.addAttribute("topThema", topThema);

		List<Reservation> themarvList;
		if (branchName.equals("전체") || branchName == null) {
			if (title.equals("전체") || title == null || title.equals("null")) {
				themarvList = reservationRepository.findByDateBetween(LocalDate.now().minusDays(30),
						LocalDate.now());
			} else {
				themarvList = reservationRepository.findByThemaNameAndDateBetween(title,LocalDate.now().minusDays(30),
						LocalDate.now());
			}
		} else {
			if (title.equals("전체") || title == null) {
				themarvList = reservationRepository.findByBranchNameAndDateBetween(branchName,LocalDate.now().minusDays(30),
						LocalDate.now());
			} else {
				themarvList = reservationRepository.findByThemaNameAndDateBetween(title,LocalDate.now().minusDays(30),
						LocalDate.now());
			}
		}

		Map<String, Integer> themaTotal = stat.themaTotal(themarvList);
		model.addAttribute("themaTotal", themaTotal);
		Map<String, Integer> themaRvTotal = stat.themarvTotal(themarvList);
		model.addAttribute("themaRvTotal", themaRvTotal);
		Map<String, Integer> ReservationCount = stat.ReservationCount(themarvList);
		model.addAttribute("ReservationCount", ReservationCount);
		Map<Branch, Integer> banchSales = stat.banchSales(themarvList);
		model.addAttribute("banchSales", banchSales);
		Map<Thema, Integer> themaSales = stat.themaSales(themarvList);
		model.addAttribute("themaSales", themaSales);
		Map<Branch, Long> banchNoShowCount = stat.banchNoShowCount(themarvList);
		model.addAttribute("banchNoShowCount", banchNoShowCount);

		model.addAttribute("brnList", brnList);
		model.addAttribute("themaList", themaList);
		model.addAttribute("branchName", branchName);
		model.addAttribute("title", title);

		model.addAttribute("superLoggedIn", isSuperLoggedIn);

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==

		return "super/super_main";
	}

	@GetMapping("/superlogin")
	public String superLoginPage(Model model) {
		// 슈퍼 계정 로그인 여부 확인
		boolean isSuperLoggedIn = superService.isSuperLoggedIn();
		model.addAttribute("superLoggedIn", isSuperLoggedIn);
		return "super/super_login";
	}

	@PostMapping("/superlogin")
	public String loginSuperUser(String superId, String superPw, Model model, HttpSession session, PageData pageData) {
		if (session.getAttribute("admin") != null) {
			session.removeAttribute("adminId");
			session.removeAttribute("admin");
			session.removeAttribute("adminRole");
			session.removeAttribute("adminBranchId");
			session.removeAttribute("adminBn");
			session.removeAttribute("adminName");

		}

		if (session.getAttribute("user") != null) {
			session.removeAttribute("user");
			session.removeAttribute("Id");
			session.removeAttribute("userRole2");
		}

		// 슈퍼 계정 로그인 시도
		if (superService.loginSuperUser(superId, superPw)) {

			model.addAttribute("superLoggedIn", true); // 로그인 성공 시 true로 설정

			// 슈퍼 계정으로 로그인한 경우 superName을 세션에 설정
			session.setAttribute("superId", superId); // 슈퍼 계정의 아이디 설정
			session.setAttribute("userRole", "super"); // 슈퍼 계정의 역할 설정
			session.setAttribute("superLoggedIn", true);

			return "redirect:/super"; // 로그인 성공 시 이동할 페이지
		} else {
			model.addAttribute("loginError", true);

			pageData.setMsg("아이디 또는 비밀번호 일치하지 않습니다.");
			pageData.setGoUrl("/super/superlogin");
			return "inc/alert";
		}
	}

	@GetMapping("/superlogout")
	public String logoutSuperUser(HttpSession session) {
		// 슈퍼 계정 로그아웃
		superService.logoutSuperUser();
		session.invalidate();
		return "redirect:/main"; // 로그아웃 후 로그인 페이지로 이동
	}

	// ======예약 내역 등록관련 =================

	/*
	 * @GetMapping("/rvList") String rvlist(String userName,
	 * 
	 * @PageableDefault(page = 0, size = 15, sort = "rvId", direction =
	 * Direction.ASC) Pageable pageable,
	 * 
	 * @RequestParam(name = "sortBy", defaultValue = "rvDate") String sortBy, Model
	 * model, HttpSession session) { pageable =
	 * PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
	 * Sort.by(Sort.Direction.ASC, sortBy)); Page<Reservation> searchList;
	 * searchList = stat.searchTm(pageable);
	 * 
	 * List<Branch> branchList = branchRepository.findAll(); // BranchService를 이용하여
	 * 모든 지점을 가져오는 예시 model.addAttribute("branchList", branchList); String admin =
	 * (String) session.getAttribute("branchName"); Page<Reservation_backup>
	 * blackList = reservationBackUpRepository.findAll(pageable);
	 * model.addAttribute("blackList", blackList); model.addAttribute("searchList",
	 * searchList); model.addAttribute("today", LocalDate.now());
	 * model.addAttribute("pageable", pageable); model.addAttribute("userName",
	 * userName); model.addAttribute("sortBy", sortBy); // 세션등록 ==start==
	 * model.addAttribute("userRole", session.getAttribute("userRole"));
	 * model.addAttribute("admin", session.getAttribute("admin"));
	 * model.addAttribute("adminBn", session.getAttribute("adminBn"));
	 * model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
	 * // 세션등록 ==end==
	 * 
	 * return "super/reservation"; }
	 */

	@GetMapping("/rvList/{branchName}")
	public String getAll(Model model, HttpSession session, @RequestParam(required = false) String userName,
			@PathVariable String branchName,
			@PageableDefault(size = 15, sort = "rvId", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "rvDate") String sortBy,
			@RequestParam(name = "sortDirection", defaultValue = "DESC") String sortDirection) {

		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

		List<Branch> branchList = branchRepository.findAll(); // BranchService를 이용하여 모든 지점을 가져오는 예시
		Branch branch = branchRepository.findByName(branchName);
		Page<Reservation> todayList;

		if (branchName.equals("전체") || branchName == null) {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				todayList = reservationRepository.findByDateBetween(LocalDate.now(),LocalDate.now().plusDays(14), pageable);
			} else {
				todayList = reservationRepository.findByUserNameContainingAndDateBetween(userName, LocalDate.now(),LocalDate.now().plusDays(14), pageable);
			}
		} else {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				todayList = reservationRepository.findByBranchNameAndDateBetween(branchName, LocalDate.now(),LocalDate.now().plusDays(14), pageable);
			} else {
				todayList = reservationRepository.findByBranchNameAndUserNameContainingAndDateBetween(branchName, userName,
						LocalDate.now(),LocalDate.now().plusDays(14), pageable);

			}
		}

		Page<Reservation> searchList;

		if (branchName.equals("전체") || branchName == null) {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				searchList = reservationRepository.findAll(pageable);
			} else {
				searchList = reservationRepository.findByUserNameContaining(userName, pageable);
			}
		} else {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				searchList = reservationRepository.findByBranchName(branchName, pageable);
			} else {
				searchList = reservationRepository.findByBranchNameAndUserNameContaining(branchName, userName,
						pageable);

			}
		}

		Page<Reservation_backup> blackList;
		if (branchName.equals("전체") || branchName == null) {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				blackList = reservationBackUpRepository.findAll(pageable);
			} else {
				blackList = reservationBackUpRepository.findByUserNameContaining(userName, pageable);
			}
		} else {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				blackList = reservationBackUpRepository.findByBranchName(branchName, pageable);

			} else {
				blackList = reservationBackUpRepository.findByBranchNameAndUserNameContaining(branchName, userName,
						pageable);
			}
		}
		model.addAttribute("blackList", blackList);
		String admin = (String) session.getAttribute("branchName");

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		model.addAttribute("today", LocalDate.now());
		model.addAttribute("branchList", branchList);
		model.addAttribute("admin", admin);
		model.addAttribute("searchList", searchList);
		model.addAttribute("todayList", todayList);
		model.addAttribute("pageable", pageable);
		model.addAttribute("userName", userName);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("branchName", branchName);

		return "super/reservation";
	}

	@GetMapping("/usedList/{branchName}")
	public String usedList(Model model, HttpSession session, @RequestParam(required = false) String userName,
			@PathVariable String branchName,
			@PageableDefault(size = 12, sort = "rvId", direction = Direction.ASC) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "date") String sortBy,
			@RequestParam(name = "sortDirection", defaultValue = "ASC") String sortDirection) {

		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

		List<Branch> branchList = branchRepository.findAll(); // BranchService를 이용하여 모든 지점을 가져오는 예시
		Branch branch = branchRepository.findByName(branchName);
		
		Page<Reservation> searchList;

		if (branchName.equals("전체") || branchName == null) {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				searchList = reservationRepository.findAll(pageable);
			} else {
				searchList = reservationRepository.findByUserNameContaining(userName, pageable);
			}
		} else {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				searchList = reservationRepository.findByBranchName(branchName, pageable);
			} else {
				searchList = reservationRepository.findByBranchNameAndUserNameContaining(branchName, userName,
						pageable);

			}
		}

		Page<Reservation_backup> blackList;
		if (branchName.equals("전체") || branchName == null) {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				blackList = reservationBackUpRepository.findAll(pageable);
			} else {
				blackList = reservationBackUpRepository.findByUserNameContaining(userName, pageable);
			}
		} else {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				blackList = reservationBackUpRepository.findByBranchName(branchName, pageable);

			} else {
				blackList = reservationBackUpRepository.findByBranchNameAndUserNameContaining(branchName, userName,
						pageable);
			}
		}
		model.addAttribute("blackList", blackList);
		String admin = (String) session.getAttribute("branchName");

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		model.addAttribute("today", LocalDate.now());
		model.addAttribute("branchList", branchList);
		model.addAttribute("admin", admin);
		model.addAttribute("searchList", searchList);
	
		model.addAttribute("pageable", pageable);
		model.addAttribute("userName", userName);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("branchName", branchName);

		return "super/usedlist";
	}
	// ======관리자 등록관련 =================

	@GetMapping("/admin/signup")
	public String showAdminSignupForm(Model model, HttpSession session) {
		List<Branch> branchlist = branchRepository.findAll();
		model.addAttribute("branchlist", branchlist);
		model.addAttribute("admin", new Admin());
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));

		// 세션등록 ==end==

		return "super/admin/signup";
	}

	@PostMapping("/admin/signup")
	public String processAdminSignupForm(@Valid @ModelAttribute("admin") Admin admin, BindingResult br,
			@RequestParam("adminPw1") String adminPw1, Model model, String emailDomain) {
		List<Branch> branchlist = branchRepository.findAll();
		model.addAttribute("branchlist", branchlist);
		if (br.hasErrors()) {
			System.out.println("에러1");
			return "super/admin/signup";
		}

		if (!admin.getAdminPw().equals(adminPw1)) {
			br.rejectValue("adminPw1", null, "비밀번호가 일치하지 않습니다.");
			System.out.println("에러2");
			return "super/admin/signup";
		}
		System.out.println(emailDomain);
		admin.setEmail(admin.getEmail() + emailDomain);

		adminRepository.save(admin);

		return "redirect:/super/adminList";
	}


	@GetMapping("/checkAdminId")
	@ResponseBody
	public boolean checkAdminId(@RequestParam String adminId) {
		Admin admin = adminRepository.findByAdminId(adminId);
		return admin == null;
	}

	@GetMapping("/checkEmail")
	@ResponseBody
	public boolean checkEmail(@RequestParam String email) {
		Admin admin = adminRepository.findByEmail(email);
		return admin == null;
	}

	@GetMapping("/adminList")
	public String showAdminList(Model model, HttpSession session) {
		// 슈퍼 계정으로 로그인한 경우만 관리자 목록 조회 가능
		if (superService.isSuperLoggedIn()) {
			List<Admin> admins = adminRepository.findAll();

			model.addAttribute("admins", admins);
			// 세션등록 ==start==
			model.addAttribute("userRole", session.getAttribute("userRole"));
			model.addAttribute("admin", session.getAttribute("admin"));
			model.addAttribute("adminBn", session.getAttribute("adminBn"));
			model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
			// 세션등록 ==end==

			return "super/admin/adminlist"; // 관리자 목록을 보여주는 뷰 페이지로 이동
		} else {
			// 슈퍼 계정이 아닌 경우 권한이 없음을 표시하거나 로그인 페이지로 리다이렉트
			return "redirect:/super/superlogin"; // 예시로 슈퍼 로그인 페이지로 리다이렉트
		}
	}

	@GetMapping("/admindelete")
	public String showAdminDeletePage(Model model) {
		// 슈퍼 계정으로 로그인한 경우만 관리자 삭제 페이지에 접근 가능
		if (superService.isSuperLoggedIn()) {
			// 관리자 목록을 가져와 모델에 추가
			List<Admin> admins = adminRepository.findAll();
			model.addAttribute("admins", admins);
			return "super/admin/admindelete"; // 관리자 삭제 페이지로 이동
		} else {
			// 슈퍼 계정이 아닌 경우 권한이 없음을 표시하거나 로그인 페이지로 리다이렉트
			return "redirect:/super/superlogin"; // 예시로 슈퍼 로그인 페이지로 리다이렉트
		}
	}

	@Transactional
	@PostMapping("/admindelete")
	public String deleteAdminUser(String adminIdToDelete, String superAdminPw, Model model, HttpSession session,
			PageData pagedata) {
		// 슈퍼 계정으로 로그인한 경우만 관리자 삭제 가능
		if (superService.isSuperLoggedIn()) {
			// 슈퍼 계정 비밀번호 확인
			List<Admin> admin =adminRepository.findAll();
			for(Admin a:admin) {
				if(a.getAdminId().equals(adminIdToDelete)) {
					
					if (SUPERPASSWORD.equals(superAdminPw)) {
						// 슈퍼 계정 비밀번호가 일치하는 경우

						// 관리자 삭제 수행
						adminRepository.deleteByAdminId(adminIdToDelete);

						return "redirect:/super"; // 관리자 삭제 후 목록 페이지로 리다이렉트
					} else {
						pagedata.setMsg("비밀번호를 확인해주세요");
						pagedata.setGoUrl("/super/admindelete");
						return "inc/alert";
						 // 슈퍼 계정 비밀번호가 일치하지 않는 경우
					}
					
					
				}else {
					pagedata.setMsg("아이디를 확인해주세요");
					pagedata.setGoUrl("/super/admindelete");
					return "inc/alert";
				}
				
			}
			if (SUPERPASSWORD.equals(superAdminPw)) {
				// 슈퍼 계정 비밀번호가 일치하는 경우

				// 관리자 삭제 수행
				adminRepository.deleteByAdminId(adminIdToDelete);

				return "redirect:/super"; // 관리자 삭제 후 목록 페이지로 리다이렉트
			} else {
				model.addAttribute("deleteError", true);
				return "super/superadmindelete"; // 슈퍼 계정 비밀번호가 일치하지 않는 경우
			}
			
			
		
		} else {
			// 슈퍼 계정이 아닌 경우 권한이 없음을 표시하거나 로그인 페이지로 리다이렉트
			return "redirect:/super/superlogin"; // 예시로 슈퍼 로그인 페이지로 리다이렉트
		}
	}

	// ======= 회원목록===================================

	@GetMapping("/memberList")
	public String memberList(String userName,
			@PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy, Model model, HttpSession session) {

		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.DESC, sortBy));

		Page<User> memberList;
		if (userName == null || userName.isEmpty()) {
			memberList = userRepository.findAll(pageable);
		} else {
			memberList = stat.searchUser(userName, pageable);
		}
		
		List<DeleteUser> deletelist = deleteUserRepository.findAll();
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		model.addAttribute("memberList", memberList);
		model.addAttribute("deletelist", deletelist);
		model.addAttribute("pageable", pageable);
		model.addAttribute("userName", userName);
		model.addAttribute("sortBy", sortBy);

		return "super/memberList";
	}

	@RequestMapping("/memberDetail/{id}")
	String detail(Model model, @PathVariable int id, HttpSession session) {

		Optional<User> Ouser = userRepository.findById(id);

		User user = new User();

		if (Ouser.isPresent()) {
			user = Ouser.get();
			model.addAttribute("user", user);
		} else {

		}
		List<Reservation> reservation = reservationRepository.findByUser(user);

		model.addAttribute("reservation", reservation);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		return "super/memberDetail";
	}

	@GetMapping("/deleteUser")
	public String showDeletedUsers(Model model) {
		List<DeleteUser> deletedUsers = deleteUserRepository.findAll();
		model.addAttribute("deletedUsers", deletedUsers);
		return "super/deleteUser";
	}

	// ===========지점관련 등록 및 수정, 리스트 ===================

	// 지점리스트
	@GetMapping("/branchList")
	public String getBrunch(String name,
			@PageableDefault(size = 8, sort = "rvDate", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "name") String sortBy, Model model, HttpSession session) {

		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.ASC, sortBy));

		Page<Branch> searchList = stat.brList(pageable);

		if (name == null || name.isEmpty()) {
			// themaName이 없거나 비어있는 경우 전체 리스트 조회
			searchList = stat.brList(pageable);
		} else {
			// themaName이 있는 경우 해당 테마로 필터링하여 조회
			searchList = stat.brSertchList(name, pageable);
		}
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		model.addAttribute("list", searchList);
		model.addAttribute("pageable", pageable);
		model.addAttribute("name", name);
		model.addAttribute("sortBy", sortBy);
		return "super/branch/branchList";
	}

	@GetMapping("/branch/detail/{branchName}")
	public String Branchdetail(@PathVariable("branchName") String branchName, Model model, HttpSession session) {
		// 선택한 Branch의 이름을 기반으로 해당 Branch 정보를 가져옴
		Branch selectedBranch = branchMapper.selectByName(branchName);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		// 모델에 선택한 Branch 정보를 설정
		model.addAttribute("selectedBranch", selectedBranch);

		return "home/branchInfo";
	}

	@GetMapping("/branch/form")
	public String showBranchForm(Model model, HttpSession session) {

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		model.addAttribute("branch", new Branch());
		return "super/branch/branchForm";
	}

	@PostMapping("/branch/form")
	public String createBranch(@Valid @ModelAttribute("branch") Branch branch, BindingResult result, Model model,
			HttpSession session) {
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		if (result.hasErrors()) {

			return "super/branch/branchForm";
		}

		branchRepository.save(branch);

		return "redirect:/super/branchList"; // 성공 페이지로 리다이렉트
	}

	// 지점 정보 수정 폼 보기
	@GetMapping("/branch/update/{branchId}")
	public String showBranchUpdateForm(@PathVariable("branchId") String branchId, Model model, HttpSession session) {
		Branch branch = branchMapper.selectById(Integer.parseInt(branchId));
		System.out.println(branch.getBranchId());
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		model.addAttribute("branch", branch);
		return "super/branch/branchUpdate";
	}

	// 지점 정보 수정 처리
	@PostMapping("/branch/update/{branchId}")
	public String submitBranchUpdate(@ModelAttribute @Valid Branch branch, BindingResult result,
			@PathVariable("branchId") String branchId, Model model, HttpSession session) {
		if (result.hasErrors()) {

			// 세션등록 ==start==
			model.addAttribute("userRole", session.getAttribute("userRole"));
			model.addAttribute("admin", session.getAttribute("admin"));
			model.addAttribute("adminBn", session.getAttribute("adminBn"));
			model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
			// 세션등록 ==end==
			model.addAttribute("branch", branch);

			return "super/branch/branchUpdate";
		}
		branchRepository.save(branch);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));

		// 세션등록 ==end==

		return "redirect:/super/branchList";
	}

	// 지점 정보 삭제 폼 보기
	@GetMapping("/branch/delete/{branchId}")
	public String showBranchDeleteForm(@PathVariable("branchId") int branchId, Model model, HttpSession session,
			PageData pageData) {
		Branch branch = branchMapper.selectById(branchId);

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		model.addAttribute("branch", branch);
		return "super/branch/branchDelete";
	}

	// 지점 정보 삭제 처리
	@PostMapping("/branch/delete")
	public String submitBranchDelete(int branchId, PageData pageData) {

		Optional<Branch> obr = branchRepository.findById(branchId);
		if (obr.isPresent()) {
			Branch branch = obr.get();

			if (branch.getRv() != null) {
				for (Reservation rv : branch.getRv()) {
					System.out.println(rv.getDate().isAfter(LocalDate.now()));
					
					if (!rv.isCancle() && rv.getDate().isAfter(LocalDate.now().minusDays(1))) {
						pageData.setMsg("현재 예약중인 내역이 있어 삭제할수 없습니다.");
						pageData.setGoUrl("/super/branchList");
						return "inc/alert";
					} else {
						Reservation_backup rvBack = new Reservation_backup();
						rvBack.setBranchName(rv.getBranch().getName());
						rvBack.setCancle(rv.isCancle());
						rvBack.setDate(rv.getDate());
						if (rv.getPay() != null) {

							rvBack.setPay_id(rv.getPay().getPayId());
						}
						if (rv.getRvpay() != null) {
							rvBack.setImp_uid(rv.getRvpay().getImp_uid());
							rvBack.setImpUidexe(rv.getRvpay().getImpUidexe());
						}
						rvBack.setNoShow(rv.isNoShow());
						rvBack.setPaid(rv.isPaid());
						rvBack.setPayCancle(rv.isPayCancle());
						rvBack.setPrice(rv.getPrice());
						rvBack.setRvPrice(rv.getRvPrice());
						rvBack.setRvPeople(rv.getRvPeople());
						rvBack.setThemaName(rv.getThemaName());

						reservationBackUpRepository.save(rvBack);
					}
				}
			}

		} else {
			pageData.setMsg("없는 지점입니다.");
			pageData.setGoUrl("/super/branchList");
			return "inc/alert";
		}

		branchRepository.deleteById(branchId);
		System.out.println("삭제되었습니다");
		return "redirect:/super/branchList";
	}

	@GetMapping("/adminModify/{adminId}")
	public String showAdminModifyForm(@PathVariable String adminId, Model model, HttpSession session) {

		model.addAttribute("userRole", session.getAttribute("userRole"));
		// 관리자 정보 조회
		Admin admin = adminRepository.findByAdminId(adminId);
		List<Branch> branchlist = branchRepository.findAll();
		model.addAttribute("branchlist", branchlist);

		if (admin == null) {
			return "redirect:/adminList";
		}

		model.addAttribute("admin", admin);

		return "super/admin/adminModify"; // 관리자 수정 폼으로 이동
	}

	@PostMapping("/adminModify/{adminId}")
	public String showAdminModifyReg(PageData pageData, @Valid @ModelAttribute("admin") Admin updateAdmin,
			BindingResult br, HttpSession session, Model model) {

		String adminId = updateAdmin.getAdminId();

		// 유효성 검사 오류 확인
		if (br.hasErrors()) {
			List<Branch> branchlist = branchRepository.findAll();
			model.addAttribute("branchlist", branchlist);
			return "super/admin/adminModify"; // 수정 폼으로 다시 이동
		}

		Admin admin = adminRepository.findByAdminId(adminId);
		
		admin.setAdminId(updateAdmin.getAdminId());
		admin.setAdminPw(updateAdmin.getAdminPw());
		admin.setAdminPw1(updateAdmin.getAdminPw1());
		admin.setName(updateAdmin.getName());
		admin.setEmail(updateAdmin.getEmail());
		admin.setPhone(updateAdmin.getPhone());
		admin.setBranchName(updateAdmin.getBranchName());
		adminRepository.save(admin);

		session.setAttribute("adminId", admin.getAdminId());
		model.addAttribute("admin", session.getAttribute("admin"));

		pageData.setMsg("수정 완료");
		pageData.setGoUrl("/super/adminList");

		return "inc/alert";
	}

	// ===========홈페이지 수정=============
	@GetMapping("home/update/{title1}")
	public String editHomepage(@PathVariable("title1") String title1, Model model, HttpSession session) {
		HomepageDTO homepage = homepageMapper.selectHomepageByTitle(title1);
		model.addAttribute("homepage", homepage);
		model.addAttribute("userRole", session.getAttribute("userRole"));

		return "super/homeUpdate"; // editHomepage.html로 넘어감
	}

	@PostMapping("home/update")
	public String updateHomepage(@ModelAttribute HomepageDTO homepage) {
		homepageMapper.updateHomepage(homepage);
		return "redirect:/home/intro";
	}

	// ============== 정현 쿠폰 수정 ===============
	@RequestMapping("couponManage")
	public String couponManage(Model model, HttpSession session) {

		System.out.println("쿠폰관리 진입");
		model.addAttribute("userRole", session.getAttribute("userRole"));

		List<Coupon> unusedCouponsAll = couponRepository.findByUsedFalse();
		List<Coupon> usedCouponsAll = couponRepository.findByUsedTrue();

		model.addAttribute("unusedCouponsAll", unusedCouponsAll); // 모델에 쿠폰 목록을 추가합니다.
		model.addAttribute("usedCouponsAll", usedCouponsAll); // 모델에 쿠폰 목록을 추가합니다.

		return "super/couponManage";
	}

	@GetMapping("couponForm")
	public String couponForm(Model model, HttpSession session) {

		System.out.println("쿠폰발급 폼 진입");
		model.addAttribute("userRole", session.getAttribute("userRole"));

		return "super/couponForm";
	}

	@PostMapping("couponForm")
	public String couponFormReg(Model model, HttpSession session, Coupon newCoupon, PageData pageData,
			@RequestParam String userId) {

		System.out.println("쿠폰발급 진입");
		model.addAttribute("userRole", session.getAttribute("userRole"));

		User user = userRepository.findByUserId(userId);

		if (user == null) {
			pageData.setMsg("해당 Id의 유저가 존재하지 않습니다");
			pageData.setGoUrl("/super/couponForm");

			return "inc/alert";
		}

		newCoupon.setUser(user);
		newCoupon.setRegDate(LocalDateTime.now().withNano(0));

		Random ran = new Random();
		while (true) {
			int i = ran.nextInt(1, 100000000);

			String cpnum = String.format("%08d", i);
			// 데이터베이스에서 해당 쿠폰 번호가 이미 존재하는지 확인
			boolean isDuplicate = couponRepository.existsByCouponNum(cpnum);
			if (!isDuplicate) {
				newCoupon.setCouponNum(cpnum);
				break;
			}
		}

		System.out.println("쿠폰 잘 들어왔니~" + newCoupon);
		couponRepository.save(newCoupon);
		pageData.setMsg("쿠폰발급 성공!");
		pageData.setGoUrl("/super/couponManage");

		return "inc/alert";
	}

	@GetMapping("couponModify/{couponId}")
	public String couponModify(@PathVariable int couponId, HttpSession session, Model model) {

		System.out.println("쿠폰수정 진입");
		model.addAttribute("userRole", session.getAttribute("userRole"));

		Optional<Coupon> oCoupon = couponRepository.findById(couponId);
		model.addAttribute("coupon", oCoupon.get());
		model.addAttribute("couponId", couponId);

		return "super/couponModify";
	}

	@PostMapping("couponModify/{couponId}")
	public String couponModifyReg(@PathVariable int couponId, Model model, Coupon coupon) {

		System.out.println("쿠폰수정 확정진입");
		Optional<Coupon> oCoupon = couponRepository.findById(couponId);
		Coupon newCoupon = oCoupon.get();
		newCoupon.setCouponName(coupon.getCouponName());
		newCoupon.setDiscount(coupon.getDiscount());
		couponRepository.save(newCoupon);

		System.out.println("쿠폰수정 완료");
		return "redirect:/super/couponManage";
	}

	@RequestMapping("couponDelete/{couponId}")
	public String couponDelete(@PathVariable int couponId) {

		System.out.println("쿠폰삭제 진입");
		Optional<Coupon> oCoupon = couponRepository.findById(couponId);
		couponRepository.delete(oCoupon.get());

		System.out.println("쿠폰삭제 완료");
		return "redirect:/super/couponManage";
	}

	// ============== 정현 쿠폰 수정 끝 ===============

	// ================== 정현 지점정보 수정요청 관리 시작 ==================
	@RequestMapping("modiBranchList")
	public String modiBranchList(Model model, HttpSession session) {

		model.addAttribute("userRole", session.getAttribute("userRole"));

		List<ModiBranch> modiBranchList = modiBranchRepository.findAll();
		// System.out.println("널이니?" + modiBranchList);
		model.addAttribute("modiBranchList", modiBranchList);

		return "super/branch/modiBranchList";
	}

	@RequestMapping("modiBranchOK")
	public String modiBranchOK(@RequestParam int BranchId) {
		System.out.println(BranchId);
		Optional<Branch> oBranch = branchRepository.findById(BranchId);
		Branch branch = oBranch.get();
		System.out.println("OK에서 오리진 브런치:" + branch);
		Optional<ModiBranch> oNewBranch = modiBranchRepository.findByOriginbranchId(BranchId);
		ModiBranch newBranch = oNewBranch.get();
		System.out.println("OK에서 요청들어온 브런치내용:" + newBranch);

		branch.setPhone(newBranch.getPhone());
		System.out.println("수정완료된 오리진 브런치:" + branch);
		branchRepository.save(branch);
		modiBranchRepository.delete(newBranch);
		return "redirect:/super/modiBranchList";
	}

	@RequestMapping("modiBranchNO")
	public String modiBranchNO(@RequestParam int BranchId) {
		Optional<ModiBranch> oNewBranch = modiBranchRepository.findByOriginbranchId(BranchId);
		ModiBranch newBranch = oNewBranch.get();
		System.out.println("NO에서 삭제할 요청된 브런치 잘왓니?" + newBranch);

		modiBranchRepository.delete(newBranch);

		return "redirect:/super/modiBranchList";
	}

	// ================== 정현 지점정보 수정요청 관리 끝 ==================

	// 통계 처리

	@GetMapping("/daily-stats")
	public String showDailyStats(Model model) {
		// 여기에서 일별 통계를 생성하는 코드를 호출하고 결과를 모델에 추가합니다.
		List<Reservation> rvList = reservationRepository.findAll();
		Map<String, Integer> dailyStats = stat.generateDailyStats(rvList);
		model.addAttribute("dailyStats", dailyStats);
		return "daily-stats"; // daily-stats.html 템플릿을 렌더링합니다.
	}

	@GetMapping("/weekly-stats")
	public String showWeeklyStats(Model model) {
		// 여기에서 요일별 통계를 생성하는 코드를 호출하고 결과를 모델에 추가합니다.

		return "weekly-stats"; // weekly-stats.html 템플릿을 렌더링합니다.
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

}
