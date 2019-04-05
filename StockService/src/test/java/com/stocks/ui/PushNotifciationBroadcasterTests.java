package com.stocks.ui;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.stocks.services.EventType;
import com.vaadin.flow.shared.Registration;

@RunWith( MockitoJUnitRunner.class)
@SpringBootTest
public class PushNotifciationBroadcasterTests {

	@Test
	public void registerTest() {
		Consumer<EventType> listener = event -> {};
		Registration registration = PushNotifciationBroadcaster.register(listener);
		assertNotNull(registration);
	}

	@Test
	public void broadcastEventTest() {
		@SuppressWarnings("unchecked")
		Consumer<EventType> listener = mock(Consumer.class);
		PushNotifciationBroadcaster.register(listener);
		PushNotifciationBroadcaster.broadcastEvent(EventType.StocksListChanged);
		
		verify(listener).accept(EventType.StocksListChanged);
	}

}
