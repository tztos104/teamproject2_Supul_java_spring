package supul.controll;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import supul.mapper.BranchMapper;
import supul.mapper.ThemaMapper;
import supul.mapper.UserMapper;
import supul.model.Admin;
import supul.model.Branch;
import supul.model.ModiBranch;
import supul.model.PageData;
import supul.model.Pay;
import supul.model.Ranking;
import supul.model.Reservation;
import supul.model.Thema;
import supul.model.User;
import supul.repository.AdminRepository;
import supul.repository.BranchRepository;
import supul.repository.ModiBranchRepository;
import supul.repository.PayRepository;
import supul.repository.RankingRepository;
import supul.repository.ReservationRepository;
import supul.repository.ThemaRepository;
import supul.repository.UserRepository;
import supul.repository.board.NoticeRepository;

import supul.service.SaleService;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	AdminRepository adminRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	ThemaRepository themaRepository;
	@Autowired
	BranchRepository branchRepository;
	@Autowired
	RankingRepository rankingRepository;
	@Autowired
	PayRepository payRepository;
	@Autowired
	SaleService stat;
	@Autowired
	BranchMapper branchMapper;
	@Autowired
	UserMapper usermapper;
	@Autowired
	ThemaMapper themaMapper;
	@Autowired
	NoticeRepository boardNRepository;
	@Autowired
	ModiBranchRepository modiBranchRepository;
	@Resource
	ThemaMapper tmMapper;

	@RequestMapping("")
	public String adminmainmm(Model model, HttpSession session,
			@PageableDefault(size = 5, sort = "rvId", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "time") String sortBy,
			@RequestParam(required = false) String branchName,
			@RequestParam(required = false, defaultValue = "전체") String title) {
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.ASC, sortBy));
		branchName = (String) session.getAttribute("adminBn");
		LocalDate today = LocalDate.now();
		Page<Reservation> todayReservations = reservationRepository.findByBranchNameAndDate(branchName, today, pageable);
		List<Reservation> yesterReservations = reservationRepository.findByBranchNameAndDate(branchName,
				LocalDate.now().minusDays(1));
		Map<String, Integer> themaTotal = stat.themaTotal(yesterReservations);
		model.addAttribute("yesterday", today.minusDays(1));
		
		List<Reservation> rvList = reservationRepository.findByBranchName(branchName);
		Map<String, Integer> dailyStats = stat.generateDailyStats(rvList);
		Map<String, Integer> sortedDailyStats = dailyStats.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		Map<String, Integer> themaTotal2 = stat.themaTotal(rvList);
		
		Object topSellingTheme = themaTotal2.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
				.orElse("없음");
		model.addAttribute("themaTotal", themaTotal);
		Thema topThema = themaRepository.findByTitle(topSellingTheme);
		model.addAttribute("topThema", topThema);
		model.addAttribute("dailyStats", sortedDailyStats);
		Map<String, Integer> weeklyStats = stat.generateWeeklyStats(rvList);
		model.addAttribute("weeklyStats", weeklyStats);
		Map<String, Integer> MonthlyStats = stat.MonthlyStats(rvList);
		model.addAttribute("MonthlyStats", MonthlyStats);

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		model.addAttribute("todayReservations", todayReservations);

		return "admin/admin_main";
	}

	@GetMapping("/logout")
	public String adminLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/main";
	}

	@GetMapping("/login")
	public String adminLoginForm() {
		return "admin/admin_login";
	}

	@PostMapping("/login")
	public String processAdminLoginForm(PageData pageData, Model model, @RequestParam String adminId,
			@RequestParam String adminPw, HttpSession session) {
		Admin admin = adminRepository.findByAdminId(adminId);
		System.out.println("어드민" + admin);

		if (session.getAttribute("user") != null || session.getAttribute("super") != null) {
			session.invalidate();
		}

		if (admin != null && admin.getAdminPw().equals(adminPw) && admin.getAdminId().equals(adminId)) {

			// 영준 수정 2023-0923
			Branch branch = branchRepository.findByName(admin.getBranchName());
			if (branch != null) {
				session.setAttribute("adminId", admin.getAdminId());
				session.setAttribute("admin", admin);
				session.setAttribute("adminRole", "admin");
				session.setAttribute("adminBranchId", branch.getBranchId());
				session.setAttribute("adminBn", admin.getBranchName());
				session.setAttribute("adminName", admin.getName());
				return "redirect:/admin";
			} else {
				pageData.setMsg(admin.getBranchName() + "을 찾을 수 없습니다.지점을 등록하거나 지점이름이 맞는지 확인해주세요.");
				pageData.setGoUrl("/admin/login");
				return "inc/alert";
			}

		}

		pageData.setMsg("아이디 또는 비밀번호가 틀렸습니다.");
		pageData.setGoUrl("/admin/login");

		// 관리자가 없거나 비밀번호가 일치하지 않는 경우
		return "inc/alert"; // 로그인 실패 시 로그인 페이지로 다시 이동
	}

	// =============================== 정현 수정 시작 ===============================
	// 지점정보 수정요청 폼 보기
	@GetMapping("/branch/modiBranchForm/{branchId}")
	public String modiBranchForm(@PathVariable("branchId") String branchId, Model model, HttpSession session) {
		if (branchId.equals(null) || branchId.equals("null")) {
			return "redirect:/admin/login";
		}
		Branch branch = null;
		if (branchId != null || branchId == "null") {
			branch = branchMapper.selectById(Integer.parseInt(branchId));
			model.addAttribute("branch", branch);
			model.addAttribute("branchId", branchId);
			model.addAttribute("branchName", branch.getName());
		}

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		return "admin/modiBranchForm";
	}

	// 지점정보 수정요청 처리
	@PostMapping("/branch/modiBranchForm")
	public String modiBranchFormReg(@ModelAttribute ModiBranch modiBranch, Model model, HttpSession session,
			@RequestParam String originbranchId, @RequestParam String originbranchname) {

		Optional<ModiBranch> oModiBranch = modiBranchRepository.findByOriginbranchId(Integer.parseInt(originbranchId));
		if (oModiBranch.isEmpty()) {
			// 요청된 rqBranchId에 해당하는 엔티티가 존재하지 않는 경우
			modiBranch.setOriginbranchId(Integer.parseInt(originbranchId));
			modiBranch.setOriginbranchname(originbranchname);
			modiBranchRepository.save(modiBranch);
		} else {
			// 요청된 rqBranchId에 해당하는 엔티티가 이미 존재하는 경우
			ModiBranch originBranch = oModiBranch.get();
			originBranch.setPhone(modiBranch.getPhone());
			modiBranchRepository.save(originBranch);
		}
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		return "redirect:/admin";
	}

	// =============================== 정현 수정 끝 ===============================

	@RequestMapping("rankList/{branchName}/{title}")
	String list(Model model, Ranking rd, Branch bd, HttpSession session, Thema tm, @PathVariable String branchName,
			@PathVariable String title) {

		// 랭킹 데이터 전체
		List<Ranking> rankData = rankingRepository.findAll();
		List<Ranking> sortedRankData = rankData.stream()
				.sorted(Comparator.comparingInt(Ranking::getThemaRank).reversed()).collect(Collectors.toList());
		List<Branch> brnList = themaMapper.brList(bd);
		List<Thema> themaList = themaRepository.findAll();
		List<Ranking> filterRankData = new ArrayList<>();
		for (Ranking rank : sortedRankData) {
			if ("전체".equals(branchName) || branchName.equals(rank.getBranchName())) {
				if ("전체".equals(title) || title.equals(rank.getThemaName())) {
					filterRankData.add(rank);
				}
			}
		}

		model.addAttribute("filterRankData", filterRankData);
		model.addAttribute("rankData", rankData);
		model.addAttribute("brnList", brnList);
		model.addAttribute("themaList", themaList);

		// 해당하는 아이디만 보이는 데이터
		model.addAttribute("branchName", branchName); // 선택된 branchName을 모델에 추가
		model.addAttribute("title", title);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		return "admin/admin_RankList";
	}

	@GetMapping("rank/insert")
	public String insert(Model model, Ranking rd, Thema td, Branch bd, @RequestParam int rvId,
			@PageableDefault(size = 8, sort = "title", direction = Direction.DESC) Pageable pageable,
			HttpSession session) {

		List<Thema> themaList = themaRepository.findAll();
		List<Branch> brnList = themaMapper.brList(bd);
		List<Ranking> ranking = rankingRepository.findAll();
		Optional<Reservation> Orv = reservationRepository.findById(rvId);
		Reservation rv = Orv.get();
		model.addAttribute("brnList", brnList);
		model.addAttribute("themaList", themaList);
		model.addAttribute("rv", rv);
		model.addAttribute("pageData", new PageData());

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		return "admin/rankForm";
	}

	@PostMapping("rank/insert")
	String insertReg(Ranking rd, int rvId, PageData pageData, HttpSession session) {
		Reservation rv = reservationRepository.findByRvId(rvId);
		rd.setPay(rv.getPay());
		rd.setThemaName(rv.getThemaName());
		rd.setThema(rv.getThema());
		rd.setUser(rv.getUser());
		String branchName = (String) session.getAttribute("adminBn");
		rankingRepository.save(rd);

		List<Ranking> ranking = rankingRepository.findByThemaThemaId(rv.getThema().getThemaId());
		ranking.sort(Comparator.comparing(Ranking::getMinutes).reversed().thenComparing(Ranking::getSeconds));
		for (Ranking rank : ranking) {
			rank.calcRank(ranking);
			System.out.println("rank" + rank.getThemaRank());
			rankingRepository.save(rank);
		}
		pageData.setMsg("작성되었습니다.");
		pageData.setGoUrl("/admin/rankList/" + branchName + "/전체");

		return "inc/alert";
	}

	@GetMapping("list")
	String rvlist(@RequestParam(required = false) String userName,
			@PageableDefault(size = 15, sort = "rvId", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "rvDate") String sortBy, Model model, HttpSession session) {
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.DESC, sortBy));
		Page<Reservation> searchList;

		searchList = stat.searchTm(pageable);

		List<Branch> branchList = branchRepository.findAll(); // BranchService를 이용하여 모든 지점을 가져오는 예시
		model.addAttribute("branchList", branchList);

		model.addAttribute("searchList", searchList);
		model.addAttribute("pageable", pageable);
		model.addAttribute("userName", userName);
		model.addAttribute("sortBy", sortBy);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		return "admin/reservation";
	}

	@GetMapping("/rvList/{branchName}")
	public String getAll(@RequestParam(required = false) String userName, @PathVariable String branchName,
			@PageableDefault(size = 15, sort = "date", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "date") String sortBy, Model model, HttpSession session,
			@RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection) {

		Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		if (branchName.equals("null")) {
			return "redirect:/admin/login";
		}

		List<Branch> branchList = branchRepository.findAll(); // BranchService를 이용하여 모든 지점을 가져오는 예시
		model.addAttribute("branchList", branchList);

		Branch branch = branchRepository.findByName(branchName);
		Page<Reservation> blackList = reservationRepository.findByNoShow(true, pageable);
		Page<Reservation> searchList;
		if (branchName.equals("전체") || branchName == null) {

			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				searchList = reservationRepository.findAll(pageable);
			} else {
				searchList = reservationRepository.findByUserName(userName, pageable);
			}
		} else {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				// 사용자 이름과 지점으로 검색하고 페이징을 적용합니다.
				searchList = reservationRepository.findByBranchName(branchName, pageable);
			} else {
				searchList = reservationRepository.findByBranchNameAndUserNameContaining(branchName, userName,
						pageable);
			}
		}

		model.addAttribute("tomorrow", LocalDate.now().plusDays(1));
		model.addAttribute("today", LocalDate.now());
		model.addAttribute("searchList", searchList);
		model.addAttribute("blackList", blackList);
		model.addAttribute("pageable", pageable);
		model.addAttribute("userName", userName);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("branchName", branchName);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		return "admin/reservation";
	}

	// 노쇼처리
	@PostMapping("/rvList/{branchName}/{rvId}/updateNoShow")
	public String updateNoShowStatus(@PathVariable("rvId") int rvId, @PathVariable String branchName,
			@RequestParam int page) {
		Reservation reservation = reservationRepository.findByRvId(rvId);

		if (reservation != null) {
			// 사용자 정보 가져오기
			User user = userRepository.findById(reservation.getUser().getId())
					.orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));

			// 노쇼 상태를 토글 (true -> false, false -> true)
			boolean newNoShowStatus = !reservation.isNoShow();
			stat.updateNoShowStatus(rvId, newNoShowStatus);

			if (user != null) {
				int noShowCount = user.getNoShowCount();
				// 노쇼 횟수 증가 또는 감소
				if (newNoShowStatus) {
					// 새로운 상태가 노쇼인 경우
					noShowCount++;
				} else {
					// 새로운 상태가 노쇼 취소인 경우
					noShowCount--;
				}
				user.setNoShowCount(noShowCount);

				// 노쇼 횟수가 3회 이상이면 예약 못하게 처리
				if (noShowCount >= 3) {
					user.setBlacklist(true);
				} else {
					user.setBlacklist(false); // 노쇼 횟수가 3회 미만인 경우 블랙리스트 해제
				}

				userRepository.save(user);
			}
		}

		return "redirect:/admin/rvList/{branchName}?page=" + page; // 노쇼 상태 업데이트 후 다시 목록 페이지로 리다이렉트
	}

	@PostMapping("/rvList/{branchName}/{rvId}/alert2")
	public String updatealert2(@PathVariable("rvId") int rvId, @PathVariable("branchName") String branchName,
			Model model, PageData pageData, @RequestParam int page) {
		pageData.setMsg("결제취소 하시겠습니까?");
		pageData.setGoUrl("/admin/rvList/" + branchName + "/" + rvId + "/updatepaid?page=" + page);

		return "inc/alert";
	}

	// 결제처리
	@GetMapping("/rvList/{branchName}/{rvId}/updatepaid")
	public String updatePaidStatus(@PathVariable("rvId") int rvId, @PathVariable("branchName") String branchName,
			Model model, HttpSession session,PageData pageData, @RequestParam int page) {

		Reservation a = stat.pay(rvId);
		
		
		if (a.isPaid() == false) {
			stat.updatePaidStatus(rvId, true);
			Pay b = new Pay();
			b.setReservation(a);
			b.setTotalprice(a.getPrice());
			b.setSaleDate(LocalDateTime.now());
			payRepository.save(b);
		} else {
			stat.updatePaidStatus(rvId, false);

			Pay b = payRepository.findByReservationRvId(rvId);
			payRepository.deleteById(b.getPayId());
		}

		return "redirect:/admin/rvList/{branchName}?page=" + page;
	}

	@PostMapping("/rvList/{branchName}/{rvId}/alert")
	public String updatealert(@PathVariable("rvId") int rvId, HttpSession session,
			@PathVariable("branchName") String branchName, Model model, PageData pageData, @RequestParam int page) {
		pageData.setMsg("결제완료 되었습니다.");
		pageData.setGoUrl("/admin/rvList/" + branchName + "/" + rvId + "/updatepaid?page=" + page);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		return "inc/alert";
	}

	@GetMapping("/blackList/{branchName}")
	public String blacklistbranch(@RequestParam(required = false) String userName, @PathVariable String branchName,
			@PageableDefault(size = 15, sort = "rvId", direction = Direction.DESC) Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "rvId") String sortBy,
			@RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,

			Model model, HttpSession session) {
		// 정렬을 적용합니다. (Spring Data JPA의 PageRequest 객체를 사용)
		Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		if (branchName.equals("null")) {
			return "redirect:/admin/login";
		}

		List<Branch> branchList = branchRepository.findAll(); // BranchService를 이용하여 모든 지점을 가져오는 예시
		model.addAttribute("branchList", branchList);
		List<User> userList = userRepository.findByBlacklist(true);
		Branch branch = branchRepository.findByName(branchName);

		Page<Reservation> searchList;
		if (branchName.equals("전체") || branchName == null) {

			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				searchList = reservationRepository.findByUserBlacklist(true, pageable);
			} else {
				searchList = reservationRepository.findByUserNameAndUserBlacklist(userName, true, pageable);
			}
		} else {
			if (userName == null || userName.isEmpty() || userName.equals("null")) {
				searchList = reservationRepository.findByBranchNameAndUserBlacklist(branchName, true, pageable);
			} else {
				searchList = reservationRepository.findByBranchNameAndUserBlacklistAndUserName(branchName,
						true, userName, pageable);
			}
		}

		model.addAttribute("userList", userList);
		model.addAttribute("searchList", searchList);
		model.addAttribute("pageable", pageable);
		model.addAttribute("userName", userName);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("branchName", branchName);
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==
		return "admin/blackList";
	}

	// ==================== 정현 수정 ====================
	@GetMapping("rank/modify/{branchName}/{rankId}")
	String modify(Model model, @PathVariable int rankId, @RequestParam int rvId, Branch bd, Thema td,
			@PathVariable String branchName, HttpSession session) {

		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		// 세션등록 ==end==

		List<Thema> themaList = tmMapper.tmList(td);
		List<Branch> brnList = tmMapper.brList(bd);
		Reservation rv = reservationRepository.findByRvId(rvId);
		model.addAttribute("rv", rv);
		Ranking modiData = usermapper.rankId(rankId);
		model.addAttribute("username", session.getAttribute("username"));
		model.addAttribute("brnList", brnList);
		model.addAttribute("themaList", themaList);
		model.addAttribute("ranking", modiData);
		model.addAttribute("user", session.getAttribute("user"));
		return "admin/rankModify";
	}

	@PostMapping("rank/modify/{branchName}/{rankId}")
	String modifyReg(Ranking rd, PageData pageData, HttpSession session, @RequestParam int rvId,
			@PathVariable String branchName) {
		Reservation rv = reservationRepository.findByRvId(rvId);
		rd.setPay(rv.getPay());
		rd.setThemaName(rv.getThemaName());
		rd.setThema(rv.getThema());
		rd.setUser(rv.getUser());

		rankingRepository.save(rd);
		List<Ranking> ranking = rankingRepository.findByThemaThemaId(rv.getThema().getThemaId());
		ranking.sort(Comparator.comparing(Ranking::getMinutes).reversed().thenComparing(Ranking::getSeconds));
	
		for (Ranking rank : ranking) {
			rank.calcRank(ranking);
			System.out.println("rank" + rank.getThemaRank());
			rankingRepository.save(rank);
		}
		pageData.setMsg("수정 되었습니다.");
		pageData.setGoUrl("/admin/rankList/" + branchName + "/전체");

		return "inc/alert";
	}

	@RequestMapping("rank/delete/{rankId}/{branchName}")
	String deleteReg(@PathVariable("rankId") int rankId, @PathVariable("branchName") String branchName,
			HttpSession session, Model model, PageData pageData) {
		model.addAttribute("user", session.getAttribute("user"));
		Ranking delData = usermapper.rankId(rankId);
		int cnt = usermapper.rankdel(delData);
		if (cnt > 0) {

			pageData.setMsg("삭제되었습니다.");
			pageData.setGoUrl("/admin/rankList/" + branchName + "/전체");

		}

		return "inc/alert";
	}

	// ==================== 정현 수정 끝 ====================
}
