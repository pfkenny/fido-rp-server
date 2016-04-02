package com.daon.identityx.config; 
 
import org.h2.server.web.WebServlet; 
import org.springframework.boot.context.embedded.ServletRegistrationBean; 
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
 
/**
 * Initializes the H2 {@link WebServlet} so we can access our in memory database 
 * from the URL "/h2". 
 * 
 * @author Rob Winch 
 */ 
@Configuration 
public class H2Initializer { 
 
 @Bean 
 public ServletRegistrationBean h2Servlet() { 
  ServletRegistrationBean servletBean = new ServletRegistrationBean(); 
  servletBean.addUrlMappings("/h2/*"); 
  servletBean.setServlet(new WebServlet()); 
  return servletBean; 
 } 
}
