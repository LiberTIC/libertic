package controllers;

import controllers.securesocial.SecureSocial;
import notifier.Mails;
import play.Play;
import play.cache.Cache;
import play.data.validation.Email;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import securesocial.provider.SocialUser;

import java.util.concurrent.ExecutionException;

/**
 * Controller that managed contact forms.
 */
public class Contact extends AbstractController {

    /**
     * Display the contact form.
     */
    public static void index() {
        String randomID = Codec.UUID();
        SocialUser user = SecureSocial.getCurrentUser();
        if (user != null) {
            params.put("author", user.displayName);
            params.put("email", user.email);
        }
        render(randomID);
    }

    /**
     * Generate a captcha & store captcha text in cache.
     *
     * @param id
     */
    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        // we set the text color of the captcha
        String code = captcha.getText("#065381");
        // we set the life time of the captcha
        Cache.set(id, code, "30mn");
        renderBinary(captcha);
    }

    /**
     * Send the contact mail.
     *
     * @param author
     * @param message
     * @param email
     * @param code
     * @param randomID
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static void send(@Required String author, @Required String message, @Required @Email String email, @Required String code, String randomID) throws InterruptedException, ExecutionException {
        if (!Play.id.equals("test")) {
            validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
        }
        if (validation.hasErrors()) {
            params.flash();
            validation.keep();
            randomID = Codec.UUID();
            render("@index", randomID);
        }
        Mails.contact(author, message, email);
        flash.success("Merci pour votre intéret %s", author);

        index();
    }

}
