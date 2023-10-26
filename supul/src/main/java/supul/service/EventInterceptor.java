package supul.service;




import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Service
public class EventInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		
        String username = (String) session.getAttribute("username");
     
		
		
		if(username == null){
		
			response.sendRedirect("/user/login");
			return false;
		}
		
		
		return true;
	}
}
