package controllers;

import models.Project;
import models.cms.CMSFile;
import play.data.validation.Required;
import play.data.validation.Valid;

import java.util.List;

public class Projects  extends AbstractController {

    /**
     * Display all project.
     */
    public static void index() {
        List<Project> projects = Project.all().fetch();
        render(projects);
    }

    /**
     * Display edit form for a project.
     *
     * @param id
     */
    public static void edit(Long id) {
        Project project = Project.findById(id);
        renderTemplate("@edit", project);
    }

    /**
     * Display form to add a project.
     */
    public static void add() {
        Project project = new Project();
        renderTemplate("@edit", project);
    }

    /**
     * Saving a project.
     *
     * @param project
     */
    public static void save(@Valid Project project, @Required String imageId) {
        if (request.params.get("delete") != null) {
            project.delete();
            index();
        }
        if (validation.hasErrors()) {
            renderTemplate("@edit", project);
        }

        if(imageId != null && !imageId.isEmpty()) {
            CMSFile file = CMSFile.findById(imageId);
            if(file != null) {
                project.image = file;
            }
        }
        else {
            project.image = null;
        }

        project.save();
        flash.success("Saved");

        edit(project.id);
    }


    /**
     * Delete a page.
     *
     * @param id
     */
    public static void delete(Long id) {
        Project project = Project.findById(id);
        notFoundIfNull(project);
        project.delete();
        index();
    }
}
