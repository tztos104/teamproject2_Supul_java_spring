package supul.service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SuperInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
       
   
        // 사용자가 로그인한 경우에만 요청을 허용
        if (session.getAttribute("userRole") == null) {
     
          response.sendRedirect("/super/superlogin"); // 로그인 페이지로 리디렉션
          return false;

        }

        return true;
    }
}
