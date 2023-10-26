package supul.repository;

import java.math.BigDecimal;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import supul.model.Branch;
import supul.model.Reservation;
import supul.model.Thema;
import supul.model.User;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	boolean existsByUserId(int userId);

	// 검색 및 페이징처리
	Page<Reservation> findByUserName(String userName, Pageable pageable);
	Page<Reservation> findByUserNameContaining(String userName, Pageable pageable);
	Page<Reservation> findByUserNameContainingAndDate(String userName, LocalDate today, Pageable pageable);
	Page<Reservation> findByUserNameContainingAndDateBetween(String userName,  LocalDate start, LocalDate end, Pageable pageable);

	Page<Reservation> findByUserNameContainingAndNoShow(String userName, Boolean NOShow, Pageable pageable);
	Page<Reservation> findByUserNameContainingAndUserBlacklist(String userName, Boolean NOShow, Pageable pageable);
	Page<Reservation> findByUserNameAndUserBlacklist(String userName, Boolean NOShow, Pageable pageable);

	Page<Reservation> findByBranchName(String branch, Pageable pageable);
	List<Reservation> findByBranchName(String branch );
	List<Reservation> findByBranchNameAndDate(String branch,LocalDate today );
	Page<Reservation> findByBranchNameAndDate(String branch,LocalDate today, Pageable pageable );
	List<Reservation> findByDateBetween( LocalDate start, LocalDate end);
	Page<Reservation> findByDateBetween( LocalDate start, LocalDate end, Pageable pageable);
	List<Reservation> findByThemaNameAndDateBetween(String thema, LocalDate start, LocalDate end);
	List<Reservation> findByThemaNameAndDate(String thema, LocalDate start);
	List<Reservation> findByBranchNameAndDateBetween(String branch, LocalDate start, LocalDate end);
	Page<Reservation> findByBranchNameAndDateBetween(String branch, LocalDate start, LocalDate end, Pageable pageable);
	List<Reservation> findByUserBlacklist(Boolean b);
	Page<Reservation> findByUserBlacklist(Boolean b, Pageable pageable);

	Page<Reservation> findByBranchAndNoShow(Branch branch, Boolean NOShow, Pageable pageable);
	Page<Reservation> findByBranchNameAndUserBlacklist(String branch, Boolean NOShow, Pageable pageable);

	Page<Reservation> findByThemaNameContaining(String thema, Pageable pageable);
	List<Reservation> findByThemaName(String thema);

	Page<Reservation> findByBranchNameAndUserNameContainingAndDate(String branch, String useraName, LocalDate start, Pageable pageable);
	Page<Reservation> findByBranchNameAndUserNameContainingAndDateBetween(String branch, String useraName, LocalDate start, LocalDate end , Pageable pageable);
	Page<Reservation> findByBranchNameAndUserNameContaining(String branch, String useraName, Pageable pageable);

	Page<Reservation> findByBranchNameAndUserBlacklistAndUserName(String branch, Boolean NOShow, String useraName,
			Pageable pageable);

	List<Reservation> findByUserNameContaining(String themaName);

	Page<Reservation> findByDate(LocalDate today, Pageable pageable);
	List<Reservation> findByDate(String today, Pageable pageable);

	List<Reservation> findByDate(LocalDate today);
	List<Reservation> findByCancle(Boolean cancle);

	Page<Reservation> findByNoShow(Boolean NOShow, Pageable pageable);

	Reservation findByRvId(int rvId);

	Reservation findByRvNum(String rvNum);

	List<Reservation> findByUser(User user);

	// 현재 날짜포함하고 이후의 예약 데이터를 조회하는 메서드
	@Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.date >= :nowDate ORDER BY r.date ASC")
	List<Reservation> findAllUpcomingReservationsForUser(int userId, LocalDate nowDate);
	@Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.date >= :nowDate AND r.cancle = false ORDER BY r.date ASC")
	List<Reservation> findAllUpcomingNonCancelledReservationsForUser(int userId, LocalDate nowDate);
	// 현재 날짜포함하고 이후의 예약 데이터를 조회하는 메서드
	@Query("SELECT r FROM Reservation r WHERE r.date >= :nowDate ORDER BY r.date ASC")
	List<Reservation> findTodaydate(LocalDate nowDate);

	// 그냥 id로 모든예약 찾기
	List<Reservation> findAllByUserId(int userId);

	@Query("SELECT s.themaName ,SUM(s.price),s.rvDate FROM Reservation s WHERE s.date BETWEEN :startDate AND :endDate GROUP BY s.themaName,s.rvDate")
	List<Reservation> Total(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


	List<Reservation> findByThemaThemaId(int themaId);

	List<Reservation> findAllByBranch(Branch branch);

}
