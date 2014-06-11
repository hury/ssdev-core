package ctd.account.tenant;

import ctd.controller.notifier.ConfigurableNotifier;
import ctd.controller.watcher.WatcherTopics;

public class TenantNotifier extends ConfigurableNotifier {

	public TenantNotifier() {
		super(WatcherTopics.TENANT);
	}


}
