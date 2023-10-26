package supul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import supul.model.PayResponse;
import supul.model.TokenSave;

@Service
public class TockenCreate {

	@Value("${imp.key}")
    private String apiKey;

    @Value("${imp.secret}")
    private String apiSecret;
    @Autowired
   RestTemplate restTemplate;


    public PayResponse getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        TokenSave request = new TokenSave(apiKey, apiSecret);
        HttpEntity<TokenSave> entity = new HttpEntity<>(request, headers);

        ResponseEntity<PayResponse> responseEntity = restTemplate.exchange(
                "https://api.iamport.kr/users/getToken",
                HttpMethod.POST,
                entity,
                PayResponse.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Access Token 발급에 실패했습니다.");
        }
    }
	
}
