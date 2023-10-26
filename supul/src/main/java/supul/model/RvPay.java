package supul.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class RvPay {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	int RvPay_id;
	
	String impUidexe;
	@Column(name="imp_uid") 
	String imp_uid;
	String merchant_uid;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "rv_id")
	Reservation reservation;
	@Override
	public String toString() {
		return "RvPay [RvPay_id=" + RvPay_id + ", impUidexe=" + impUidexe + ", imp_uid=" + imp_uid + ", merchant_uid="
				+ merchant_uid + ", reservation=" + reservation + "]";
	}
	
	
	 
	
}
 