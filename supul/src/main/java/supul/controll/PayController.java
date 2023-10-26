package supul.controll;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import supul.model.PageData;
import supul.model.PayResponse;
import supul.model.Reservation;
import supul.model.RvPay;
import supul.repository.CancelHistoryRepository;
import supul.repository.ReservationRepository;
import supul.repository.RvPayRepository;
import supul.service.PayCancelService;
import supul.service.TockenCreate;

@RestController
@RequestMapping("pay")
public class PayController {

	@Autowired
	CancelHistoryRepository cancelHistoryRepository;
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	RvPayRepository rvPayRepository;
	@Autowired
	TockenCreate tockenCreate;
	@Autowired 
	PayCancelService payCancelService;
	
	@PostMapping("/saveRsp")
    public ResponseEntity<String> saveRsp(@RequestBody RvPay rvpay,HttpSession session) {
        try {
        	rvPayRepository.save(rvpay);
        	session.setAttribute("rvpay", rvpay);          
            // 성공적으로 처리한 경우
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            // 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed\"}");
        }
    } 

	@GetMapping("/getToken")
    public String  getAccessToken() {
        String accessToken = tockenCreate.getAccessToken().getResponse().getAccess_token();
        System.out.println(accessToken);
        return "Access Token: " + accessToken;
    }
	@PostMapping("/cancelPay")
    public PayResponse cancelPayment(@RequestBody RvPay request, PageData pageData) {
        // TockenCreate 서비스를 사용하여 액세스 토큰을 얻어옴
		System.out.println(request);
        String accessToken = tockenCreate.getAccessToken().getResponse().getAccess_token();
        
        RvPay a= rvPayRepository.findByImpUidexe(request.getImpUidexe());
        
       	request.setImp_uid(request.getImpUidexe());
        
   
        Reservation rv = reservationRepository.findById(a.getReservation().getRvId())
		   .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        rv.setPayCancle(true);
        rv.setCancle(true);
        reservationRepository.save(rv);
        // 결제 취소 서비스를 호출하고 액세스 토큰을 전달
        return payCancelService.cancelPayment(accessToken, request);
    }
	
	
}
