package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.service.NotificationHolder;
import fi.om.municipalityinitiative.service.ui.Notification;
import fi.om.municipalityinitiative.service.ui.NotificationEditDto;
import fi.om.municipalityinitiative.sql.QNotification;
import fi.om.municipalityinitiative.util.Locales;
import org.joda.time.DateTime;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class JdbcNotificationDao implements NotificationHolder {

    @Resource
    PostgresQueryFactory queryFactory;

    private volatile Map<Locale, Notification> notificationMap;

    public void save(NotificationEditDto notificationEditDto) {
        queryFactory.delete(QNotification.notification).execute();
        queryFactory.insert(QNotification.notification)
                .set(QNotification.notification.fi, notificationEditDto.getFi())
                .set(QNotification.notification.sv, notificationEditDto.getSv())
                .set(QNotification.notification.urlfi, notificationEditDto.getUrlFi())
                .set(QNotification.notification.urlsv, notificationEditDto.getUrlSv())
                .set(QNotification.notification.urlfitext, notificationEditDto.getUrlFiText())
                .set(QNotification.notification.urlsvtext, notificationEditDto.getUrlSvText())
                .set(QNotification.notification.enabled, notificationEditDto.isEnabled())
                .set(QNotification.notification.createtime, DateTime.now())
                .execute();
        refreshCache();
    }

    public NotificationEditDto getNotificationForEdit() {
        return Optional.ofNullable(queryFactory.from(QNotification.notification)
                .limit(1)
                .singleResult(new MappingProjection<NotificationEditDto>(NotificationEditDto.class, QNotification.notification.all()) {
                    @Override
                    protected NotificationEditDto map(Tuple row) {
                        NotificationEditDto r = new NotificationEditDto();

                        r.setFi(row.get(QNotification.notification.fi));
                        r.setUrlFi(row.get(QNotification.notification.urlfi));
                        r.setUrlFiText(row.get(QNotification.notification.urlfitext));
                        r.setSv(row.get(QNotification.notification.sv));
                        r.setUrlSv(row.get(QNotification.notification.urlsv));
                        r.setUrlSvText(row.get(QNotification.notification.urlsvtext));
                        r.setEnabled(row.get(QNotification.notification.enabled));
                        r.setCreateTime(row.get(QNotification.notification.createtime).getMillis());
                        return r;

                    }
                })).orElse(new NotificationEditDto());

    }

    public void refreshCache() {

        HashMap<Locale, Notification> localeNotificationHashMap = new HashMap<>();

        NotificationEditDto notification = getNotificationForEdit();
        if (notification.isEnabled()) {
            localeNotificationHashMap.put(
                    Locales.LOCALE_FI,
                    new Notification(notification.getFi(), notification.getUrlFi(), notification.getUrlFiText(), notification.getCreateTime())
            );
            localeNotificationHashMap.put(
                    Locales.LOCALE_SV,
                    new Notification(notification.getSv(), notification.getUrlSv(), notification.getUrlSvText(), notification.getCreateTime())
            );
        }

        notificationMap = localeNotificationHashMap;

    }

    @Override
    public Optional<Notification> getCachedNotification(Locale locale) {
        return Optional.ofNullable(notificationMap.get(locale));
    }
}
