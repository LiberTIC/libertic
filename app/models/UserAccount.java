package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "useraccount")
public class UserAccount extends Model {

    /**
     * The user id of external service.
     */
    public String userId;

    /**
     * The provider this user belongs to.
     */
    public String provider;

}
