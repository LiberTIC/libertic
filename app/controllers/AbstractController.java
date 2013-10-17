package controllers;

import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import securesocial.provider.SocialUser;
import service.UserService;
import controllers.securesocial.SecureSocial;

/**
 * Abstract controller with some user/session helpers.
 */
public class AbstractController extends Controller {

    private static final String ROOT         = "/";
    private static final String ORIGINAL_URL = "originalUrl";
    private static final String GET          = "GET";

    @Before
    public static void setUser() {
        Boolean isAdmin = Boolean.FALSE;
        SocialUser user = SecureSocial.getCurrentUser();
        if (user != null && user.id != null) {
            session.put("username", user.id.id);
        }
    }

    /**
     * Return the current user connected.
     *
     * @return
     */
    public static models.User getCurrentUser(){
        SocialUser user = SecureSocial.getCurrentUser();
        if(user != null) {
            models.User member = UserService.findUser(user.id);
            return member;
        }
        return null;
    }

    /**
     * Check if there is a logged user ? Else we redirect to login page.
     */
    protected static void isValidUser() {
        SocialUser user = SecureSocial.getCurrentUser();
        if (user == null) {
            final String originalUrl = request.method.equals(GET) ? request.url : ROOT;
            flash.put(ORIGINAL_URL, originalUrl);
            Logger.debug("Redirect user to login page");
            SecureSocial.login();
        }
        else {
            Logger.debug("User is " + user.displayName);
        }
    }

    /**
     * Check if the current user is an admin ?  Else we redirect to a forbidden.
     */
    protected static void isAdminUser() {
        isValidUser();
        SocialUser user = SecureSocial.getCurrentUser();
        models.User member = UserService.findUser(user.id);
        if (member == null || member.isAdmin == null || !member.isAdmin) {
            forbidden();
        }
    }

    /**
     * Return true if the user has admin right.
     *
     * @return
     */
    public static Boolean hasAdminRight() {
        Boolean isAdmin = Boolean.FALSE;
        SocialUser user = SecureSocial.getCurrentUser();
        if (user != null && user.id != null) {
            models.User member = UserService.findUser(user.id);
            if (member != null) {
                isAdmin = member.isAdmin;
            }
        }
        return isAdmin;
    }
}
