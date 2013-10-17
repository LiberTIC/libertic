import models.cms.CMSPage;
import org.junit.Test;
import play.test.UnitTest;

import java.util.List;

/**
 * Test case for organismemaster & organisme model.
 */
public class CMSTest extends UnitTest {

    /**
     * Testing getLast organismemaster method.
     */
    @Test
    public void getAllCMSTemplate() {
        List<String> templates = CMSPage.getAllTemplate();
        assertEquals(2, templates.size());
    }

}
