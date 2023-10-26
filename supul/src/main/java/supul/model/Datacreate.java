package supul.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;
import supul.repository.BranchRepository;
import supul.repository.ReservationRepository;
import supul.repository.ThemaRepository;
import supul.repository.UserRepository;

@Data
public class Datacreate {
	private  final int NUM_RESERVATIONS_TO_GENERATE = 10;
	 @Autowired
	   ReservationRepository reservationRepository;
	   @Autowired
	  	UserRepository userRepository;
	  	@Autowired
	  	BranchRepository branchRepository;
	  	@Autowired
	  	ThemaRepository themaRepository;
	  	 public void generateRandomReservations() {
       
	        Random random = new Random();

	        for (int i = 0; i < NUM_RESERVATIONS_TO_GENERATE; i++) {
	        	 int min = 10000000; // 최소값 (8자리 숫자의 최소값)
	             int max = 99999999; // 최대값 (8자리 숫자의 최대값)
	             int randomNumber = random.nextInt(max - min + 1) + min;
	        	Reservation reservation = new Reservation();
	        	 Thema thema = themaRepository.findById(random.nextInt(30) + 1).orElse(null);
		        reservation.setThema(thema);
	        	reservation.setRvNum(Integer.toString(randomNumber));
	          
	            reservation.setThemaName(thema.getTitle()); // 테마 이름은 무작위로 설정
	           
	            
	            LocalTime time = thema.getTimetable().get(random.nextInt(5));
	            reservation.setTime(time);

	            LocalDate date = LocalDate.now().minusDays(random.nextInt(90));
	            reservation.setDate(date);

	            reservation.setRvPeople(random.nextInt(3) + 1); // 1부터 10까지 무작위 인원 수 설정
	            reservation.setRvPrice(thema.getRvPrice()); // 50부터 200까지 무작위 가격 설정
	            reservation.setPrice(thema.getPrice());

	            // 사용자, 지점, 테마 엔티티 설정 (실제로는 엔티티를 검색해서 설정)
	            User user = userRepository.findById(random.nextInt(48) + 1).orElse(null);
	            reservation.setUser(user);
	            reservation.setUserName(user.getUserName());
	           
	            reservation.setBranch(thema.getBranch());

	          

	            reservation.setPaid(false);
	            reservation.setNoShow(false);
	            reservation.setCancle(false);
	            reservation.setPayCancle(false);
	            reservation.setRvdelete(false);
	            reservation.setReviewYN(false);

	            reservationRepository.save(reservation); // 예약 저장
	        }
	    
}
}
