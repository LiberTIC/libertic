package models;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Objet to save people for the newsletter.
 */
@Entity
@Table(name = "newsletter_member")
public class NewsLetterMember extends GenericModel  {

    @Id
    @Email
    @Required
    public String email;
}
