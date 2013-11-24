package models;

import models.cms.CMSFile;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Project extends Model {

    @Required
    public String name;

    @Lob
    @Required
    public String description;

    @ManyToOne
    public CMSFile image;

    @Required
    @URL
    public String url;
}
