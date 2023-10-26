package supul.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import supul.mapper.ThemaMapper;
import supul.model.Branch;
import supul.model.Pay;
import supul.model.Reservation;
import supul.model.Thema;
import supul.model.User;
import supul.repository.BranchRepository;
import supul.repository.PayRepository;
import supul.repository.ReservationRepository;
import supul.repository.ThemaRepository;
import supul.repository.UserRepository;

@Service
public class SaleService {

	@Resource
	private ReservationRepository sales;

	@Resource
	private BranchRepository branch;
	@Resource
	ThemaRepository themaRepository;
	@Autowired
	ThemaMapper themaMapper;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PayRepository  payRepository;

	public Page<User> searchUser(String userName, Pageable pageable) {
		return userRepository.findByUserNameContaining(userName, pageable);
	}

	public List<LocalTime> getTimetableByThemaId(int themaId) {
		return themaMapper.timetableList(themaId);
	}

	ArrayList<String> chkrvstatus(LocalDate date, int thema_id) {

		return themaMapper.chkrvstatus(date);
	};

	// 노쇼 처리
	public void updateNoShowStatus(int rvId, boolean noShow) {
		Reservation reservation = sales.findById(rvId).orElse(null);
		if (reservation != null) {
			reservation.setNoShow(noShow);
			sales.save(reservation);

		}
	}

	// 결제처리
	public void updatePaidStatus(int rvId, boolean paid) {
		Reservation reservation = sales.findById(rvId).orElse(null);
		if (reservation != null) {
			reservation.setPaid(paid);
			sales.save(reservation);
		}
	}

	public Reservation pay(int rvId) {
		return sales.findByRvId(rvId);
	}

	// 예약 리스트, 검색리스트

	public Page<Reservation> searchTm(Pageable pageable) {
		return sales.findAll(pageable);
	}

	@Transactional
	public List<Reservation> searchTm1(String User) {
		return sales.findByUserNameContaining(User);
	}

	public Page<Thema> themaList(Pageable pageable) {
		return themaRepository.findAll(pageable);
	}

	// 브런치리스트, 검색리스트
	public Page<Branch> brList(Pageable pageable) {
		return branch.findAll(pageable);
	}

	public Page<Branch> brSertchList(String name, Pageable pageable) {
		return branch.findByNameContaining(name, pageable);
	}

	public List<Branch> brSertchList(String name) {
		return branch.findByNameContaining(name);
	}

	public Page<Reservation> paging(Pageable pageable) {
		return sales.findAll(pageable);
	}

	// 일별 매출
	public Map<String, Integer> generateDailyStats(List<Reservation> reservations) {
	    Map<String, Integer> dailyStats = new HashMap<>();

	    // 예약 데이터를 일자별로 그룹화
	    Map<LocalDate, List<Reservation>> reservationsByDate = reservations.stream()
	            .filter(reservation -> !reservation.isCancle()) // 취소가 아닌 예약만 고려
	            .collect(Collectors.groupingBy(Reservation::getDate));

	    // 각 일자의 매출 합계 계산
	    for (LocalDate date : reservationsByDate.keySet()) {
	        List<Reservation> reservationsOnDate = reservationsByDate.get(date);
	        int totalSales = reservationsOnDate.stream().mapToInt(Reservation::getPrice).sum();
	        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        dailyStats.put(formattedDate, totalSales);
	    }

	    return dailyStats;
	}

	// 주별 매출
	public Map<String, Integer> generateWeeklyStats(List<Reservation> reservations) {
		Map<String, Integer> weeklyStats = new LinkedHashMap<>();

	    // 요일별로 매출 합계 계산
	    String[] daysOfWeek = {"월요일","화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};

	    Calendar calendar = Calendar.getInstance();
	    for (Reservation reservation : reservations) {
	    	if (reservation.isCancle()) { // 취소된 예약은 무시
	            continue;
	        }
	        calendar.setTime(Date.valueOf(reservation.getDate()));
	        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-3 ; // 요일에 맞게 인덱스 조정
	        if (dayOfWeek < 0) {
	            dayOfWeek += 7; // 음수값 처리
	        }
	        String dayOfWeekStr = daysOfWeek[dayOfWeek];
	        int totalSales = reservation.getPrice();
	        weeklyStats.put(dayOfWeekStr, weeklyStats.getOrDefault(dayOfWeekStr, 0) + totalSales);
	    }
		return weeklyStats;
	}

	

	
	
	// 월별 매출
	public Map<String, Integer> MonthlyStats(List<Reservation> rv) {
		Map<String, Integer> monthlyStats = new HashMap<>();

		for (Reservation reservation : rv) {
			if (reservation.isCancle()) { // 취소된 예약은 무시
	            continue;
	        }
			LocalDate rvDate = reservation.getDate();
			// 날짜를 월 단위로 포맷
			String month = DateTimeFormatter.ofPattern("yyyy-MM").format(rvDate);
			int price = reservation.getPrice();

			monthlyStats.put(month, monthlyStats.getOrDefault(month, 0) + price);
		}
		  Map<String, Integer> sortedMonthlyStats = monthlyStats.entrySet()
		            .stream()
		            .sorted(Map.Entry.comparingByKey())
		            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		return sortedMonthlyStats;
	}


	public Map<String, Integer> ReservationCount(List<Reservation> reservations) {
		Map<String, Integer> reservationCount = new HashMap<>();

		for (Reservation reservation : reservations) {
			if (reservation.isCancle()) { // 취소된 예약은 무시
	            continue;
	        }
			String themaName = reservation.getThemaName();
			reservationCount.put(themaName, reservationCount.getOrDefault(themaName, 0) + 1);
		}

		return reservationCount;
	}

	public int NoShowCount(List<Reservation> reservations) {
		int noShowCount = 0;

		for (Reservation reservation : reservations) {
			if (reservation.isCancle()) { // 취소된 예약은 무시
	            continue;
	        }
			if (reservation.isNoShow()) {
				noShowCount++;
			}
		}

		return noShowCount;
	}

	public Map<String, Integer> themaTotal(List<Reservation> reservations) {
		Map<String, Integer> themaTotal = new HashMap<>();
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		for (Reservation reservation : reservations) {
			if (reservation.isCancle()) { // 취소된 예약은 무시
	            continue;
	        }

			String themaName = reservation.getThema().getTitle();
			int price = reservation.getRvPrice();

			// 같은 테마 이름으로 예약된 가격을 누적
			themaTotal.put(themaName, themaTotal.getOrDefault(themaName, 0) + price);
		}

		return themaTotal;
	}

	public Map<String, Integer> pagethemaTotal(Page<Reservation> reservations, Pageable pageable) {
	    Map<String, Integer> themaTotal = new HashMap<>();

	    // 현재 페이지에 있는 예약 목록만 가져오기
	    List<Reservation> reservationList = reservations.getContent();

	    for (Reservation reservation : reservationList) {
	    	if (reservation.isCancle()) { // 취소된 예약은 무시
	            continue;
	        }
	        String themaName = reservation.getThema().getTitle();
	        int price = reservation.getPrice();

	        // 같은 테마 이름으로 예약된 가격을 누적
	        themaTotal.put(themaName, themaTotal.getOrDefault(themaName, 0) + price);
	    }

	    return themaTotal;
	}
	
	
	public Map<String, Integer> themarvTotal(List<Reservation> reservations) {
		Map<String, Integer> themaTotal = new HashMap<>();

		for (Reservation reservation : reservations) {
			if (reservation.isCancle()) { // 취소된 예약은 무시
	            continue;
	        }

			String themaName = reservation.getThema().getTitle();
			int price = reservation.getRvPrice();

			// 같은 테마 이름으로 예약된 가격을 누적
			themaTotal.put(themaName, themaTotal.getOrDefault(themaName, 0) + price);
		}

		return themaTotal;
	}

	// 지점별 매출
	public Map<Branch, Integer> banchSales(List<Reservation> reservations) {
		return reservations.stream()
				.collect(Collectors.groupingBy(Reservation::getBranch, Collectors.summingInt(Reservation::getRvPrice)));
	}

	// 태마별 매출
	public Map<Thema, Integer> themaSales(List<Reservation> reservations) {
		return reservations.stream()
				.collect(Collectors.groupingBy(Reservation::getThema, Collectors.summingInt(Reservation::getRvPrice)));
	}

	// 지점별 노쇼
	public static Map<Branch, Long> banchNoShowCount(List<Reservation> reservations) {
		return reservations.stream().filter(Reservation::isNoShow)
				.collect(Collectors.groupingBy(Reservation::getBranch, Collectors.counting()));
	}

	public Map<LocalDate, Integer> ReservationsByDate(List<Reservation> reservations) {
	    Map<LocalDate, Integer> reservationsByDate = new HashMap<>();
	    		
	    for (Reservation reservation : reservations) {
	    	 if (!reservation.isCancle()) { // Check if it's not canceled
	             LocalDate date = reservation.getDate();
	             reservationsByDate.put(date, reservationsByDate.getOrDefault(date, 0) + 1);
	         }
	      
	    }

	    return reservationsByDate;
	}
	public Map<LocalDate, Integer> CanceledReservationsByDate(List<Reservation> reservations) {
	    Map<LocalDate, Integer> canceledReservationsByDate = new HashMap<>();
	    
	    for (Reservation reservation : reservations) {
	        if (reservation.isCancle()) { // Check if it's canceled
	            LocalDate date = reservation.getDate();
	            canceledReservationsByDate.put(date, canceledReservationsByDate.getOrDefault(date, 0) + 1);
	        }
	    }

	    return canceledReservationsByDate;
	}
	
    public int TotalSales(List<Reservation> reservations) {
        // 주어진 기간 내의 예약 필터링
       

        // 필터링된 예약들의 매출 합산
        int totalSales = reservations.stream()
                .mapToInt(Reservation::getPrice)
                .sum();

        return totalSales;
    }
    @Transactional
    public void generateRandomReservations() {
        
        Random random = new Random(); 

        for (int i = 0; i < 50; i++) {
        	 int min = 10000000; // 최소값 (8자리 숫자의 최소값)
             int max = 99999999; // 최대값 (8자리 숫자의 최대값)
             int randomNumber = random.nextInt(max - min + 1) + min;
        	Reservation reservation = new Reservation();
        	 Thema thema = themaRepository.findById(random.nextInt(23) + 1).orElse(null);
	        reservation.setThema(thema);
        	reservation.setRvNum(Integer.toString(randomNumber));
          
            reservation.setThemaName(thema.getTitle()); // 테마 이름은 무작위로 설정
           
            
            LocalTime time = thema.getTimetable().get(random.nextInt(6));
            reservation.setTime(time);
            LocalDate today =LocalDate.now().minusDays(2);
            LocalDate date = today.plusDays(random.nextInt(2));
            LocalDateTime datetime = LocalDateTime.now().minusDays(3);
            reservation.setDate(date);
            reservation.setRvDate(datetime);

            reservation.setRvPeople(random.nextInt(3) + 1); // 1부터 10까지 무작위 인원 수 설정
            reservation.setRvPrice(thema.getRvPrice()); // 50부터 200까지 무작위 가격 설정
            reservation.setPrice(thema.getPrice());

            // 사용자, 지점, 테마 엔티티 설정 (실제로는 엔티티를 검색해서 설정)
            User user = userRepository.findById(random.nextInt(48) + 1).orElse(null);
            reservation.setUser(user);
            reservation.setUserName(user.getUserName());
           
            reservation.setBranch(thema.getBranch());

          
 
            reservation.setPaid(true);
            reservation.setNoShow(false);
            reservation.setCancle(false);
            reservation.setPayCancle(false);
            reservation.setRvdelete(false);
            reservation.setReviewYN(false);

            if(reservation.isPaid()) {
            	Pay a= new Pay();
            	a.setReservation(reservation);
            	a.setSaleDate(LocalDateTime.now().minusHours(random.nextInt(2160)));
            	a.setTotalprice(reservation.getPrice());
            	reservation.setPay(a);
            	payRepository.save(a);
            }
            
            
            sales.save(reservation); // 예약 저장
        }}
    
}

