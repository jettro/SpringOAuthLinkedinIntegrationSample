package nl.gridshore.samples.springoauth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.linkedin.LinkedInApi;
import org.springframework.social.linkedin.LinkedInProfile;
import org.springframework.social.linkedin.connect.LinkedInServiceProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

/**
 * <p>Controller that handles all linkedin api requests. It contains methods to request information from linkedin.
 * All authentication and authorization requests are handled by the spring provided controller
 * {@see org.springframework.social.web.connect.ConnectController}.</p>
 *
 * @author Jettro Coenradie
 */
@Controller
public class LinkedinController {

    private LinkedInServiceProvider linkedInServiceProvider;

    @Autowired
    public LinkedinController(LinkedInServiceProvider linkedInServiceProvider) {
        this.linkedInServiceProvider = linkedInServiceProvider;
    }

    /**
     * Handles the request to obtain all the connections from the current logged in user.
     *
     * @param request Spring provided WebRequest
     * @return String representing the name of the view to show
     */
    @RequestMapping(value = "/connect/linkedin/connections", method = RequestMethod.GET)
    public String connections(WebRequest request) {
        request.setAttribute("connections", linkedinApi().getConnections(), WebRequest.SCOPE_REQUEST);

        return "linkedin/connections";
    }

    /**
     * Handles the request to obtain the profile of the current logged in user
     *
     * @param request Spring provided WebRequest
     * @return String representing the name of the view to show
     */
    @RequestMapping(value = "/connect/linkedin/profile", method = RequestMethod.GET)
    public String profile(WebRequest request) {

        request.setAttribute("profile", linkedinApi().getUserProfile(), WebRequest.SCOPE_REQUEST);
        return "linkedin/profile";
    }

    private LinkedInApi linkedinApi() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return linkedInServiceProvider.getConnections(principal.getUsername()).get(0).getServiceApi();
    }


}
