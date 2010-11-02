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
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.social.linkedin.LinkedInProfile;
import org.springframework.social.linkedin.LinkedInTemplate;
import org.springframework.social.oauth.OAuthSigningClientHttpRequestFactory;
import org.springframework.social.oauth1.OAuth1ClientRequestSigner;
import org.springframework.social.oauth1.OAuth1RequestSignerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller that handles all connect requests. It support GET requests for checking current connectivity and POST
 * requests to handle the authorization requests.
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

    @RequestMapping(value = "linkedin", method = RequestMethod.GET)
    public String showConnectLinkedin(WebRequest request) {
        Token accessToken = obtainAccessTokenFromSession(request);
        if (accessToken == null) {
            return "connect/linkedin_connect";
        } else {
            return "connect/linkedin_connected";
        }
    }

    @RequestMapping(value = "linkedin", method = RequestMethod.POST)
    public String requestConnectionLinkedin(WebRequest request) {

        Token requestToken = getOAuthService().getRequestToken();
        request.setAttribute(OAUTH_REQUEST_TOKEN_ATTRIBUTE, requestToken, WebRequest.SCOPE_SESSION);

        return "redirect:" + "https://www.linkedin.com/uas/oauth/authorize?oauth_token=" + requestToken.getToken();
    }

    @RequestMapping(value="linkedin", method=RequestMethod.GET, params="oauth_token")
    public String authorizeCallback(@RequestParam("oauth_token") String token,
                                    @RequestParam(value="oauth_verifier", defaultValue="verifier") String verifier,
                                    WebRequest request) {
        Token requestToken = obtainRequestTokenFromSession(request);
        Token accessToken = getOAuthService().getAccessToken(requestToken, new Verifier(verifier));

        request.setAttribute(OAUTH_ACCESS_TOKEN_ATTRIBUTE, accessToken, WebRequest.SCOPE_SESSION);
        return "redirect:linkedin";

    }

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
        List<String> names = new ArrayList<String>();
        for (LinkedInProfile theProfile : connections) {
            names.add(theProfile.getLastName());
        }
        request.setAttribute("names",names,WebRequest.SCOPE_REQUEST);
        return "connections";
    }

    private Token obtainRequestTokenFromSession(WebRequest request) {
        return (Token)request.getAttribute(OAUTH_REQUEST_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
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
        return new OAuth10aServiceImpl(new HMACSha1SignatureService(), new TimestampServiceImpl(), new BaseStringExtractorImpl(), new HeaderExtractorImpl(), new TokenExtractorImpl(), new TokenExtractorImpl(), config);
    }


    private Token obtainAccessTokenFromSession(WebRequest request) {
        return (Token)request.getAttribute(OAUTH_ACCESS_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
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
