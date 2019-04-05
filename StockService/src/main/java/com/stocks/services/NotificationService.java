package com.stocks.services;

import org.springframework.stereotype.Service;

import com.stocks.ui.PushNotifciationBroadcaster;

/**
 * A general interface to define possible methods needed for Notification.
 * 
 * @author Jalal
 * @since 20190403
 * @version 1.0
 */
@Service
public interface NotificationService {
	/**
	 * Given and {@link EventType}, this method sends the event to desired targets
	 * e.g. {@link PushNotifciationBroadcaster}. Notifying any component abou an
	 * {@link EventType} must occur here.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @version 1.0
	 * @param event
	 */
	public void notifyEvent(EventType event);
}
