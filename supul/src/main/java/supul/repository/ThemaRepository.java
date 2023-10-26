package supul.repository;


import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import supul.model.Branch;
import supul.model.Thema;






public interface ThemaRepository extends JpaRepository<Thema, Integer> {

	Page<Thema> findAllByTitleContaining(String themaname, Pageable pageable);
	List<Thema> findByRankingThemaName(String themaname);
	List<Thema> findByBranch(Branch branch);
	Page<Thema> findByBranchName(String branch , Pageable pageable);
	Thema findByTitle(Object title);
	Page<Thema> findByTitle(String title, Pageable pageable);
	Page<Thema> findByBranchNameAndTitleContaining(String branch ,String title, Pageable pageable);

}
  