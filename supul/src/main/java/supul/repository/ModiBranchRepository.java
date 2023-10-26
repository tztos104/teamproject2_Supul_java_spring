package supul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.ModiBranch;

public interface ModiBranchRepository extends JpaRepository<ModiBranch, Integer> {

	Optional<ModiBranch> findByOriginbranchId(int branchId);

}
