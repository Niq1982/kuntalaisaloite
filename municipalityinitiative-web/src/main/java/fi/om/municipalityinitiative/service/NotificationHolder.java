package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.service.ui.Notification;

import java.util.Locale;
import java.util.Optional;

public interface NotificationHolder {

    Optional<Notification> getCachedNotification(Locale locale);
}
