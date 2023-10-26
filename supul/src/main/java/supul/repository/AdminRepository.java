package supul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer>{

	Admin findByAdminId(String adminId);

	Admin findByEmail(String email);

	List<Admin> findAll();

	void deleteByAdminId(String adminIdToDelete);

	Admin findByBranchName(String branchName);

}
