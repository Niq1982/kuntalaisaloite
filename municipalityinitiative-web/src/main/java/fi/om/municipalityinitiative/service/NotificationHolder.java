package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.service.ui.Notification;

import java.util.Locale;

public interface NotificationHolder {

    Notification getNotification(Locale locale);
}
