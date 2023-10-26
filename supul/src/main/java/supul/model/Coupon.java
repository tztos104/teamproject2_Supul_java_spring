package supul.model;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="coupon")
@Data
public class Coupon {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coupon_id")
	int couponId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	User user;
	
	@Column(name = "coupon_name", nullable = false)
	String couponName;
	
	@Column(name = "coupon_num", nullable = false)
	String couponNum;
	
	@Column(name = "reg_date", nullable = false)
	LocalDateTime regDate;
	
	@Column(name = "used_date")
	LocalDateTime usedDate;
	
	@Column(name = "discount", nullable = false) 
	int discount;
	
	@Column(name = "used", columnDefinition = "boolean default false") 
	boolean used;
	
	
	
}
 