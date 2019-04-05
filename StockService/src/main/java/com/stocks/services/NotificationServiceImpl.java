package com.stocks.services;

import org.springframework.stereotype.Service;

import com.stocks.ui.PushNotifciationBroadcaster;

/**
 * An implementation of {@link NotificationService}. This service is used to
 * notify desired modules and components with different types of events.
 * 
 * @author Jalal
 * @since 20190403
 * @version 1.0
 */
@Service
public class NotificationServiceImpl implements NotificationService {
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
	@Override
	public void notifyEvent(EventType event) {
		// notify UI
		PushNotifciationBroadcaster.broadcastEvent(event);

	}

}
