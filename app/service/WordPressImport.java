package service;

import models.cms.CMSPage;
import play.Logger;
import play.Play;

import javax.xml.stream.XMLInputFactory;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WordPressImport {

    public static int importXML() throws Exception {
        File file = Play.getFile("conf/import.xml");

        XMLInputFactory factory = XMLInputFactory.newInstance();
        javax.xml.stream.XMLStreamReader parser = factory.createXMLStreamReader(new FileInputStream(file));

        int nbArticleImported = 0;
        try {
            int depth = 0;
            ArrayList<String> currentXMLTags = new ArrayList<String>();
            CMSPage page = null;

            while (true) {
                int event = parser.next();
                if (event == javax.xml.stream.XMLStreamConstants.END_DOCUMENT) {
                    break;
                }
                switch (event) {

                    // we found a starting XML element
                    case javax.xml.stream.XMLStreamConstants.START_ELEMENT:
                        currentXMLTags.add(depth, parser.getLocalName());
                        String tagPath = currentXMLTags.toString();

                        // found an item => we start a CMS Page
                        if (tagPath.equals("[rss, channel, item]")) {
                            Logger.info("New page");
                            page = new CMSPage();
                            page.template = "blog";
                            page.keywords = "";
                            page.published = true;
                        }

                        if (tagPath.equals("[rss, channel, item, title]")) {
                            page.title = parser.getElementText();
                            Logger.info("page title is " + page.title);
                            currentXMLTags.remove(depth);
                            depth--;
                        }

                        if (tagPath.equals("[rss, channel, item, post_date]")) {
                            String wpDate = parser.getElementText();
                            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(wpDate);
                            page.created = date;
                            page.updated = date;
                            Logger.info("page date is " + page.created.toString());
                            currentXMLTags.remove(depth);
                            depth--;
                        }

                        if (tagPath.equals("[rss, channel, item, post_name]")) {
                            page.name = parser.getElementText();
                            Logger.info("page name is " + page.name);
                            currentXMLTags.remove(depth);
                            depth--;
                        }

                        if (tagPath.equals("[rss, channel, item, description]")) {
                            page.description = parser.getElementText();
                            Logger.info("page description is " + page.description);
                            currentXMLTags.remove(depth);
                            depth--;
                        }

                        if (tagPath.equals("[rss, channel, item, encoded]")) {
                            if (parser.getName().getPrefix().equals("content")) {
                                page.body = parser.getElementText();
                                currentXMLTags.remove(depth);
                                depth--;
                            }
                        }

                        if (tagPath.equals("[rss, channel, item, category]")) {
                            if (parser.getAttributeValue(null, "domain").equals("post_tag")) {
                                page.keywords += " " + parser.getElementText();
                                currentXMLTags.remove(depth);
                                depth--;
                            }

                        }

                        depth++;
                        break;

                    // we found an ending XML element
                    case javax.xml.stream.XMLStreamConstants.END_ELEMENT:
                        if (currentXMLTags.toString().equals("[rss, channel, item]")) {
                            if (page.title != null && !page.title.equals("") && page.body != null && !page.body.equals("")) {
                                if (page.name == null || page.name.equals("")) {
                                    String urlName = page.title;
                                    urlName = urlName.replaceAll("[ |'|`|\"]", "-");
                                    urlName = urlName.replaceAll("[e|é|è|ê]", "e");
                                    urlName = urlName.replaceAll("[à|a]", "a");
                                    urlName = urlName.replaceAll("[ï|î]", "i");

                                    // check if already exist and increment a counter
                                    Integer i = 0;
                                    String findUrl = urlName;
                                    while (CMSPage.findById(findUrl) != null) {
                                        i++;
                                        findUrl = urlName + "-" + i;
                                    }
                                    page.name = findUrl;
                                }
                                page._save();
                                nbArticleImported++;
                            } else {
                                page = null;
                            }
                        }
                        depth--;
                        currentXMLTags.remove(depth);
                        break;

                    // otherwise...
                    default:
                        break;
                }
            }
        } finally {
            parser.close();
        }
        return nbArticleImported;
    }

}
