package supul.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import supul.model.Coupon;

import supul.model.User;


public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	boolean existsByCouponNum(String cpnum);

	// user 통해서 Coupon리스트를 뽑아오고 사용했는지 안했는지, 발급날짜 순으로 정렬
	List<Coupon> findByUserAndUsedIsFalseOrderByRegDate(User user);

	List<Coupon> findByUserAndUsedIsTrueOrderByRegDate(User user);

	// 쿠폰 번호로 특정 쿠폰 찾기
	Coupon findByCouponNum(String couponNum);
	
	//현재 시간보다 3개월 이전의 쿠폰찾기
	List<Coupon> findByRegDateBefore(LocalDateTime currentDate);
	
	//현재 시간보다 3개월 이전의 미사용 쿠폰찾기
	List<Coupon> findByRegDateBeforeAndUsedIsFalse(LocalDateTime currentDate);
	
	//사용하지않은 모든 쿠폰 불러오기
	List<Coupon> findByUsedFalse();
	
	//사용한 모든 쿠폰 불러오기
	List<Coupon> findByUsedTrue();

}
  