package supul.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import supul.model.DeleteUser;
import supul.repository.DeleteUserRepository;

@Service
public class DeleteUserService {
    private final DeleteUserRepository deleteUserRepository;

    public DeleteUserService(DeleteUserRepository deleteUserRepository) {
        this.deleteUserRepository = deleteUserRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void copyAndDeleteUserData() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
        
        // 3개월 이전에 회원탈퇴한 사용자 정보를 가져옵니다.
        List<DeleteUser> usersToDelete = deleteUserRepository.findByDeletedAtBefore(threeMonthsAgo);

        for (DeleteUser deleteUser : usersToDelete) {
            // 회원탈퇴 날짜로부터 3개월 이상 경과한 경우 해당 데이터를 삭제합니다.
            if (deleteUser.getDeletedAt().plusMonths(3).isBefore(LocalDateTime.now())) {
                deleteUserRepository.delete(deleteUser);
            }
        }
    }
}
