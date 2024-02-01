package world.grendel.cringeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication
public class CringeItApplication {

    public static void main(String[] args) {
        SpringApplication.run(CringeItApplication.class, args);
    }

    @RequestMapping
    public String home() {
        return "redirect:/login";
    }

}
