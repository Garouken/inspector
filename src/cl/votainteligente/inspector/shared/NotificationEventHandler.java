package cl.votainteligente.inspector.shared;

import com.google.gwt.event.shared.EventHandler;

public interface NotificationEventHandler extends EventHandler {
	void onNotification(NotificationEvent notificationEvent);
}
