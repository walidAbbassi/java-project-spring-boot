/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

public class CustomErrorController implements ErrorController {
	
	public CustomErrorController() {}
 
	/**
	 * Customize the default error page for a Spring Boot application
	 * BAD_REQUEST(400, "Bad Request"),UNAUTHORIZED(401, "Unauthorized"),PAYMENT_REQUIRED(402, "Payment Required"),FORBIDDEN(403, "Forbidden"),
	 * NOT_FOUND(404, "Not Found"),METHOD_NOT_ALLOWED(405, "Method Not Allowed"),NOT_ACCEPTABLE(406, "Not Acceptable"),
	 * PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),REQUEST_TIMEOUT(408, "Request Timeout"),
	 * CONFLICT(409, "Conflict"),GONE(410, "Gone"),LENGTH_REQUIRED(411, "Length Required"),PRECONDITION_FAILED(412, "Precondition Failed"),
	 * PAYLOAD_TOO_LARGE(413, "Payload Too Large")
	 * @param request
	 * @return
	 */
	//Annotation for mapping HTTP GET requests onto specific handler methods
	//Specifically, @GetMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.GET).
	//new annotation since 4.3
	@GetMapping(value = "/error")
    public String handleError(HttpServletRequest request) {
        
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
        
            Integer statusCode = Integer.valueOf(status.toString());
            //url:http://localhost:8080/error/403.html
            //404 Forbidden Access
            if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return "403";
            }
            //url:http://localhost:8080/error/404.html    
            //404 File Not Found
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404";
            }
            //url:http://localhost:8080/error/500.html   
            //Internal server error
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "500";
            }
        }
        return "error";
    }

    /* (non-Javadoc)
     * @see org.springframework.boot.web.servlet.error.ErrorController#getErrorPath()
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }
}