package nl.gridshore.samples.springoauth.web;

import org.scribe.extractors.BaseStringExtractorImpl;
import org.scribe.extractors.HeaderExtractorImpl;
import org.scribe.extractors.TokenExtractorImpl;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth10aServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.services.HMACSha1SignatureService;
import org.scribe.services.TimestampServiceImpl;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.linkedin.LinkedInProfile;
import org.springframework.social.linkedin.LinkedInTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

/**
 * <p>Controller that handles all linkedin connect requests. It contains methods to request information from linkedin.
 * as well as methods to handle the authentication with linkedin using OAuth and the spring-social project.</p>
 *
 * @author Jettro Coenradie
 */
@Controller
@RequestMapping(value = "/connect")
public class ConnectController {
    private static final String OAUTH_REQUEST_TOKEN_ATTRIBUTE = "oauthRequestToken";
    private static final String OAUTH_ACCESS_TOKEN_ATTRIBUTE = "oauthAccessToken";
    private String apiKey;
    private String apiSecret;
    private String callbackUrl;

    /**
     * Shows a page that enables you to start the connection process if you have not connected yet. If you did connect,
     * a page is shown with the actions you can perform.
     *
     * @param request Spring provided WebRequest
     * @return String representing the name of the view to show
     */
    @RequestMapping(value = "linkedin", method = RequestMethod.GET)
    public String showConnectLinkedin(WebRequest request) {
        Token accessToken = obtainAccessTokenFromSession(request);
        if (accessToken == null) {
            return "connect/linkedin_connect";
        } else {
            return "connect/linkedin_connected";
        }
    }

    /**
     * Redirects the use to the linkedin authentication service
     *
     * @param request Spring provided WebRequest
     * @return String representing the name of the view to show
     */
    @RequestMapping(value = "linkedin", method = RequestMethod.POST)
    public String requestConnectionLinkedin(WebRequest request) {

        Token requestToken = getOAuthService().getRequestToken();
        request.setAttribute(OAUTH_REQUEST_TOKEN_ATTRIBUTE, requestToken, WebRequest.SCOPE_SESSION);

        return "redirect:" + "https://www.linkedin.com/uas/oauth/authorize?oauth_token=" + requestToken.getToken();
    }

    /**
     * Handles the callback from Linkedin. Uses the RequestToken to obtain the AccessToken. The AccessToken is stored
     * in the session to be used for later on.
     *
     * @param verifier String containing a string provided by Linkedin to use for varifying the request token.
     * @param request  Spring provided WebRequest
     * @return String representing the name of the view to show
     */
    @RequestMapping(value = "linkedin", method = RequestMethod.GET, params = "oauth_token")
    public String authorizeCallback(@RequestParam(value = "oauth_verifier", defaultValue = "verifier") String verifier,
                                    WebRequest request) {
        Token requestToken = obtainRequestTokenFromSession(request);
        Token accessToken = getOAuthService().getAccessToken(requestToken, new Verifier(verifier));

        request.setAttribute(OAUTH_ACCESS_TOKEN_ATTRIBUTE, accessToken, WebRequest.SCOPE_SESSION);
        return "redirect:linkedin";

    }

    /**
     * Handles the request to obtain all the connections from the current logged in user.
     *
     * @param request Spring provided WebRequest
     * @return String representing the name of the view to show
     */
    @RequestMapping(value = "linkedin/connections", method = RequestMethod.GET)
    public String connections(WebRequest request) {
        Token accessToken = obtainAccessTokenFromSession(request);
        if (accessToken == null) {
            throw new RuntimeException("An access token must be available here");
        }
        LinkedInTemplate template = new LinkedInTemplate(
                apiKey,
                apiSecret,
                accessToken.getToken(),
                accessToken.getSecret());

        List<LinkedInProfile> connections = template.getConnections();
        request.setAttribute("connections", connections, WebRequest.SCOPE_REQUEST);

        return "connections";
    }

    private Token obtainRequestTokenFromSession(WebRequest request) {
        return (Token) request.getAttribute(OAUTH_REQUEST_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
    }

    private OAuthService getOAuthService() {
        OAuthConfig config = new OAuthConfig();
        config.setRequestTokenEndpoint("https://api.linkedin.com/uas/oauth/requestToken");
        config.setAccessTokenEndpoint("https://api.linkedin.com/uas/oauth/accessToken");
        config.setAccessTokenVerb(Verb.POST);
        config.setRequestTokenVerb(Verb.POST);
        config.setApiKey(apiKey);
        config.setApiSecret(apiSecret);
        config.setCallback(callbackUrl);

        return new OAuth10aServiceImpl(
                new HMACSha1SignatureService(),
                new TimestampServiceImpl(),
                new BaseStringExtractorImpl(),
                new HeaderExtractorImpl(),
                new TokenExtractorImpl(),
                new TokenExtractorImpl(),
                config);
    }


    private Token obtainAccessTokenFromSession(WebRequest request) {
        return (Token) request.getAttribute(OAUTH_ACCESS_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
    }

    @Required
    @Value("${linkedin.api.key}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Required
    @Value("${linkedin.api.secret}")
    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    @Required
    @Value("${linkedin.callback}")
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
