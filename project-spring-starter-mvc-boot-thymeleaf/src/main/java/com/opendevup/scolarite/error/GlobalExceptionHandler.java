/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//Specialization of @Component for classes that declare @ExceptionHandler, @InitBinder, or @ModelAttribute methods to be shared across multiple @Controller classes.
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param exception
     * @param redirectAttributes
     * @return
     */
    //https://jira.spring.io/browse/SPR-14651
    //4.3.5 supports RedirectAttributes redirectAttributes
	//Annotation for handling exceptions in specific handler classes and/or handler methods.
    @ExceptionHandler(MultipartException.class)
    public String handleError(MultipartException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", exception.getCause().getMessage());
        return "redirect:/Index";
    }
}