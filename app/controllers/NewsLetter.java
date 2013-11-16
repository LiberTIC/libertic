package controllers;

import models.NewsLetterMember;
import notifier.Mails;
import play.data.validation.Email;
import play.data.validation.Required;

import java.util.List;

/**
 * Newsletter controller.
 */
public class NewsLetter extends AbstractController {

    /**
     * Page to register to the newsletter.
     */
    public static void register() {
        render();
    }

    /**
     * Action to save the registration.
     *
     * @param email
     */
    public static void registerSave(@Required @Email String email, @Required Boolean cgu) {
        // is it valid ?
        if (validation.hasErrors()) {
            params.flash();
            validation.keep();
            register();
        }
        NewsLetterMember member = NewsLetterMember.findById(email);
        if (member == null) {
            member = new NewsLetterMember();
            member.email = email;
            member.save();
        }
        flash.success("Votre inscription est validée. Vous allez recevoir un email de confirmation");
        Mails.newsletterRegister(email);
        register();
    }

    /**
     * Page to unregister to the newsletter.
     */
    public static void unregister() {
        render();
    }

    /**
     * Action to save the unregistration.
     */
    public static void unregisterSave(@Required @Email String email) {
        // is it valid ?
        if (validation.hasErrors()) {
            params.flash();
            validation.keep();
            unregister();
        }

        NewsLetterMember member = NewsLetterMember.findById(email);
        if (member != null) {
            member.delete();
            Mails.newsletterUnregister(email);
        }
        flash.success("Votre desinscription est enregistrée. Vous allez recevoir un email de confirmation.");
        unregister();
    }

    /**
     * Page to register to the newsletter.
     */
    public static void admin() {
        List<NewsLetterMember> members = NewsLetterMember.findAll();
        render(members);
    }

    /**
     * Delete a member without notification.
     *
     * @param email
     */
    public static void delete(String email){
        // only admin can do this action
        isAdminUser();

        NewsLetterMember member = NewsLetterMember.findById(email);
        if(member != null){
            member.delete();
        }
        admin();
    }

}
