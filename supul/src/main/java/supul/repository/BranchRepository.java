package supul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.Branch;
import supul.model.Reservation;

import java.util.List;
import supul.model.Thema;



public interface BranchRepository extends JpaRepository<Branch, Integer> {
	Page<Branch> findByNameContaining(String name, Pageable pageable);
	List<Branch> findByNameContaining(String name);
	Branch findByName(String name);
	 Page<Branch> findAll(Pageable pageable);

}
