package controllers;

import org.junit.Test;
import play.Logger;
import play.cache.Cache;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Http.Response;
import play.mvc.Router;
import play.mvc.Scope;
import play.test.FunctionalTest;

import java.util.HashMap;
import java.util.Map;

public class ContactTest extends FunctionalTest {

    /**
     * Testing controller that display contact form.
     */
    @Test
    public void testIndex() {
        Response response = GET("/contact");

        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("UTF-8", response);
    }

    /**
     * Testing the generation of captcha
     */
    @Test
    public void testCaptcha() {
        String captchaId = Codec.UUID();
        Response response = GET("/contact/captcha?id=" + captchaId);

        assertIsOk(response);
        assertContentType("image/png", response);
        assertNotNull(Cache.get(captchaId));
    }

    /**
     * Testing the mail sender.
     */
    @Test
    public void testSend(){
        //@Required String author, @Required String message, @Required @Email String email, @Required String code, String randomID
        Map<String, String> args = new HashMap<String, String>();
        args.put("type", "contact");
        args.put("author", "Benoît Simard");
        args.put("email", "bsimard@yopmail.com");
        args.put("code", "XXXX");
        args.put("randomId", "XXXX");
        args.put("message", "Test of message");
        Response response = POST("/contact", args);

        // getting a 302 if it's OK (post-redirect-get)
        assertStatus(302, response);
    }
}