package supul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import supul.service.SuperService;

@SpringBootApplication
public class SupulApplication {

	 @Autowired
	    private SuperService superService;

	   public static void main(String[] args) {
	      SpringApplication.run(SupulApplication.class, args);
	   }
	   
	   @PostConstruct
	       public void initSuperUser() {
	           if (!superService.doesSuperUserExist()) { // 데이터베이스에 'super' 사용자가 없을 때만 생성
	            superService.createSuperUser();
	        }
	    }
	   
	   @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

}
