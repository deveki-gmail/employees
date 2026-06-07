package practice.microservice.deveki.employees;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import practice.microservice.deveki.employees.controller.EmployeeController;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

@Component
public class LoggingFilter implements Filter{
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("Logging filter started. ");
		log.info("Request URL -> "+((HttpServletRequest)request).getRequestURI());
		log.info("Logging filter end. ");
		chain.doFilter(request, response);
	}
	
}
