package sop.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		String requestURI = request.getRequestURI();

		// Check if the user is logged in
		if (session == null || session.getAttribute("user") == null) {
			// User is not logged in, allow access to the client page only
			if (requestURI.equals("")) {
				return true; // Allow access
			} else {
				response.sendRedirect(""); // Redirect to client page
				return false; // Block access to other pages
			}
		}

		// User is logged in, get the role from session
		Integer role = (Integer) session.getAttribute("role");

		// Role-based access control
		if (role == 0) {
			return true; // Role 0: Access all pages
		} else if (role == 1) {
			// Role 1: Only allow access to employee page
			if (requestURI.equals("/employee")) {
				return true; // Allow access to employee page
			} else {
				response.sendRedirect("/employee"); // Redirect to employee page
				return false; // Block access to other pages
			}
		}else if(role == 2) {
			if (requestURI.equals("/client")) {
				return true; // Allow access to employee page
			} else {
				response.sendRedirect("/client"); // Redirect to employee page
				return false; // Block access to other pages
			}
		}

		// Default: Block access
		response.sendRedirect("/client"); // Redirect to client page
		return false;
	}

}
