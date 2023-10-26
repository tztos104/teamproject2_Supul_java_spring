package supul.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import supul.model.PayResponse;
import supul.model.Payment;
import supul.model.RvPay;

@Service
public class PayCancelService {

    @Autowired
   RestTemplate restTemplate;
    
	 public PayResponse cancelPayment(String accessToken, RvPay request) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Authorization", "Bearer " + accessToken); // 액세스 토큰을 헤더에 추가

	        HttpEntity<RvPay> entity = new HttpEntity<>(request, headers);

	        ResponseEntity<PayResponse> responseEntity = restTemplate.exchange(
	                "https://api.iamport.kr/payments/cancel",
	                HttpMethod.POST,
	                entity,
	                PayResponse.class
	        );

	        if (responseEntity.getStatusCode() == HttpStatus.OK) {
	            return responseEntity.getBody();
	        } else {
	            throw new RuntimeException("결제 취소 요청에 실패했습니다.");
	        }
	    }
}
