package project_hoang.web.qdl;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

// https://www.baeldung.com/spring-boot-custom-error-page
@Controller
public class QdlError implements ErrorController {

    // @RequestMapping("/error")
    // public String handleError() {
    // // bắt lỗi chung chung, đơn giản, không phân loại
    // //do something like logging
    // return "error.html";
    // }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Đối tượng chứa lỗi:
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // Phân tích kỹ chi tiết bên trong đối tượng lỗi
        // Phân loại lỗi để có thông báo phù hợp
        if (status != null) 
        {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value(
                
            )) 
            {
                return "error-404.html";
            } 
            else 
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) 
            {
                return "error-500.html";
            }
        }

        return "error.html";
    }

}

