package com.stocks.ui;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.stocks.services.EventType;
import com.vaadin.flow.shared.Registration;

/**
 * This class is responsible for pushing the events to UI.
 * 
 * @author Jalal
 * @since 20190403
 * @version 1.0
 */
public class PushNotifciationBroadcaster {
	private static Executor executor = Executors.newCachedThreadPool();
	private static LinkedList<Consumer<EventType>> listeners = new LinkedList<>();

	/**
	 * Using this method, every View registers itself as listener to get push
	 * notifications.
	 * 
	 * @author Jalal
	 * @since 20190403
	 */
	public static synchronized Registration register(Consumer<EventType> listener) {
		listeners.add(listener);

		return () -> {
			synchronized (PushNotifciationBroadcaster.class) {
				listeners.remove(listener);
			}
		};
	}

	/**
	 * Using this method, a given {@link EventType} is broadcast to the registered
	 * listeners.
	 * 
	 * @author Jalal
	 * @since 20190403
	 * @param event
	 */
	public static synchronized void broadcastEvent(EventType event) {
		for (Consumer<EventType> listener : listeners) {
			executor.execute(() -> listener.accept(event));
		}
	}
}