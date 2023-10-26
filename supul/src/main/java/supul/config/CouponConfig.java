package supul.config;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import supul.model.Coupon;

@Configuration
public class CouponConfig {

    @Bean
    public Coupon welcomeCoupon() {
        Coupon coupon = new Coupon();
        // 필요한 쿠폰 속성들을 설정
        coupon.setCouponName("가입환영쿠폰");
        coupon.setDiscount(5000);
        coupon.setRegDate(LocalDateTime.now().withNano(0));
        
        return coupon;
    }
    
}
