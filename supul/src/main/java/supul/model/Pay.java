package supul.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="pay")
@Data
public class Pay {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pay_id")
	int payId;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rv_id")
	Reservation reservation;
	
	int totalprice;
	LocalDateTime saleDate;
	
	@OneToOne(mappedBy = "pay", fetch = FetchType.EAGER)
	Ranking rank;
	
}
