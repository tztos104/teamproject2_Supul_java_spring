package supul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import supul.model.Super;
import supul.repository.SuperRepository;

@Component
public class SuperUserInitializer {

    @Autowired
    private SuperRepository superRepository;

    // 애플리케이션 실행 시 슈퍼 계정을 데이터베이스에 저장
    public void initSuperUser() {
        Super superUser = new Super();
        superUser.setSuperId("super");
        superUser.setSuperPw("super");
        // 필요한 다른 필드 설정

        // 데이터베이스에 슈퍼 계정 정보 저장
        superRepository.save(superUser);
    }

    // 슈퍼 계정으로 로그인 시 아이디와 비밀번호를 검증하는 메서드
    public boolean authenticateSuperUser(String superId, String superPw) {
        Super superUser = superRepository.findBySuperId(superId);
        if (superUser != null && superUser.getSuperPw().equals(superPw)) {
            return true; // 슈퍼 계정으로 인증 성공
        }
        return false; // 슈퍼 계정으로 인증 실패
    }
}


