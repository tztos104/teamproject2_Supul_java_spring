package supul.service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
       
    	
    	HttpSession session = request.getSession();
		if(session.getAttribute("admin")!=null){
			return true;
		}
        
    	
        if(session.getAttribute("userRole")== null){
    		
			response.sendRedirect("/super/login");
			return false;
		}
        
        // "user"가 포함되지 않은 다른 URL에 대해서는 허용
       
        return true;
    }
}
