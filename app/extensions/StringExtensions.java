package extensions;

import org.jsoup.Jsoup;
import play.templates.JavaExtensions;

public class StringExtensions extends JavaExtensions{

    public static String truncateHtml(String html, int finalLenght) {
        return Jsoup.parse(html).text().substring(0, finalLenght);
    }
}
