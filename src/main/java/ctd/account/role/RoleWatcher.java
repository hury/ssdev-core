package ctd.account.role;


import ctd.controller.watcher.ConfigurableWatcher;
import ctd.controller.watcher.WatcherTopics;

public class RoleWatcher extends ConfigurableWatcher {

	public RoleWatcher() {
		super(WatcherTopics.ROLE);
	}

	

}
