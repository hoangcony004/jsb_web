package project_hoang.web.qdl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QdlIndex 
{

    @GetMapping({"/index", "/home"})
    public String indexAction() 
    {
        return "Hom Page-Java Spring Boot";
    }
    
    @GetMapping("/hello")
    public String helloAction() 
    {
        return "Hello Java Web Spring Boot !";
    }

}


