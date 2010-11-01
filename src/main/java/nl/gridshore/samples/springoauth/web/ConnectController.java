package nl.gridshore.samples.springoauth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that handles all connect requests. It support GET requests for checking current connectivity and POST
 * requests to handle the authorization requests.
 *
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping(value = "/connect")
public class ConnectController {

    @RequestMapping(value = "linkedin", method = RequestMethod.GET)
    public String connectLinkedin() {
        return "connect/linkedin_connect";
    }
}
