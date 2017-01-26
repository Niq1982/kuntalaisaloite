package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.service.ui.Notification;
import fi.om.municipalityinitiative.service.ui.NotificationEditDto;
import fi.om.municipalityinitiative.util.Locales;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional(readOnly = false)
public class JdbcNotificationDaoTest {

    @Resource
    private JdbcNotificationDao notificationDao;

    @Test
    public void add_and_get() {


        NotificationEditDto notificationEditDto = new NotificationEditDto();
        notificationEditDto.setEnabled(true);
        notificationEditDto.setFi("fi");
        notificationEditDto.setUrlFi("urlfi");
        notificationEditDto.setUrlFiText("urlfitext");
        notificationEditDto.setSv("sv");
        notificationEditDto.setUrlSv("urlsv");
        notificationEditDto.setUrlSvText("urlsvtext");

        notificationDao.save(notificationEditDto);
        NotificationEditDto notificationForEdit = notificationDao.getNotificationForEdit();

        assertThat(notificationForEdit.getFi(), is("fi"));
        assertThat(notificationForEdit.getUrlFi(), is("urlfi"));
        assertThat(notificationForEdit.getUrlFiText(), is("urlfitext"));
        assertThat(notificationForEdit.getSv(), is("sv"));
        assertThat(notificationForEdit.getUrlSv(), is("urlsv"));
        assertThat(notificationForEdit.getUrlSvText(), is("urlsvtext"));
        assertThat(notificationForEdit.getCreateTime(), is(notNullValue()));
    }

    @Test
    public void multiple_inserts_succeeds() {

        NotificationEditDto notificationEditDto = new NotificationEditDto();
        notificationEditDto.setEnabled(false);
        notificationDao.save(notificationEditDto);
        notificationDao.save(notificationEditDto);
        notificationDao.save(notificationEditDto);
        notificationDao.save(notificationEditDto);
        notificationDao.save(notificationEditDto);
        notificationDao.save(notificationEditDto);
        notificationEditDto.setEnabled(true);
        notificationDao.save(notificationEditDto);


        assertThat(notificationDao.getNotificationForEdit().isEnabled(), is(true));
    }

    @Test
    public void create_time_is_updated() throws InterruptedException {

        notificationDao.save(new NotificationEditDto());
        Long firstCreateTime = notificationDao.getNotificationForEdit().getCreateTime();

        notificationDao.save(new NotificationEditDto());
        Long secondCreateTime = notificationDao.getNotificationForEdit().getCreateTime();
        assertThat(firstCreateTime, is(not(secondCreateTime)));

    }

    @Test
    public void cache_works() {

        NotificationEditDto notificationEditDto = new NotificationEditDto();
        notificationEditDto.setEnabled(true);
        notificationEditDto.setFi("fi");
        notificationEditDto.setUrlFi("urlfi");
        notificationEditDto.setUrlFiText("urlfitext");
        notificationEditDto.setSv("sv");
        notificationEditDto.setUrlSv("urlsv");
        notificationEditDto.setUrlSvText("urlsvtext");

        notificationDao.save(notificationEditDto);

        Notification fi = notificationDao.getCachedNotification(Locales.LOCALE_FI).get();
        assertThat(fi.getText(), is("fi"));
        assertThat(fi.getLink(), is("http://urlfi"));
        assertThat(fi.getLinkText(), is("urlfitext"));

        Notification sv = notificationDao.getCachedNotification(Locales.LOCALE_SV).get();
        assertThat(sv.getText(), is("sv"));
        assertThat(sv.getLink(), is("http://urlsv"));
        assertThat(sv.getLinkText(), is("urlsvtext"));

    }

    @Test
    public void empty_cache_works() {

        NotificationEditDto notificationEditDto = new NotificationEditDto();
        notificationEditDto.setEnabled(false);

        notificationDao.save(notificationEditDto);

        assertThat(notificationDao.getCachedNotification(Locales.LOCALE_FI).isPresent(), is(false));
        assertThat(notificationDao.getCachedNotification(Locales.LOCALE_SV).isPresent(), is(false));


    }

}