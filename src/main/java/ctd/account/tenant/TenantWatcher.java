package ctd.account.tenant;

import ctd.controller.watcher.ConfigurableWatcher;
import ctd.controller.watcher.WatcherTopics;

public class TenantWatcher extends ConfigurableWatcher {

	public TenantWatcher() {
		super(WatcherTopics.TENANT);
	}

}
