package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.service.NotificationHolder;
import fi.om.municipalityinitiative.service.ui.Notification;
import fi.om.municipalityinitiative.service.ui.NotificationEditDto;
import fi.om.municipalityinitiative.util.Locales;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NotificationDao implements NotificationHolder {

    // TODO: Store to db
    private NotificationEditDto notification;

    private volatile Map<Locale, Notification> notificationMap;

    public void save(NotificationEditDto notificationEditDto) {
        this.notification = notificationEditDto;
        refreshCache();
    }

    private void refreshCache() {

        HashMap<Locale, Notification> localeNotificationHashMap = new HashMap<>();

        if (notification.isEnabled()) {
            localeNotificationHashMap.put(
                    Locales.LOCALE_FI,
                    new Notification(notification.getFi(), notification.getUrlFi(), notification.getUrlFiText())
            );
            localeNotificationHashMap.put(
                    Locales.LOCALE_SV,
                    new Notification(notification.getSv(), notification.getUrlSv(), notification.getUrlSvText())
            );
        }

        notificationMap = localeNotificationHashMap;

    }

    public NotificationEditDto getNotificationForEdit() {
        if (notification == null) {
            notification = new NotificationEditDto();
        }
        return notification;
    }

    @Override
    public Notification getNotification(Locale locale) {
        getNotificationForEdit();
        if (notificationMap == null) {
            refreshCache();
        }
        return notificationMap.get(locale);
    }
}
