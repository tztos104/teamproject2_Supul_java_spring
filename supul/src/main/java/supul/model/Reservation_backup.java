package supul.model;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.DateFormatter;

import org.apache.ibatis.type.Alias;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="reservation_backup")
@Data
public class Reservation_backup {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name="rv_id")
	    int rvId;
	    String rvNum;
	    @Column(name="user_name")
	    String userName;
	    @Column(name="thema_name")
	    String themaName;
	     
	    LocalTime time;
	    
	    LocalDate date;
	    
	    @Column(name="rv_people")
	    int rvPeople;
	    @Column(name="rv_price")
	    int rvPrice;    
	    int price;    
	    
	    String impUidexe;
		@Column(name="imp_uid") 
		String imp_uid;
		String merchant_uid;
	
	    String user_id;
	    
	    @Column(name = "rv_date")
	    LocalDateTime rvDate; 
	    
	    @Column(name="branch_name")
	    String branchName;
	    
	
	    int thema_id;
	    
	    // 결제 상태 추가
	    @Column(nullable = false, columnDefinition = "boolean default false")
	    boolean paid;

	    // 노쇼 상태 추가
	    @Column(nullable = false, columnDefinition = "boolean default false")
	    boolean noShow;
	    
	    
	    // 리뷰  상태 추가(true가 작성)
	    @Column(nullable = false, columnDefinition = "boolean default false")
	    boolean reviewYN;
	    
	   
	    int pay_id;
	     
	    int rvpay_id;
	        
	    //취소
	    @Column(nullable = false, columnDefinition = "boolean default false")
	    boolean cancle;
	    @Column(nullable = false, columnDefinition = "boolean default false")
	    boolean payCancle;
	     
	    
	 
	    
	    
	    
		@Override
		public String toString() {
			return "Reservation [rvId=" + rvId + ", price=" + price + ", rvDate=" + rvDate + "]";
		}
		
	     
	    
}
