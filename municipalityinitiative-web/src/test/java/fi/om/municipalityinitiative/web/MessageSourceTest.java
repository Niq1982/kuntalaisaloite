package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.conf.WebTestConfiguration;
import fi.om.municipalityinitiative.util.Locales;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={WebTestConfiguration.class})
public class MessageSourceTest {
    
    @Resource 
    protected MessageSource messageSource;

    @Test
    public void Load_Message() {
        assertEquals("Tallenna", messageSource.getMessage("action.save", null, Locales.LOCALE_FI));
    }
    
}
