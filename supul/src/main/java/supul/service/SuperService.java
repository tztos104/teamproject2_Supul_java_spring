package supul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import supul.model.Super;
import supul.repository.SuperRepository;

import java.util.List;

@Service
public class SuperService {
	
	@Autowired
    private SuperRepository superRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private HttpSession httpSession;
    
    @Autowired
    private HttpServletRequest request;

    // 슈퍼 계정 생성 및 저장
    public void createSuperUser() {
        Super superUser = new Super();
        superUser.setSuperId("super");
        superUser.setSuperPw("super");
        // 필요한 다른 필드 설정

        superRepository.save(superUser);
    }

    @Transactional
    public boolean doesSuperUserExist() {
        List<?> resultList = entityManager.createQuery("SELECT 1 FROM Super WHERE superId = 'super'")
                .setMaxResults(1)
                .getResultList();
        return !resultList.isEmpty();
    }

    // 슈퍼 계정 로그인
    public boolean loginSuperUser(String superId, String superPw) {
        Super superUser = superRepository.findBySuperId(superId);
        if (superUser != null && superUser.getSuperPw().equals(superPw)) {
            // 슈퍼 계정 정보를 세션에 저장 (사용자 역할만 저장)
            httpSession.setAttribute("userRole", "super");
            httpSession.setAttribute("superLoggedIn", true); // 로그인 성공 시 true로 설정
            return true; // 로그인 성공
        }
        return false; // 로그인 실패
    }



    // 슈퍼 계정 로그아웃
    public void logoutSuperUser() {
        httpSession.removeAttribute("superUser");
        httpSession.removeAttribute("superLoggedIn"); // 로그아웃 시 superLoggedIn 속성 제거
    }

    
    

    // 슈퍼 계정 로그인 여부 확인
    public boolean isSuperLoggedIn() {
        // HttpServletRequest에서 세션을 가져옵니다.
        HttpSession session = request.getSession(false); // 세션이 없을 경우 null 반환

        // 세션에서 "superLoggedIn" 속성을 가져와서 슈퍼 사용자 로그인 여부를 확인합니다.
        if (session != null) {
            Boolean superLoggedIn = (Boolean) session.getAttribute("superLoggedIn");
            return superLoggedIn != null && superLoggedIn;
        }

        // 세션이 없거나 "superLoggedIn" 속성이 없는 경우 로그인되지 않은 것으로 간주합니다.
        return false;
    }

}
