package dk.magenta.datafordeler.registerdemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lars on 13-03-17.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping("/")
    String index() {
        return "index";
    }

}
