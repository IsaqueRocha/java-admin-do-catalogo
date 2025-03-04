package com.isaque.admin.catalogo.domain.exceptions;

import com.isaque.admin.catalogo.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(String message, Notification notification) {
        super(message, notification.getErrors());
    }
}
