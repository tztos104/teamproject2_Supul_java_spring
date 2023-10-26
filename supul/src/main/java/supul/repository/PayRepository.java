package supul.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import supul.model.Pay;


public interface PayRepository extends JpaRepository<Pay, Integer> {
	Pay findByReservationRvId(int rvid);

	Page<Pay> findAll(Pageable pageable);

	@Query("SELECT s.themaName, SUM(s.price), MAX(s.userName)FROM Reservation s GROUP BY s.themaName")
	List<Object[]> thema_price();

	@Query("SELECT s.themaName, SUM(s.price), MAX(s.userName)FROM Reservation s WHERE s.date BETWEEN :startDate AND :endDate GROUP BY  s.themaName")
	List<Object[]> thema_priceDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	
	
	
	@Query("SELECT s.themaName, SUM(s.price),s.rvDate FROM Reservation s WHERE s.date BETWEEN :startDate AND :endDate GROUP BY  s.themaName ,s.rvDate")
	List<Object[]> Total(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	
	@Query("SELECT s.branch.name, s.themaName, SUM(s.price) FROM Reservation s WHERE s.date BETWEEN :startDate AND :endDate GROUP BY  s.branch.name, s.themaName")
	List<Object[]> themaTotal(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	
	@Query("SELECT s.branch.name ,SUM(s.price) FROM Reservation s WHERE s.date BETWEEN :startDate AND :endDate GROUP BY  s.branch.name")
	List<Object[]> branchTotal(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	
	@Query("SELECT SUM(s.price) FROM Reservation s WHERE s.date BETWEEN :startDate AND :endDate")
	BigDecimal TotalSales(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
 
	
} 
  