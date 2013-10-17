package controllers;

import org.junit.Test;
import play.mvc.Http.Response;
import play.test.FunctionalTest;

/**
 * Functional test for User controller.
 */
public class UserTest extends FunctionalTest {

    /**
     * Test to see 'my profile" when user is not logged (redirect to login page).
     */
    @Test
    public void testMyProfilNotLogged() {
        Response response = GET("/user/me");
        assertStatus(302, response);
    }

    /**
     * Test to edit 'my profile" when user is not logged (redirect to login page).
     */
    @Test
    public void testEditMyProfilNotLogged() {
        Response response = GET("/user/me/edit");
        assertStatus(302, response);
    }

    /**
     * Test to see a user profile.
     */
    @Test
    public void testViewProfile() {
        Response response = GET("/user/1");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertContentMatch("Simard", response);
    }

    /**
     * Test to see a none existaing user profile.
     */
    @Test
    public void testViewProfile404() {
        Response response = GET("/user/51");
        assertStatus(404, response);
    }

}