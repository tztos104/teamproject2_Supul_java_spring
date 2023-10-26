package supul.mapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import supul.model.Branch;
import supul.model.Reservation;
import supul.model.Thema;



@Mapper
public interface ThemaMapper {

	// 매장 데이터 조회
	List<Branch> brList(Branch bd);

	// 테마 데이터 조회
	List<Thema> tmList(Thema td);
	
	Thema tmBranch(int themaId);
	
	// 매장과 동일한 아이디의 테마들 가져올때 사용
	List<Thema> brnList(int branchId);
	
	// 테마명 검색
	String tmTitle(int themaId);

	// 매장명 검색
	String brName(int branchId);
	//타임 저장
	 List<LocalTime> timetableList(@Param("themaId") int themaId);
	//타임테이블 리스트 출력(한개만) 
		List<LocalTime> timetablebyId(int themaId);
	    void insertTimetable(int themaId, LocalTime time); 
	    int timeId ();
	//==================테마소개=====================//
    List<Thema> selectList(); 
    
    Thema detail(int thema_id);   
    Thema selectById(int thema_id);
    void insertThema(Thema thema); 
    void updateThema(Thema thema);
    void deleteThema(int themaId);
    void deleteTimetable(int thema_id);
    
    
    int listCnt();
    int maxId();
    void addCount(int thema_id);
    int fileDelete(int thema_id);
    int idPwChk(Thema thema);
    
    //===================정현부분=======================
    ArrayList<String> chkrvstatus(LocalDate date );
    //오늘날짜 모든 예약 보기
    List<Reservation> todayRv(LocalDate today);
    
    //예약 있는지 체크하기
    Reservation confirmRv(Reservation rvDTO);
    //예약 취소
    int cancleRv(Reservation rvDTO);
    
	// 예약번호 중복체크
	int chkrvnum(String rvnum);

	// 예약 하기
	int reserve(Reservation rvDTO);
    
}
