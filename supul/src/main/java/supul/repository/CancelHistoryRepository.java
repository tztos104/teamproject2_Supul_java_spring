package supul.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.CancelHistory;
import supul.model.Payment;





public interface CancelHistoryRepository extends JpaRepository<CancelHistory, Integer> {

	

}
