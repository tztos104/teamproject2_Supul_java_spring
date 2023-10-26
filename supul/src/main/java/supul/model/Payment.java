package supul.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private int paymentId;

    private String access_token;
    private Integer expired_at;
    private Integer now;
    private String impUid;
    private String merchantUid; 
    private String payMethod;
    private String channel;
    private String pgProvider;
    private String embPgProvider;
    private String pgTid;
    private String pgId;
    private boolean escrow;
    private String applyNum;
    private String bankCode;
    private String bankName;
    private String cardCode;
    private String cardName;
    private int cardQuota;
    private String cardNumber;
    private int cardType;
    private String vbankCode;
    private String vbankName;
    private String vbankNum;
    private String vbankHolder;
    private long vbankDate;
    private long vbankIssuedAt;
    private String name;
    private int amount;
    private int cancelAmount;
    private String currency;
    private String buyerName;
    private String buyerEmail;
    private String buyerTel;
    private String buyerAddr; 
    private String buyerPostcode;
    private String customData;
    private String userAgent;
    private String status;
    private long startedAt;
    private long paidAt;
    private long failedAt;
    private long cancelledAt;
    private String failReason;
    private String cancelReason;
    private String receiptUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "payment")
    private List<CancelHistory> cancelHistory;

    @ElementCollection
    private List<String> cancelReceiptUrls;

    private boolean cashReceiptIssued;
    private String customerUid;
    private String customerUidUsage;

    // getters and setters
}