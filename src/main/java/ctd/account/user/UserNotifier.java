package ctd.account.user;

import ctd.controller.notifier.ConfigurableNotifier;
import ctd.controller.watcher.WatcherTopics;

public class UserNotifier extends ConfigurableNotifier {

	public UserNotifier() {
		super(WatcherTopics.USER);
	}


}
