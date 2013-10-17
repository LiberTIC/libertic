package models;

import org.junit.Test;
import play.test.UnitTest;
import securesocial.provider.SocialUser;

/**
 * Test case for user model.
 */
public class UserTest extends UnitTest {

    /**
     * Testing the model.
     */
    @Test
    public void findById() {
        User usr = User.findById(Long.valueOf(1));
        assertEquals("Benoît Simard", usr.displayName);
        assertTrue(usr.isAdmin);
        assertEquals(2, usr.accounts.size());
    }

    /**
     * Testing transformations user to socialuser.
     */
    @Test
    public void conversionToSocialUser() {
        User usr = User.findById(Long.valueOf(1));
        SocialUser socialUsr = usr.toUserSocial();

        // we test if the user from
        assertEquals(usr.displayName, socialUsr.displayName);
        assertEquals(usr.avatarUrl, socialUsr.avatarUrl);
        assertEquals(usr.email, socialUsr.email);
        assertEquals(usr.accessToken, socialUsr.accessToken);
        assertEquals(usr.password, socialUsr.password);
        assertEquals(usr.secret, socialUsr.secret);
        assertNotNull(socialUsr.id);
    }

    /**
     * Testing transformations user to socialuser.
     */
    @Test
    public void conversionFromSocialUser() {
        User usr = User.findById(Long.valueOf(1));
        SocialUser socialUsr = usr.toUserSocial();
        User usr2 = User.fromUserSocial(socialUsr);

        // we test if the user from
        assertEquals(usr.displayName, usr2.displayName);
        assertEquals(usr.avatarUrl, usr2.avatarUrl);
        assertEquals(usr.email, usr2.email);
        assertEquals(usr.accessToken, usr2.accessToken);
        assertEquals(usr.password, usr2.password);
        assertEquals(usr.secret, usr2.secret);
    }


}
