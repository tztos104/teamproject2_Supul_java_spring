package supul.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import supul.service.AdminInterceptor;
import supul.service.SuperInterceptor;
import supul.service.UserInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    AdminInterceptor adminInterceptor;
    @Autowired
    SuperInterceptor superInterceptor;
    @Autowired
    UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
     
       
        registry.addInterceptor(adminInterceptor)
        .addPathPatterns("/admin")
        .addPathPatterns("/admin/**") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("/super/rvList/null") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("/home/branchInfo/null") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("/reserve/tonggye/null") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("/thema/themaList ")       //  '/admin' 페이지에 대한 인터셉터 적용
        // '/admin' 페이지에 대한 인터셉터 적용
        .excludePathPatterns("/admin/login"); // 로그인 페이지 제외
        registry.addInterceptor(superInterceptor)
        .addPathPatterns("/super/**") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("/reserve/tonggye/전체") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("/admin/branch/update/null") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("/admin/rvList/null") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("/home/branchInfo/null") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("http://localhost/admin/branch/update/null") // '/admin' 페이지에 대한 인터셉터 적용
        .addPathPatterns("/reserve/tonggye")
        .excludePathPatterns("/super/superlogin") // 로그인 페이지 제외
        .excludePathPatterns("/super/branchList") // 로그인 페이지 제외
        .excludePathPatterns("/super/branch/update"); // 로그인 페이지 제외
        
        
        registry.addInterceptor(userInterceptor)
        .addPathPatterns("/super/branchList")
        .addPathPatterns("/thema/themaList"); 
   
}
    }
