package supul.repository;


import org.springframework.data.jpa.repository.JpaRepository;




import supul.model.RvPay;


public interface RvPayRepository extends JpaRepository<RvPay, Integer> {

	RvPay findByImpUidexe(String impUid);
} 
  