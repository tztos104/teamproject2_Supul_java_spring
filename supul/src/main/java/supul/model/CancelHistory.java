package supul.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CancelHistory {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cancell_id")
    private int cancellId;

    private String pgTid;
    private int amount;
    private long cancelledAt;
    private String reason;
    private String receiptUrl;
 
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

 
}
