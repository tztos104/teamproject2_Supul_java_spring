package supul.repository;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.Branch;
import supul.model.Reservation;
import supul.model.Reservation_backup;



public interface ReservationBackUpRepository extends JpaRepository<Reservation_backup, Integer> {

	Page<Reservation_backup> findByBranchName(String  branchName, Pageable pageable);
	Page<Reservation_backup> findByUserNameContaining(String userName, Pageable pageable);
	Page<Reservation_backup> findByBranchNameAndUserNameContaining(String branchName, String useraName, Pageable pageable);

}
