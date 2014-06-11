package ctd.account.role;

import ctd.controller.notifier.ConfigurableNotifier;
import ctd.controller.watcher.WatcherTopics;

public class RoleNotifier extends ConfigurableNotifier {

	public RoleNotifier() {
		super(WatcherTopics.ROLE);
	}


}
