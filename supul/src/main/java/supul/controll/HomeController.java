package supul.controll;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import supul.mapper.BranchMapper;
import supul.mapper.HomepageMapper;
import supul.mapper.ThemaMapper;
import supul.model.Branch;
import supul.model.HomepageDTO;
import supul.model.Thema;
import supul.model.User;
import supul.model.Datacreate;
import supul.model.board.NoticeBoard;
import supul.model.board.ReviewBoard;
import supul.repository.ThemaRepository;
import supul.repository.board.NoticeRepository;
import supul.repository.board.ReviewRepository;
import supul.service.SaleService;

@Controller
public class HomeController {
	// 홈페이지 소개
	@Autowired
	HomepageMapper homepageMapper;
	@Autowired
	BranchMapper branchMapper;
	@Autowired
	ThemaMapper themaMapper;
	@Autowired
	  ThemaRepository themaRepository;
	@Autowired
	   NoticeRepository noticeRepository;
	@Autowired
	   ReviewRepository reviewRepository;
	@Autowired
	SaleService sale;

	@RequestMapping("")
	public String firstMain() {
		return "firstMain";
	}
	
  
	@RequestMapping("/main")
	public String homeGo(Model model, HttpSession session) {
	      // 사용자 정보 가져오기
	      User user = (User) session.getAttribute("user"); // User 객체를 가져옴
	      List<Thema> thema = themaRepository.findAll();
	      List<NoticeBoard> notice = noticeRepository.findTop5ByOrderByRegDateDesc();
	      List<ReviewBoard> review = reviewRepository.findTop5ByOrderByRegDateDesc();
	     
	      //sale.generateRandomReservations();
	      model.addAttribute("thema", thema);
	      model.addAttribute("notice", notice);
	      model.addAttribute("review", review); 

	      // 세션등록 ==start==
	      model.addAttribute("userRole", session.getAttribute("userRole"));
	      model.addAttribute("admin", session.getAttribute("admin"));
	      model.addAttribute("adminBn", session.getAttribute("adminBn"));
	      model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));

	      // 세션등록 ==end==
	      model.addAttribute("user", user); // User 객체를 모델에 추가
	      return "main";
	   }


	@GetMapping("home/intro")
	public String homeIntro(Model model, HttpSession session) {
		// 데이터베이스에서 정보를 조회
		List<HomepageDTO> homepageIntroList = homepageMapper.selectHomepageIntro();

		// 모델에 데이터를 추가하여 HTML 페이지로 전달
		model.addAttribute("homepageIntroList", homepageIntroList);
		model.addAttribute("username", session.getAttribute("username"));
		// 세션등록 ==start==
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션등록 ==end==
		// Thymeleaf 템플릿 이름 반환
		return "home/intro";
	}

	// 매장 안내 페이지

	// 지점관리

	@GetMapping("home/branchInfo/{branchName}")
	public String showBranchInfo(@PathVariable("branchName") String branchName, Model model, HttpSession session) {
		// 선택한 Branch의 이름을 기반으로 해당 Branch 정보를 가져옴
		if (branchName.equals("null")) {
			return "redirect:/admin/login";
		}

		Branch selectedBranch = branchMapper.selectByName(branchName);
		List<Branch> branches = branchMapper.selectList();

		if (selectedBranch == null) {
			if (selectedBranch == null) {
				selectedBranch = branches.get(0);
			}
			branchName = selectedBranch.getName();
		}
		// 세션 등록 ==start==
		model.addAttribute("adminBn", session.getAttribute("adminBn"));
		model.addAttribute("adminBranchId", session.getAttribute("adminBranchId"));
		model.addAttribute("admin", session.getAttribute("admin"));
		model.addAttribute("userRole", session.getAttribute("userRole"));
		model.addAttribute("user", session.getAttribute("user"));
		// 세션 등록 ==end==
		model.addAttribute("branches", branches);
		model.addAttribute("username", session.getAttribute("username"));
		model.addAttribute("selectedBranch", selectedBranch);

		// 해당 지점과 관련된 테마 정보를 가져와 모델에 추가
		List<Thema> themas = themaMapper.brnList(selectedBranch.getBranchId());
		model.addAttribute("themas", themas);

		return "home/storeInfo";
	}

}
