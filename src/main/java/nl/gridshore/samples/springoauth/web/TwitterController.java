package nl.gridshore.samples.springoauth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.twitter.TwitterApi;
import org.springframework.social.twitter.connect.TwitterServiceProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

/**
 * <p>Controller that handles all service calls that are twitter related. All authorization calls are handled by
 * the spring provided {@see org.springframework.social.web.connect.ConnectController}.</p>
 *
 * @author Jettro Coenradie
 */
@Controller
public class TwitterController {
    private TwitterServiceProvider twitterServiceProvider;

    @Autowired
    public TwitterController(TwitterServiceProvider twitterServiceProvider) {
        this.twitterServiceProvider = twitterServiceProvider;
    }

    /**
     * Returns all tweets of friends of the first registered twitter account.
     *
     * @param request WebRequest used to store parameters to provide to the jsp
     * @return String containing the name of the view to render
     */
    @RequestMapping(value = "/connect/twitter/friends", method = RequestMethod.GET)
    public String obtainFriendTweets(WebRequest request) {

        request.setAttribute("tweets", twitterApi().getFriendsTimeline(), WebRequest.SCOPE_REQUEST);
        return "twitter/friends";
    }

    /**
     * Returns the profile of the first registered twitter account.
     *
     * @param request WebRequest used to store parameters to provide to the jsp
     * @return String containing the name of the view to render
     */
    @RequestMapping(value = "/connect/twitter/profile", method = RequestMethod.GET)
    public String obtainProfile(WebRequest request) {

        request.setAttribute("profile", twitterApi().getUserProfile(), WebRequest.SCOPE_REQUEST);
        return "twitter/profile";
    }

    private TwitterApi twitterApi() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return twitterServiceProvider.getConnections(principal.getUsername()).get(0).getServiceApi();
    }
}
