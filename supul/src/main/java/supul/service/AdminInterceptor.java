package supul.service;

import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminInterceptor implements HandlerInterceptor {

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       HttpSession session = request.getSession();

   
       
       
       if (session.getAttribute("admin") == null) {
         
          response.sendRedirect("/admin/login");
           return false;
           }
       

       // 로그인하지 않은 경우, 로그인 페이지로 리디렉션
      
       return true;
   }


}


