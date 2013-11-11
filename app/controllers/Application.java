package controllers;

import controllers.cms.Admin;
import models.cms.CMSPage;
import play.modules.search.Query;
import play.modules.search.Search;
import play.mvc.Http;
import play.mvc.results.Ok;
import service.WordPressImport;

import java.util.List;

/**
 * Controller that managed principal page of the site.
 */
public class Application extends AbstractController {

    public final static int ITEM_PER_PAGE = 5;
    public static final String MAP_RESULT_LIST = "result";
    public static final String MAP_RESULT_NB = "nb";

    /**
     * Home page.
     */
    public static void index() {
        List<CMSPage> blogs = CMSPage.getLastests("blog",Boolean.TRUE, 3);
        render(blogs);
    }

    /**
     * Blog page.
     */
    public static void blog() {
        CMSPage page = CMSPage.getLastest("blog", Boolean.TRUE);
        render("cms/blog.html", page);
    }

    /**
     * Search action.
     */
    public static void search(String search, Integer page) {
        if (page == null) {
            page = 1;
        }
        // default search
        String query = search;
        if(search == null || search.trim().length() == 0) {
            query = "*:*";
        }

        query += " AND (template:'blog' OR template:'page')";
        Query q = Search.search(query, CMSPage.class);
        List<CMSPage> pages = q.page((page-1) * ITEM_PER_PAGE, ITEM_PER_PAGE).fetch();
        Integer nbItems = q.count();

        render(search, page, pages, nbItems);
    }

    /**
     * Admin page.
     */
    public static void admin(){
        // check if it's an admin user
        isAdminUser();

        Admin.index("page");
    }

    /**
     * Sitemap.xml
     */
    public static void sitemap(){
        List<CMSPage> pages = CMSPage.all().fetch();
        response.contentType = "application/xml";
        render(pages);
    }

    /**
     * Robots.txt
     */
    public static void robots(){
        response.contentType = "text/plain; charset=" + Http.Response.current().encoding;
        render("Application/robots.html");
    }

    public static void wpimport() throws Exception {
        int nbImported = WordPressImport.importXML();
        renderText("Imported article : " + nbImported);
    }
}