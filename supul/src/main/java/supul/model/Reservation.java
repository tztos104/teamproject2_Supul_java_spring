package supul.model;



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
@Alias("rvDTO")
@Entity
@Table(name="reservation")
@Data
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rv_id")
	int rvId;
	String rvNum;
	@Column(name = "user_name")
	String userName;
	@Column(name = "thema_name")
	String themaName;

	LocalTime time;

	LocalDate date;

	@Column(name = "rv_people")
	int rvPeople;
	@Column(name = "rv_price")
	int rvPrice;
	int price;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	User user;

	@Column(name = "rv_date")
	LocalDateTime rvDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "branch_id")
	Branch branch;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "thema_id")
	Thema thema;

	// 결제 상태 추가
	@Column(nullable = false, columnDefinition = "boolean default false")
	boolean paid;

	// 노쇼 상태 추가
	@Column(nullable = false, columnDefinition = "boolean default false")
	boolean noShow;

	// 취소
	@Column(nullable = false, columnDefinition = "boolean default false")
	boolean cancle;
	@Column(nullable = false, columnDefinition = "boolean default false")
	boolean payCancle;
	// 삭제
	@Column(nullable = false, columnDefinition = "boolean default false")
	boolean rvdelete;

	// 리뷰 상태 추가(true가 작성)
	@Column(nullable = false, columnDefinition = "boolean default false")
	boolean reviewYN;

	@OneToOne(mappedBy = "reservation", fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	Pay pay;

	@OneToOne(mappedBy = "reservation", fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	RvPay rvpay;

	public String getReserChkStr() {

		String res = date + "_" + time + "_" + thema.getThemaId()+cancle;

		return res;

	}

	@Override
	public String toString() {
		return "Reservation [rvId=" + rvId + ", price=" + price + ", rvDate=" + rvDate + "]"+cancle;
	}

}
