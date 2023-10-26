package supul.mapper;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import supul.model.Reservation;

import supul.model.Thema;



@Mapper
public interface ReserveMapper {
	
	//예약 하기
	int reserve(Reservation rvDTO);
	
	//예약 있는지 체크하기
	Reservation confirmreserve(Reservation rvDTO);
	
	//모든 예약 보기
	List<Reservation> allreserve();
	
	//오늘날짜 모든 예약 보기
	List<Reservation> todayreserve(LocalDate today);
	
	//예약 취소
	int canclereserve(Reservation rvDTO);
	
	//예약번호 중복체크
	int chkrvnum(String rvnum);
	
	public List<LocalTime> timelist(int timetableId);
	
	//예약시간 이미 있는지 확인
	//그 결과값을 ArrayList에 넣어줌
	ArrayList<String> chkrvstatus(LocalDate date);
	

	
}
